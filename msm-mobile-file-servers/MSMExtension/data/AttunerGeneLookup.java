package com.bigbluebubble.mysingingmonsters.data;

import com.bigbluebubble.BBBServer.GameSettings;
import com.bigbluebubble.BBBServer.util.IDbWrapper;
import com.bigbluebubble.mysingingmonsters.Logger;
import com.bigbluebubble.mysingingmonsters.schedules.Schedule;
import com.bigbluebubble.mysingingmonsters.schedules.ScheduleLookup;
import com.bigbluebubble.mysingingmonsters.staticdata.StaticDataLookup;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

public class AttunerGeneLookup extends StaticDataLookup<AttunerGene> {
   private static AttunerGeneLookup instance;
   private Map<Integer, AttunerGene> genes_ = new ConcurrentHashMap();
   private Map<String, AttunerGene> genesByLetter_ = new ConcurrentHashMap();
   private Map<Integer, AttunerOffsetData> offsetData_ = new ConcurrentHashMap();
   static final String CACHE_NAME = "attuner_gene_data";

   public static AttunerGeneLookup getInstance() {
      return instance;
   }

   private AttunerGeneLookup(IDbWrapper db) throws Exception {
      this.lastChanged_ = 0L;
      String sql = "SELECT id, gene, island_id, schedule_id, instability, critter_graphic, attuner_graphic, last_changed FROM attuner_genes";
      ISFSArray results = db.query("SELECT id, gene, island_id, schedule_id, instability, critter_graphic, attuner_graphic, last_changed FROM attuner_genes");
      Iterator i = results.iterator();

      while(i.hasNext()) {
         SFSObject geneData = (SFSObject)((SFSObject)((SFSDataWrapper)i.next()).getObject());
         if (geneData != null) {
            AttunerGene gene = new AttunerGene(geneData);
            this.genes_.put(gene.getId(), gene);
            this.genesByLetter_.put(gene.getGene(), gene);
            this.lastChanged_ = Math.max(this.lastChanged_, gene.lastChanged());
         }
      }

      this.initAttunerOffsetData();
   }

   private void initAttunerOffsetData() throws Exception {
      SFSArray settings = SFSArray.newFromJsonData(GameSettings.get("USER_ATTUNER_OFFSET_DATA"));
      Iterator itr = settings.iterator();

      while(itr.hasNext()) {
         SFSObject offsetDataSFS = (SFSObject)((SFSObject)((SFSDataWrapper)itr.next()).getObject());
         AttunerOffsetData offsetData = new AttunerOffsetData(offsetDataSFS);
         this.offsetData_.put(offsetData.offset(), offsetData);
      }

   }

   public static void init(IDbWrapper db) throws Exception {
      instance = new AttunerGeneLookup(db);
   }

   public static AttunerGene get(String geneLetter) {
      return instance.getEntry(geneLetter);
   }

   public String getCacheName() {
      return "attuner_gene_data";
   }

   public Iterable<AttunerGene> entries() {
      return this.genes_.values();
   }

   public AttunerGene getEntry(int id) {
      return (AttunerGene)this.genes_.get(id);
   }

   public AttunerGene getEntry(String letter) {
      return (AttunerGene)this.genesByLetter_.get(letter);
   }

   public int attuningCost(String gene) {
      return this.attuningCost(gene, this.activeAttunerGene());
   }

   public int attuningCost(String gene, AttunerGene active) {
      int offset = this.attunerGeneOffset(gene, active);
      return this.getOffsetData(offset).cost();
   }

   public int attuningDurationInHours(String gene) {
      return this.attuningDurationInHours(gene, this.activeAttunerGene());
   }

   public int attuningDurationInHours(String gene, AttunerGene active) {
      int offset = this.attunerGeneOffset(gene, active);
      return this.getOffsetData(offset).durationInHours();
   }

   public int attunedIslandId() {
      return this.activeAttunerGene().getIslandId();
   }

   public AttunerGene getIslandGene(int islandId) {
      Iterable<AttunerGene> genes = this.entries();
      Iterator var3 = genes.iterator();

      AttunerGene gene;
      do {
         if (!var3.hasNext()) {
            return null;
         }

         gene = (AttunerGene)var3.next();
      } while(islandId != gene.getIslandId());

      return gene;
   }

   private AttunerOffsetData getOffsetData(int offset) {
      return (AttunerOffsetData)this.offsetData_.get(offset);
   }

   private int attunerGeneOffset(String gene, AttunerGene active) {
      AttunerGene activeGene = active;
      AttunerGene selectedGene = get(gene);
      ArrayList<AttunerGene> sorted = this.nextStartTimeSortedAttunerGenes();
      int activeIndex = 0;
      int selectedIndex = 0;

      int i;
      for(i = 0; i < sorted.size(); ++i) {
         if (selectedGene.getId() == ((AttunerGene)sorted.get(i)).getId()) {
            selectedIndex = i;
         }

         if (activeGene.getId() == ((AttunerGene)sorted.get(i)).getId()) {
            activeIndex = i;
         }
      }

      i = Math.abs(activeIndex - selectedIndex);
      int numGenes = this.numAttunerGenes();
      if (i <= numGenes / 2) {
         return i;
      } else {
         return numGenes - i;
      }
   }

   private ArrayList<AttunerGene> nextStartTimeSortedAttunerGenes() {
      ArrayList<AttunerGene> genes = new ArrayList();
      Iterator var2 = this.genes_.values().iterator();

      while(var2.hasNext()) {
         AttunerGene gene = (AttunerGene)var2.next();
         genes.add(gene);
      }

      Collections.sort(genes, new Comparator<AttunerGene>() {
         public int compare(AttunerGene a, AttunerGene b) {
            if (ScheduleLookup.schedule(a.getScheduleId()).isActive()) {
               return -1;
            } else if (ScheduleLookup.schedule(b.getScheduleId()).isActive()) {
               return 1;
            } else {
               Long aStartTime = ScheduleLookup.schedule(a.getScheduleId()).getNextStartTime(5);
               Long bStartTime = ScheduleLookup.schedule(b.getScheduleId()).getNextStartTime(5);
               return aStartTime.compareTo(bStartTime);
            }
         }
      });
      return genes;
   }

   public int numAttunerGenes() {
      int counter = 0;

      for(Iterator var2 = this.entries().iterator(); var2.hasNext(); ++counter) {
         Object i = var2.next();
      }

      return counter;
   }

   public AttunerGene activeAttunerGene() {
      Iterable<AttunerGene> genes = this.entries();
      Iterator var2 = genes.iterator();

      AttunerGene gene;
      Schedule s;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         gene = (AttunerGene)var2.next();
         s = ScheduleLookup.schedule(gene.getScheduleId());
      } while(s == null || !s.isActive() || s.timeRemaining() <= 0L);

      return gene;
   }

   public AttunerGene activeAttunerGene(Calendar currentTime) {
      Iterable<AttunerGene> genes = this.entries();
      Iterator var3 = genes.iterator();

      AttunerGene gene;
      Schedule s;
      do {
         if (!var3.hasNext()) {
            return null;
         }

         gene = (AttunerGene)var3.next();
         s = ScheduleLookup.schedule(gene.getScheduleId());
      } while(s == null || !s.isActive(currentTime) || s.timeRemaining(currentTime) <= 0L);

      return gene;
   }

   public Schedule activeAttunerGeneSchedule() {
      AttunerGene activeGene = this.activeAttunerGene();
      return activeGene != null ? ScheduleLookup.schedule(activeGene.getScheduleId()) : null;
   }

   public void PrintScehduleTest() {
      SimpleDateFormat sdf = new SimpleDateFormat("E, MMM dd yyyy, hh:mm:ss a");
      Logger.trace("~~~~~~~~~~ current time " + sdf.format(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime()));
      Schedule s = this.activeAttunerGeneSchedule();
      if (s != null && s.isActive() && s.timeRemaining() > 0L) {
         Logger.trace("~~~~~~~~~~ schedule active " + s.getId() + " time left " + s.timeRemaining() / 60000L);
      }

   }
}

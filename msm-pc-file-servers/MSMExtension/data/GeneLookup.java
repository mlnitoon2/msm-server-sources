package com.bigbluebubble.mysingingmonsters.data;

import com.bigbluebubble.BBBServer.util.IDbWrapper;
import com.bigbluebubble.mysingingmonsters.staticdata.StaticDataLookup;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GeneLookup extends StaticDataLookup<Gene> {
   private static GeneLookup instance;
   private Map<Integer, Gene> genes_ = new ConcurrentHashMap();
   private Map<String, Gene> genesByLetter_ = new ConcurrentHashMap();
   static final String CACHE_NAME = "genes_data";

   public static GeneLookup getInstance() {
      return instance;
   }

   private GeneLookup(IDbWrapper db) throws Exception {
      this.lastChanged_ = 0L;
      String sql = "SELECT gene_id, gene_letter, gene_graphic, gene_string, last_changed, sort_order, min_server_version FROM genes";
      ISFSArray results = db.query("SELECT gene_id, gene_letter, gene_graphic, gene_string, last_changed, sort_order, min_server_version FROM genes");
      Iterator i = results.iterator();

      while(i.hasNext()) {
         SFSObject geneData = (SFSObject)((SFSObject)((SFSDataWrapper)i.next()).getObject());
         if (geneData != null) {
            Gene gene = new Gene(geneData);
            this.genes_.put(gene.getId(), gene);
            this.genesByLetter_.put(gene.getGeneLetter(), gene);
            this.lastChanged_ = Math.max(this.lastChanged_, gene.lastChanged());
         }
      }

   }

   public static void init(IDbWrapper db) throws Exception {
      instance = new GeneLookup(db);
   }

   public static Gene get(String geneLetter) {
      return instance.getEntry(geneLetter);
   }

   public String getCacheName() {
      return "genes_data";
   }

   public Iterable<Gene> entries() {
      return this.genes_.values();
   }

   public Gene getEntry(int id) {
      return (Gene)this.genes_.get(id);
   }

   public Gene getEntry(String letter) {
      return (Gene)this.genesByLetter_.get(letter);
   }
}

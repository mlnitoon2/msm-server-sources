package com.bigbluebubble.mysingingmonsters.data;

import com.bigbluebubble.mysingingmonsters.schedules.Schedule;
import com.bigbluebubble.mysingingmonsters.schedules.ScheduleLookup;
import com.bigbluebubble.mysingingmonsters.staticdata.StaticData;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;

public class AttunerGene extends StaticData {
   protected static final String GENE_ID_KEY = "id";
   protected static final String GENE_KEY = "gene";
   protected static final String ISLAND_ID_KEY = "island_id";
   protected static final String SCHEDULE_ID_KEY = "schedule_id";
   protected static final String SCHEDULE_DATA_KEY = "schedule";
   protected static final String INSTABILITY_KEY = "instability";
   protected static final String CRITTER_GRAPHIC_KEY = "critter_graphic";
   protected static final String ATTUNER_GRAPHIC_KEY = "attuner_graphic";

   public AttunerGene(ISFSObject geneData) {
      super(geneData);
   }

   public ISFSObject getData() {
      ISFSObject geneData = new SFSObject();
      geneData.putInt("id", this.data.getInt("id"));
      geneData.putUtfString("gene", this.data.getUtfString("gene"));
      geneData.putInt("island_id", this.data.getInt("island_id"));
      geneData.putInt("instability", this.data.getInt("instability"));
      geneData.putUtfString("critter_graphic", this.data.getUtfString("critter_graphic"));
      geneData.putUtfString("attuner_graphic", this.data.getUtfString("attuner_graphic"));
      geneData.putLong("last_changed", this.data.getLong("last_changed"));
      int scheduleId = this.data.getInt("schedule_id");
      Schedule schedule = ScheduleLookup.schedule(scheduleId);
      geneData.putSFSObject("schedule", schedule.getSFSObject(5));
      return geneData;
   }

   public boolean shouldAlwaysUpdate() {
      return true;
   }

   public int getId() {
      return this.data.getInt("id");
   }

   public String getGene() {
      return this.data.getUtfString("gene");
   }

   public int getIslandId() {
      return this.data.getInt("island_id");
   }

   public int getScheduleId() {
      return this.data.getInt("schedule_id");
   }

   public int getInstability() {
      return this.data.getInt("instability");
   }
}

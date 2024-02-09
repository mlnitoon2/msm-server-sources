package com.bigbluebubble.mysingingmonsters.data;

import com.bigbluebubble.BBBServer.util.VersionInfo;
import com.bigbluebubble.mysingingmonsters.staticdata.StaticData;
import com.smartfoxserver.v2.entities.data.ISFSObject;

public class Gene extends StaticData {
   protected static final String GENE_ID_KEY = "gene_id";
   protected static final String GENE_LETTER_KEY = "gene_letter";
   protected static final String GENE_GRAPHIC_KEY = "gene_graphic";
   protected static final String MIN_VER_KEY = "min_server_version";

   public Gene(ISFSObject geneData) {
      super(geneData);
      this.minVersion = new VersionInfo(this.data.getUtfString("min_server_version"));
   }

   public int getId() {
      return this.data.getInt("gene_id");
   }

   public String getGeneLetter() {
      return this.data.getUtfString("gene_letter");
   }

   public String getGeneGraphic() {
      return this.data.getUtfString("gene_graphic");
   }
}

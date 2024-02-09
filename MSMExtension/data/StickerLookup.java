package com.bigbluebubble.mysingingmonsters.data;

import com.bigbluebubble.BBBServer.util.IDbWrapper;
import com.bigbluebubble.mysingingmonsters.staticdata.StaticDataLookup;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StickerLookup extends StaticDataLookup<Sticker> {
   private static StickerLookup instance;
   private Map<Integer, Sticker> stickers_ = new ConcurrentHashMap();
   static final String CACHE_NAME = "stickers";

   public static StickerLookup getInstance() {
      return instance;
   }

   private StickerLookup(IDbWrapper db) throws Exception {
      String sql = "SELECT * FROM stickers";
      ISFSArray results = db.query("SELECT * FROM stickers");
      Iterator i = results.iterator();

      while(i.hasNext()) {
         Sticker sticker = new Sticker((SFSObject)((SFSDataWrapper)i.next()).getObject());
         this.stickers_.put(sticker.getID(), sticker);
      }

   }

   public static void init(IDbWrapper db) throws Exception {
      instance = new StickerLookup(db);
   }

   public static Sticker get(int stickerId) {
      return instance.getEntry(stickerId);
   }

   public String getCacheName() {
      return "stickers";
   }

   public Iterable<Sticker> entries() {
      return this.stickers_.values();
   }

   public Sticker getEntry(int id) {
      return (Sticker)this.stickers_.get(id);
   }
}

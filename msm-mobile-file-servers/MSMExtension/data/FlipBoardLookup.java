package com.bigbluebubble.mysingingmonsters.data;

import com.bigbluebubble.BBBServer.util.IDbWrapper;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class FlipBoardLookup {
   public static ConcurrentHashMap<String, SFSObject> boardDefinitions;

   public static void init(IDbWrapper db) throws Exception {
      boardDefinitions = new ConcurrentHashMap();
      String sql = "SELECT * FROM memory_flip_boards ORDER BY id";
      ISFSArray results = db.query(sql);
      Iterator i = results.iterator();

      while(i.hasNext()) {
         SFSObject board = (SFSObject)((SFSObject)((SFSDataWrapper)i.next()).getObject());
         board.putSFSObject("definition", SFSObject.newFromJsonData(board.getUtfString("definition")));
         add(board);
      }

   }

   public static void add(SFSObject boardData) {
      boardDefinitions.put(boardData.getUtfString("name"), boardData);
   }

   public static Iterator<SFSObject> iterator() {
      return boardDefinitions.values().iterator();
   }
}

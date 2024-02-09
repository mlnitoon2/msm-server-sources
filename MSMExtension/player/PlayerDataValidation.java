package com.bigbluebubble.mysingingmonsters.player;

import com.smartfoxserver.v2.entities.data.ISFSObject;
import java.util.Iterator;
import java.util.Set;

public class PlayerDataValidation {
   public static void validateKeys(ISFSObject data, Set<String> validKeys) throws PlayerLoadingException {
      Iterator var2 = data.getKeys().iterator();

      String key;
      do {
         if (!var2.hasNext()) {
            return;
         }

         key = (String)var2.next();
      } while(validKeys.contains(key));

      throw new PlayerLoadingException(String.format("Found Invalid Key: %s", key));
   }
}

package com.bigbluebubble.mysingingmonsters.data;

import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.Collection;

public class FlipSpecialLevel {
   private int id;
   private Collection<Integer> levels;

   public FlipSpecialLevel(SFSObject sfsDef) {
      this.id = sfsDef.getInt("id");
      this.levels = sfsDef.getIntArray("levels");
   }

   public boolean appliesToLevel(int level) {
      return this.levels.contains(level);
   }

   public int id() {
      return this.id;
   }
}

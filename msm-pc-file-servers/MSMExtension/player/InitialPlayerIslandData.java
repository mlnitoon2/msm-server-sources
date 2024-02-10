package com.bigbluebubble.mysingingmonsters.player;

import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;

public class InitialPlayerIslandData {
   public SFSArray structures;
   public SFSArray monsters;
   public ISFSObject volatiles;

   public InitialPlayerIslandData(SFSArray structureList, SFSArray monsterList, ISFSObject volatileList) {
      this.structures = structureList;
      this.monsters = monsterList;
      this.volatiles = volatileList;
   }
}

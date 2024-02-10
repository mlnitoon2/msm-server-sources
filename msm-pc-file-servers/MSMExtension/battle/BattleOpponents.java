package com.bigbluebubble.mysingingmonsters.battle;

import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class BattleOpponents {
   List<BattleOpponentData> opponents_ = new ArrayList();

   public BattleOpponents() {
   }

   public BattleOpponents(ISFSArray data) {
      Iterator i = data.iterator();

      while(i.hasNext()) {
         ISFSObject obj = (ISFSObject)((ISFSObject)((SFSDataWrapper)i.next()).getObject());
         BattleOpponentData opponent = new BattleOpponentData(obj);
         this.opponents_.add(opponent);
      }

   }

   public void add(BattleOpponentData data) {
      this.opponents_.add(data);
   }

   public BattleOpponentData get(int i) {
      return (BattleOpponentData)this.opponents_.get(i);
   }

   public Iterable<BattleOpponentData> opponents() {
      return this.opponents_;
   }

   public int size() {
      return this.opponents_.size();
   }

   public ISFSArray toSFSArray() {
      ISFSArray arr = new SFSArray();
      Iterator var2 = this.opponents_.iterator();

      while(var2.hasNext()) {
         BattleOpponentData opponent = (BattleOpponentData)var2.next();
         arr.addSFSObject(opponent.toSFSObject());
      }

      return arr;
   }
}

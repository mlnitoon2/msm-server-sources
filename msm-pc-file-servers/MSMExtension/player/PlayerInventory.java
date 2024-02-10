package com.bigbluebubble.mysingingmonsters.player;

import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.security.InvalidParameterException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class PlayerInventory {
   public static final String ITEMS_KEY = "items";
   private static final String ITEM_ID_KEY = "k";
   private static final String ITEM_AMOUNT_KEY = "v";
   private ISFSArray items;
   private boolean isDirty_ = false;

   public boolean isDirty() {
      return this.isDirty_;
   }

   public void setDirty(boolean dirty) {
      this.isDirty_ = dirty;
   }

   public PlayerInventory() {
      this.items = new SFSArray();
   }

   public PlayerInventory(ISFSObject inventoryData) {
      if (!inventoryData.containsKey("items")) {
         this.items = new SFSArray();
      } else {
         this.items = inventoryData.getSFSArray("items");
      }

   }

   public boolean hasItem(int itemId) {
      if (itemId == 0) {
         return true;
      } else {
         return this.getItemAmount(itemId) > 0;
      }
   }

   public int getItemAmount(int itemId) {
      ISFSObject item = this.getItem(itemId);
      return item != null ? item.getInt("v") : 0;
   }

   private ISFSObject getItem(int itemId) {
      Iterator i = this.items.iterator();

      ISFSObject item;
      do {
         if (!i.hasNext()) {
            return null;
         }

         item = (ISFSObject)((ISFSObject)((SFSDataWrapper)i.next()).getObject());
      } while(item.getInt("k") != itemId);

      return item;
   }

   private ISFSObject createItem(int itemId) {
      ISFSObject item = new SFSObject();
      item.putInt("k", itemId);
      item.putInt("v", 0);
      this.items.addSFSObject(item);
      return item;
   }

   public void addItem(int itemId, int amount) throws Exception {
      if (amount <= 0) {
         throw new InvalidParameterException();
      } else {
         ISFSObject item = this.getItem(itemId);
         if (item == null) {
            item = this.createItem(itemId);
         }

         int total = item.getInt("v");
         item.putInt("v", total + amount);
         this.isDirty_ = true;
      }
   }

   public void removeItem(int itemId, int amount) throws Exception {
      if (amount <= 0) {
         throw new InvalidParameterException("amount");
      } else {
         ISFSObject item = this.getItem(itemId);
         if (item == null) {
            throw new Exception("Item not in inventory");
         } else {
            int total = item.getInt("v");
            if (total > amount) {
               item.putInt("v", total - amount);
            } else {
               if (total != amount) {
                  throw new Exception("Not enough!");
               }

               for(int idx = 0; idx < this.items.size(); ++idx) {
                  if (((ISFSObject)this.items.getElementAt(idx)).getInt("k") == itemId) {
                     this.items.removeElementAt(idx);
                     break;
                  }
               }
            }

            this.isDirty_ = true;
         }
      }
   }

   public ISFSObject toSFSObject() {
      ISFSObject obj = new SFSObject();
      obj.putSFSArray("items", this.items);
      return obj;
   }

   public Iterable<Integer> itemIds() {
      return new Iterable<Integer>() {
         public Iterator<Integer> iterator() {
            return new Iterator<Integer>() {
               int pos = 0;

               public boolean hasNext() {
                  if (PlayerInventory.this.items == null) {
                     return false;
                  } else {
                     return this.pos < PlayerInventory.this.items.size();
                  }
               }

               public Integer next() {
                  if (!this.hasNext()) {
                     throw new NoSuchElementException();
                  } else {
                     return PlayerInventory.this.items.getSFSObject(this.pos++).getInt("k");
                  }
               }

               public void remove() {
                  throw new UnsupportedOperationException("remove");
               }
            };
         }
      };
   }
}

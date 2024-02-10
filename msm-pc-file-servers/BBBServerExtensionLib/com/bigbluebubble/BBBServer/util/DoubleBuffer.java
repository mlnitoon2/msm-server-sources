package com.bigbluebubble.BBBServer.util;

import java.lang.reflect.Array;

public class DoubleBuffer<E> {
   private transient E[] _writeBuffer;
   private transient E[] _readBuffer;
   private transient Object _lock;
   private transient int _writeIndex;
   private final transient int _CAPACITY;

   public DoubleBuffer(Class<E> e, int capacity) {
      this._CAPACITY = capacity;
      this._writeBuffer = (Object[])((Object[])Array.newInstance(e, capacity));
      this._readBuffer = (Object[])((Object[])Array.newInstance(e, capacity));
      this._lock = new Object();
      this._writeIndex = 0;
   }

   public boolean add(E data) {
      synchronized(this._lock) {
         if (this._writeIndex < this._CAPACITY) {
            this._writeBuffer[this._writeIndex++] = data;
            return true;
         } else {
            return false;
         }
      }
   }

   public E[] swapBuffers() {
      synchronized(this._lock) {
         for(int i = 0; i < this._CAPACITY; ++i) {
            this._readBuffer[i] = null;
         }

         E[] temp = this._readBuffer;
         this._readBuffer = this._writeBuffer;
         this._writeBuffer = temp;
         this._writeIndex = 0;
         return this._readBuffer;
      }
   }
}

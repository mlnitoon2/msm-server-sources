package com.bigbluebubble.BBBServer.util;

public class ByteBuffer {
   private transient byte[] _bytes;
   private transient int _writeIndex;

   public ByteBuffer(int capacity) {
      this._bytes = new byte[capacity];
      this._writeIndex = 0;
   }

   public boolean add(byte data) {
      if (this._writeIndex < this._bytes.length) {
         this._bytes[this._writeIndex++] = data;
         return true;
      } else {
         return false;
      }
   }

   public void clear() {
      this._writeIndex = 0;
   }

   public byte[] getBytes() {
      return this._bytes;
   }

   public int used() {
      return this._writeIndex;
   }

   public int available() {
      return this._bytes.length - this._writeIndex;
   }
}

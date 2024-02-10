package com.bigbluebubble.BBBServer.util;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import net.spy.memcached.transcoders.SerializingTranscoder;

public class CustomSerializingTranscoder extends SerializingTranscoder {
   protected Object deserialize(byte[] bytes) {
      final ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
      ObjectInputStream in = null;

      Object var5;
      try {
         ByteArrayInputStream bs = new ByteArrayInputStream(bytes);
         in = new ObjectInputStream(bs) {
            protected Class<?> resolveClass(ObjectStreamClass objectStreamClass) throws IOException, ClassNotFoundException {
               try {
                  return currentClassLoader.loadClass(objectStreamClass.getName());
               } catch (Exception var3) {
                  return super.resolveClass(objectStreamClass);
               }
            }
         };
         var5 = in.readObject();
      } catch (Exception var9) {
         SimpleLogger.trace(var9);
         throw new RuntimeException(var9);
      } finally {
         closeStream(in);
      }

      return var5;
   }

   private static void closeStream(Closeable c) {
      if (c != null) {
         try {
            c.close();
         } catch (IOException var2) {
            SimpleLogger.trace((Exception)var2);
         }
      }

   }
}

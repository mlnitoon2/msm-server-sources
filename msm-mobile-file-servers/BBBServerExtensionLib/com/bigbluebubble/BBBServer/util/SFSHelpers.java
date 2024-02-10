package com.bigbluebubble.BBBServer.util;

import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSDataType;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

public class SFSHelpers {
   public static boolean equals(ISFSObject a, ISFSObject b) {
      if (a != null && b != null) {
         Set<String> k1 = a.getKeys();
         if (k1.size() != b.getKeys().size()) {
            return false;
         } else {
            Iterator var3 = k1.iterator();

            SFSDataWrapper d1;
            SFSDataWrapper d2;
            do {
               if (!var3.hasNext()) {
                  return true;
               }

               String k = (String)var3.next();
               d1 = a.get(k);
               d2 = b.get(k);
            } while(equals(d1, d2));

            return false;
         }
      } else {
         return a == b;
      }
   }

   public static boolean equals(ISFSArray a, ISFSArray b) {
      if (a != null && b != null) {
         if (a.size() != b.size()) {
            return false;
         } else {
            Iterator<SFSDataWrapper> i1 = a.iterator();
            Iterator<SFSDataWrapper> i2 = b.iterator();
            return equals((SFSDataWrapper)i1.next(), (SFSDataWrapper)i2.next());
         }
      } else {
         return a == b;
      }
   }

   public static boolean equals(SFSDataWrapper a, SFSDataWrapper b) {
      if (a != null && b != null) {
         if (a.getTypeId() != b.getTypeId()) {
            return false;
         } else if (a.getTypeId() == SFSDataType.SFS_OBJECT) {
            return equals((ISFSObject)a.getObject(), (ISFSObject)b.getObject());
         } else {
            return a.getTypeId() == SFSDataType.SFS_ARRAY ? equals((ISFSArray)a.getObject(), (ISFSArray)b.getObject()) : a.getObject().equals(b.getObject());
         }
      } else {
         return a == b;
      }
   }

   public static ISFSObject clone(ISFSObject s) {
      ISFSObject cloned = new SFSObject();
      Iterator it = s.iterator();

      while(it.hasNext()) {
         Entry<String, SFSDataWrapper> e = (Entry)it.next();
         cloned.put((String)e.getKey(), (SFSDataWrapper)e.getValue());
      }

      return cloned;
   }

   public static ISFSArray clone(ISFSArray a) {
      ISFSArray cloned = new SFSArray();
      Iterator it = a.iterator();

      while(it.hasNext()) {
         cloned.add((SFSDataWrapper)it.next());
      }

      return cloned;
   }

   public static double getDouble(int index, ISFSArray arr) throws ClassCastException {
      Object obj = arr.getElementAt(index);
      Class<?> objClass = obj.getClass();
      if (objClass == Float.class) {
         return ((Float)obj).doubleValue();
      } else if (objClass == Double.class) {
         return (Double)obj;
      } else if (objClass == Short.class) {
         return ((Short)obj).doubleValue();
      } else if (objClass == Integer.class) {
         return ((Integer)obj).doubleValue();
      } else if (objClass == Long.class) {
         return ((Long)obj).doubleValue();
      } else {
         throw new ClassCastException("Invalid data type " + objClass.toString());
      }
   }

   public static double getDouble(String key, ISFSObject obj) throws ClassCastException {
      return getDouble(obj.get(key));
   }

   public static double getDouble(SFSDataWrapper d) throws ClassCastException {
      switch(d.getTypeId()) {
      case FLOAT:
         return ((Float)d.getObject()).doubleValue();
      case DOUBLE:
         return (Double)d.getObject();
      case SHORT:
         return ((Short)d.getObject()).doubleValue();
      case INT:
         return ((Integer)d.getObject()).doubleValue();
      case LONG:
         return ((Long)d.getObject()).doubleValue();
      default:
         throw new ClassCastException("Invalid data type " + d.getTypeId());
      }
   }

   public static long getLong(int index, ISFSArray arr) throws ClassCastException {
      Object obj = arr.getElementAt(index);
      Class<?> objClass = obj.getClass();
      if (objClass == Short.class) {
         return ((Short)obj).longValue();
      } else if (objClass == Integer.class) {
         return ((Integer)obj).longValue();
      } else if (objClass == Long.class) {
         return (Long)obj;
      } else {
         throw new ClassCastException("Invalid data type " + objClass.toString());
      }
   }

   public static long getLong(String key, ISFSObject obj) throws ClassCastException {
      return getLong(obj.get(key));
   }

   public static long getLong(SFSDataWrapper d) throws ClassCastException {
      switch(d.getTypeId()) {
      case SHORT:
         return ((Short)d.getObject()).longValue();
      case INT:
         return ((Integer)d.getObject()).longValue();
      case LONG:
         return (Long)d.getObject();
      default:
         throw new ClassCastException("Invalid data type " + d.getTypeId());
      }
   }

   public static String getUtfString(String key, ISFSObject obj) throws ClassCastException {
      return getUtfString(obj.get(key));
   }

   public static String getUtfString(SFSDataWrapper d) throws ClassCastException {
      switch(d.getTypeId()) {
      case UTF_STRING:
         return (String)d.getObject();
      default:
         throw new ClassCastException("Invalid data type " + d.getTypeId());
      }
   }

   public static boolean getBool(String key, ISFSObject obj) throws ClassCastException {
      return getBool(obj.get(key));
   }

   public static boolean getBool(SFSDataWrapper d) throws ClassCastException {
      switch(d.getTypeId()) {
      case BOOL:
         return (Boolean)d.getObject();
      default:
         throw new ClassCastException("Invalid data type " + d.getTypeId());
      }
   }

   public static int getInt(String key, ISFSObject obj) throws ClassCastException {
      return getInt(obj.get(key));
   }

   public static int getInt(SFSDataWrapper d) throws ClassCastException {
      switch(d.getTypeId()) {
      case INT:
         return (Integer)d.getObject();
      default:
         throw new ClassCastException("Invalid data type " + d.getTypeId());
      }
   }
}

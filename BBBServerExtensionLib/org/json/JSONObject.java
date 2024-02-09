package org.json;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;
import java.util.Map.Entry;

public class JSONObject {
   private Map map;
   public static final Object NULL = new JSONObject.Null();

   public JSONObject() {
      this.map = new HashMap();
   }

   public JSONObject(JSONObject jo, String[] names) {
      this();

      for(int i = 0; i < names.length; ++i) {
         try {
            this.putOnce(names[i], jo.opt(names[i]));
         } catch (Exception var5) {
         }
      }

   }

   public JSONObject(JSONTokener x) throws JSONException {
      this();
      if (x.nextClean() != '{') {
         throw x.syntaxError("A JSONObject text must begin with '{'");
      } else {
         while(true) {
            char c = x.nextClean();
            switch(c) {
            case '\u0000':
               throw x.syntaxError("A JSONObject text must end with '}'");
            case '}':
               return;
            default:
               x.back();
               String key = x.nextValue().toString();
               c = x.nextClean();
               if (c == '=') {
                  if (x.next() != '>') {
                     x.back();
                  }
               } else if (c != ':') {
                  throw x.syntaxError("Expected a ':' after a key");
               }

               this.putOnce(key, x.nextValue());
               switch(x.nextClean()) {
               case ',':
               case ';':
                  if (x.nextClean() == '}') {
                     return;
                  }

                  x.back();
                  break;
               case '}':
                  return;
               default:
                  throw x.syntaxError("Expected a ',' or '}'");
               }
            }
         }
      }
   }

   public JSONObject(Map map) {
      this.map = new HashMap();
      if (map != null) {
         Iterator i = map.entrySet().iterator();

         while(i.hasNext()) {
            Entry e = (Entry)i.next();
            this.map.put(e.getKey(), wrap(e.getValue()));
         }
      }

   }

   public JSONObject(Object bean) {
      this();
      this.populateMap(bean);
   }

   public JSONObject(Object object, String[] names) {
      this();
      Class c = object.getClass();

      for(int i = 0; i < names.length; ++i) {
         String name = names[i];

         try {
            this.putOpt(name, c.getField(name).get(object));
         } catch (Exception var7) {
         }
      }

   }

   public JSONObject(String source) throws JSONException {
      this(new JSONTokener(source));
   }

   public JSONObject accumulate(String key, Object value) throws JSONException {
      testValidity(value);
      Object o = this.opt(key);
      if (o == null) {
         this.put(key, value instanceof JSONArray ? (new JSONArray()).put(value) : value);
      } else if (o instanceof JSONArray) {
         ((JSONArray)o).put(value);
      } else {
         this.put(key, (Object)(new JSONArray()).put(o).put(value));
      }

      return this;
   }

   public JSONObject append(String key, Object value) throws JSONException {
      testValidity(value);
      Object o = this.opt(key);
      if (o == null) {
         this.put(key, (Object)(new JSONArray()).put(value));
      } else {
         if (!(o instanceof JSONArray)) {
            throw new JSONException("JSONObject[" + key + "] is not a JSONArray.");
         }

         this.put(key, (Object)((JSONArray)o).put(value));
      }

      return this;
   }

   public static String doubleToString(double d) {
      if (!Double.isInfinite(d) && !Double.isNaN(d)) {
         String s = Double.toString(d);
         if (s.indexOf(46) > 0 && s.indexOf(101) < 0 && s.indexOf(69) < 0) {
            while(s.endsWith("0")) {
               s = s.substring(0, s.length() - 1);
            }

            if (s.endsWith(".")) {
               s = s.substring(0, s.length() - 1);
            }
         }

         return s;
      } else {
         return "null";
      }
   }

   public Object get(String key) throws JSONException {
      Object o = this.opt(key);
      if (o == null) {
         throw new JSONException("JSONObject[" + quote(key) + "] not found.");
      } else {
         return o;
      }
   }

   public boolean getBoolean(String key) throws JSONException {
      Object o = this.get(key);
      if (!o.equals(Boolean.FALSE) && (!(o instanceof String) || !((String)o).equalsIgnoreCase("false"))) {
         if (!o.equals(Boolean.TRUE) && (!(o instanceof String) || !((String)o).equalsIgnoreCase("true"))) {
            throw new JSONException("JSONObject[" + quote(key) + "] is not a Boolean.");
         } else {
            return true;
         }
      } else {
         return false;
      }
   }

   public double getDouble(String key) throws JSONException {
      Object o = this.get(key);

      try {
         return o instanceof Number ? ((Number)o).doubleValue() : Double.valueOf((String)o);
      } catch (Exception var4) {
         throw new JSONException("JSONObject[" + quote(key) + "] is not a number.");
      }
   }

   public int getInt(String key) throws JSONException {
      Object o = this.get(key);
      return o instanceof Number ? ((Number)o).intValue() : (int)this.getDouble(key);
   }

   public JSONArray getJSONArray(String key) throws JSONException {
      Object o = this.get(key);
      if (o instanceof JSONArray) {
         return (JSONArray)o;
      } else {
         throw new JSONException("JSONObject[" + quote(key) + "] is not a JSONArray.");
      }
   }

   public JSONObject getJSONObject(String key) throws JSONException {
      Object o = this.get(key);
      if (o instanceof JSONObject) {
         return (JSONObject)o;
      } else {
         throw new JSONException("JSONObject[" + quote(key) + "] is not a JSONObject.");
      }
   }

   public long getLong(String key) throws JSONException {
      Object o = this.get(key);
      return o instanceof Number ? ((Number)o).longValue() : (long)this.getDouble(key);
   }

   public static String[] getNames(JSONObject jo) {
      int length = jo.length();
      if (length == 0) {
         return null;
      } else {
         Iterator i = jo.keys();
         String[] names = new String[length];

         for(int j = 0; i.hasNext(); ++j) {
            names[j] = (String)i.next();
         }

         return names;
      }
   }

   public static String[] getNames(Object object) {
      if (object == null) {
         return null;
      } else {
         Class klass = object.getClass();
         Field[] fields = klass.getFields();
         int length = fields.length;
         if (length == 0) {
            return null;
         } else {
            String[] names = new String[length];

            for(int i = 0; i < length; ++i) {
               names[i] = fields[i].getName();
            }

            return names;
         }
      }
   }

   public String getString(String key) throws JSONException {
      return this.get(key).toString();
   }

   public boolean has(String key) {
      return this.map.containsKey(key);
   }

   public JSONObject increment(String key) throws JSONException {
      Object value = this.opt(key);
      if (value == null) {
         this.put(key, 1);
      } else if (value instanceof Integer) {
         this.put(key, (Integer)value + 1);
      } else if (value instanceof Long) {
         this.put(key, (Long)value + 1L);
      } else if (value instanceof Double) {
         this.put(key, (Double)value + 1.0D);
      } else {
         if (!(value instanceof Float)) {
            throw new JSONException("Unable to increment [" + key + "].");
         }

         this.put(key, (double)((Float)value + 1.0F));
      }

      return this;
   }

   public boolean isNull(String key) {
      return NULL.equals(this.opt(key));
   }

   public Iterator keys() {
      return this.map.keySet().iterator();
   }

   public int length() {
      return this.map.size();
   }

   public JSONArray names() {
      JSONArray ja = new JSONArray();
      Iterator keys = this.keys();

      while(keys.hasNext()) {
         ja.put(keys.next());
      }

      return ja.length() == 0 ? null : ja;
   }

   public static String numberToString(Number n) throws JSONException {
      if (n == null) {
         throw new JSONException("Null pointer");
      } else {
         testValidity(n);
         String s = n.toString();
         if (s.indexOf(46) > 0 && s.indexOf(101) < 0 && s.indexOf(69) < 0) {
            while(s.endsWith("0")) {
               s = s.substring(0, s.length() - 1);
            }

            if (s.endsWith(".")) {
               s = s.substring(0, s.length() - 1);
            }
         }

         return s;
      }
   }

   public Object opt(String key) {
      return key == null ? null : this.map.get(key);
   }

   public boolean optBoolean(String key) {
      return this.optBoolean(key, false);
   }

   public boolean optBoolean(String key, boolean defaultValue) {
      try {
         return this.getBoolean(key);
      } catch (Exception var4) {
         return defaultValue;
      }
   }

   public double optDouble(String key) {
      return this.optDouble(key, Double.NaN);
   }

   public double optDouble(String key, double defaultValue) {
      try {
         Object o = this.opt(key);
         return o instanceof Number ? ((Number)o).doubleValue() : new Double((String)o);
      } catch (Exception var5) {
         return defaultValue;
      }
   }

   public int optInt(String key) {
      return this.optInt(key, 0);
   }

   public int optInt(String key, int defaultValue) {
      try {
         return this.getInt(key);
      } catch (Exception var4) {
         return defaultValue;
      }
   }

   public JSONArray optJSONArray(String key) {
      Object o = this.opt(key);
      return o instanceof JSONArray ? (JSONArray)o : null;
   }

   public JSONObject optJSONObject(String key) {
      Object o = this.opt(key);
      return o instanceof JSONObject ? (JSONObject)o : null;
   }

   public long optLong(String key) {
      return this.optLong(key, 0L);
   }

   public long optLong(String key, long defaultValue) {
      try {
         return this.getLong(key);
      } catch (Exception var5) {
         return defaultValue;
      }
   }

   public String optString(String key) {
      return this.optString(key, "");
   }

   public String optString(String key, String defaultValue) {
      Object o = this.opt(key);
      return o != null ? o.toString() : defaultValue;
   }

   private void populateMap(Object bean) {
      Class klass = bean.getClass();
      boolean includeSuperClass = klass.getClassLoader() != null;
      Method[] methods = includeSuperClass ? klass.getMethods() : klass.getDeclaredMethods();

      for(int i = 0; i < methods.length; ++i) {
         try {
            Method method = methods[i];
            if (Modifier.isPublic(method.getModifiers())) {
               String name = method.getName();
               String key = "";
               if (name.startsWith("get")) {
                  if (!name.equals("getClass") && !name.equals("getDeclaringClass")) {
                     key = name.substring(3);
                  } else {
                     key = "";
                  }
               } else if (name.startsWith("is")) {
                  key = name.substring(2);
               }

               if (key.length() > 0 && Character.isUpperCase(key.charAt(0)) && method.getParameterTypes().length == 0) {
                  if (key.length() == 1) {
                     key = key.toLowerCase();
                  } else if (!Character.isUpperCase(key.charAt(1))) {
                     key = key.substring(0, 1).toLowerCase() + key.substring(1);
                  }

                  Object result = method.invoke(bean, (Object[])null);
                  this.map.put(key, wrap(result));
               }
            }
         } catch (Exception var10) {
         }
      }

   }

   public JSONObject put(String key, boolean value) throws JSONException {
      this.put(key, (Object)(value ? Boolean.TRUE : Boolean.FALSE));
      return this;
   }

   public JSONObject put(String key, Collection value) throws JSONException {
      this.put(key, (Object)(new JSONArray(value)));
      return this;
   }

   public JSONObject put(String key, double value) throws JSONException {
      this.put(key, (Object)(new Double(value)));
      return this;
   }

   public JSONObject put(String key, int value) throws JSONException {
      this.put(key, (Object)(new Integer(value)));
      return this;
   }

   public JSONObject put(String key, long value) throws JSONException {
      this.put(key, (Object)(new Long(value)));
      return this;
   }

   public JSONObject put(String key, Map value) throws JSONException {
      this.put(key, (Object)(new JSONObject(value)));
      return this;
   }

   public JSONObject put(String key, Object value) throws JSONException {
      if (key == null) {
         throw new JSONException("Null key.");
      } else {
         if (value != null) {
            testValidity(value);
            this.map.put(key, value);
         } else {
            this.remove(key);
         }

         return this;
      }
   }

   public JSONObject putOnce(String key, Object value) throws JSONException {
      if (key != null && value != null) {
         if (this.opt(key) != null) {
            throw new JSONException("Duplicate key \"" + key + "\"");
         }

         this.put(key, value);
      }

      return this;
   }

   public JSONObject putOpt(String key, Object value) throws JSONException {
      if (key != null && value != null) {
         this.put(key, value);
      }

      return this;
   }

   public static String quote(String string) {
      if (string != null && string.length() != 0) {
         char c = 0;
         int len = string.length();
         StringBuffer sb = new StringBuffer(len + 4);
         sb.append('"');

         for(int i = 0; i < len; ++i) {
            char b = c;
            c = string.charAt(i);
            switch(c) {
            case '\b':
               sb.append("\\b");
               continue;
            case '\t':
               sb.append("\\t");
               continue;
            case '\n':
               sb.append("\\n");
               continue;
            case '\f':
               sb.append("\\f");
               continue;
            case '\r':
               sb.append("\\r");
               continue;
            case '"':
            case '\\':
               sb.append('\\');
               sb.append(c);
               continue;
            case '/':
               if (b == '<') {
                  sb.append('\\');
               }

               sb.append(c);
               continue;
            }

            if (c >= ' ' && (c < 128 || c >= 160) && (c < 8192 || c >= 8448)) {
               sb.append(c);
            } else {
               String t = "000" + Integer.toHexString(c);
               sb.append("\\u" + t.substring(t.length() - 4));
            }
         }

         sb.append('"');
         return sb.toString();
      } else {
         return "\"\"";
      }
   }

   public Object remove(String key) {
      return this.map.remove(key);
   }

   public Iterator sortedKeys() {
      return (new TreeSet(this.map.keySet())).iterator();
   }

   public static Object stringToValue(String s) {
      if (s.equals("")) {
         return s;
      } else if (s.equalsIgnoreCase("true")) {
         return Boolean.TRUE;
      } else if (s.equalsIgnoreCase("false")) {
         return Boolean.FALSE;
      } else if (s.equalsIgnoreCase("null")) {
         return NULL;
      } else {
         char b = s.charAt(0);
         if (b >= '0' && b <= '9' || b == '.' || b == '-' || b == '+') {
            if (b == '0' && s.length() > 2 && (s.charAt(1) == 'x' || s.charAt(1) == 'X')) {
               try {
                  return new Integer(Integer.parseInt(s.substring(2), 16));
               } catch (Exception var4) {
               }
            }

            try {
               if (s.indexOf(46) <= -1 && s.indexOf(101) <= -1 && s.indexOf(69) <= -1) {
                  Long myLong = new Long(s);
                  if (myLong == (long)myLong.intValue()) {
                     return new Integer(myLong.intValue());
                  }

                  return myLong;
               }

               return Double.valueOf(s);
            } catch (Exception var3) {
            }
         }

         return s;
      }
   }

   static void testValidity(Object o) throws JSONException {
      if (o != null) {
         if (o instanceof Double) {
            if (((Double)o).isInfinite() || ((Double)o).isNaN()) {
               throw new JSONException("JSON does not allow non-finite numbers.");
            }
         } else if (o instanceof Float && (((Float)o).isInfinite() || ((Float)o).isNaN())) {
            throw new JSONException("JSON does not allow non-finite numbers.");
         }
      }

   }

   public JSONArray toJSONArray(JSONArray names) throws JSONException {
      if (names != null && names.length() != 0) {
         JSONArray ja = new JSONArray();

         for(int i = 0; i < names.length(); ++i) {
            ja.put(this.opt(names.getString(i)));
         }

         return ja;
      } else {
         return null;
      }
   }

   public String toString() {
      try {
         Iterator keys = this.keys();
         StringBuffer sb = new StringBuffer("{");

         while(keys.hasNext()) {
            if (sb.length() > 1) {
               sb.append(',');
            }

            Object o = keys.next();
            sb.append(quote(o.toString()));
            sb.append(':');
            sb.append(valueToString(this.map.get(o)));
         }

         sb.append('}');
         return sb.toString();
      } catch (Exception var4) {
         return null;
      }
   }

   public String toString(int indentFactor) throws JSONException {
      return this.toString(indentFactor, 0);
   }

   String toString(int indentFactor, int indent) throws JSONException {
      int n = this.length();
      if (n == 0) {
         return "{}";
      } else {
         Iterator keys = this.sortedKeys();
         StringBuffer sb = new StringBuffer("{");
         int newindent = indent + indentFactor;
         Object o;
         if (n == 1) {
            o = keys.next();
            sb.append(quote(o.toString()));
            sb.append(": ");
            sb.append(valueToString(this.map.get(o), indentFactor, indent));
         } else {
            while(true) {
               int j;
               if (!keys.hasNext()) {
                  if (sb.length() > 1) {
                     sb.append('\n');

                     for(j = 0; j < indent; ++j) {
                        sb.append(' ');
                     }
                  }
                  break;
               }

               o = keys.next();
               if (sb.length() > 1) {
                  sb.append(",\n");
               } else {
                  sb.append('\n');
               }

               for(j = 0; j < newindent; ++j) {
                  sb.append(' ');
               }

               sb.append(quote(o.toString()));
               sb.append(": ");
               sb.append(valueToString(this.map.get(o), indentFactor, newindent));
            }
         }

         sb.append('}');
         return sb.toString();
      }
   }

   static String valueToString(Object value) throws JSONException {
      if (value != null && !value.equals((Object)null)) {
         if (value instanceof JSONString) {
            String o;
            try {
               o = ((JSONString)value).toJSONString();
            } catch (Exception var3) {
               throw new JSONException(var3);
            }

            if (o instanceof String) {
               return (String)o;
            } else {
               throw new JSONException("Bad value from toJSONString: " + o);
            }
         } else if (value instanceof Number) {
            return numberToString((Number)value);
         } else if (!(value instanceof Boolean) && !(value instanceof JSONObject) && !(value instanceof JSONArray)) {
            if (value instanceof Map) {
               return (new JSONObject((Map)value)).toString();
            } else if (value instanceof Collection) {
               return (new JSONArray((Collection)value)).toString();
            } else {
               return value.getClass().isArray() ? (new JSONArray(value)).toString() : quote(value.toString());
            }
         } else {
            return value.toString();
         }
      } else {
         return "null";
      }
   }

   static String valueToString(Object value, int indentFactor, int indent) throws JSONException {
      if (value != null && !value.equals((Object)null)) {
         try {
            if (value instanceof JSONString) {
               Object o = ((JSONString)value).toJSONString();
               if (o instanceof String) {
                  return (String)o;
               }
            }
         } catch (Exception var4) {
         }

         if (value instanceof Number) {
            return numberToString((Number)value);
         } else if (value instanceof Boolean) {
            return value.toString();
         } else if (value instanceof JSONObject) {
            return ((JSONObject)value).toString(indentFactor, indent);
         } else if (value instanceof JSONArray) {
            return ((JSONArray)value).toString(indentFactor, indent);
         } else if (value instanceof Map) {
            return (new JSONObject((Map)value)).toString(indentFactor, indent);
         } else if (value instanceof Collection) {
            return (new JSONArray((Collection)value)).toString(indentFactor, indent);
         } else {
            return value.getClass().isArray() ? (new JSONArray(value)).toString(indentFactor, indent) : quote(value.toString());
         }
      } else {
         return "null";
      }
   }

   static Object wrap(Object object) {
      try {
         if (object == null) {
            return NULL;
         } else if (!(object instanceof JSONObject) && !(object instanceof JSONArray) && !(object instanceof Byte) && !(object instanceof Character) && !(object instanceof Short) && !(object instanceof Integer) && !(object instanceof Long) && !(object instanceof Boolean) && !(object instanceof Float) && !(object instanceof Double) && !(object instanceof String) && !NULL.equals(object)) {
            if (object instanceof Collection) {
               return new JSONArray((Collection)object);
            } else if (object.getClass().isArray()) {
               return new JSONArray(object);
            } else if (object instanceof Map) {
               return new JSONObject((Map)object);
            } else {
               Package objectPackage = object.getClass().getPackage();
               String objectPackageName = objectPackage != null ? objectPackage.getName() : "";
               return !objectPackageName.startsWith("java.") && !objectPackageName.startsWith("javax.") && object.getClass().getClassLoader() != null ? new JSONObject(object) : object.toString();
            }
         } else {
            return object;
         }
      } catch (Exception var3) {
         return null;
      }
   }

   public Writer write(Writer writer) throws JSONException {
      try {
         boolean b = false;
         Iterator keys = this.keys();
         writer.write(123);

         for(; keys.hasNext(); b = true) {
            if (b) {
               writer.write(44);
            }

            Object k = keys.next();
            writer.write(quote(k.toString()));
            writer.write(58);
            Object v = this.map.get(k);
            if (v instanceof JSONObject) {
               ((JSONObject)v).write(writer);
            } else if (v instanceof JSONArray) {
               ((JSONArray)v).write(writer);
            } else {
               writer.write(valueToString(v));
            }
         }

         writer.write(125);
         return writer;
      } catch (IOException var6) {
         throw new JSONException(var6);
      }
   }

   private static final class Null {
      private Null() {
      }

      protected final Object clone() {
         return this;
      }

      public boolean equals(Object object) {
         return object == null || object == this;
      }

      public String toString() {
         return "null";
      }

      // $FF: synthetic method
      Null(Object x0) {
         this();
      }
   }
}

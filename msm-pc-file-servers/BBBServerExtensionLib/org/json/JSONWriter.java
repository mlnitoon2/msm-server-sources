package org.json;

import java.io.IOException;
import java.io.Writer;

public class JSONWriter {
   private static final int maxdepth = 20;
   private boolean comma = false;
   protected char mode = 'i';
   private JSONObject[] stack = new JSONObject[20];
   private int top = 0;
   protected Writer writer;

   public JSONWriter(Writer w) {
      this.writer = w;
   }

   private JSONWriter append(String s) throws JSONException {
      if (s == null) {
         throw new JSONException("Null pointer");
      } else if (this.mode != 'o' && this.mode != 'a') {
         throw new JSONException("Value out of sequence.");
      } else {
         try {
            if (this.comma && this.mode == 'a') {
               this.writer.write(44);
            }

            this.writer.write(s);
         } catch (IOException var3) {
            throw new JSONException(var3);
         }

         if (this.mode == 'o') {
            this.mode = 'k';
         }

         this.comma = true;
         return this;
      }
   }

   public JSONWriter array() throws JSONException {
      if (this.mode != 'i' && this.mode != 'o' && this.mode != 'a') {
         throw new JSONException("Misplaced array.");
      } else {
         this.push((JSONObject)null);
         this.append("[");
         this.comma = false;
         return this;
      }
   }

   private JSONWriter end(char m, char c) throws JSONException {
      if (this.mode != m) {
         throw new JSONException(m == 'o' ? "Misplaced endObject." : "Misplaced endArray.");
      } else {
         this.pop(m);

         try {
            this.writer.write(c);
         } catch (IOException var4) {
            throw new JSONException(var4);
         }

         this.comma = true;
         return this;
      }
   }

   public JSONWriter endArray() throws JSONException {
      return this.end('a', ']');
   }

   public JSONWriter endObject() throws JSONException {
      return this.end('k', '}');
   }

   public JSONWriter key(String s) throws JSONException {
      if (s == null) {
         throw new JSONException("Null key.");
      } else if (this.mode == 'k') {
         try {
            this.stack[this.top - 1].putOnce(s, Boolean.TRUE);
            if (this.comma) {
               this.writer.write(44);
            }

            this.writer.write(JSONObject.quote(s));
            this.writer.write(58);
            this.comma = false;
            this.mode = 'o';
            return this;
         } catch (IOException var3) {
            throw new JSONException(var3);
         }
      } else {
         throw new JSONException("Misplaced key.");
      }
   }

   public JSONWriter object() throws JSONException {
      if (this.mode == 'i') {
         this.mode = 'o';
      }

      if (this.mode != 'o' && this.mode != 'a') {
         throw new JSONException("Misplaced object.");
      } else {
         this.append("{");
         this.push(new JSONObject());
         this.comma = false;
         return this;
      }
   }

   private void pop(char c) throws JSONException {
      if (this.top <= 0) {
         throw new JSONException("Nesting error.");
      } else {
         char m = this.stack[this.top - 1] == null ? 97 : 107;
         if (m != c) {
            throw new JSONException("Nesting error.");
         } else {
            --this.top;
            this.mode = (char)(this.top == 0 ? 100 : (this.stack[this.top - 1] == null ? 97 : 107));
         }
      }
   }

   private void push(JSONObject jo) throws JSONException {
      if (this.top >= 20) {
         throw new JSONException("Nesting too deep.");
      } else {
         this.stack[this.top] = jo;
         this.mode = (char)(jo == null ? 97 : 107);
         ++this.top;
      }
   }

   public JSONWriter value(boolean b) throws JSONException {
      return this.append(b ? "true" : "false");
   }

   public JSONWriter value(double d) throws JSONException {
      return this.value(new Double(d));
   }

   public JSONWriter value(long l) throws JSONException {
      return this.append(Long.toString(l));
   }

   public JSONWriter value(Object o) throws JSONException {
      return this.append(JSONObject.valueToString(o));
   }
}

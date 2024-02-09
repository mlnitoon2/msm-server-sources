package com.bigbluebubble.BBBServer.util;

import java.util.Arrays;

public class MigBase64 {
   private static final char[] CA = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();
   private static final int[] IA = new int[256];

   public static final char[] encodeToChar(byte[] sArr, boolean lineSep) {
      int sLen = sArr != null ? sArr.length : 0;
      if (sLen == 0) {
         return new char[0];
      } else {
         int eLen = sLen / 3 * 3;
         int cCnt = (sLen - 1) / 3 + 1 << 2;
         int dLen = cCnt + (lineSep ? (cCnt - 1) / 76 << 1 : 0);
         char[] dArr = new char[dLen];
         int left = 0;
         int d = 0;
         int cc = 0;

         while(left < eLen) {
            int i = (sArr[left++] & 255) << 16 | (sArr[left++] & 255) << 8 | sArr[left++] & 255;
            dArr[d++] = CA[i >>> 18 & 63];
            dArr[d++] = CA[i >>> 12 & 63];
            dArr[d++] = CA[i >>> 6 & 63];
            dArr[d++] = CA[i & 63];
            if (lineSep) {
               ++cc;
               if (cc == 19 && d < dLen - 2) {
                  dArr[d++] = '\r';
                  dArr[d++] = '\n';
                  cc = 0;
               }
            }
         }

         left = sLen - eLen;
         if (left > 0) {
            d = (sArr[eLen] & 255) << 10 | (left == 2 ? (sArr[sLen - 1] & 255) << 2 : 0);
            dArr[dLen - 4] = CA[d >> 12];
            dArr[dLen - 3] = CA[d >>> 6 & 63];
            dArr[dLen - 2] = left == 2 ? CA[d & 63] : 61;
            dArr[dLen - 1] = '=';
         }

         return dArr;
      }
   }

   public static final byte[] decode(char[] sArr) {
      int sLen = sArr != null ? sArr.length : 0;
      if (sLen == 0) {
         return new byte[0];
      } else {
         int sepCnt = 0;

         int pad;
         for(pad = 0; pad < sLen; ++pad) {
            if (IA[sArr[pad]] < 0) {
               ++sepCnt;
            }
         }

         if ((sLen - sepCnt) % 4 != 0) {
            return null;
         } else {
            pad = 0;
            int len = sLen;

            while(len > 1) {
               --len;
               if (IA[sArr[len]] > 0) {
                  break;
               }

               if (sArr[len] == '=') {
                  ++pad;
               }
            }

            len = ((sLen - sepCnt) * 6 >> 3) - pad;
            byte[] dArr = new byte[len];
            int s = 0;
            int d = 0;

            while(d < len) {
               int i = 0;

               for(int j = 0; j < 4; ++j) {
                  int c = IA[sArr[s++]];
                  if (c >= 0) {
                     i |= c << 18 - j * 6;
                  } else {
                     --j;
                  }
               }

               dArr[d++] = (byte)(i >> 16);
               if (d < len) {
                  dArr[d++] = (byte)(i >> 8);
                  if (d < len) {
                     dArr[d++] = (byte)i;
                  }
               }
            }

            return dArr;
         }
      }
   }

   public static final byte[] decodeFast(char[] sArr) {
      int sLen = sArr.length;
      if (sLen == 0) {
         return new byte[0];
      } else {
         int sIx = 0;

         int eIx;
         for(eIx = sLen - 1; sIx < eIx && IA[sArr[sIx]] < 0; ++sIx) {
         }

         while(eIx > 0 && IA[sArr[eIx]] < 0) {
            --eIx;
         }

         int pad = sArr[eIx] == '=' ? (sArr[eIx - 1] == '=' ? 2 : 1) : 0;
         int cCnt = eIx - sIx + 1;
         int sepCnt = sLen > 76 ? (sArr[76] == '\r' ? cCnt / 78 : 0) << 1 : 0;
         int len = ((cCnt - sepCnt) * 6 >> 3) - pad;
         byte[] dArr = new byte[len];
         int d = 0;
         int i = 0;
         int r = len / 3 * 3;

         while(d < r) {
            int i = IA[sArr[sIx++]] << 18 | IA[sArr[sIx++]] << 12 | IA[sArr[sIx++]] << 6 | IA[sArr[sIx++]];
            dArr[d++] = (byte)(i >> 16);
            dArr[d++] = (byte)(i >> 8);
            dArr[d++] = (byte)i;
            if (sepCnt > 0) {
               ++i;
               if (i == 19) {
                  sIx += 2;
                  i = 0;
               }
            }
         }

         if (d < len) {
            i = 0;

            for(r = 0; sIx <= eIx - pad; ++r) {
               i |= IA[sArr[sIx++]] << 18 - r * 6;
            }

            for(r = 16; d < len; r -= 8) {
               dArr[d++] = (byte)(i >> r);
            }
         }

         return dArr;
      }
   }

   public static final byte[] encodeToByte(byte[] sArr, boolean lineSep) {
      int sLen = sArr != null ? sArr.length : 0;
      if (sLen == 0) {
         return new byte[0];
      } else {
         int eLen = sLen / 3 * 3;
         int cCnt = (sLen - 1) / 3 + 1 << 2;
         int dLen = cCnt + (lineSep ? (cCnt - 1) / 76 << 1 : 0);
         byte[] dArr = new byte[dLen];
         int left = 0;
         int d = 0;
         int cc = 0;

         while(left < eLen) {
            int i = (sArr[left++] & 255) << 16 | (sArr[left++] & 255) << 8 | sArr[left++] & 255;
            dArr[d++] = (byte)CA[i >>> 18 & 63];
            dArr[d++] = (byte)CA[i >>> 12 & 63];
            dArr[d++] = (byte)CA[i >>> 6 & 63];
            dArr[d++] = (byte)CA[i & 63];
            if (lineSep) {
               ++cc;
               if (cc == 19 && d < dLen - 2) {
                  dArr[d++] = 13;
                  dArr[d++] = 10;
                  cc = 0;
               }
            }
         }

         left = sLen - eLen;
         if (left > 0) {
            d = (sArr[eLen] & 255) << 10 | (left == 2 ? (sArr[sLen - 1] & 255) << 2 : 0);
            dArr[dLen - 4] = (byte)CA[d >> 12];
            dArr[dLen - 3] = (byte)CA[d >>> 6 & 63];
            dArr[dLen - 2] = left == 2 ? (byte)CA[d & 63] : 61;
            dArr[dLen - 1] = 61;
         }

         return dArr;
      }
   }

   public static final byte[] decode(byte[] sArr) {
      int sLen = sArr.length;
      int sepCnt = 0;

      int pad;
      for(pad = 0; pad < sLen; ++pad) {
         if (IA[sArr[pad] & 255] < 0) {
            ++sepCnt;
         }
      }

      if ((sLen - sepCnt) % 4 != 0) {
         return null;
      } else {
         pad = 0;
         int len = sLen;

         while(len > 1) {
            --len;
            if (IA[sArr[len] & 255] > 0) {
               break;
            }

            if (sArr[len] == 61) {
               ++pad;
            }
         }

         len = ((sLen - sepCnt) * 6 >> 3) - pad;
         byte[] dArr = new byte[len];
         int s = 0;
         int d = 0;

         while(d < len) {
            int i = 0;

            for(int j = 0; j < 4; ++j) {
               int c = IA[sArr[s++] & 255];
               if (c >= 0) {
                  i |= c << 18 - j * 6;
               } else {
                  --j;
               }
            }

            dArr[d++] = (byte)(i >> 16);
            if (d < len) {
               dArr[d++] = (byte)(i >> 8);
               if (d < len) {
                  dArr[d++] = (byte)i;
               }
            }
         }

         return dArr;
      }
   }

   public static final byte[] decodeFast(byte[] sArr) {
      int sLen = sArr.length;
      if (sLen == 0) {
         return new byte[0];
      } else {
         int sIx = 0;

         int eIx;
         for(eIx = sLen - 1; sIx < eIx && IA[sArr[sIx] & 255] < 0; ++sIx) {
         }

         while(eIx > 0 && IA[sArr[eIx] & 255] < 0) {
            --eIx;
         }

         int pad = sArr[eIx] == 61 ? (sArr[eIx - 1] == 61 ? 2 : 1) : 0;
         int cCnt = eIx - sIx + 1;
         int sepCnt = sLen > 76 ? (sArr[76] == 13 ? cCnt / 78 : 0) << 1 : 0;
         int len = ((cCnt - sepCnt) * 6 >> 3) - pad;
         byte[] dArr = new byte[len];
         int d = 0;
         int i = 0;
         int r = len / 3 * 3;

         while(d < r) {
            int i = IA[sArr[sIx++]] << 18 | IA[sArr[sIx++]] << 12 | IA[sArr[sIx++]] << 6 | IA[sArr[sIx++]];
            dArr[d++] = (byte)(i >> 16);
            dArr[d++] = (byte)(i >> 8);
            dArr[d++] = (byte)i;
            if (sepCnt > 0) {
               ++i;
               if (i == 19) {
                  sIx += 2;
                  i = 0;
               }
            }
         }

         if (d < len) {
            i = 0;

            for(r = 0; sIx <= eIx - pad; ++r) {
               i |= IA[sArr[sIx++]] << 18 - r * 6;
            }

            for(r = 16; d < len; r -= 8) {
               dArr[d++] = (byte)(i >> r);
            }
         }

         return dArr;
      }
   }

   public static final String encodeToString(byte[] sArr, boolean lineSep) {
      return new String(encodeToChar(sArr, lineSep));
   }

   public static final byte[] decode(String str) {
      int sLen = str != null ? str.length() : 0;
      if (sLen == 0) {
         return new byte[0];
      } else {
         int sepCnt = 0;

         int pad;
         for(pad = 0; pad < sLen; ++pad) {
            if (IA[str.charAt(pad)] < 0) {
               ++sepCnt;
            }
         }

         if ((sLen - sepCnt) % 4 != 0) {
            return null;
         } else {
            pad = 0;
            int len = sLen;

            while(len > 1) {
               --len;
               if (IA[str.charAt(len)] > 0) {
                  break;
               }

               if (str.charAt(len) == '=') {
                  ++pad;
               }
            }

            len = ((sLen - sepCnt) * 6 >> 3) - pad;
            byte[] dArr = new byte[len];
            int s = 0;
            int d = 0;

            while(d < len) {
               int i = 0;

               for(int j = 0; j < 4; ++j) {
                  int c = IA[str.charAt(s++)];
                  if (c >= 0) {
                     i |= c << 18 - j * 6;
                  } else {
                     --j;
                  }
               }

               dArr[d++] = (byte)(i >> 16);
               if (d < len) {
                  dArr[d++] = (byte)(i >> 8);
                  if (d < len) {
                     dArr[d++] = (byte)i;
                  }
               }
            }

            return dArr;
         }
      }
   }

   public static final byte[] decodeFast(String s) {
      int sLen = s.length();
      if (sLen == 0) {
         return new byte[0];
      } else {
         int sIx = 0;

         int eIx;
         for(eIx = sLen - 1; sIx < eIx && IA[s.charAt(sIx) & 255] < 0; ++sIx) {
         }

         while(eIx > 0 && IA[s.charAt(eIx) & 255] < 0) {
            --eIx;
         }

         int pad = s.charAt(eIx) == '=' ? (s.charAt(eIx - 1) == '=' ? 2 : 1) : 0;
         int cCnt = eIx - sIx + 1;
         int sepCnt = sLen > 76 ? (s.charAt(76) == '\r' ? cCnt / 78 : 0) << 1 : 0;
         int len = ((cCnt - sepCnt) * 6 >> 3) - pad;
         byte[] dArr = new byte[len];
         int d = 0;
         int i = 0;
         int r = len / 3 * 3;

         while(d < r) {
            int i = IA[s.charAt(sIx++)] << 18 | IA[s.charAt(sIx++)] << 12 | IA[s.charAt(sIx++)] << 6 | IA[s.charAt(sIx++)];
            dArr[d++] = (byte)(i >> 16);
            dArr[d++] = (byte)(i >> 8);
            dArr[d++] = (byte)i;
            if (sepCnt > 0) {
               ++i;
               if (i == 19) {
                  sIx += 2;
                  i = 0;
               }
            }
         }

         if (d < len) {
            i = 0;

            for(r = 0; sIx <= eIx - pad; ++r) {
               i |= IA[s.charAt(sIx++)] << 18 - r * 6;
            }

            for(r = 16; d < len; r -= 8) {
               dArr[d++] = (byte)(i >> r);
            }
         }

         return dArr;
      }
   }

   static {
      Arrays.fill(IA, -1);
      int i = 0;

      for(int iS = CA.length; i < iS; IA[CA[i]] = i++) {
      }

      IA[61] = 0;
   }
}

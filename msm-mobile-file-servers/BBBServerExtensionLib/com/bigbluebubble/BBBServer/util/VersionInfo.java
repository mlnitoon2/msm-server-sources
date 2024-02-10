package com.bigbluebubble.BBBServer.util;

public class VersionInfo implements Comparable<VersionInfo> {
   private int major;
   private int minor;
   private int rev;

   public VersionInfo(String s) {
      this.major = this.minor = this.rev = 0;
      if (s != null) {
         String[] parts = s.split("\\.");
         if (parts.length >= 1) {
            try {
               this.major = Integer.parseInt(parts[0]);
            } catch (NumberFormatException var6) {
            }
         }

         if (parts.length >= 2) {
            try {
               this.minor = Integer.parseInt(parts[1]);
            } catch (NumberFormatException var5) {
            }
         }

         if (parts.length >= 3) {
            try {
               this.rev = Integer.parseInt(parts[2]);
            } catch (NumberFormatException var4) {
            }
         }
      }

   }

   public VersionInfo(int mjr, int mnr, int rv) {
      this.major = mjr;
      this.minor = mnr;
      this.rev = rv;
   }

   public VersionInfo(VersionInfo v) {
      this(v.getMajor(), v.getMinor(), v.getRev());
   }

   public String toString() {
      String s = "";
      s = s + this.major + "." + this.minor + "." + this.rev;
      return s;
   }

   public int compareTo(VersionInfo info) {
      return this.compareTo(info.major, info.minor, info.rev);
   }

   public boolean lessThan(VersionInfo info) {
      return this.compareTo(info) < 0;
   }

   public boolean lessThanEqual(VersionInfo info) {
      return this.compareTo(info) <= 0;
   }

   public boolean greaterThan(VersionInfo info) {
      return this.compareTo(info) > 0;
   }

   public boolean greaterThanEqual(VersionInfo info) {
      return this.compareTo(info) >= 0;
   }

   public int compareTo(int mjr, int mnr, int rv) {
      if (this.major < mjr) {
         return -3;
      } else if (this.major > mjr) {
         return 3;
      } else if (this.minor < mnr) {
         return -2;
      } else if (this.minor > mnr) {
         return 2;
      } else if (this.rev < rv) {
         return -1;
      } else {
         return this.rev > rv ? 1 : 0;
      }
   }

   public int compareTo(String s) {
      int maj = 0;
      int min = 0;
      int r = 0;
      if (s != null) {
         String[] parts = s.split("\\.");
         if (parts.length >= 1) {
            try {
               maj = Integer.parseInt(parts[0]);
            } catch (NumberFormatException var9) {
            }
         }

         if (parts.length >= 2) {
            try {
               min = Integer.parseInt(parts[1]);
            } catch (NumberFormatException var8) {
            }
         }

         if (parts.length >= 3) {
            try {
               r = Integer.parseInt(parts[2]);
            } catch (NumberFormatException var7) {
            }
         }
      }

      return this.compareTo(maj, min, r);
   }

   public boolean equals(Object o) {
      if (o instanceof VersionInfo) {
         return this.compareTo((VersionInfo)o) == 0;
      } else {
         return false;
      }
   }

   public int getMajor() {
      return this.major;
   }

   public int getMinor() {
      return this.minor;
   }

   public int getRev() {
      return this.rev;
   }
}

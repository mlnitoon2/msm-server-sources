package com.bigbluebubble.mysingingmonsters.exceptions;

public class InvalidPlayerException extends Exception {
   private static final long serialVersionUID = 2761403409396064496L;

   public InvalidPlayerException(String s) {
      super(s);
   }
}

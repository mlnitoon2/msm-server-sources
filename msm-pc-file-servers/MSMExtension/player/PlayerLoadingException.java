package com.bigbluebubble.mysingingmonsters.player;

public class PlayerLoadingException extends Exception {
   private static final long serialVersionUID = 1L;
   private String message;

   public String getMessage() {
      return this.message;
   }

   public PlayerLoadingException(String message) {
      this.message = message;
   }

   public PlayerLoadingException(Exception cause, String message) {
      super(cause);
      this.message = message;
   }
}

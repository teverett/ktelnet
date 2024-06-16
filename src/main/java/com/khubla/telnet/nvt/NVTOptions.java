package com.khubla.telnet.nvt;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class NVTOptions {
   /**
    * EOR (tn3270)
    */
   public static final int EOR = 239;
   /**
    * tn3270 functions
    */
   private Set<Integer> tn3270Functions = null;
   /**
    * eor
    */
   //  RFC 885 default is false
   private boolean eor = false;
   /**
    * extended ascii
    */
   private boolean clientcanextendedascii = false;
   /**
    * clientcancharset
    */
   private boolean clientcancharset = false;
   /**
    * binary (default is ASCII not binary,RFC 856)
    */
   private boolean binaryMode = false;
   /**
    * tn3270
    */
   private boolean tn3270 = false;
   /**
    * tn3270 device
    */
   private String tn3270Device = null;
   /**
    * term x
    */
   private short termX;
   /**
    * term y
    */
   private short termY;
   /**
    * term type
    */
   private String termtype;
   /**
    * term speed
    */
   private String termSpeed;
   /**
    * logout (if true then the client will send IP to logout)
    */
   // RFC 727, default is false
   private boolean logout = false;
   /**
    * suppress go-ahead
    */
   // RFC 858, default is false
   private boolean suppressGoAhead = false;
   /**
    * dont like linemode
    */
   //  RFC 1184 default is false
   private boolean lineMode = false;

   public void show() {
      System.out.println("NVT Options:");
      System.out.println("EOR: " + eor);
      System.out.println("suppressGoAhead: " + suppressGoAhead);
      System.out.println("lineMode: " + lineMode);
      System.out.println("clientcanextendedascii: " + clientcanextendedascii);
      System.out.println("clientcancharset: " + clientcancharset);
      System.out.println("binaryMode: " + binaryMode);
      System.out.println("tn3270: " + tn3270);
      System.out.println("tn3270Device: " + tn3270Device);
      System.out.println("termX: " + termX);
      System.out.println("termY: " + termY);
      System.out.println("termtype: " + termtype);
      System.out.println("termSpeed: " + termSpeed);
      System.out.println("logout: " + logout);
   }
}

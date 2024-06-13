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

   public void show() {
      System.out.println("NVT Options:");
      System.out.println("EOR: " + eor);
      System.out.println("clientcanextendedascii: " + clientcanextendedascii);
      System.out.println("clientcancharset: " + clientcancharset);
      System.out.println("binaryMode: " + binaryMode);
      System.out.println("tn3270: " + tn3270);
      System.out.println("tn3270Device: " + tn3270Device);
      System.out.println("termX: " + termX);
      System.out.println("termY: " + termY);
      System.out.println("termtype: " + termtype);
      System.out.println("termSpeed: " + termSpeed);
   }
}

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
}

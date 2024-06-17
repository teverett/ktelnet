package com.khubla.telnet.nvt;

import com.khubla.telnet.nvt.stream.NVTStream;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
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
   // RFC 858, default is false. this the CLIENT status.  we never send GA
   private boolean suppressGoAhead = false;
   /**
    * dont like linemode
    */
   //  RFC 1184 default is false
   private boolean lineMode = false;
   /**
    * extended options
    */
   // RFC 861. default is faliae
   private boolean extendedOptions = false;
   /**
    * env vars (client can send env vars)
    */
   // RFC 1408 default is false
   private boolean envvars = false;
   /*
    * timing mark
    */
   // RFC 869.  default is false
   private boolean timingMark = false;

   public void show(NVTStream nvtStream) throws IOException {
      nvtStream.writeln("EOR: " + eor);
      nvtStream.writeln("suppressGoAhead: " + suppressGoAhead);
      nvtStream.writeln("lineMode: " + lineMode);
      nvtStream.writeln("clientcanextendedascii: " + clientcanextendedascii);
      nvtStream.writeln("clientcancharset: " + clientcancharset);
      nvtStream.writeln("binaryMode: " + binaryMode);
      nvtStream.writeln("tn3270: " + tn3270);
      nvtStream.writeln("tn3270Device: " + tn3270Device);
      nvtStream.writeln("termX: " + termX);
      nvtStream.writeln("termY: " + termY);
      nvtStream.writeln("termtype: " + termtype);
      nvtStream.writeln("termSpeed: " + termSpeed);
      nvtStream.writeln("logout: " + logout);
      nvtStream.writeln("echo: " + nvtStream.isEcho());
      nvtStream.writeln("extendedOptions: " + extendedOptions);
      nvtStream.writeln("envvars: " + envvars);
      nvtStream.writeln("timingMark: " + timingMark);
   }
}

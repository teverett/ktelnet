/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet.nvt;

import java.io.IOException;

public interface IACHandler {
   /**
    * Codes
    */
   // RFC 856
   public static final int IAC_CODE_BINARY = 0;
   // RFC 857
   public static final int IAC_CODE_ECHO = 1;
   // RFC 858
   public static final int IAC_CODE_SUPPRESS_GOAHEAD = 3;
   // RFC 859
   public static final int IAC_CODE_STATUS = 5;
   public static final int IAC_CODE_MARK = 6;
   // RFC 1091
   public static final int IAC_CODE_TERMTYPE = 24;
   // RFC 885
   public static final int IAC_CODE_EOR = 25;
   //
   public static final int IAC_CODE_3270_REGIME = 29;
   // RFC 1073
   public static final int IAC_CODE_WINSIZE = 31;
   // RFC 1079
   public static final int IAC_CODE_TERMSPEED = 32;
   // RFC 1372
   public static final int IAC_CODE_REMOTE_FLOW_CONTROL = 33;
   // RFC 1184
   public static final int IAC_CODE_LINEMODE = 34;
   public static final int IAC_CODE_ENVVAR = 36;
   // RFC 1416, RFC 2941
   public static final int IAC_CODE_AUTHENTICATION = 37;

   void process(NVT nvt, int cmd, int option) throws IOException;
}

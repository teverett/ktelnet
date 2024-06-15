/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet.nvt;

import java.io.IOException;

public interface IACCommandHandler {
   /**
    * IAC Commands (RFC 854)
    */
   // End of subnegotiation parameters.
   int IAC_COMMAND_SE = 240;
   // No operation.
   int IAC_COMMAND_NOP = 241;
   // The data stream portion of a Synch
   int IAC_COMMAND_DATAMARK = 242;
   // NVT character BRK.
   int IAC_COMMAND_BREAK = 243;
   // interrupt process
   int IAC_COMMAND_IP = 244;
   // abort output
   int IAC_COMMAND_AO = 245;
   // are you there
   int IAC_COMMAND_AYT = 246;
   // erase character
   int IAC_COMMAND_EC = 247;
   // erase line
   int IAC_COMMAND_EL = 248;
   // go ahead
   int IAC_COMMAND_GA = 249;
   // Indicates that what follows is subnegotiation of the indicated option.
   int IAC_COMMAND_SB = 250;
   // Indicates the desire to begin performing, or confirmation that you are now performing, the indicated option.
   int IAC_COMMAND_WILL = 251;
   // Indicates the refusal to perform, or continue performing, the indicated option.
   int IAC_COMMAND_WONT = 252;
   // Indicates the request that the other party perform, or confirmation that you are expecting the other party to perform, the indicated option.
   int IAC_COMMAND_DO = 253;
   // Indicates the demand that the other party stop performing or confirmation that you are no longer expecting the other party to perform, the indicated option.
   int IAC_COMMAND_DONT = 254;
   // IAC
   int IAC_IAC = 255;



   void process(NVT nvt, int cmd) throws IOException;

   int getCommand();

   String getDescription();
}

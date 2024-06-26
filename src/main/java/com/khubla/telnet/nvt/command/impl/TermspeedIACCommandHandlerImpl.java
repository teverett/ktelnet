/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet.nvt.command.impl;

import com.khubla.telnet.nvt.NVT;
import com.khubla.telnet.nvt.command.AbstractIACCommandHandler;
import com.khubla.telnet.nvt.stream.IAC;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class TermspeedIACCommandHandlerImpl extends AbstractIACCommandHandler {
   /**
    * constants...
    */
   public static final int IS = 0;
   public static final int SEND = 1;
   // RFC 1079
   public static final int IAC_CODE_TERMSPEED = 32;
   /**
    * logger
    */
   private static final Logger logger = LogManager.getLogger(TermspeedIACCommandHandlerImpl.class);

   @Override
   public void process(NVT nvt, int cmd) throws IOException {
      switch (cmd) {
         case IAC.IAC_COMMAND_DO:
            logger.info("Received IAC DO Termspeed");
            // no
            nvt.sendIACCommand(IAC.IAC_COMMAND_WONT, IAC_CODE_TERMSPEED);
            break;
         case IAC.IAC_COMMAND_DONT:
            logger.info("Received IAC DONT Termspeed");
            nvt.sendIACCommand(IAC.IAC_COMMAND_WONT, IAC_CODE_TERMSPEED);
            break;
         case IAC.IAC_COMMAND_WILL:
            logger.info("Received IAC WILL Termspeed");
            // request it
            nvt.getNvtStream().writeBytes(IAC.IAC_IAC, IAC.IAC_COMMAND_SB, IAC_CODE_TERMSPEED, SEND, IAC.IAC_IAC, IAC.IAC_COMMAND_SE);
            break;
         case IAC.IAC_COMMAND_WONT:
            logger.info("Received IAC WONT Termspeed");
            break;
         case IAC.IAC_COMMAND_SB:
            logger.info("Received IAC SB Termspeed");
            final byte[] sn = readSubnegotiation(nvt);
            if (sn[0] == IS) {
               final String termSpeedString = readString(sn, 1, sn.length);
               nvt.getNvtOptions().setTermSpeed(termSpeedString);
               logger.info("Remote terminal termspeed is: " + termSpeedString);
            } else if (sn[0] == SEND) {
               // send the termspeed
            }
            break;
         default:
            logger.info("Received Unknown IAC Command:" + cmd);
            break;
      }
   }

   @Override
   public int getCommand() {
      return IAC_CODE_TERMSPEED;
   }

   @Override
   public String getDescription() {
      return "TERMSPEED";
   }
}

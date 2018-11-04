/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet.nvt.iac.command;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.khubla.telnet.nvt.NVT;

public class TermspeedIACCommandHandlerImpl extends AbstractIACCommandHandler {
   /**
    * logger
    */
   static final Logger logger = LoggerFactory.getLogger(TermspeedIACCommandHandlerImpl.class);
   /**
    * constants...
    */
   public static final int IS = 0;
   public static final int SEND = 1;

   @Override
   public void process(NVT nvt, int cmd) throws IOException {
      switch (cmd) {
         case NVT.IAC_COMMAND_DO:
            logger.info("Received IAC DO Termspeed");
            break;
         case NVT.IAC_COMMAND_DONT:
            logger.info("Received IAC DONT Termspeed");
            break;
         case NVT.IAC_COMMAND_WILL:
            logger.info("Received IAC WILL Termspeed");
            // great, we like it
            nvt.sendIACCommand(NVT.IAC_COMMAND_DO, NVT.IAC_CODE_TERMSPEED);
            // request it
            nvt.writeBytes(NVT.IAC_IAC, NVT.IAC_COMMAND_SB, NVT.IAC_CODE_TERMSPEED, SEND, NVT.IAC_IAC, NVT.IAC_COMMAND_SE);
            break;
         case NVT.IAC_COMMAND_WONT:
            logger.info("Received IAC WONT Termspeed");
            break;
         case NVT.IAC_COMMAND_SB:
            logger.info("Received IAC SB Termspeed");
            final byte[] sn = readSubnegotiation(nvt);
            if (sn[0] == IS) {
               final String termSpeedString = readString(sn, 1, sn.length);
               nvt.setTermSpeed(termSpeedString);
            } else if (sn[0] == SEND) {
               // send the termspeed
            }
            break;
         default:
            logger.info("Received Unknown IAC Command:" + cmd);
      }
   }
}

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

import com.khubla.telnet.nvt.IACCommandHandler;
import com.khubla.telnet.nvt.NVT;

public class WinsizeIAICCommandHandlerImpl implements IACCommandHandler {
   /**
    * logger
    */
   static final Logger logger = LoggerFactory.getLogger(WinsizeIAICCommandHandlerImpl.class);

   @Override
   public void process(NVT nvt, int cmd) throws IOException {
      switch (cmd) {
         case NVT.IAC_COMMAND_DO:
            logger.info("Received IAC DO winsize");
            break;
         case NVT.IAC_COMMAND_DONT:
            logger.info("Received IAC DONT winsize");
            break;
         case NVT.IAC_COMMAND_WILL:
            // great, please do send it along
            nvt.sendIACCommand(NVT.IAC_COMMAND_DO, NVT.IAC_CODE_WINSIZE);
            logger.info("Received IAC WILL winsize");
            break;
         case NVT.IAC_COMMAND_WONT:
            logger.info("Received IAC WONT winsize");
            break;
         case NVT.IAC_COMMAND_SB:
            logger.info("Received IAC SB winsize");
            final short x = nvt.readShort();
            final short y = nvt.readShort();
            nvt.setTermX(x);
            nvt.setTermY(y);
            final int nextIACIAC = nvt.readRawByte();
            if (nextIACIAC == NVT.IAC_IAC) {
               final int nextIAC = nvt.readRawByte();
               if (nextIAC != NVT.IAC_COMMAND_SE) {
                  logger.info("Expected IAC:" + NVT.IAC_COMMAND_SE);
               }
            } else {
               logger.info("Expected: " + NVT.IAC_IAC);
            }
            break;
         default:
            logger.info("Received Unknown IAC Command:" + cmd);
      }
   }
}

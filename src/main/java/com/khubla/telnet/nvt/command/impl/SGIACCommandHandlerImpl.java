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

public class SGIACCommandHandlerImpl extends AbstractIACCommandHandler {
   // RFC 858
   public static final int IAC_CODE_SUPPRESS_GOAHEAD = 3;
   /**
    * logger
    */
   private static final Logger logger = LogManager.getLogger(SGIACCommandHandlerImpl.class);

   @Override
   public void process(NVT nvt, int cmd) throws IOException {
      switch (cmd) {
         case IAC.IAC_COMMAND_DO:
            logger.info("Received IAC DO SG");
            nvt.sendIACCommand(IAC.IAC_COMMAND_WILL, IAC_CODE_SUPPRESS_GOAHEAD);
            nvt.getNvtOptions().setSuppressGoAhead(true);
            break;
         case IAC.IAC_COMMAND_DONT:
            logger.info("Received IAC DONT SG");
            nvt.sendIACCommand(IAC.IAC_COMMAND_WONT, IAC_CODE_SUPPRESS_GOAHEAD);
            nvt.getNvtOptions().setSuppressGoAhead(false);
            break;
         case IAC.IAC_COMMAND_WILL:
            logger.info("Received IAC WILL SG");
            break;
         case IAC.IAC_COMMAND_WONT:
            logger.info("Received IAC WONT SG");
            break;
         case IAC.IAC_COMMAND_SB:
            logger.info("Received IAC SB SG");
            break;
         default:
            logger.info("Received Unknown IAC Command:" + cmd);
            break;
      }
   }

   @Override
   public int getCommand() {
      return IAC_CODE_SUPPRESS_GOAHEAD;
   }

   @Override
   public String getDescription() {
      return "SUPPRESSGOAHEAD";
   }
}

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

public class BinaryIAICCommandHandlerImpl extends AbstractIACCommandHandler {
   // RFC 856
   public static final int IAC_CODE_BINARY = 0;
   /**
    * logger
    */
   private static final Logger logger = LogManager.getLogger(BinaryIAICCommandHandlerImpl.class);

   @Override
   public void process(NVT nvt, int cmd) throws IOException {
      switch (cmd) {
         case IAC.IAC_COMMAND_DO:
            logger.info("Received IAC DO binary");
            nvt.getNvtOptions().setBinaryMode(true);
            nvt.sendIACCommand(IAC.IAC_COMMAND_WILL, IAC_CODE_BINARY);
            break;
         case IAC.IAC_COMMAND_DONT:
            logger.info("Received IAC DONT binary");
            nvt.getNvtOptions().setBinaryMode(false);
            nvt.sendIACCommand(IAC.IAC_COMMAND_WONT, IAC_CODE_BINARY);
            break;
         case IAC.IAC_COMMAND_WILL:
            logger.info("Received IAC WILL binary");
            nvt.getNvtOptions().setBinaryMode(true);
            break;
         case IAC.IAC_COMMAND_WONT:
            logger.info("Received IAC WONT binary");
            nvt.getNvtOptions().setBinaryMode(false);
            break;
         case IAC.IAC_COMMAND_SB:
            logger.info("Received IAC SB binary");
            break;
         default:
            logger.info("Received Unknown IAC Command:" + cmd);
            break;
      }
   }

   @Override
   public int getCommand() {
      return IAC_CODE_BINARY;
   }

   @Override
   public String getDescription() {
      return "BINARY";
   }
}

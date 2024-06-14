/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet.nvt.iac.command;

import com.khubla.telnet.nvt.IACCommandHandler;
import com.khubla.telnet.nvt.NVT;
import com.khubla.telnet.nvt.stream.IACProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class BinaryIAICCommandHandlerImpl extends AbstractIACCommandHandler {
   // RFC 856
   public static final int IAC_CODE_BINARY = 0;
   /**
    * logger
    */
   private static final Logger logger = LoggerFactory.getLogger(BinaryIAICCommandHandlerImpl.class);

   @Override
   public void process(NVT nvt, int cmd) throws IOException {
      switch (cmd) {
         case IACProcessor.IAC_COMMAND_DO:
            logger.info("Received IAC DO binary");
            nvt.getNvtOptions().setBinaryMode(true);
            nvt.sendIACCommand(IACProcessor.IAC_COMMAND_WILL, IAC_CODE_BINARY);
            break;
         case IACProcessor.IAC_COMMAND_DONT:
            logger.info("Received IAC DONT binary");
            nvt.getNvtOptions().setBinaryMode(false);
            nvt.sendIACCommand(IACProcessor.IAC_COMMAND_WONT, IAC_CODE_BINARY);
            break;
         case IACProcessor.IAC_COMMAND_WILL:
            logger.info("Received IAC WILL binary");
            nvt.getNvtOptions().setBinaryMode(true);
            break;
         case IACProcessor.IAC_COMMAND_WONT:
            logger.info("Received IAC WONT binary");
            nvt.getNvtOptions().setBinaryMode(false);
            break;
         case IACProcessor.IAC_COMMAND_SB:
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

   @Override
   public boolean negotiate() {
      return false;
   }
}

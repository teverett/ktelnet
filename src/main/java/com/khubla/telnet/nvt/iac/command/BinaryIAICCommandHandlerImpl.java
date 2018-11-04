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
import com.khubla.telnet.nvt.IACHandler;
import com.khubla.telnet.nvt.NVT;

public class BinaryIAICCommandHandlerImpl extends AbstractIACCommandHandler {
   /**
    * logger
    */
   static final Logger logger = LoggerFactory.getLogger(BinaryIAICCommandHandlerImpl.class);

   @Override
   public void process(NVT nvt, int cmd) throws IOException {
      switch (cmd) {
         case IACCommandHandler.IAC_COMMAND_DO:
            logger.info("Received IAC DO binary");
            nvt.setBinaryMode(true);
            nvt.sendIACCommand(IACCommandHandler.IAC_COMMAND_DO, IACHandler.IAC_CODE_BINARY);
            break;
         case IACCommandHandler.IAC_COMMAND_DONT:
            logger.info("Received IAC DONT binary");
            nvt.setBinaryMode(false);
            // nvt.sendIACCommand(IACCommandHandler.IAC_COMMAND_WONT, IACHandler.IAC_CODE_BINARY);
            break;
         case IACCommandHandler.IAC_COMMAND_WILL:
            logger.info("Received IAC WILL binary");
            break;
         case IACCommandHandler.IAC_COMMAND_WONT:
            logger.info("Received IAC WONT binary");
            break;
         case IACCommandHandler.IAC_COMMAND_SB:
            logger.info("Received IAC SB binary");
            break;
         default:
            logger.info("Received Unknown IAC Command:" + cmd);
      }
   }
}

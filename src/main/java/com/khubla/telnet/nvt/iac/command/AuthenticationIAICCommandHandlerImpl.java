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

public class AuthenticationIAICCommandHandlerImpl extends AbstractIACCommandHandler {
   /**
    * logger
    */
   static final Logger logger = LoggerFactory.getLogger(AuthenticationIAICCommandHandlerImpl.class);
   /**
    * constants...
    */
   public static final int IS = 0;
   public static final int SEND = 1;
   public static final int REPLY = 2;
   public static final int NAME = 3;

   @Override
   public void process(NVT nvt, int cmd) throws IOException {
      switch (cmd) {
         case IACCommandHandler.IAC_COMMAND_DO:
            logger.info("Received IAC DO auth");
            nvt.sendIACCommand(IACCommandHandler.IAC_COMMAND_WONT, IACHandler.IAC_CODE_AUTHENTICATION);
            break;
         case IACCommandHandler.IAC_COMMAND_DONT:
            logger.info("Received IAC DONT auth");
            break;
         case IACCommandHandler.IAC_COMMAND_WILL:
            logger.info("Received IAC WILL auth");
            break;
         case IACCommandHandler.IAC_COMMAND_WONT:
            logger.info("Received IAC WONT auth");
            break;
         case IACCommandHandler.IAC_COMMAND_SB:
            logger.info("Received IAC SB auth");
            break;
         default:
            logger.info("Received Unknown IAC Command:" + cmd);
      }
   }
}

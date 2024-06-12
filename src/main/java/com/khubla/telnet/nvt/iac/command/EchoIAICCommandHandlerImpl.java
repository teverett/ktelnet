/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet.nvt.iac.command;

import com.khubla.telnet.nvt.IACCommandHandler;
import com.khubla.telnet.nvt.NVT;
import com.khubla.telnet.nvt.iac.IACHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class EchoIAICCommandHandlerImpl extends AbstractIACCommandHandler {
   /**
    * logger
    */
   private static final Logger logger = LoggerFactory.getLogger(EchoIAICCommandHandlerImpl.class);

   @Override
   public void process(NVT nvt, int cmd) throws IOException {
      switch (cmd) {
         case IACCommandHandler.IAC_COMMAND_DO:
            logger.info("Received IAC DO echo");
            nvt.getNvtStream().setEcho(true);
            nvt.sendIACCommand(IACCommandHandler.IAC_COMMAND_WILL, IACHandler.IAC_CODE_ECHO);
            break;
         case IACCommandHandler.IAC_COMMAND_DONT:
            logger.info("Received IAC DONT echo");
            nvt.getNvtStream().setEcho(false);
            nvt.sendIACCommand(IACCommandHandler.IAC_COMMAND_WONT, IACHandler.IAC_CODE_ECHO);
            break;
         case IACCommandHandler.IAC_COMMAND_WILL:
            logger.info("Received IAC WILL echo");
            nvt.getNvtStream().setEcho(true);
            break;
         case IACCommandHandler.IAC_COMMAND_WONT:
            logger.info("Received IAC WONT echo");
            nvt.getNvtStream().setEcho(false);
            break;
         case IACCommandHandler.IAC_COMMAND_SB:
            logger.info("Received IAC SB echo");
            break;
         default:
            logger.info("Received Unknown IAC Command:" + cmd);
            break;
      }
   }
}

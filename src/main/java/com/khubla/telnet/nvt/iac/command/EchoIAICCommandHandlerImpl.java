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

public class EchoIAICCommandHandlerImpl implements IACCommandHandler {
   /**
    * logger
    */
   static final Logger logger = LoggerFactory.getLogger(EchoIAICCommandHandlerImpl.class);

   @Override
   public void process(NVT nvt, int cmd) throws IOException {
      switch (cmd) {
         case NVT.IAC_COMMAND_DO:
            logger.info("Received IAC DO echo");
            nvt.setEcho(true);
            nvt.sendIACCommand(NVT.IAC_COMMAND_WILL, NVT.IAC_CODE_ECHO);
            break;
         case NVT.IAC_COMMAND_DONT:
            logger.info("Received IAC DONT echo");
            nvt.setEcho(false);
            nvt.sendIACCommand(NVT.IAC_COMMAND_WONT, NVT.IAC_CODE_ECHO);
            break;
         case NVT.IAC_COMMAND_WILL:
            logger.info("Received IAC WILL echo");
            break;
         case NVT.IAC_COMMAND_WONT:
            logger.info("Received IAC WONT echo");
            break;
         default:
            logger.info("Received Unknown IAC Command :" + cmd);
      }
   }
}
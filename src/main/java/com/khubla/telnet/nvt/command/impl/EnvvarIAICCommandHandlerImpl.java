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

public class EnvvarIAICCommandHandlerImpl extends AbstractIACCommandHandler {
   /**
    * constants
    */
   public static final int IS = 0;
   public static final int SEND = 1;
   public static final int INFO = 2;
   public static final int VAR = 0;
   public static final int VALUE = 1;
   public static final int ESC = 2;
   public static final int USERVAR = 3;
   // RFC 1408
   public static final int IAC_CODE_ENVVAR = 36;
   /**
    * logger
    */
   private static final Logger logger = LogManager.getLogger(EnvvarIAICCommandHandlerImpl.class);

   @Override
   public void process(NVT nvt, int cmd) throws IOException {
      switch (cmd) {
         case IAC.IAC_COMMAND_DO:
            logger.info("Received IAC DO envvar");
            break;
         case IAC.IAC_COMMAND_DONT:
            logger.info("Received IAC DONT envvar");
            break;
         case IAC.IAC_COMMAND_WILL:
            logger.info("Received IAC WILL envvar");
            // we don't do envvars
            nvt.sendIACCommand(IAC.IAC_COMMAND_DONT, IAC_CODE_ENVVAR);
            break;
         case IAC.IAC_COMMAND_WONT:
            logger.info("Received IAC WONT envvar");
            break;
         case IAC.IAC_COMMAND_SB:
            logger.info("Received IAC SB envvar");
            break;
         default:
            logger.info("Received Unknown IAC Command:" + cmd);
            break;
      }
   }

   @Override
   public int getCommand() {
      return IAC_CODE_ENVVAR;
   }

   @Override
   public String getDescription() {
      return "ENVVAR";
   }
}

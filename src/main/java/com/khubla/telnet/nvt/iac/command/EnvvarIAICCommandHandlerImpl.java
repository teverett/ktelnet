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
   /**
    * logger
    */
   private static final Logger logger = LoggerFactory.getLogger(EnvvarIAICCommandHandlerImpl.class);
   // RFC 1408
   public static final int  IAC_CODE_ENVVAR = 36;

   @Override
   public void process(NVT nvt, int cmd) throws IOException {
      switch (cmd) {
         case IACProcessor.IAC_COMMAND_DO:
            logger.info("Received IAC DO envvar");
            break;
         case IACProcessor.IAC_COMMAND_DONT:
            logger.info("Received IAC DONT envvar");
            break;
         case IACProcessor.IAC_COMMAND_WILL:
            logger.info("Received IAC WILL envvar");
            // we don't do envvars
            nvt.sendIACCommand(IACProcessor.IAC_COMMAND_DONT, IAC_CODE_ENVVAR);
            break;
         case IACProcessor.IAC_COMMAND_WONT:
            logger.info("Received IAC WONT envvar");
            break;
         case IACProcessor.IAC_COMMAND_SB:
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

   @Override
   public boolean negotiate() {
      return false;
   }
}

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

/**
 * Negotiate About Vertcial Tabstops
 *
 * @author tom
 */
public class NAOVTSIAICCommandHandlerImpl extends AbstractIACCommandHandler {
   /**
    * logger
    */
   private static final Logger logger = LoggerFactory.getLogger(NAOVTSIAICCommandHandlerImpl.class);
   // RFC 657
   public static final int  IAC_CODE_NAOVTD = 15;

   @Override
   public void process(NVT nvt, int cmd) throws IOException {
      switch (cmd) {
         case IACProcessor.IAC_COMMAND_DO:
            logger.info("Received IAC DO NAOVTS");
            break;
         case IACProcessor.IAC_COMMAND_DONT:
            logger.info("Received IAC DONT NAOVTS");
            break;
         case IACProcessor.IAC_COMMAND_WILL:
            logger.info("Received IAC DO NAOVTS");
            break;
         case IACProcessor.IAC_COMMAND_WONT:
            logger.info("Received IAC WONT NAOVTS");
            break;
         case IACProcessor.IAC_COMMAND_SB:
            logger.info("Received IAC SB NAOVTS");
            break;
         default:
            logger.info("Received Unknown IAC Command:" + cmd);
            break;
      }
   }

   @Override
   public int getCommand() {
      return IAC_CODE_NAOVTD;
   }

   @Override
   public String getDescription() {
      return "NAOVTD";
   }

   @Override
   public boolean negotiate() {
      return false;
   }
}

/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet.nvt.command.impl;

import com.khubla.telnet.nvt.command.IACCommandHandler;
import com.khubla.telnet.nvt.NVT;
import com.khubla.telnet.nvt.command.AbstractIACCommandHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Negotiate About Vertcial Tabstops
 *
 * @author tom
 */
public class NAOVTSIAICCommandHandlerImpl extends AbstractIACCommandHandler {
   // RFC 657
   public static final int IAC_CODE_NAOVTD = 15;
   /**
    * logger
    */
   private static final Logger logger = LogManager.getLogger(NAOVTSIAICCommandHandlerImpl.class);

   @Override
   public void process(NVT nvt, int cmd) throws IOException {
      switch (cmd) {
         case IACCommandHandler.IAC_COMMAND_DO:
            logger.info("Received IAC DO NAOVTS");
            break;
         case IACCommandHandler.IAC_COMMAND_DONT:
            logger.info("Received IAC DONT NAOVTS");
            break;
         case IACCommandHandler.IAC_COMMAND_WILL:
            logger.info("Received IAC DO NAOVTS");
            break;
         case IACCommandHandler.IAC_COMMAND_WONT:
            logger.info("Received IAC WONT NAOVTS");
            break;
         case IACCommandHandler.IAC_COMMAND_SB:
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
}

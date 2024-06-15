/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet.nvt.command.impl;

import com.khubla.telnet.nvt.NVT;
import com.khubla.telnet.nvt.command.AbstractIACCommandHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Negotiate About Output Linefeed Disposition
 *
 * @author tom
 */
public class NAOFLDIAICCommandHandlerImpl extends AbstractIACCommandHandler {
   // RFC 658
   public static final int IAC_CODE_NAOLFD = 16;
   /**
    * logger
    */
   private static final Logger logger = LogManager.getLogger(NAOFLDIAICCommandHandlerImpl.class);

   @Override
   public void process(NVT nvt, int cmd) throws IOException {
      switch (cmd) {
         case IAC_COMMAND_DO:
            logger.info("Received IAC DO NAOFLD");
            break;
         case IAC_COMMAND_DONT:
            logger.info("Received IAC DONT NAOFLD");
            break;
         case IAC_COMMAND_WILL:
            logger.info("Received IAC DO NAOFLD");
            break;
         case IAC_COMMAND_WONT:
            logger.info("Received IAC WONT NAOFLD");
            break;
         case IAC_COMMAND_SB:
            logger.info("Received IAC SB NAOFLD");
            break;
         default:
            logger.info("Received Unknown IAC Command:" + cmd);
            break;
      }
   }

   @Override
   public int getCommand() {
      return IAC_CODE_NAOLFD;
   }

   @Override
   public String getDescription() {
      return "NAOLFD";
   }
}

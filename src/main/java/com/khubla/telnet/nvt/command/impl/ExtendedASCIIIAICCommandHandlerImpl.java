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

public class ExtendedASCIIIAICCommandHandlerImpl extends AbstractIACCommandHandler {
   // RFC 698
   public static final int IAC_CODE_EXTENDED_ASCII = 17;
   /**
    * logger
    */
   private static final Logger logger = LogManager.getLogger(ExtendedASCIIIAICCommandHandlerImpl.class);

   @Override
   public void process(NVT nvt, int cmd) throws IOException {
      switch (cmd) {
         case IAC_COMMAND_DO:
            logger.info("Received IAC DO extended ascii");
            nvt.getNvtOptions().setClientcanextendedascii(true);
            break;
         case IAC_COMMAND_DONT:
            logger.info("Received IAC DONT extended ascii");
            nvt.getNvtOptions().setClientcanextendedascii(false);
            break;
         case IAC_COMMAND_WILL:
            logger.info("Received IAC WILL extended ascii");
            break;
         case IAC_COMMAND_WONT:
            logger.info("Received IAC WONT extended ascii");
            break;
         case IAC_COMMAND_SB:
            logger.info("Received IAC SB extended ascii");
            break;
         default:
            logger.info("Received Unknown IAC Command:" + cmd);
            break;
      }
   }

   @Override
   public int getCommand() {
      return IAC_CODE_EXTENDED_ASCII;
   }

   @Override
   public String getDescription() {
      return "EXTENDEDASCII";
   }
}

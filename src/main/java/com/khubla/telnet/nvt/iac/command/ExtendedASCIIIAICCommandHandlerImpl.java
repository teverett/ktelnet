/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet.nvt.iac.command;

import com.khubla.telnet.nvt.IACCommandHandler;
import com.khubla.telnet.nvt.NVT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ExtendedASCIIIAICCommandHandlerImpl extends AbstractIACCommandHandler {
   /**
    * logger
    */
   private static final Logger logger = LoggerFactory.getLogger(ExtendedASCIIIAICCommandHandlerImpl.class);
   // RFC 698
   public static final int  IAC_CODE_EXTENDED_ASCII = 17;

   @Override
   public void process(NVT nvt, int cmd) throws IOException {
      switch (cmd) {
         case IACCommandHandler.IAC_COMMAND_DO:
            logger.info("Received IAC DO extended ascii");
            nvt.getNvtOptions().setClientcanextendedascii(true);
            break;
         case IACCommandHandler.IAC_COMMAND_DONT:
            logger.info("Received IAC DONT extended ascii");
            nvt.getNvtOptions().setClientcanextendedascii(false);
            break;
         case IACCommandHandler.IAC_COMMAND_WILL:
            logger.info("Received IAC WILL extended ascii");
            break;
         case IACCommandHandler.IAC_COMMAND_WONT:
            logger.info("Received IAC WONT extended ascii");
            break;
         case IACCommandHandler.IAC_COMMAND_SB:
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

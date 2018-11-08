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
import com.khubla.telnet.nvt.iac.IACHandler;

/**
 * TELNET EXTENDED OPTIONS - LIST OPTION - RFC 861
 *
 * @author tom
 */
public class ExtendedOptionsListIAICCommandHandlerImpl extends AbstractIACCommandHandler {
   /**
    * logger
    */
   private static final Logger logger = LoggerFactory.getLogger(ExtendedOptionsListIAICCommandHandlerImpl.class);

   @Override
   public void process(NVT nvt, int cmd) throws IOException {
      switch (cmd) {
         case IACCommandHandler.IAC_COMMAND_DO:
            logger.info("Received IAC DO extendedoptionslist");
            sendOptions(nvt);
            break;
         case IACCommandHandler.IAC_COMMAND_DONT:
            logger.info("Received IAC DONT extendedoptionslist");
            break;
         case IACCommandHandler.IAC_COMMAND_WILL:
            logger.info("Received IAC WILL extendedoptionslist");
            break;
         case IACCommandHandler.IAC_COMMAND_WONT:
            logger.info("Received IAC WONT extendedoptionslist");
            break;
         case IACCommandHandler.IAC_COMMAND_SB:
            logger.info("Received IAC SB extendedoptionslist");
            break;
         default:
            logger.info("Received Unknown IAC Command:" + cmd);
            break;
      }
   }

   private void sendOption(NVT nvt, int option) throws IOException {
      nvt.getNvtStream().writeBytes(IACCommandHandler.IAC_IAC, IACCommandHandler.IAC_COMMAND_SB, IACHandler.IAC_CODE_EXTENDED_OPTIONS_LIST, IACHandler.IAC_CODE_EXTENDED_OPTIONS_LIST,
            IACCommandHandler.IAC_IAC, IACCommandHandler.IAC_COMMAND_SE);
   }

   private void sendOptions(NVT nvt) throws IOException {
      sendOption(nvt, IACHandler.IAC_CODE_EXTENDED_OPTIONS_LIST);
      sendOption(nvt, IACHandler.IAC_CODE_ECHO);
      sendOption(nvt, IACHandler.IAC_CODE_BINARY);
      sendOption(nvt, IACHandler.IAC_CODE_NEW_ENVIRON);
   }
}

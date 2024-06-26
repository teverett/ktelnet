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

/**
 * TELNET EXTENDED OPTIONS - LIST OPTION - RFC 861
 *
 * @author tom
 */
public class ExtendedOptionsListIAICCommandHandlerImpl extends AbstractIACCommandHandler {
   // RFC 861
   public static final int IAC_CODE_EXTENDED_OPTIONS_LIST = 255;
   /**
    * logger
    */
   private static final Logger logger = LogManager.getLogger(ExtendedOptionsListIAICCommandHandlerImpl.class);

   @Override
   public void process(NVT nvt, int cmd) throws IOException {
      switch (cmd) {
         case IAC.IAC_COMMAND_DO:
            logger.info("Received IAC DO extendedoptionslist");
            sendOptions(nvt);
            break;
         case IAC.IAC_COMMAND_DONT:
            logger.info("Received IAC DONT extendedoptionslist");
            break;
         case IAC.IAC_COMMAND_WILL:
            logger.info("Received IAC WILL extendedoptionslist");
            nvt.getNvtOptions().setExtendedOptions(true);
            break;
         case IAC.IAC_COMMAND_WONT:
            logger.info("Received IAC WONT extendedoptionslist");
            nvt.getNvtOptions().setExtendedOptions(false);
            break;
         case IAC.IAC_COMMAND_SB:
            logger.info("Received IAC SB extendedoptionslist");
            break;
         default:
            logger.info("Received Unknown IAC Command:" + cmd);
            break;
      }
   }

   @Override
   public int getCommand() {
      return IAC_CODE_EXTENDED_OPTIONS_LIST;
   }

   @Override
   public String getDescription() {
      return "EXTENDEDOOPTIONSLIST";
   }

   private void sendOption(NVT nvt, int option) throws IOException {
      nvt.getNvtStream().writeBytes(IAC.IAC_IAC, IAC.IAC_COMMAND_SB, IAC_CODE_EXTENDED_OPTIONS_LIST, IAC_CODE_EXTENDED_OPTIONS_LIST, IAC.IAC_IAC, IAC.IAC_COMMAND_SE);
   }

   private void sendOptions(NVT nvt) throws IOException {
      //    sendOption(nvt, IAC_CODE_EXTENDED_OPTIONS_LIST);
      //  sendOption(nvt, IAC_CODE_ECHO);
      //  sendOption(nvt, IAC_CODE_BINARY);
      //  sendOption(nvt, IAC_CODE_NEW_ENVIRON);
   }
}

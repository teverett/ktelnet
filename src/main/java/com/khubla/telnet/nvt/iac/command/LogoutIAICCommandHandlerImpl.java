/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet.nvt.iac.command;

import com.khubla.telnet.nvt.IACCommandHandler;
import com.khubla.telnet.nvt.NVT;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * TELNET Logout Option - RFC 727
 *
 * @author tom
 */
public class LogoutIAICCommandHandlerImpl extends AbstractIACCommandHandler {
   // RFC 727
   public static final int IAC_CODE_LOGOUT = 18;
   /**
    * logger
    */
   private static final Logger logger = LogManager.getLogger(LogoutIAICCommandHandlerImpl.class);

   @Override
   public void process(NVT nvt, int cmd) throws IOException {
      switch (cmd) {
         case IACCommandHandler.IAC_COMMAND_DO:
            logger.info("Received IAC DO logout");
            break;
         case IACCommandHandler.IAC_COMMAND_DONT:
            logger.info("Received IAC DONT logout");
            break;
         case IACCommandHandler.IAC_COMMAND_WILL:
            logger.info("Received IAC WILL logout");
            nvt.getNvtOptions().setLogout(true);
            break;
         case IACCommandHandler.IAC_COMMAND_WONT:
            logger.info("Received IAC WONT logout");
            nvt.getNvtOptions().setLogout(false);
            break;
         case IACCommandHandler.IAC_COMMAND_SB:
            logger.info("Received IAC SB logout");
            break;
         default:
            logger.info("Received Unknown IAC Command:" + cmd);
            break;
      }
   }

   @Override
   public int getCommand() {
      return IAC_CODE_LOGOUT;
   }

   @Override
   public String getDescription() {
      return "LOGOUT";
   }
}

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

public class TN3270RegimeIAICCommandHandlerImpl extends AbstractIACCommandHandler {
   // RFC 1041
   public static final int IAC_CODE_3270_REGIME = 29;
   /**
    * logger
    */
   private static final Logger logger = LogManager.getLogger(TN3270RegimeIAICCommandHandlerImpl.class);

   @Override
   public void process(NVT nvt, int cmd) throws IOException {
      switch (cmd) {
         case IAC.IAC_COMMAND_DO:
            logger.info("Received IAC DO 3270-regime");
            break;
         case IAC.IAC_COMMAND_DONT:
            logger.info("Received IAC DONT 3270-regime");
            break;
         case IAC.IAC_COMMAND_WILL:
            logger.info("Received IAC WILL 3270-regime");
            // we don't do 3270 Regime
            nvt.sendIACCommand(IAC.IAC_COMMAND_DONT, IAC_CODE_3270_REGIME);
            break;
         case IAC.IAC_COMMAND_WONT:
            logger.info("Received IAC WONT 3270-regime");
            break;
         case IAC.IAC_COMMAND_SB:
            logger.info("Received IAC SB 3270-regime");
            break;
         default:
            logger.info("Received Unknown IAC Command:" + cmd);
            break;
      }
   }

   @Override
   public int getCommand() {
      return IAC_CODE_3270_REGIME;
   }

   @Override
   public String getDescription() {
      return "TM3270REGIME";
   }
}

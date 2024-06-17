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

public class WinsizeIAICCommandHandlerImpl extends AbstractIACCommandHandler {
   // RFC 1073
   public static final int IAC_CODE_WINSIZE = 31;
   /**
    * logger
    */
   private static final Logger logger = LogManager.getLogger(WinsizeIAICCommandHandlerImpl.class);

   @Override
   public void process(NVT nvt, int cmd) throws IOException {
      switch (cmd) {
         case IAC.IAC_COMMAND_DO:
            logger.info("Received IAC DO winsize");
            break;
         case IAC.IAC_COMMAND_DONT:
            logger.info("Received IAC DONT winsize");
            break;
         case IAC.IAC_COMMAND_WILL:
            logger.info("Received IAC WILL winsize");
            break;
         case IAC.IAC_COMMAND_WONT:
            logger.info("Received IAC WONT winsize");
            break;
         case IAC.IAC_COMMAND_SB:
            logger.info("Received IAC SB winsize");
            final byte[] sn = readSubnegotiation(nvt);
            final short x = readShort(sn, 0);
            final short y = readShort(sn, 2);
            nvt.getNvtOptions().setTermX(x);
            nvt.getNvtOptions().setTermY(y);
            logger.info("Remote terminal winsize is: " + x + "x" + y);
            break;
         default:
            logger.info("Received Unknown IAC Command:" + cmd);
            break;
      }
   }

   @Override
   public int getCommand() {
      return IAC_CODE_WINSIZE;
   }

   @Override
   public String getDescription() {
      return "WINSIZE";
   }
}

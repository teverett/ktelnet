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
 * TELNET STATUS OPTION - RFC 859
 *
 * @author tom
 */
public class StatusIAICCommandHandlerImpl extends AbstractIACCommandHandler {
   /**
    * codes
    */
   public static final int DO = 253;
   public static final int DONT = 254;
   public static final int WILL = 251;
   public static final int WONT = 252;
   public static final int SB = 250;
   // RFC 859
   public static final int IAC_CODE_STATUS = 5;
   /**
    * logger
    */
   private static final Logger logger = LogManager.getLogger(StatusIAICCommandHandlerImpl.class);

   @Override
   public void process(NVT nvt, int cmd) throws IOException {
      switch (cmd) {
         case IAC.IAC_COMMAND_DO:
            logger.info("Received IAC DO status");
            break;
         case IAC.IAC_COMMAND_DONT:
            logger.info("Received IAC DONT status");
            break;
         case IAC.IAC_COMMAND_WILL:
            logger.info("Received IAC WILL status");
            break;
         case IAC.IAC_COMMAND_WONT:
            logger.info("Received IAC WONT status");
            break;
         case IAC.IAC_COMMAND_SB:
            logger.info("Received IAC SB status");
            byte[] b = readSubnegotiation(nvt);
            break;
         default:
            logger.info("Received Unknown IAC Command:" + cmd);
            break;
      }
   }

   @Override
   public int getCommand() {
      return IAC_CODE_STATUS;
   }

   @Override
   public String getDescription() {
      return "STATUS";
   }
}

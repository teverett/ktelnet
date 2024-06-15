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
   /**
    * logger
    */
   private static final Logger logger = LoggerFactory.getLogger(StatusIAICCommandHandlerImpl.class);
   // RFC 859
   public static final int  IAC_CODE_STATUS = 5;

   @Override
   public void process(NVT nvt, int cmd) throws IOException {
      switch (cmd) {
         case IACCommandHandler.IAC_COMMAND_DO:
            logger.info("Received IAC DO status");
            break;
         case IACCommandHandler.IAC_COMMAND_DONT:
            logger.info("Received IAC DONT status");
            break;
         case IACCommandHandler.IAC_COMMAND_WILL:
            logger.info("Received IAC WILL status");
            break;
         case IACCommandHandler.IAC_COMMAND_WONT:
            logger.info("Received IAC WONT status");
            break;
         case IACCommandHandler.IAC_COMMAND_SB:
            logger.info("Received IAC SB status");
            readSubnegotiation(nvt);
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

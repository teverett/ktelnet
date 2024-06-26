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
 * TELNET CHARSET Option - RFC 2066
 *
 * @author tom
 */
public class CharsetIAICCommandHandlerImpl extends AbstractIACCommandHandler {
   /**
    * constants...
    */
   public static final int CHARSET = 42;
   public static final int REQUEST = 1;
   public static final int ACCEPTED = 2;
   public static final int REJECTED = 3;
   public static final int TTABLE_IS = 4;
   public static final int TTABLE_REJECTED = 5;
   public static final int TTABLE_ACK = 6;
   public static final int TTABLE_NAK = 7;
   // RFC 2066
   public static final int IAC_CODE_CHARSET = 42;
   /**
    * logger
    */
   private static final Logger logger = LogManager.getLogger(CharsetIAICCommandHandlerImpl.class);

   @Override
   public void process(NVT nvt, int cmd) throws IOException {
      switch (cmd) {
         case IAC.IAC_COMMAND_DO:
            logger.info("Received IAC DO charset");
            nvt.getNvtOptions().setClientcancharset(true);
            break;
         case IAC.IAC_COMMAND_DONT:
            logger.info("Received IAC DONT charset");
            nvt.getNvtOptions().setClientcancharset(false);
            break;
         case IAC.IAC_COMMAND_WILL:
            logger.info("Received IAC WILL charset");
            break;
         case IAC.IAC_COMMAND_WONT:
            logger.info("Received IAC WONT charset");
            break;
         case IAC.IAC_COMMAND_SB:
            logger.info("Received IAC SB charset");
            break;
         default:
            logger.info("Received Unknown IAC Command:" + cmd);
            break;
      }
   }

   @Override
   public int getCommand() {
      return IAC_CODE_CHARSET;
   }

   @Override
   public String getDescription() {
      return "CHARSET";
   }
}

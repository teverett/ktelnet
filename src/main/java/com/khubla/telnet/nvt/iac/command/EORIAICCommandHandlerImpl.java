/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet.nvt.iac.command;

import com.khubla.telnet.nvt.IACCommandHandler;
import com.khubla.telnet.nvt.NVT;
import com.khubla.telnet.nvt.stream.IACProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * TELNET END OF RECORD OPTION - RFC 885
 *
 * @author tom
 */
public class EORIAICCommandHandlerImpl extends AbstractIACCommandHandler {
   /**
    * logger
    */
   private static final Logger logger = LoggerFactory.getLogger(EORIAICCommandHandlerImpl.class);
   // RFC 885
   public static final int  IAC_CODE_EOR = 25;

   @Override
   public void process(NVT nvt, int cmd) throws IOException {
      switch (cmd) {
         case IACProcessor.IAC_COMMAND_DO:
            logger.info("Received IAC DO EOR");
            // we don't do EOR
            nvt.sendIACCommand(IACProcessor.IAC_COMMAND_WONT, IAC_CODE_EOR);
            break;
         case IACProcessor.IAC_COMMAND_DONT:
            logger.info("Received IAC DONT EOR");
            break;
         case IACProcessor.IAC_COMMAND_WILL:
            logger.info("Received IAC WILL EOR");
            nvt.getNvtOptions().setEor(true);
            break;
         case IACProcessor.IAC_COMMAND_WONT:
            logger.info("Received IAC WONT EOR");
            nvt.getNvtOptions().setEor(false);
            break;
         case IACProcessor.IAC_COMMAND_SB:
            logger.info("Received IAC SB EOR");
            break;
         default:
            logger.info("Received Unknown IAC Command:" + cmd);
            break;
      }
   }

   @Override
   public int getCommand() {
      return IAC_CODE_EOR;
   }

   @Override
   public String getDescription() {
      return "EOR";
   }

   @Override
   public boolean negotiate() {
      return false;
   }
}

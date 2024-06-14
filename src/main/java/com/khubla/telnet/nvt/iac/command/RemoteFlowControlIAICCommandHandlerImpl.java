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

public class RemoteFlowControlIAICCommandHandlerImpl extends AbstractIACCommandHandler {
   /**
    * logger
    */
   private static final Logger logger = LoggerFactory.getLogger(RemoteFlowControlIAICCommandHandlerImpl.class);
   // RFC 1372
   public static final int  IAC_CODE_REMOTE_FLOW_CONTROL = 33;

   @Override
   public void process(NVT nvt, int cmd) throws IOException {
      switch (cmd) {
         case IACProcessor.IAC_COMMAND_DO:
            logger.info("Received IAC DO remoteflowcontrol");
            break;
         case IACProcessor.IAC_COMMAND_DONT:
            logger.info("Received IAC DONT remoteflowcontrol");
            break;
         case IACProcessor.IAC_COMMAND_WILL:
            logger.info("Received IAC WILL remoteflowcontrol");
            // we dont do flow control
            nvt.sendIACCommand(IACProcessor.IAC_COMMAND_DONT, IAC_CODE_REMOTE_FLOW_CONTROL);
            break;
         case IACProcessor.IAC_COMMAND_WONT:
            logger.info("Received IAC WONT remoteflowcontrol");
            break;
         case IACProcessor.IAC_COMMAND_SB:
            logger.info("Received IAC SB remoteflowcontrol");
            break;
         default:
            logger.info("Received Unknown IAC Command:" + cmd);
            break;
      }
   }

   @Override
   public int getCommand() {
      return IAC_CODE_REMOTE_FLOW_CONTROL;
   }

   @Override
   public String getDescription() {
      return "REMOTEFLOWCONTROL";
   }

   @Override
   public boolean negotiate() {
      return false;
   }
}

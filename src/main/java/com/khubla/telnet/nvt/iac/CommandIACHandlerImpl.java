/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet.nvt.iac;

import java.io.IOException;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.khubla.telnet.nvt.IACCommandHandler;
import com.khubla.telnet.nvt.IACHandler;
import com.khubla.telnet.nvt.NVT;
import com.khubla.telnet.nvt.iac.command.EchoIAICCommandHandlerImpl;
import com.khubla.telnet.nvt.iac.command.SGIACCommandHandlerImpl;
import com.khubla.telnet.nvt.iac.command.TermspeedIACCommandHandlerImpl;
import com.khubla.telnet.nvt.iac.command.TermtypeIACCommandHandlerImpl;

public class CommandIACHandlerImpl implements IACHandler {
   /**
    * logger
    */
   static final Logger logger = LoggerFactory.getLogger(CommandIACHandlerImpl.class);
   /**
    * IAC Command Handlers
    */
   private final HashMap<Integer, IACCommandHandler> iacCommandHandlers = new HashMap<Integer, IACCommandHandler>();

   public CommandIACHandlerImpl() {
      /*
       * IAC commands
       */
      iacCommandHandlers.put(NVT.IAC_CODE_ECHO, new EchoIAICCommandHandlerImpl());
      iacCommandHandlers.put(NVT.IAC_CODE_SUPPRESS_GOAHEAD, new SGIACCommandHandlerImpl());
      iacCommandHandlers.put(NVT.IAC_CODE_TERMTYPE, new TermtypeIACCommandHandlerImpl());
      iacCommandHandlers.put(NVT.IAC_CODE_TERMSPEED, new TermspeedIACCommandHandlerImpl());
   }

   @Override
   public void process(NVT nvt, int cmd, int option) throws IOException {
      final IACCommandHandler iacCommandHandler = iacCommandHandlers.get(option);
      if (null != iacCommandHandler) {
         iacCommandHandler.process(nvt, cmd);
      } else {
         logger.info("No handler for AIC Command:" + option);
         /*
          * send a "nope"
          */
         nvt.sendIACCommand(NVT.IAC_COMMAND_WONT, option);
      }
   }
}

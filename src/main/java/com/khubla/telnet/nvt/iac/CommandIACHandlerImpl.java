/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet.nvt.iac;

import java.io.IOException;
import java.util.HashMap;

import com.khubla.telnet.nvt.iac.command.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.khubla.telnet.nvt.IACCommandHandler;
import com.khubla.telnet.nvt.NVT;

public class CommandIACHandlerImpl implements IACHandler {
   /**
    * logger
    */
   private static final Logger logger = LoggerFactory.getLogger(CommandIACHandlerImpl.class);
   /**
    * IAC Command Handlers
    */
   private final HashMap<Integer, IACCommandHandler> iacCommandHandlers = new HashMap<Integer, IACCommandHandler>();

   public CommandIACHandlerImpl() {
      /*
       * IAC commands
       */
      // commands which are not fully implemented are commented out
      iacCommandHandlers.put(IACHandler.IAC_CODE_ECHO, new EchoIAICCommandHandlerImpl());
      iacCommandHandlers.put(IACHandler.IAC_CODE_SUPPRESS_GOAHEAD, new SGIACCommandHandlerImpl());
      iacCommandHandlers.put(IACHandler.IAC_CODE_TERMTYPE, new TermtypeIACCommandHandlerImpl());
      iacCommandHandlers.put(IACHandler.IAC_CODE_TERMSPEED, new TermspeedIACCommandHandlerImpl());
      iacCommandHandlers.put(IACHandler.IAC_CODE_MARK, new MarkIAICCommandHandlerImpl());
      iacCommandHandlers.put(IACHandler.IAC_CODE_STATUS, new StatusIAICCommandHandlerImpl());
      iacCommandHandlers.put(IACHandler.IAC_CODE_WINSIZE, new WinsizeIAICCommandHandlerImpl());
      // iacCommandHandlers.put(IACHandler.IAC_CODE_REMOTE_FLOW_CONTROL, new RemoteFlowControlIAICCommandHandlerImpl());
      // iacCommandHandlers.put(IACHandler.IAC_CODE_LINEMODE, new LineModeIAICCommandHandlerImpl());
      // iacCommandHandlers.put(IACHandler.IAC_CODE_AUTHENTICATION, new AuthenticationIAICCommandHandlerImpl());
      iacCommandHandlers.put(IACHandler.IAC_CODE_BINARY, new BinaryIAICCommandHandlerImpl());
      iacCommandHandlers.put(IACHandler.IAC_CODE_EOR, new EORIAICCommandHandlerImpl());
      // iacCommandHandlers.put(IACHandler.IAC_CODE_3270_REGIME, new TN3270RegimeIAICCommandHandlerImpl());
      // iacCommandHandlers.put(IACHandler.IAC_CODE_ENVVAR, new EnvvarIAICCommandHandlerImpl());
      // iacCommandHandlers.put(IACHandler.IAC_CODE_NAOCRD, new NAOCRDIAICCommandHandlerImpl());
      // iacCommandHandlers.put(IACHandler.IAC_CODE_NAOHTS, new NAHTSIAICCommandHandlerImpl());
      // iacCommandHandlers.put(IACHandler.IAC_CODE_NAOHTD, new NAOHTDIAICCommandHandlerImpl());
      // iacCommandHandlers.put(IACHandler.IAC_CODE_NAOFFD, new NAOFFDIAICCommandHandlerImpl());
      // iacCommandHandlers.put(IACHandler.IAC_CODE_NAOVTS, new NAOVTSIAICCommandHandlerImpl());
      // iacCommandHandlers.put(IACHandler.IAC_CODE_NAOVTD, new NAOVTDIAICCommandHandlerImpl());
      // iacCommandHandlers.put(IACHandler.IAC_CODE_NAOLFD, new NAOFLDIAICCommandHandlerImpl());
      // iacCommandHandlers.put(IACHandler.IAC_CODE_RCTE, new RCTEIAICCommandHandlerImpl());
      // iacCommandHandlers.put(IACHandler.IAC_CODE_TN3270E, new TN3270EIAICCommandHandlerImpl());
      // iacCommandHandlers.put(IACHandler.IAC_CODE_CHARSET, new CharsetIAICCommandHandlerImpl());
      // acCommandHandlers.put(IACHandler.IAC_CODE_EXTENDED_ASCII, new ExtendedASCIIIAICCommandHandlerImpl());
      iacCommandHandlers.put(IACHandler.IAC_CODE_NEW_ENVIRON, new NewEnvironIAICCommandHandlerImpl());
      // iacCommandHandlers.put(IACHandler.IAC_CODE_ENCRYPT, new EncryptIAICCommandHandlerImpl());
      // iacCommandHandlers.put(IACHandler.IAC_CODE_EXTENDED_OPTIONS_LIST, new ExtendedOptionsListIAICCommandHandlerImpl());
      // iacCommandHandlers.put(IACHandler.IAC_CODE_LOGOUT, new LogoutIAICCommandHandlerImpl());
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
         if (cmd == IACCommandHandler.IAC_COMMAND_DO) {
            nvt.sendIACCommand(IACCommandHandler.IAC_COMMAND_WONT, option);
         } else if (cmd == IACCommandHandler.IAC_COMMAND_WILL) {
            nvt.sendIACCommand(IACCommandHandler.IAC_COMMAND_DONT, option);
         } else {
            logger.info("Unexpected IAC command:" + cmd + " option: " + option);
         }
      }
   }
}

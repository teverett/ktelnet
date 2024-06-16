/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet.nvt.iac.impl;

import com.khubla.telnet.nvt.NVT;
import com.khubla.telnet.nvt.command.IACCommandHandler;
import com.khubla.telnet.nvt.command.impl.*;
import com.khubla.telnet.nvt.iac.IACHandler;
import com.khubla.telnet.nvt.stream.IAC;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;

public class CommandIACHandlerImpl implements IACHandler {
   /**
    * logger
    */
   private static final Logger logger = LogManager.getLogger(CommandIACHandlerImpl.class);
   /**
    * IAC Command Handlers
    */
   private final HashMap<Integer, IACCommandHandler> iacCommandHandlers = new HashMap<Integer, IACCommandHandler>();

   public CommandIACHandlerImpl() {
      /*
       * IAC commands
       */
      // commands which are not fully implemented are commented out
      addIACCommandHandler(new EchoIAICCommandHandlerImpl());
      addIACCommandHandler(new SGIACCommandHandlerImpl());
      addIACCommandHandler(new TermtypeIACCommandHandlerImpl());
      addIACCommandHandler(new TermspeedIACCommandHandlerImpl());
      addIACCommandHandler(new MarkIAICCommandHandlerImpl());
      addIACCommandHandler(new StatusIAICCommandHandlerImpl());
      addIACCommandHandler(new WinsizeIAICCommandHandlerImpl());
      // addIACCommandHandler( new RemoteFlowControlIAICCommandHandlerImpl());
      // addIACCommandHandler(new LineModeIAICCommandHandlerImpl());
      addIACCommandHandler(new AuthenticationIAICCommandHandlerImpl());
      addIACCommandHandler(new BinaryIAICCommandHandlerImpl());
      addIACCommandHandler(new EORIAICCommandHandlerImpl());
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
      addIACCommandHandler(new NewEnvironIAICCommandHandlerImpl());
      // addIACCommandHandler(new EncryptIAICCommandHandlerImpl());
      // addIACCommandHandler( new ExtendedOptionsListIAICCommandHandlerImpl());
      // iacCommandHandlers.put(IACHandler.IAC_CODE_LOGOUT, new LogoutIAICCommandHandlerImpl());
   }

   private void addIACCommandHandler(IACCommandHandler iacCommandHandler) {
      iacCommandHandlers.put(iacCommandHandler.getCommand(), iacCommandHandler);
   }

   public IACCommandHandler getIACCommandHandler(int command) {
      return iacCommandHandlers.get(command);
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
         if (cmd == IAC.IAC_COMMAND_DO) {
            nvt.sendIACCommand(IAC.IAC_COMMAND_WONT, option);
         } else if (cmd == IAC.IAC_COMMAND_WILL) {
            nvt.sendIACCommand(IAC.IAC_COMMAND_DONT, option);
         } else {
            logger.info("Unexpected IAC command:" + cmd + " option: " + option);
         }
      }
   }
}

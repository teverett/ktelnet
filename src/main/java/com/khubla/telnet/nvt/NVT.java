/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet.nvt;

import com.khubla.telnet.nvt.command.IACCommandHandler;
import com.khubla.telnet.nvt.command.impl.*;
import com.khubla.telnet.nvt.iac.impl.CommandIACHandlerImpl;
import com.khubla.telnet.nvt.spy.NVTSpy;
import com.khubla.telnet.nvt.stream.IAC;
import com.khubla.telnet.nvt.stream.IACProcessorImpl;
import com.khubla.telnet.nvt.stream.NVTStream;
import com.khubla.telnet.nvt.stream.NVTStreamImpl;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.net.Socket;

public class NVT implements Flushable, Closeable {
   /**
    * logger
    */
   private static final Logger logger = LogManager.getLogger(NVT.class);
   /**
    * socket
    */
   private final Socket socket;
   /**
    * options
    */
   @Getter
   private final NVTOptions nvtOptions = new NVTOptions();
   /**
    * NVTStream
    */
   @Getter
   private final NVTStream nvtStream;

   public NVT(Socket socket) throws IOException {
      super();
      this.socket = socket;
      /*
       * stream
       */
      nvtStream = new NVTStreamImpl(socket.getInputStream(), socket.getOutputStream(), new IACProcessorImpl(this));
      /*
       * send config
       */
      sendConfigParameters();
      /*
       * send GA
       */
      if (!nvtOptions.isSuppressGoAhead()) {
         // sendIACCommand(IACIAC.IAC_COMMAND_GA);
      }
   }

   @Override
   public void close() {
      try {
         socket.close();
      } catch (final Exception e) {
         logger.error(e.getMessage(), e);
      }
   }

   @Override
   public void flush() throws IOException {
      nvtStream.flush();
   }

   private void sendConfigParameters() throws IOException {
      /*
       * let's exchange options
       */
      sendIACCommand(IAC.IAC_COMMAND_DO, ExtendedOptionsListIAICCommandHandlerImpl.IAC_CODE_EXTENDED_OPTIONS_LIST);
      /*
       * i don't talk binary
       */
      sendIACCommand(IAC.IAC_COMMAND_DONT, BinaryIAICCommandHandlerImpl.IAC_CODE_BINARY);
      /*
       * no go-aheads pls
       */
      sendIACCommand(IAC.IAC_COMMAND_DO, SGIACCommandHandlerImpl.IAC_CODE_SUPPRESS_GOAHEAD);
      /*
       * echo
       */
      sendIACCommand(IAC.IAC_COMMAND_WILL, EchoIAICCommandHandlerImpl.IAC_CODE_ECHO);
      /*
       * i don't line line mode
       */
      sendIACCommand(IAC.IAC_COMMAND_DONT, LineModeIAICCommandHandlerImpl.IAC_CODE_LINEMODE);
      /*
       * tell me your terminal type
       */
      sendIACCommand(IAC.IAC_COMMAND_DO, TermtypeIACCommandHandlerImpl.IAC_CODE_TERMTYPE);
      /*
       * EOR
       */
      sendIACCommand(IAC.IAC_COMMAND_DONT, EORIAICCommandHandlerImpl.IAC_CODE_EOR);
      /*
       * tell me your termspeed type
       */
      sendIACCommand(IAC.IAC_COMMAND_DO, TermspeedIACCommandHandlerImpl.IAC_CODE_TERMSPEED);
      /*
       * tell me your winsize
       */
      sendIACCommand(IAC.IAC_COMMAND_DO, WinsizeIAICCommandHandlerImpl.IAC_CODE_WINSIZE);
      /*
       * i accept environment variables
       */
      // sendIACCommand(IACCommandHandler.IAC_COMMAND_DO, IACHandler.IAC_CODE_ENVVAR);
      /*
       * i am able to receive 3270E information
       */
      // sendIACCommand(IACCommandHandler.IAC_COMMAND_DO, IACHandler.IAC_CODE_TN3270E);
      /*
       * i would like to talk about charsets
       */
      // sendIACCommand(IACCommandHandler.IAC_COMMAND_WILL, IACHandler.IAC_CODE_CHARSET);
      /*
       * i like to talk in extended ASCII
       */
      // sendIACCommand(IACCommandHandler.IAC_COMMAND_WILL, IACHandler.IAC_CODE_EXTENDED_ASCII);
      /*
       * lets talk about the environment
       */
      //    sendIACCommand(IACCommandHandler.IAC_COMMAND_DO, IACHandler.IAC_CODE_NEW_ENVIRON);
      /*
       * query 3270. we must have negotiated termtype, EOR, and and binary before we can ask for 3270 regime
       */
      // sendIACCommand(IACCommandHandler.IAC_COMMAND_DO, IACHandler.IAC_CODE_3270_REGIME);
      /*
       * tell me your status
       */
      // we can send this but no telnet clients i am testing with will do it....
      // sendIACCommand(IAC.IAC_COMMAND_DO, StatusIAICCommandHandlerImpl.IAC_CODE_STATUS);
   }

   public void sendIACCommand(int command, int option) throws IOException {
      CommandIACHandlerImpl cmdHandler = new CommandIACHandlerImpl();
      IACCommandHandler iacCommandHandler = cmdHandler.getIACCommandHandler(option);
      if (null != iacCommandHandler) {
         logger.info("Sent IAC command: " + commandToString(command) + " option: " + iacCommandHandler.getDescription());
         nvtStream.writeBytes(IAC.IAC_IAC, command, option);
         flush();
      } else {
         throw new IOException("Unknown option: " + option);
      }
   }

   public void setNvtSpy(NVTSpy nvtSpy) {
      nvtStream.setNvtSpy(nvtSpy);
   }

   private String commandToString(int command) {
      switch (command) {
         case IAC.IAC_COMMAND_DO:
            return "do";
         case IAC.IAC_COMMAND_DONT:
            return "dont";
         case IAC.IAC_COMMAND_SB:
            return "sb";
         case IAC.IAC_COMMAND_NOP:
            return "nop";
         case IAC.IAC_COMMAND_WILL:
            return "will";
         case IAC.IAC_COMMAND_WONT:
            return "wont";
         default:
            return "<unknown>";
      }
   }

   public String getClientAddress() {
      return socket.getInetAddress().toString();
   }
}

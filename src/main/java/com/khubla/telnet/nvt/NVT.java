/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet.nvt;

import com.khubla.telnet.nvt.iac.CommandIACHandlerImpl;
import com.khubla.telnet.nvt.iac.IACHandler;
import com.khubla.telnet.nvt.iac.NOPIACHandlerImpl;
import com.khubla.telnet.nvt.iac.command.BinaryIAICCommandHandlerImpl;
import com.khubla.telnet.nvt.iac.command.EchoIAICCommandHandlerImpl;
import com.khubla.telnet.nvt.iac.command.SGIACCommandHandlerImpl;
import com.khubla.telnet.nvt.spy.NVTSpy;
import com.khubla.telnet.nvt.stream.IACProcessor;
import com.khubla.telnet.nvt.stream.NVTStream;
import com.khubla.telnet.nvt.stream.NVTStreamImpl;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

public class NVT implements Flushable, Closeable, IACProcessor {
   /**
    * logger
    */
   private static final Logger logger = LoggerFactory.getLogger(NVT.class);
   /**
    * socket
    */
   private final Socket socket;
   /**
    * IAC handlers
    */
   private final HashMap<Integer, IACHandler> iacHandlers = new HashMap<Integer, IACHandler>();
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
      nvtStream = new NVTStreamImpl(socket.getInputStream(), socket.getOutputStream(), this);
      /*
       * IACs
       */
      iacHandlers.put(IACCommandHandler.IAC_COMMAND_WILL, new CommandIACHandlerImpl());
      iacHandlers.put(IACCommandHandler.IAC_COMMAND_WONT, new CommandIACHandlerImpl());
      iacHandlers.put(IACCommandHandler.IAC_COMMAND_DO, new CommandIACHandlerImpl());
      iacHandlers.put(IACCommandHandler.IAC_COMMAND_DONT, new CommandIACHandlerImpl());
      iacHandlers.put(IACCommandHandler.IAC_COMMAND_SB, new CommandIACHandlerImpl());
      iacHandlers.put(IACCommandHandler.IAC_COMMAND_NOP, new NOPIACHandlerImpl());
      /*
       * send config
       */
      sendConfigParameters();
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

   @Override
   public void processIAC() throws IOException {
      final int cmd = nvtStream.readRawByte();
      final int option = nvtStream.readRawByte();
      final IACHandler iacHandler = iacHandlers.get(cmd);
      if (null != iacHandler) {
         iacHandler.process(this, cmd, option);
      } else {
         logger.info("No handler for AIC command:" + cmd + " option:" + option);
      }
   }

   private void sendConfigParameters() throws IOException {
      /*
       * lets exchange options
       */
      // sendIACCommand(IACCommandHandler.IAC_COMMAND_WILL, IACHandler.IAC_CODE_EXTENDED_OPTIONS_LIST);
      // sendIACCommand(IACCommandHandler.IAC_COMMAND_DO, IACHandler.IAC_CODE_EXTENDED_OPTIONS_LIST);
      /*
       * i can talk binary
       */
      sendIACCommand(IACCommandHandler.IAC_COMMAND_DO, BinaryIAICCommandHandlerImpl.IAC_CODE_BINARY);
      /*
       * no go-aheads pls
       */
      sendIACCommand(IACCommandHandler.IAC_COMMAND_DO, SGIACCommandHandlerImpl.IAC_CODE_SUPPRESS_GOAHEAD);
      /*
       * tell me your status
       */
      // sendIACCommand(IACCommandHandler.IAC_COMMAND_DO, IACHandler.IAC_CODE_STATUS);
      /*
       * echo
       */
      sendIACCommand(IACCommandHandler.IAC_COMMAND_WILL, EchoIAICCommandHandlerImpl.IAC_CODE_ECHO);
      /*
       * ask to linemode
       */
      // sendIACCommand(IACCommandHandler.IAC_COMMAND_DO, IACHandler.IAC_CODE_LINEMODE);
      /*
       * i accept environment variables
       */
      // sendIACCommand(IACCommandHandler.IAC_COMMAND_DO, IACHandler.IAC_CODE_ENVVAR);
      /*
       * tell me your terminal type
       */
      //    sendIACCommand(IACCommandHandler.IAC_COMMAND_DO, IACHandler.IAC_CODE_TERMTYPE);
      /*
       * EOR
       */
      // sendIACCommand(IACCommandHandler.IAC_COMMAND_DO, IACHandler.IAC_CODE_EOR);
      /*
       * query 3270. we must have negotiated termtype, EOR, and and binary before we can ask for 3270 regime
       */
      // sendIACCommand(IACCommandHandler.IAC_COMMAND_DO, IACHandler.IAC_CODE_3270_REGIME);
      /*
       * tell me your termspeed type
       */
      //   sendIACCommand(IACCommandHandler.IAC_COMMAND_DO, IACHandler.IAC_CODE_TERMSPEED);
      /*
       * tell me your winsize
       */
      //   sendIACCommand(IACCommandHandler.IAC_COMMAND_DO, IACHandler.IAC_CODE_WINSIZE);
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
       * i can encrypt
       */
      // sendIACCommand(IACCommandHandler.IAC_COMMAND_DO, IACHandler.IAC_CODE_ENCRYPT);
      /*
       * logout
       */
      // sendIACCommand(IACCommandHandler.IAC_COMMAND_WILL, IACHandler.IAC_CODE_LOGOUT);
   }

   public void sendIACCommand(int command, int option) throws IOException {
      CommandIACHandlerImpl cmdHandler = new CommandIACHandlerImpl();
      IACCommandHandler iacCommandHandler = cmdHandler.getIACCommandHandler(option);
      if (null != iacCommandHandler) {
         logger.info("Sent IAC command: " + commandToString(command) + " option: " + iacCommandHandler.getDescription());
         nvtStream.writeBytes(IACCommandHandler.IAC_IAC, command, option);
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
         case IACCommandHandler.IAC_COMMAND_DO:
            return "do";
         case IACCommandHandler.IAC_COMMAND_DONT:
            return "dont";
         case IACCommandHandler.IAC_COMMAND_SB:
            return "sb";
         case IACCommandHandler.IAC_COMMAND_NOP:
            return "nop";
         case IACCommandHandler.IAC_COMMAND_WILL:
            return "will";
         case IACCommandHandler.IAC_COMMAND_WONT:
            return "wont";
         default:
            return "<unknown>";
      }
   }

   public String getClientAddress() {
      return socket.getInetAddress().toString();
   }
}

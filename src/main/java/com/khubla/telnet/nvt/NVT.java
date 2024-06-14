/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet.nvt;

import com.khubla.telnet.nvt.iac.CommandIACHandlerImpl;
import com.khubla.telnet.nvt.iac.command.BinaryIAICCommandHandlerImpl;
import com.khubla.telnet.nvt.iac.command.EchoIAICCommandHandlerImpl;
import com.khubla.telnet.nvt.iac.command.SGIACCommandHandlerImpl;
import com.khubla.telnet.nvt.spy.NVTSpy;
import com.khubla.telnet.nvt.stream.IACProcessor;
import com.khubla.telnet.nvt.stream.IACProcessorImpl;
import com.khubla.telnet.nvt.stream.NVTStream;
import com.khubla.telnet.nvt.stream.NVTStreamImpl;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.net.Socket;

public class NVT implements Flushable, Closeable {
   /**
    * logger
    */
   private static final Logger logger = LoggerFactory.getLogger(NVT.class);
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
       * lets exchange options
       */
      // sendIACCommand(IACProcessor.IAC_COMMAND_WILL, IACHandler.IAC_CODE_EXTENDED_OPTIONS_LIST);
      // sendIACCommand(IACProcessor.IAC_COMMAND_DO, IACHandler.IAC_CODE_EXTENDED_OPTIONS_LIST);
      /*
       * i can talk binary
       */
      sendIACCommand(IACProcessor.IAC_COMMAND_DO, BinaryIAICCommandHandlerImpl.IAC_CODE_BINARY);
      /*
       * no go-aheads pls
       */
      sendIACCommand(IACProcessor.IAC_COMMAND_DO, SGIACCommandHandlerImpl.IAC_CODE_SUPPRESS_GOAHEAD);
      /*
       * tell me your status
       */
      // sendIACCommand(IACProcessor.IAC_COMMAND_DO, IACHandler.IAC_CODE_STATUS);
      /*
       * echo
       */
      sendIACCommand(IACProcessor.IAC_COMMAND_WILL, EchoIAICCommandHandlerImpl.IAC_CODE_ECHO);
      /*
       * ask to linemode
       */
      // sendIACCommand(IACProcessor.IAC_COMMAND_DO, IACHandler.IAC_CODE_LINEMODE);
      /*
       * i accept environment variables
       */
      // sendIACCommand(IACProcessor.IAC_COMMAND_DO, IACHandler.IAC_CODE_ENVVAR);
      /*
       * tell me your terminal type
       */
      //    sendIACCommand(IACProcessor.IAC_COMMAND_DO, IACHandler.IAC_CODE_TERMTYPE);
      /*
       * EOR
       */
      // sendIACCommand(IACProcessor.IAC_COMMAND_DO, IACHandler.IAC_CODE_EOR);
      /*
       * query 3270. we must have negotiated termtype, EOR, and and binary before we can ask for 3270 regime
       */
      // sendIACCommand(IACProcessor.IAC_COMMAND_DO, IACHandler.IAC_CODE_3270_REGIME);
      /*
       * tell me your termspeed type
       */
      //   sendIACCommand(IACProcessor.IAC_COMMAND_DO, IACHandler.IAC_CODE_TERMSPEED);
      /*
       * tell me your winsize
       */
      //   sendIACCommand(IACProcessor.IAC_COMMAND_DO, IACHandler.IAC_CODE_WINSIZE);
      /*
       * i am able to receive 3270E information
       */
      // sendIACCommand(IACProcessor.IAC_COMMAND_DO, IACHandler.IAC_CODE_TN3270E);
      /*
       * i would like to talk about charsets
       */
      // sendIACCommand(IACProcessor.IAC_COMMAND_WILL, IACHandler.IAC_CODE_CHARSET);
      /*
       * i like to talk in extended ASCII
       */
      // sendIACCommand(IACProcessor.IAC_COMMAND_WILL, IACHandler.IAC_CODE_EXTENDED_ASCII);
      /*
       * lets talk about the environment
       */
      //    sendIACCommand(IACProcessor.IAC_COMMAND_DO, IACHandler.IAC_CODE_NEW_ENVIRON);
   }

   public void sendIACCommand(int command, int option) throws IOException {
      CommandIACHandlerImpl cmdHandler = new CommandIACHandlerImpl();
      IACCommandHandler iacCommandHandler = cmdHandler.getIACCommandHandler(option);
      if (null != iacCommandHandler) {
         logger.info("Sent IAC command: " + commandToString(command) + " option: " + iacCommandHandler.getDescription());
         nvtStream.writeBytes(IACProcessor.IAC_IAC, command, option);
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
         case IACProcessor.IAC_COMMAND_DO:
            return "do";
         case IACProcessor.IAC_COMMAND_DONT:
            return "dont";
         case IACProcessor.IAC_COMMAND_SB:
            return "sb";
         case IACProcessor.IAC_COMMAND_NOP:
            return "nop";
         case IACProcessor.IAC_COMMAND_WILL:
            return "will";
         case IACProcessor.IAC_COMMAND_WONT:
            return "wont";
         default:
            return "<unknown>";
      }
   }

   public String getClientAddress() {
      return socket.getInetAddress().toString();
   }
}

/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet.nvt;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.khubla.telnet.nvt.iac.CommandIACHandlerImpl;
import com.khubla.telnet.nvt.iac.IACHandler;
import com.khubla.telnet.nvt.iac.NOPIACHandlerImpl;
import com.khubla.telnet.nvt.spy.NVTSpy;
import com.khubla.telnet.nvt.stream.IACProcessor;
import com.khubla.telnet.nvt.stream.NVTStream;
import com.khubla.telnet.nvt.stream.NVTStreamImpl;

public class NVT implements Flushable, Closeable, IACProcessor {
   /**
    * logger
    */
   private static final Logger logger = LoggerFactory.getLogger(NVT.class);
   /**
    * EOR (tn3270)
    */
   public static final int EOR = 239;
   /**
    * socket
    */
   private final Socket socket;
   /**
    * term x
    */
   private short termX;
   /**
    * term y
    */
   private short termY;
   /**
    * term type
    */
   private String termtype;
   /**
    * term speed
    */
   private String termSpeed;
   /**
    * binary
    */
   private boolean binaryMode = false;
   /**
    * tn3270
    */
   private boolean tn3270 = false;
   /**
    * tn3270 device
    */
   private String tn3270Device = null;
   /**
    * tn3270 functions
    */
   private Set<Integer> tn3270Functions = null;
   /**
    * eor
    */
   private boolean eor = false;
   /**
    * extended ascii
    */
   private boolean clientcanextendedascii = false;
   /**
    * clientcancharset
    */
   private boolean clientcancharset = false;
   /**
    * NVTStream
    */
   private NVTStream nvtStream;
   /**
    * IAC handlers
    */
   private final HashMap<Integer, IACHandler> iacHandlers = new HashMap<Integer, IACHandler>();

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

   public NVTStream getNvtStream() {
      return nvtStream;
   }

   public String getTermSpeed() {
      return termSpeed;
   }

   public String getTermtype() {
      return termtype;
   }

   public short getTermX() {
      return termX;
   }

   public short getTermY() {
      return termY;
   }

   public String getTn3270Device() {
      return tn3270Device;
   }

   public Set<Integer> getTn3270Functions() {
      return tn3270Functions;
   }

   public boolean isBinaryMode() {
      return binaryMode;
   }

   public boolean isClientcancharset() {
      return clientcancharset;
   }

   public boolean isClientcanextendedascii() {
      return clientcanextendedascii;
   }

   public boolean isEor() {
      return eor;
   }

   public boolean isTn3270() {
      return tn3270;
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
      sendIACCommand(IACCommandHandler.IAC_COMMAND_DO, IACHandler.IAC_CODE_BINARY);
      /*
       * no go-aheads pls
       */
      sendIACCommand(IACCommandHandler.IAC_COMMAND_DO, IACHandler.IAC_CODE_SUPPRESS_GOAHEAD);
      /*
       * tell me your status
       */
      // sendIACCommand(IACCommandHandler.IAC_COMMAND_DO, IACHandler.IAC_CODE_STATUS);
      /*
       * echo
       */
      sendIACCommand(IACCommandHandler.IAC_COMMAND_WILL, IACHandler.IAC_CODE_ECHO);
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
      sendIACCommand(IACCommandHandler.IAC_COMMAND_DO, IACHandler.IAC_CODE_TERMTYPE);
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
      sendIACCommand(IACCommandHandler.IAC_COMMAND_DO, IACHandler.IAC_CODE_TERMSPEED);
      /*
       * tell me your winsize
       */
      sendIACCommand(IACCommandHandler.IAC_COMMAND_DO, IACHandler.IAC_CODE_WINSIZE);
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
      sendIACCommand(IACCommandHandler.IAC_COMMAND_DO, IACHandler.IAC_CODE_NEW_ENVIRON);
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
      nvtStream.writeBytes(IACCommandHandler.IAC_IAC, command, option);
      flush();
   }

   public void setBinaryMode(boolean binaryMode) {
      this.binaryMode = binaryMode;
   }

   public void setClientcancharset(boolean clientcancharset) {
      this.clientcancharset = clientcancharset;
   }

   public void setClientcanextendedascii(boolean clientcanextendedascii) {
      this.clientcanextendedascii = clientcanextendedascii;
   }

   public void setEor(boolean eor) {
      this.eor = eor;
   }

   public void setNvtSpy(NVTSpy nvtSpy) {
      nvtStream.setNvtSpy(nvtSpy);
   }

   public void setNvtStream(NVTStreamImpl nvtStream) {
      this.nvtStream = nvtStream;
   }

   public void setTermSpeed(String termSpeed) {
      this.termSpeed = termSpeed;
   }

   public void setTermtype(String termtype) {
      this.termtype = termtype;
   }

   public void setTermX(short termX) {
      this.termX = termX;
   }

   public void setTermY(short termY) {
      this.termY = termY;
   }

   public void setTn3270(boolean tn3270) {
      this.tn3270 = tn3270;
   }

   public void setTn3270Device(String tn3270Device) {
      this.tn3270Device = tn3270Device;
   }

   public void setTn3270Functions(Set<Integer> tn3270Functions) {
      this.tn3270Functions = tn3270Functions;
   }
}

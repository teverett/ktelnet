/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet.nvt;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.Flushable;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.khubla.telnet.nvt.iac.CommandIACHandlerImpl;

public class NVT implements Flushable, Closeable {
   /**
    * logger
    */
   static final Logger logger = LoggerFactory.getLogger(NVT.class);
   /**
    * keys (RFC 854)
    */
   // No Operation
   public static final int KEY_NULL = 0;
   // Produces an audible or visible signal (which does NOT move the print head).
   public static final int KEY_BEL = 7;
   // Moves the print head one character position towards the left margin.
   public static final int KEY_BS = 8;
   // Moves the printer to the next horizontal tab stop. It remains unspecified how either party determines or establishes where such tab stops are located.
   public static final int KEY_HT = 9;
   // Moves the printer to the next print line, keeping the same horizontal position.
   public static final int KEY_LF = 10;
   // Moves the printer to the next vertical tab stop. It remains unspecified how either party determines or establishes where such tab stops are located.
   public static final int KEY_VT = 11;
   // Moves the printer to the top of the next page, keeping the same horizontal position.
   public static final int KEY_FF = 12;
   // Moves the printer to the left margin of the current line.
   public static final int KEY_CR = 13;
   public static final int KEY_ESC = 27;
   public static final int KEY_DEL = 127;
   /**
    * EOL
    */
   public final static String EOL = new String("\r\n");
   /**
    * in stream
    */
   private final DataInputStream dataInputStream;
   /**
    * out stream
    */
   private final DataOutputStream dataOutputStream;
   /**
    * charset
    */
   private final Charset charsetUTF8 = Charset.forName("UTF-8");
   /**
    * socket
    */
   private final Socket socket;
   /**
    * autoflush
    */
   private boolean autoflush = true;
   /**
    * echo
    */
   private boolean echo = true;
   /**
    * IAC handlers
    */
   private final HashMap<Integer, IACHandler> iacHandlers = new HashMap<Integer, IACHandler>();
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
    * auth handler
    */
   private final AuthenticationHandler authenticationHandler;
   /**
    * binary
    */
   private boolean binaryMode = false;

   public NVT(Socket socket) throws IOException {
      this(socket, null);
   }

   public NVT(Socket socket, AuthenticationHandler authenticationHandler) throws IOException {
      super();
      this.authenticationHandler = authenticationHandler;
      this.socket = socket;
      dataInputStream = new DataInputStream(socket.getInputStream());
      dataOutputStream = new DataOutputStream(socket.getOutputStream());
      /*
       * IACs
       */
      iacHandlers.put(IACCommandHandler.IAC_COMMAND_WILL, new CommandIACHandlerImpl());
      iacHandlers.put(IACCommandHandler.IAC_COMMAND_WONT, new CommandIACHandlerImpl());
      iacHandlers.put(IACCommandHandler.IAC_COMMAND_DO, new CommandIACHandlerImpl());
      iacHandlers.put(IACCommandHandler.IAC_COMMAND_DONT, new CommandIACHandlerImpl());
      iacHandlers.put(IACCommandHandler.IAC_COMMAND_SB, new CommandIACHandlerImpl());
      /*
       * send config
       */
      sendConfigParameters();
   }

   @Override
   public void close() {
      try {
         dataInputStream.close();
      } catch (final Exception e) {
         logger.error(e.getMessage(), e);
      }
      try {
         dataOutputStream.close();
      } catch (final Exception e) {
         logger.error(e.getMessage(), e);
      }
      try {
         socket.close();
      } catch (final Exception e) {
         logger.error(e.getMessage(), e);
      }
   }

   @Override
   public void flush() throws IOException {
      dataOutputStream.flush();
   }

   public AuthenticationHandler getAuthenticationHandler() {
      return authenticationHandler;
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

   public boolean isAutoflush() {
      return autoflush;
   }

   public boolean isBinaryMode() {
      return binaryMode;
   }

   public boolean isEcho() {
      return echo;
   }

   private void processIAC() throws IOException {
      final int cmd = dataInputStream.read();
      final int option = dataInputStream.read();
      logger.info("IAC: " + cmd + " option: " + option);
      final IACHandler iacHandler = iacHandlers.get(cmd);
      if (null != iacHandler) {
         iacHandler.process(this, cmd, option);
      } else {
         logger.info("No handler for AIC command:" + cmd);
      }
   }

   public int readByte() throws IOException {
      final int c = dataInputStream.read();
      if (c == IACCommandHandler.IAC_IAC) {
         processIAC();
         return readByte();
      } else {
         if (isEcho()) {
            dataOutputStream.write(c);
         }
         return c;
      }
   }

   public String readln() throws IOException {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      boolean cont = true;
      int prevbyte = 0;
      int b = readByte();
      baos.write(b);
      while (cont) {
         prevbyte = b;
         b = readByte();
         if ((b == KEY_LF) && (prevbyte == KEY_CR)) {
            cont = false;
         } else if ((b == KEY_BS) || (b == KEY_DEL)) {
            /*
             * backspace and delete keys
             */
            String str = baos.toString(charsetUTF8.name());
            baos = new ByteArrayOutputStream();
            str = str.substring(0, str.length() - 1);
            if (str.length() > 0) {
               baos.write(str.getBytes(), 0, str.length());
            }
         } else if (b == KEY_ESC) {
            logger.info("ESC pressed");
         } else if (b == KEY_HT) {
            logger.info("TAB pressed");
         } else {
            baos.write(b);
         }
      }
      return baos.toString(charsetUTF8.name()).trim();
   }

   public int readRawByte() throws IOException {
      return dataInputStream.read();
   }

   public String readRawString(int marker) throws IOException {
      final ByteArrayOutputStream baos = new ByteArrayOutputStream();
      int b = readRawByte();
      while (b != marker) {
         baos.write(b);
         b = readRawByte();
      }
      return baos.toString(charsetUTF8.name()).trim();
   }

   public short readShort() throws IOException {
      return dataInputStream.readShort();
   }

   private void sendConfigParameters() throws IOException {
      /**
       * i can talk binary
       */
      sendIACCommand(IACCommandHandler.IAC_COMMAND_WILL, IACHandler.IAC_CODE_BINARY);
      /**
       * i dont provide status
       */
      sendIACCommand(IACCommandHandler.IAC_COMMAND_WONT, IACHandler.IAC_CODE_STATUS);
      /**
       * echo
       */
      if (isEcho()) {
         sendIACCommand(IACCommandHandler.IAC_COMMAND_WILL, IACHandler.IAC_CODE_ECHO);
      } else {
         sendIACCommand(IACCommandHandler.IAC_COMMAND_WONT, IACHandler.IAC_CODE_ECHO);
      }
      /**
       * auth
       */
      if (null != getAuthenticationHandler()) {
         /*
          * this is not a game of who-the-fuck-are-you! - Eddie Izzard
          */
         // sendIACCommand(IACCommandHandler.IAC_COMMAND_DO, IACHandler.IAC_CODE_AUTHENTICATION);
      }
   }

   public void sendIACCommand(int command, int option) throws IOException {
      writeBytes(IACCommandHandler.IAC_IAC, command, option);
      flush();
   }

   public void setAutoflush(boolean autoflush) {
      this.autoflush = autoflush;
   }

   public void setBinaryMode(boolean binaryMode) {
      this.binaryMode = binaryMode;
   }

   public void setEcho(boolean echo) {
      this.echo = echo;
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

   public void write(String str) throws IOException {
      dataOutputStream.write(str.getBytes(charsetUTF8), 0, str.length());
      if (isAutoflush()) {
         flush();
      }
   }

   public void writeBytes(int... b) throws IOException {
      for (final int i : b) {
         dataOutputStream.write(i);
      }
      if (isAutoflush()) {
         flush();
      }
   }

   public void writeln(String str) throws IOException {
      dataOutputStream.write(str.getBytes(charsetUTF8), 0, str.length());
      dataOutputStream.write(EOL.getBytes(charsetUTF8), 0, EOL.length());
      if (isAutoflush()) {
         flush();
      }
   }
}

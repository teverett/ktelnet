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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NVT implements Flushable, Closeable {
   /**
    * logger
    */
   static final Logger logger = LoggerFactory.getLogger(NVT.class);
   /**
    * IAC
    */
   public static final int IAC = 255;
   /**
    * Commands
    */
   public static final int IAC_COMMAND_WILL = 251;
   public static final int IAC_COMMAND_WONT = 252;
   public static final int IAC_COMMAND_DO = 253;
   public static final int IAC_COMMAND_DONT = 254;
   public static final int IAC_COMMAND_SB = 250;
   public static final int IAC_COMMAND_SE = 240;
   /**
    * Codes
    */
   public static final int IAC_CODE_ECHO = 1;
   public static final int IAC_CODE_SUPPRESS_GOAHEAD = 3;
   public static final int IAC_CODE_STATUS = 5;
   public static final int IAC_CODE_MARK = 6;
   public static final int IAC_CODE_TERMTYPE = 24;
   public static final int IAC_CODE_EOR = 25;
   public static final int IAC_CODE_WINSIZE = 31;
   public static final int IAC_CODE_TERMSPEED = 32;
   public static final int IAC_CODE_REMOTE_FLOW_CONTROL = 33;
   public static final int IAC_CODE_LINEMODE = 34;
   public static final int IAC_CODE_ENVVAR = 36;
   public static final int IAC_CODE_AUTHENTICATION = 37;
   /**
    * keys
    */
   public static final int KEY_BS = 0x08;
   public static final int KEY_DEL = 0x7f;
   public static final int KEY_ESC = 0x1b;
   public static final int KEY_CR = 0x0d;
   public static final int KEY_LF = 0x0a;
   public static final int KEY_TAB = 0x09;
   /**
    * subneg
    */
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
   private final Charset charset = Charset.forName("UTF-8");
   /**
    * socket
    */
   private final Socket socket;
   /**
    * autoflush
    */
   private boolean autoflush = true;

   public NVT(Socket socket) throws IOException {
      super();
      this.socket = socket;
      dataInputStream = new DataInputStream(socket.getInputStream());
      dataOutputStream = new DataOutputStream(socket.getOutputStream());
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

   public boolean isAutoflush() {
      return autoflush;
   }

   private void processIAC() throws IOException {
      final int cmd = dataInputStream.read();
      switch (cmd) {
         case IAC_COMMAND_DO:
            int option = dataInputStream.read();
            logger.info("Received IAC DO:" + option);
            break;
         case IAC_COMMAND_DONT:
            option = dataInputStream.read();
            logger.info("Received IAC DONT:" + option);
            break;
         case IAC_COMMAND_WILL:
            option = dataInputStream.read();
            logger.info("Received IAC WILL:" + option);
            break;
         case IAC_COMMAND_WONT:
            option = dataInputStream.read();
            logger.info("Received IAC WONT:" + option);
            break;
         default:
            logger.info("Received Unknown IAC Command :" + cmd);
      }
   }

   public int readByte() throws IOException {
      final int c = dataInputStream.read();
      if (c == IAC) {
         processIAC();
         return readByte();
      } else {
         dataOutputStream.write(c);
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
            // backspace
            String str = baos.toString(charset.name());
            baos = new ByteArrayOutputStream();
            str = str.substring(0, str.length() - 1);
            baos.write(str.getBytes(), 0, str.length());
         } else if (b == KEY_ESC) {
            logger.info("ESC pressed");
         } else if (b == KEY_TAB) {
            logger.info("TAB pressed");
         } else {
            baos.write(b);
         }
      }
      return baos.toString(charset.name()).trim();
   }

   public void setAutoflush(boolean autoflush) {
      this.autoflush = autoflush;
   }

   public void write(String str) throws IOException {
      dataOutputStream.write(str.getBytes(charset), 0, str.length());
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
      dataOutputStream.write(str.getBytes(charset), 0, str.length());
      dataOutputStream.write(EOL.getBytes(charset), 0, EOL.length());
      if (isAutoflush()) {
         flush();
      }
   }
}

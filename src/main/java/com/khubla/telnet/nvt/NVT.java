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
    * IAC Commands (RFC 854)
    */
   // End of subnegotiation parameters.
   public static final int IAC_COMMAND_SE = 240;
   // No operation.
   public static final int IAC_COMMAND_NOP = 241;
   // The data stream portion of a Synch
   public static final int IAC_COMMAND_DATAMARK = 242;
   // NVT character BRK.
   public static final int IAC_COMMAND_BREAK = 243;
   // interrupt process
   public static final int IAC_COMMAND_IP = 244;
   // abort output
   public static final int IAC_COMMAND_AO = 245;
   // are you there
   public static final int IAC_COMMAND_AYT = 246;
   // erase character
   public static final int IAC_COMMAND_EC = 247;
   // erase line
   public static final int IAC_COMMAND_EL = 248;
   // go ahead
   public static final int IAC_COMMAND_GA = 249;
   // Indicates that what follows is subnegotiation of the indicated option.
   public static final int IAC_COMMAND_SB = 250;
   // Indicates the desire to begin performing, or confirmation that you are now performing, the indicated option.
   public static final int IAC_COMMAND_WILL = 251;
   // Indicates the refusal to perform, or continue performing, the indicated option.
   public static final int IAC_COMMAND_WONT = 252;
   // Indicates the request that the other party perform, o confirmation that you are expecting the other party to perform, the indicated option.
   public static final int IAC_COMMAND_DO = 253;
   // Indicates the demand that the other party stop performing or confirmation that you are no longer expecting the other party to perform, the indicated option.
   public static final int IAC_COMMAND_DONT = 254;
   // IAC
   public static final int IAC_IAC = 255;
   /**
    * Codes
    */
   // RFC 857
   public static final int IAC_CODE_ECHO = 1;
   // RFC 858
   public static final int IAC_CODE_SUPPRESS_GOAHEAD = 3;
   public static final int IAC_CODE_STATUS = 5;
   public static final int IAC_CODE_MARK = 6;
   // RFC 1091
   public static final int IAC_CODE_TERMTYPE = 24;
   public static final int IAC_CODE_EOR = 25;
   public static final int IAC_CODE_WINSIZE = 31;
   public static final int IAC_CODE_TERMSPEED = 32;
   public static final int IAC_CODE_REMOTE_FLOW_CONTROL = 33;
   public static final int IAC_CODE_LINEMODE = 34;
   public static final int IAC_CODE_ENVVAR = 36;
   public static final int IAC_CODE_AUTHENTICATION = 37;
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

   public NVT(Socket socket) throws IOException {
      super();
      this.socket = socket;
      dataInputStream = new DataInputStream(socket.getInputStream());
      dataOutputStream = new DataOutputStream(socket.getOutputStream());
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

   void IAC_Echo(int cmd) throws IOException {
      switch (cmd) {
         case IAC_COMMAND_DO:
            logger.info("Received IAC DO echo");
            setEcho(true);
            sendIACCommand(IAC_COMMAND_WILL, IAC_CODE_ECHO);
            break;
         case IAC_COMMAND_DONT:
            logger.info("Received IAC DONT echo");
            setEcho(false);
            sendIACCommand(IAC_COMMAND_WONT, IAC_CODE_ECHO);
            break;
         case IAC_COMMAND_WILL:
            logger.info("Received IAC WILL echo");
            break;
         case IAC_COMMAND_WONT:
            logger.info("Received IAC WONT echo");
            break;
         default:
            logger.info("Received Unknown IAC Command :" + cmd);
      }
   }

   void IAC_SuppressGoahead(int cmd) throws IOException {
      switch (cmd) {
         case IAC_COMMAND_DO:
            logger.info("Received IAC DO SG");
            sendIACCommand(IAC_COMMAND_WILL, IAC_CODE_SUPPRESS_GOAHEAD);
            break;
         case IAC_COMMAND_DONT:
            logger.info("Received IAC DONT SG");
            // we always supress goahead!
            sendIACCommand(IAC_COMMAND_WILL, IAC_CODE_SUPPRESS_GOAHEAD);
            break;
         case IAC_COMMAND_WILL:
            logger.info("Received IAC WILL SG");
            break;
         case IAC_COMMAND_WONT:
            logger.info("Received IAC WONT SG");
            break;
         default:
            logger.info("Received Unknown IAC Command :" + cmd);
      }
   }

   void IAC_Termtype(int cmd) {
      switch (cmd) {
         case IAC_COMMAND_DO:
            logger.info("Received IAC DO Termtype");
            break;
         case IAC_COMMAND_DONT:
            logger.info("Received IAC DONT Termtype");
            break;
         case IAC_COMMAND_WILL:
            logger.info("Received IAC WILL Termtype");
            break;
         case IAC_COMMAND_WONT:
            logger.info("Received IAC WONT Termtype");
            break;
         default:
            logger.info("Received Unknown IAC Command :" + cmd);
      }
   }

   public boolean isAutoflush() {
      return autoflush;
   }

   public boolean isEcho() {
      return echo;
   }

   private void processIAC() throws IOException {
      final int cmd = dataInputStream.read();
      final int option = dataInputStream.read();
      if ((cmd == IAC_COMMAND_DO) || (cmd == IAC_COMMAND_DONT) || (cmd == IAC_COMMAND_WILL) || (cmd == IAC_COMMAND_WONT)) {
         processIACCommand(cmd, option);
      } else {
         logger.info("No handler for AIC command :" + cmd);
      }
   }

   private void processIACCommand(int cmd, int option) throws IOException {
      switch (option) {
         case IAC_CODE_ECHO:
            IAC_Echo(cmd);
            break;
         case IAC_CODE_SUPPRESS_GOAHEAD:
            IAC_SuppressGoahead(cmd);
            break;
         case IAC_CODE_TERMTYPE:
            IAC_Termtype(cmd);
            break;
         default:
            logger.info("No handler for AIC option :" + option);
      }
   }

   public int readByte() throws IOException {
      final int c = dataInputStream.read();
      if (c == IAC_IAC) {
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
            // backspace
            String str = baos.toString(charsetUTF8.name());
            baos = new ByteArrayOutputStream();
            str = str.substring(0, str.length() - 1);
            baos.write(str.getBytes(), 0, str.length());
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

   private void sendConfigParameters() throws IOException {
      /**
       * echo
       */
      if (isEcho()) {
         sendIACCommand(NVT.IAC_COMMAND_WILL, NVT.IAC_CODE_ECHO);
      } else {
         sendIACCommand(NVT.IAC_COMMAND_WONT, NVT.IAC_CODE_ECHO);
      }
   }

   private void sendIACCommand(int command, int option) throws IOException {
      writeBytes(NVT.IAC_IAC, command, option);
   }

   public void setAutoflush(boolean autoflush) {
      this.autoflush = autoflush;
   }

   public void setEcho(boolean echo) {
      this.echo = echo;
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

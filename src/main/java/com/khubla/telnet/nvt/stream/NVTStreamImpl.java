/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet.nvt.stream;

import com.khubla.telnet.nvt.spy.NVTSpy;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class NVTStreamImpl implements NVTStream {
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
   public final static String EOL = "\r\n";
   /**
    * logger
    */
   private static final Logger logger = LogManager.getLogger(NVTStreamImpl.class);
   /**
    * in stream
    */
   private final DataInputStream dataInputStream;
   /**
    * out stream
    */
   private final DataOutputStream dataOutputStream;
   /**
    * IAC processor
    */
   private final IACProcessor iacProcessor;
   /**
    * charset
    */
   private final Charset charsetUTF8 = StandardCharsets.UTF_8;
   /**
    * autoflush
    */
   @Getter
   @Setter
   private boolean autoflush = true;
   /**
    * nvt spy
    */
   private NVTSpy nvtSpy = null;
   /**
    * echo
    */
   @Getter
   @Setter
   private boolean echo = true;
   @Getter
   @Setter
   private boolean ipRequested = false;
   // for GNU telnet
   @Getter
   @Setter
   private boolean LFisCR = true;

   public NVTStreamImpl(InputStream inputStream, OutputStream outputStream, IACProcessor iacProcessor) {
      super();
      /*
       * iac
       */
      this.iacProcessor = iacProcessor;
      /*
       * streams
       */
      dataInputStream = new DataInputStream(new BufferedInputStream(inputStream));
      dataOutputStream = new DataOutputStream(outputStream);
   }

   /*
    * (non-Javadoc)
    * @see com.khubla.telnet.nvt.stream.NVTStream#close()
    */
   @Override
   public void close() throws IOException {
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
   }

   /*
    * (non-Javadoc)
    * @see com.khubla.telnet.nvt.stream.NVTStream#flush()
    */
   @Override
   public void flush() throws IOException {
      dataOutputStream.flush();
   }

   /*
    * (non-Javadoc)
    * @see com.khubla.telnet.nvt.stream.NVTStream#getNvtSpy()
    */
   @Override
   public NVTSpy getNvtSpy() {
      return nvtSpy;
   }

   /*
    * (non-Javadoc)
    * @see com.khubla.telnet.nvt.stream.NVTStream#setNvtSpy(com.khubla.telnet.nvt.spy.NVTSpy)
    */
   @Override
   public void setNvtSpy(NVTSpy nvtSpy) {
      this.nvtSpy = nvtSpy;
   }

   private boolean isPrintable(int c) {
      return (c >= 0x20) && (c <= 0xfd);
   }

   /*
    * (non-Javadoc)
    * @see com.khubla.telnet.nvt.stream.NVTStream#readByte()
    */
   @Override
   public int readByte() throws IOException {
      final int c = readRawByte();
      if (c == IAC.IAC_IAC) {
         iacProcessor.processIAC();
         return readByte();
      } else {
         if (isEcho()) {
            if (isPrintable(c)) {
               write(c);
            }
         }
         return c;
      }
   }

   /*
    * (non-Javadoc)
    * @see com.khubla.telnet.nvt.stream.NVTStream#readln()
    */
   @Override
   public String readln() throws IOException {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      boolean cont = true;
      while (cont && !ipRequested) {
         final int b = readByte();
         if (!LFisCR && (b == KEY_LF)) {
            /*
             * bare LF is a LF
             */
            if (isEcho()) {
               write(b);
            }
         } else if (b == KEY_NULL) {
            /*
             * ignore
             */
            logger.info("Unexpected NULL");
         } else if ((b == KEY_CR) || (LFisCR && (b == KEY_LF))) {
            /*
             * if it's followed by LF, then it means CRLF. Eat the LF
             */
            if (dataInputStream.available() > 0) {
               dataInputStream.mark(1);
               final int potentialLF = readByte();
               if (potentialLF != KEY_LF) {
                  dataInputStream.reset();
                  /*
                   * if it's followed by a NULL it means just a CR, Eat the NUL
                   */
                  dataInputStream.mark(1);
                  final int potentialNUL = readByte();
                  if (potentialNUL != KEY_NULL) {
                     dataInputStream.reset();
                     /*
                      * its a bare CR treat it like CRLF
                      */
                     cont = false;
                     write(EOL);
                  } else {
                     // its just a CR
                     cont = false;
                     write(EOL);
                  }
               } else {
                  /*
                   * its a CRLF
                   */
                  cont = false;
                  write(EOL);
               }
            } else {
               // just a bare CR, nothing after it
               cont = false;
               write(EOL);
            }
         } else if ((b == KEY_BS) || (b == KEY_DEL)) {
            /*
             * backspace and delete keys
             */
            String str = baos.toString(charsetUTF8);
            if (str.length() > 0) {
               baos = new ByteArrayOutputStream();
               str = str.substring(0, str.length() - 1);
               if (str.length() > 0) {
                  baos.write(str.getBytes(), 0, str.length());
               }
               // echo the BS/DEL back
               if (isEcho()) {
                  write(b);
               }
            }
         } else if (b == KEY_ESC) {
            logger.info("ESC pressed");
         } else if (b == KEY_HT) {
            logger.info("TAB pressed");
         } else {
            if (isPrintable(b)) {
               baos.write(b);
            }
         }
      }
      return baos.toString(charsetUTF8).trim();
   }

   /*
    * (non-Javadoc)
    * @see com.khubla.telnet.nvt.stream.NVTStream#readRawByte()
    */
   @Override
   public int readRawByte() throws IOException {
      final int c = dataInputStream.read();
      if (null != nvtSpy) {
         nvtSpy.readbyte(c);
      }
      return c;
   }

   /*
    * (non-Javadoc)
    * @see com.khubla.telnet.nvt.stream.NVTStream#readRawString(int)
    */
   @Override
   public String readRawString(int marker) throws IOException {
      final ByteArrayOutputStream baos = new ByteArrayOutputStream();
      int b = readRawByte();
      while (b != marker) {
         baos.write(b);
         b = readRawByte();
      }
      return baos.toString(charsetUTF8).trim();
   }

   /*
    * (non-Javadoc)
    * @see com.khubla.telnet.nvt.stream.NVTStream#readShort()
    */
   @Override
   public short readShort() throws IOException {
      final short c = dataInputStream.readShort();
      if (null != nvtSpy) {
         nvtSpy.readshort(c);
      }
      return c;
   }

   /*
    * (non-Javadoc)
    * @see com.khubla.telnet.nvt.stream.NVTStream#write(int)
    */
   @Override
   public void write(int c) throws IOException {
      dataOutputStream.write(c);
      if (null != nvtSpy) {
         nvtSpy.writebyte(c);
      }
      if (isAutoflush()) {
         flush();
      }
   }

   /*
    * (non-Javadoc)
    * @see com.khubla.telnet.nvt.stream.NVTStream#write(java.lang.String)
    */
   @Override
   public void write(String str) throws IOException {
      final byte[] bs = str.getBytes(charsetUTF8);
      for (int i = 0; i < bs.length; i++) {
         this.write(bs[i]);
      }
      if (isAutoflush()) {
         flush();
      }
   }

   /*
    * (non-Javadoc)
    * @see com.khubla.telnet.nvt.stream.NVTStream#writeBytes(int)
    */
   @Override
   public void writeBytes(int... b) throws IOException {
      for (final int i : b) {
         dataOutputStream.write(i);
         if (null != nvtSpy) {
            nvtSpy.writebyte(i);
         }
      }
      if (isAutoflush()) {
         flush();
      }
   }

   /*
    * (non-Javadoc)
    * @see com.khubla.telnet.nvt.stream.NVTStream#writeln(java.lang.String)
    */
   @Override
   public void writeln(String str) throws IOException {
      write(str);
      write(EOL);
   }

   @Override
   public void showOptions() {
      System.out.println("Stream Options:");
      System.out.println("autoflush: " + autoflush);
      System.out.println("echo: " + echo);
   }
}

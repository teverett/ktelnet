/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet.nvt.stream;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.khubla.telnet.nvt.IACCommandHandler;
import com.khubla.telnet.nvt.spy.NVTSpy;

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
    * logger
    */
   private static final Logger logger = LoggerFactory.getLogger(NVTStreamImpl.class);
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
    * autoflush
    */
   private boolean autoflush = true;
   /**
    * nvt spy
    */
   private NVTSpy nvtSpy = null;
   /**
    * echo
    */
   private boolean echo = true;
   /**
    * IAC processor
    */
   private final IACProcessor iacProcessor;
   /**
    * charset
    */
   private final Charset charsetUTF8 = Charset.forName("UTF-8");

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
    * @see com.khubla.telnet.nvt.stream.NVTStream#isAutoflush()
    */
   @Override
   public boolean isAutoflush() {
      return autoflush;
   }

   /*
    * (non-Javadoc)
    * @see com.khubla.telnet.nvt.stream.NVTStream#isEcho()
    */
   @Override
   public boolean isEcho() {
      return echo;
   }

   private boolean isPrintable(int c) {
      if ((c >= 0x20) && (c <= 0xfd)) {
         return true;
      }
      return false;
   }

   /*
    * (non-Javadoc)
    * @see com.khubla.telnet.nvt.stream.NVTStream#readByte()
    */
   @Override
   public int readByte() throws IOException {
      final int c = readRawByte();
      if (c == IACCommandHandler.IAC_IAC) {
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
      while (cont) {
         final int b = readByte();
         if (b == KEY_LF) {
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
         } else if (b == KEY_CR) {
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
            String str = baos.toString(charsetUTF8.name());
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
      return baos.toString(charsetUTF8.name()).trim();
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
      return baos.toString(charsetUTF8.name()).trim();
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
    * @see com.khubla.telnet.nvt.stream.NVTStream#setAutoflush(boolean)
    */
   @Override
   public void setAutoflush(boolean autoflush) {
      this.autoflush = autoflush;
   }

   /*
    * (non-Javadoc)
    * @see com.khubla.telnet.nvt.stream.NVTStream#setEcho(boolean)
    */
   @Override
   public void setEcho(boolean echo) {
      this.echo = echo;
   }

   /*
    * (non-Javadoc)
    * @see com.khubla.telnet.nvt.stream.NVTStream#setNvtSpy(com.khubla.telnet.nvt.spy.NVTSpy)
    */
   @Override
   public void setNvtSpy(NVTSpy nvtSpy) {
      this.nvtSpy = nvtSpy;
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
}

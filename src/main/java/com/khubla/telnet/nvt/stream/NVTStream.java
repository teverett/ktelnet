/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet.nvt.stream;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;

import com.khubla.telnet.nvt.spy.NVTSpy;

public interface NVTStream extends Closeable, Flushable {
   @Override
   void close() throws IOException;

   @Override
   void flush() throws IOException;

   NVTSpy getNvtSpy();

   boolean isAutoflush();

   boolean isEcho();

   /**
    * read a byte. process IAC if found. echo if appropriate
    */
   int readByte() throws IOException;

   /**
    * read a line
    */
   String readln() throws IOException;

   int readRawByte() throws IOException;

   String readRawString(int marker) throws IOException;

   short readShort() throws IOException;

   void setAutoflush(boolean autoflush);

   void setEcho(boolean echo);

   void setNvtSpy(NVTSpy nvtSpy);

   void write(int c) throws IOException;

   void write(String str) throws IOException;

   void writeBytes(int... b) throws IOException;

   void writeln(String str) throws IOException;
}
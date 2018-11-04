/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet.nvt.iac.command;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.khubla.telnet.nvt.IACCommandHandler;
import com.khubla.telnet.nvt.NVT;

public abstract class AbstractIACCommandHandler implements IACCommandHandler {
   /**
    * logger
    */
   static final Logger logger = LoggerFactory.getLogger(AbstractIACCommandHandler.class);

   protected short readShort(byte[] array, int idx) {
      final ByteBuffer bb = ByteBuffer.allocate(2);
      bb.order(ByteOrder.BIG_ENDIAN);
      bb.put(array[idx]);
      bb.put(array[idx + 1]);
      return bb.getShort(0);
   }

   protected String readString(byte[] array, int idx, int len) {
      return new String(Arrays.copyOfRange(array, idx, len));
   }

   protected byte[] readSubnegotiation(NVT nvt) throws IOException {
      final ByteArrayOutputStream baos = new ByteArrayOutputStream();
      int b = nvt.readRawByte();
      while (b != NVT.IAC_IAC) {
         baos.write(b);
         b = nvt.readRawByte();
      }
      b = nvt.readRawByte();
      if (b != NVT.IAC_COMMAND_SE) {
         logger.info("Expected IAC:" + NVT.IAC_COMMAND_SE);
      }
      return baos.toByteArray();
   }
}

/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet.nvt.command;

import com.khubla.telnet.nvt.NVT;
import com.khubla.telnet.nvt.stream.IAC;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public abstract class AbstractIACCommandHandler implements IACCommandHandler {
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
      boolean cont = true;
      while (cont) {
         final int b = nvt.getNvtStream().readRawByte();
         if (b == IAC.IAC_IAC) {
            final int b2 = nvt.getNvtStream().readRawByte();
            if (b2 == IAC.IAC_COMMAND_SE) {
               cont = false;
            } else {
               baos.write(b);
               baos.write(b2);
            }
         } else {
            baos.write(b);
         }
      }
      return baos.toByteArray();
   }
}

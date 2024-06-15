/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

public class BasicTest extends AbstractTelnetTest {
   @Test
   @Disabled
   public void test1() {
      try {
         writer.write("quit\n");
      } catch (final Exception e) {
         e.printStackTrace();
         fail();
      }
   }
}

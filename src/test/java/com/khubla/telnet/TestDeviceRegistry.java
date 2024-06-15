/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet;

import com.khubla.telnet.nvt.tn3270.devicetype.DeviceType;
import com.khubla.telnet.nvt.tn3270.devicetype.DeviceTypeRegistry;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

public class TestDeviceRegistry {
   @Test
   public void test1() {
      try {
         final DeviceType dt = DeviceTypeRegistry.getInstance().getDevice("IBM-3278-3");
         assertNotNull(dt);
      } catch (final Exception e) {
         e.printStackTrace();
         fail();
      }
   }
}

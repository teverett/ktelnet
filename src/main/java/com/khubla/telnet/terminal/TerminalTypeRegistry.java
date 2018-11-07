/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet.terminal;

import java.util.HashMap;

public class TerminalTypeRegistry {
   /**
    * singleton
    */
   private static TerminalTypeRegistry instance = null;

   public static TerminalTypeRegistry getInstance() {
      if (null == instance) {
         instance = new TerminalTypeRegistry();
      }
      return instance;
   }

   /**
    * device types
    */
   private final HashMap<String, TerminalType> deviceTypes = new HashMap<String, TerminalType>();

   private TerminalTypeRegistry() {
      add(new TerminalType("vt100", null));
      add(new TerminalType("vt102", null));
      add(new TerminalType("vt220", null));
      add(new TerminalType("vt320", null));
      add(new TerminalType("vt420", null));
      add(new TerminalType("vt520", null));
      add(new TerminalType("xterm", null));
   }

   private void add(TerminalType deviceType) {
      deviceTypes.put(deviceType.getName(), deviceType);
   }

   public TerminalType getDevice(String name) {
      return deviceTypes.get(name);
   }
}

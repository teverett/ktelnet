/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet.terminal;

public class TerminalType {
   private final String name;
   private final String size;

   public TerminalType(String name, String size) {
      super();
      this.name = name;
      this.size = size;
   }

   public String getName() {
      return name;
   }

   public String getSize() {
      return size;
   }
}

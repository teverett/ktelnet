/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet.terminal;

import lombok.Getter;

public class TerminalType {
   @Getter
   private final String name;
   @Getter
   private final String size;

   public TerminalType(String name, String size) {
      super();
      this.name = name;
      this.size = size;
   }
}

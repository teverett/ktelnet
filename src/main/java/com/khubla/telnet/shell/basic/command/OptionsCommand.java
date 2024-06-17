/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet.shell.basic.command;

import com.khubla.telnet.TelnetException;
import com.khubla.telnet.nvt.NVT;
import com.khubla.telnet.shell.command.AbstractCommand;

import java.util.HashMap;

public class OptionsCommand extends AbstractCommand {
   private final static String[] names = { "options" };

   @Override
   public boolean execute(NVT nvt, String line, HashMap<String, Object> sessionParameters) throws TelnetException {
      try {
         nvt.getNvtOptions().show(nvt.getNvtStream());
         /*
          * continue
          */
         return true;
      } catch (Exception e) {
         throw new TelnetException("Exception in execute", e);
      }
   }

   @Override
   public String[] getNames() {
      return names;
   }

   @Override
   public String getDescription() {
      return "show negotiated protocol options";
   }
}

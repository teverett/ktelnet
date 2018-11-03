/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet.shell.command;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.khubla.telnet.TelnetException;
import com.khubla.telnet.nvt.NVT;
import com.khubla.telnet.shell.AbstractShellImpl;

public abstract class CommandOrientedShellImpl extends AbstractShellImpl {
   /**
    * logger
    */
   static final Logger logger = LoggerFactory.getLogger(CommandOrientedShellImpl.class);
   /**
    * prompt
    */
   private String prompt = "> ";
   /**
    * commands
    */
   private final TelnetCommandRegistry telnetCommandRegistry;

   public CommandOrientedShellImpl(NVT nvt, TelnetCommandRegistry telnetCommandRegistry) {
      super(nvt);
      this.telnetCommandRegistry = telnetCommandRegistry;
   }

   private void commandLoop() throws TelnetException {
      try {
         boolean go = true;
         while (go) {
            getNvt().write(prompt);
            final String inputLine = getNvt().readln();
            if ((null != inputLine) && (inputLine.length() > 0)) {
               /*
                * get command
                */
               final TelnetCommand telnetCommand = telnetCommandRegistry.getCommand(inputLine);
               if (null != telnetCommand) {
                  /*
                   * process
                   */
                  go = telnetCommand.execute(inputLine);
               } else {
                  getNvt().writeln("Unknown :" + inputLine);
               }
            }
         }
      } catch (final Exception e) {
         throw new TelnetException("Exception in commandLoop", e);
      }
   }

   public String getPrompt() {
      return prompt;
   }

   protected abstract void onConnect() throws IOException;

   @Override
   public void runShell() {
      try {
         /*
          * connected
          */
         onConnect();
         /*
          * loop
          */
         commandLoop();
      } catch (final Exception e) {
         logger.error(e.getMessage(), e);
      }
   }

   public void setPrompt(String prompt) {
      this.prompt = prompt;
   }
}

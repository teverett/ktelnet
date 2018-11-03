/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet.shell.basic;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.khubla.telnet.TelnetException;
import com.khubla.telnet.command.TelnetCommand;
import com.khubla.telnet.command.TelnetCommandRegistry;
import com.khubla.telnet.nvt.NVT;
import com.khubla.telnet.shell.AbstractShellImpl;

public class BasicShellImpl extends AbstractShellImpl {
   /**
    * logger
    */
   static final Logger logger = LoggerFactory.getLogger(BasicShellImpl.class);
   /**
    * prompt
    */
   private final String prompt = "> ";
   /**
    * hello message
    */
   private final String helloMessge = "khubla.com Telnet server";
   /**
    * nvt
    */
   private final NVT nvt;
   /**
    * commands
    */
   private final TelnetCommandRegistry telnetCommandRegistry;

   public BasicShellImpl(NVT nvt, TelnetCommandRegistry telnetCommandRegistry) {
      super();
      this.nvt = nvt;
      this.telnetCommandRegistry = telnetCommandRegistry;
   }

   private void commandLoop() throws TelnetException {
      try {
         boolean go = true;
         while (go) {
            nvt.write(prompt);
            final String inputLine = nvt.readln();
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
                  nvt.writeln("Unknown :" + inputLine);
               }
            }
         }
      } catch (final Exception e) {
         throw new TelnetException("Exception in commandLoop", e);
      }
   }

   @Override
   public void run() {
      try {
         /*
          * send config
          */
         sendConfigParameters();
         /*
          * hello
          */
         sayHello();
         /*
          * loop
          */
         commandLoop();
      } catch (final Exception e) {
         logger.error(e.getMessage(), e);
      } finally {
         try {
            nvt.close();
         } catch (final Exception e) {
            logger.error(e.getMessage(), e);
         }
      }
   }

   private void sayHello() throws IOException {
      nvt.writeln(helloMessge);
   }

   private void sendConfigParameters() throws IOException {
      nvt.writeBytes(NVT.IAC, NVT.IAC_COMMAND_WILL, NVT.IAC_CODE_ECHO);
   }
}

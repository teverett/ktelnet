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

import com.khubla.telnet.nvt.NVT;
import com.khubla.telnet.shell.command.CommandOrientedShellImpl;
import com.khubla.telnet.shell.command.TelnetCommandRegistry;

public class BasicShellImpl extends CommandOrientedShellImpl {
   /**
    * logger
    */
   static final Logger logger = LoggerFactory.getLogger(BasicShellImpl.class);
   /**
    * hello message
    */
   private final String helloMessge = "khubla.com Telnet server";

   public BasicShellImpl(NVT nvt, TelnetCommandRegistry telnetCommandRegistry) {
      super(nvt, telnetCommandRegistry);
   }

   @Override
   protected void onConnect() throws IOException {
      sendConfigParameters();
      getNvt().writeln(helloMessge);
   }

   private void sendConfigParameters() throws IOException {
      getNvt().writeBytes(NVT.IAC, NVT.IAC_COMMAND_WILL, NVT.IAC_CODE_ECHO);
   }
}

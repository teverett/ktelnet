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
   private String helloMessge = "khubla.com Telnet server";

   public BasicShellImpl(NVT nvt, TelnetCommandRegistry telnetCommandRegistry) {
      super(nvt, telnetCommandRegistry);
   }

   public String getHelloMessge() {
      return helloMessge;
   }

   @Override
   protected void onConnect() throws IOException {
      getNvt().writeln(helloMessge);
   }

   @Override
   protected void onDisconnect() {
      //
   }

   public void setHelloMessge(String helloMessge) {
      this.helloMessge = helloMessge;
   }
}

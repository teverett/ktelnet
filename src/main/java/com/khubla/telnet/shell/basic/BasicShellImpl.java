/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet.shell.basic;

import com.khubla.telnet.auth.AuthenticationHandler;
import com.khubla.telnet.nvt.NVT;
import com.khubla.telnet.shell.command.CommandOrientedShellImpl;
import com.khubla.telnet.shell.command.TelnetCommandRegistry;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

public class BasicShellImpl extends CommandOrientedShellImpl {
   /**
    * bye message
    */
   @Getter
   @Setter
   private String byeMessge = "bye";
   /**
    * hello message
    */
   @Getter
   @Setter
   private String helloMessge = "khubla.com Telnet server";

   public BasicShellImpl(NVT nvt, TelnetCommandRegistry telnetCommandRegistry, AuthenticationHandler authenticationHandler) {
      super(nvt, telnetCommandRegistry, authenticationHandler);
   }

   @Override
   protected void onConnect() throws IOException {
      getNvt().getNvtStream().writeln(helloMessge);
      getNvt().getNvtStream().writeln("welcome :" + this.getNvt().getClientAddress());
   }

   @Override
   protected void onDisconnect() throws IOException {
      getNvt().getNvtStream().writeln(byeMessge);
   }
}

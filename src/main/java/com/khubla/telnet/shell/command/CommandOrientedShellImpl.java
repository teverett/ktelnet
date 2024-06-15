/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet.shell.command;

import com.khubla.telnet.TelnetException;
import com.khubla.telnet.auth.AuthenticationHandler;
import com.khubla.telnet.nvt.NVT;
import com.khubla.telnet.shell.AbstractShellImpl;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.SocketException;
import java.util.HashMap;

public abstract class CommandOrientedShellImpl extends AbstractShellImpl {
   /**
    * logger
    */
   private static final Logger logger = LogManager.getLogger(CommandOrientedShellImpl.class);
   /**
    * commands
    */
   private final TelnetCommandRegistry telnetCommandRegistry;
   /**
    * session parameters
    */
   private final HashMap<String, Object> sesssionParameters = new HashMap<String, Object>();
   /**
    * authentication
    */
   @Getter
   private final AuthenticationHandler authenticationHandler;
   /**
    * prompt
    */
   @Getter
   @Setter
   private String prompt = "> ";

   public CommandOrientedShellImpl(NVT nvt, TelnetCommandRegistry telnetCommandRegistry, AuthenticationHandler authenticationHandler) {
      super(nvt);
      this.telnetCommandRegistry = telnetCommandRegistry;
      this.authenticationHandler = authenticationHandler;
   }

   private void commandLoop() throws TelnetException {
      try {
         boolean go = true;
         while (go && !getNvt().getNvtStream().isIpRequested()) {
            getNvt().getNvtStream().write(prompt);
            final String inputLine = getNvt().getNvtStream().readln();
            if ((null != inputLine) && (inputLine.length() > 0)) {
               /*
                * get command
                */
               final TelnetCommand telnetCommand = telnetCommandRegistry.getCommand(inputLine);
               if (null != telnetCommand) {
                  /*
                   * process
                   */
                  go = telnetCommand.execute(getNvt(), inputLine, sesssionParameters);
               } else {
                  getNvt().getNvtStream().writeln("Unknown: " + inputLine);
               }
            }
         }
      } catch (final SocketException e) {
         // we're good
      } catch (final Exception e) {
         throw new TelnetException("Exception in commandLoop", e);
      } finally {
         try {
            onDisconnect();
         } catch (final Exception e) {
            logger.error("Exception in commandLoop", e);
         }
      }
   }

   /**
    * log the user in
    */
   private boolean login() throws IOException {
      getNvt().getNvtStream().write("login: ");
      final String username = getNvt().getNvtStream().readln();
      getNvt().getNvtStream().write("password: ");
      final String password = getNvt().getNvtStream().readln();
      if ((null != username) && (null != password)) {
         return authenticationHandler.login(username, password, sesssionParameters);
      }
      return false;
   }

   protected abstract void onConnect() throws IOException;

   protected abstract void onDisconnect() throws IOException;

   @Override
   public void runShell() {
      try {
         /*
          * connected
          */
         onConnect();
         /*
          * login
          */
         boolean loggedin = false;
         if (null != authenticationHandler) {
            loggedin = login();
         } else {
            loggedin = true;
         }
         /*
          * ya?
          */
         if (loggedin) {
            /*
             * loop
             */
            commandLoop();
         }
      } catch (final Exception e) {
         logger.error(e.getMessage(), e);
      }
   }
}

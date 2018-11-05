/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet;

import com.khubla.telnet.shell.basic.BasicShellFactoryImpl;

public class Main {
   private final static int THREADS = 20;
   private final static int PORT = 2121;

   public static void main(String[] args) {
      try {
         /*
          * telnet
          */
         final TelnetServer telnetServer = new TelnetServer(PORT, THREADS, new BasicShellFactoryImpl());
         telnetServer.start();
         /*
          * wait
          */
         Thread.sleep(1000);
         System.out.println("Press any key to exit");
         System.in.read();
         /*
          * shutdown
          */
         telnetServer.shutdown();
      } catch (final Exception e) {
         e.printStackTrace();
         System.exit(0);
      }
      System.exit(1);
   }
}

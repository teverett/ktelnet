/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet;

import com.khubla.telnet.shell.basic.BasicShellFactoryImpl;
import org.apache.commons.net.telnet.TelnetClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.fail;

public abstract class AbstractTelnetTest {
   private static final int PORT = 21111;
   private static final int THREADS = 2;
   protected TelnetServer telnetServer = null;
   protected TelnetClient telnetClient = null;
   protected Reader reader = null;
   protected Writer writer = null;

   @AfterEach
   public void shutdown() {
      try {
         /*
          * close streams
          */
         try {
            writer.flush();
         } catch (SocketException e) {
            // do nothing
         }
         writer = null;
         reader = null;
         /*
          * done client
          */
         if (telnetClient.isConnected()) {
            try {
               telnetClient.disconnect();
            } catch (SocketException e) {
               // do nothing
            }
         }
         telnetClient = null;
         /*
          * done server
          */
         telnetServer.shutdown();
         telnetServer = null;
      } catch (final Exception e) {
         e.printStackTrace();
         fail();
      }
   }

   @BeforeEach
   public void startup() {
      try {
         /*
          * spin server
          */
         telnetServer = new TelnetServer(PORT, THREADS, new BasicShellFactoryImpl());
         telnetServer.start();
         /*
          * connect client
          */
         telnetClient = new TelnetClient();
         telnetClient.connect("localhost", PORT);
         /*
          * reader/writer
          */
         reader = new InputStreamReader(telnetClient.getInputStream(), StandardCharsets.UTF_8);
         writer = new OutputStreamWriter(telnetClient.getOutputStream(), StandardCharsets.UTF_8);
      } catch (final Exception e) {
         e.printStackTrace();
         fail();
      }
   }
}

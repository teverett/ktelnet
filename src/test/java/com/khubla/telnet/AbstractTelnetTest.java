/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet;

import java.io.*;

import org.apache.commons.net.telnet.*;
import org.testng.*;
import org.testng.annotations.*;

import com.khubla.telnet.shell.basic.*;

public abstract class AbstractTelnetTest {
	private static final int PORT = 21111;
	private static final int THREADS = 2;
	protected TelnetServer telnetServer = null;
	protected TelnetClient telnetClient = null;
	protected Reader reader = null;
	protected Writer writer = null;

	@AfterTest
	public void shutdown() {
		try {
			/*
			 * close streams
			 */
			writer.flush();
			writer = null;
			reader = null;
			/*
			 * done client
			 */
			if (telnetClient.isConnected()) {
				telnetClient.disconnect();
			}
			telnetClient = null;
			/*
			 * done server
			 */
			telnetServer.shutdown();
			telnetServer = null;
		} catch (final Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@BeforeTest
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
			reader = new InputStreamReader(telnetClient.getInputStream(), "UTF-8");
			writer = new OutputStreamWriter(telnetClient.getOutputStream(), "UTF-8");
		} catch (final Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
}

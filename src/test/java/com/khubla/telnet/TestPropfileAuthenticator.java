/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet;

import org.testng.*;
import org.testng.annotations.*;

import com.khubla.telnet.auth.*;
import com.khubla.telnet.auth.impl.*;

public class TestPropfileAuthenticator {
	@Test
	public void test1() {
		try {
			final AuthenticationHandler authenticationHandler = new PropertiesFileAuthenticationHandlerImpl(TestPropfileAuthenticator.class.getResourceAsStream("/users.properties"));
			Assert.assertTrue(authenticationHandler.login("tom", "tge", null));
		} catch (final Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
}

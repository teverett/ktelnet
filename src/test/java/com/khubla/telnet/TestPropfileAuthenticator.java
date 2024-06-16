/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet;

import com.khubla.telnet.auth.AuthenticationHandler;
import com.khubla.telnet.auth.impl.PropertiesFileAuthenticationHandlerImpl;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class TestPropfileAuthenticator {
   @Test
   @Disabled
   public void test1() {
      try {
         final AuthenticationHandler authenticationHandler = new PropertiesFileAuthenticationHandlerImpl(TestPropfileAuthenticator.class.getResourceAsStream("/users.properties"));
         assertTrue(authenticationHandler.login("tom", "tge", null));
      } catch (final Exception e) {
         e.printStackTrace();
         fail();
      }
   }
}

/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet;

import com.khubla.telnet.auth.AuthenticationHandler;
import com.khubla.telnet.auth.impl.LDAPAuthenticationHandlerImpl;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class TestLDAPAuthenticator {
   @Test
   @Disabled
   public void test1() {
      try {
         final AuthenticationHandler authenticationHandler = new LDAPAuthenticationHandlerImpl();
         assertTrue(authenticationHandler.login("tom", "", null));
      } catch (final Exception e) {
         e.printStackTrace();
         fail();
      }
   }
}

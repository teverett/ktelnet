/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet.auth.impl;

import com.khubla.telnet.auth.AuthenticationHandler;
import com.khubla.telnet.config.Config;
import com.khubla.telnet.nvt.NVT;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.util.HashMap;
import java.util.Hashtable;

public class LDAPAuthenticationHandlerImpl implements AuthenticationHandler {
   /**
    * logger
    */
   private static final Logger logger = LogManager.getLogger(NVT.class);

   @Override
   public boolean login(String username, String password, HashMap<String, Object> sessionParameters) {
      try {
         LDAPParameters ldapParameters = new LDAPParameters();
         DirContext adminContext = buildContext(ldapParameters.server, ldapParameters.binddn, ldapParameters.bindpw);
         UserData userData = search(adminContext, ldapParameters.ldapsearchou, username);
         try {
            if (null != userData) {
               // authenticate user
               DirContext userContext = buildContext(ldapParameters.server, userData.dn, password);
               if (null != userContext) {
                  userContext.close();
                  return true;
               }
            }
            return false;
         } finally {
            if (null != adminContext) {
               adminContext.close();
            }
         }
      } catch (final Exception e) {
         logger.error(e);
         return false;
      }
   }

   private UserData search(DirContext dirContext, String ldapsearchou, String username) throws NamingException {
      String filter = "(&(objectClass=person)(uid=" + username + "))";
      String[] attrIDs = { "cn" };
      SearchControls searchControls = new SearchControls();
      searchControls.setReturningAttributes(attrIDs);
      searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
      NamingEnumeration<SearchResult> searchResults = dirContext.search(ldapsearchou, filter, searchControls);
      if (searchResults.hasMore()) {
         UserData userData = new UserData();
         SearchResult result = searchResults.next();
         Attributes attrs = result.getAttributes();
         userData.dn = result.getNameInNamespace();
         userData.cn = attrs.get("cn").toString();
         return userData;
      }
      return null;
   }

   private DirContext buildContext(String server, String binddn, String bindpw) throws NamingException {
      Hashtable<String, String> environment = new Hashtable<String, String>();
      environment.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
      environment.put(Context.PROVIDER_URL, server);
      environment.put(Context.SECURITY_AUTHENTICATION, "simple");
      environment.put(Context.SECURITY_PRINCIPAL, binddn);
      environment.put(Context.SECURITY_CREDENTIALS, bindpw);
      return new InitialDirContext(environment);
   }

   @Getter
   public static class LDAPParameters {
      private final String server;
      private final String binddn;
      private final String bindpw;
      private final String ldapsearchou;

      public LDAPParameters() {
         Config config = Config.getInstance();
         server = config.getProperty("ldapserver");
         binddn = config.getProperty("ldapbinddn");
         bindpw = config.getProperty("ldapbindpw");
         ldapsearchou = config.getProperty("ldapsearchou");
      }
   }

   private static class UserData {
      public String cn;
      public String dn;
   }
}

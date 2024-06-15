/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet.nvt.iac.command;

import com.khubla.telnet.nvt.IACCommandHandler;
import com.khubla.telnet.nvt.NVT;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class AuthenticationIAICCommandHandlerImpl extends AbstractIACCommandHandler {
   /**
    * constants...
    */
   public static final int IS = 0;
   public static final int SEND = 1;
   public static final int REPLY = 2;
   public static final int NAME = 3;
   /**
    * auth types
    */
   public static final int AUTHTYPE_NULL = 0;
   public static final int AUTHTYPE_KERBEROS_V4 = 1;
   public static final int AUTHTYPE_KERBEROS_V5 = 2;
   public static final int AUTHTYPE_SPX = 3;
   public static final int AUTHTYPE_RSA = 6;
   public static final int AUTHTYPE_LOKI = 10;
   /**
    * auth modifiers
    */
   public static final int MODIFIER_AUTH_WHO_MASK = 1;
   public static final int MODIFIER_AUTH_CLIENT_TO_SERVER = 0;
   public static final int MODIFIER_AUTH_SERVER_TO_CLIENT = 1;
   public static final int MODIFIER_AUTH_HOW_MASK = 2;
   public static final int MODIFIER_AUTH_HOW_ONE_WAY = 0;
   public static final int MODIFIER_AUTH_HOW_MUTUAL = 2;
   // RFC 1416, RFC 2941
   public static final int IAC_CODE_AUTHENTICATION = 37;
   /**
    * logger
    */
   private static final Logger logger = LogManager.getLogger(AuthenticationIAICCommandHandlerImpl.class);

   @Override
   public void process(NVT nvt, int cmd) throws IOException {
      switch (cmd) {
         case IACCommandHandler.IAC_COMMAND_DO:
            logger.info("Received IAC DO auth");
            nvt.sendIACCommand(IACCommandHandler.IAC_COMMAND_WONT, IAC_CODE_AUTHENTICATION);
            break;
         case IACCommandHandler.IAC_COMMAND_DONT:
            logger.info("Received IAC DONT auth");
            break;
         case IACCommandHandler.IAC_COMMAND_WILL:
            logger.info("Received IAC WILL auth");
            // request auth
            nvt.getNvtStream().writeBytes(IACCommandHandler.IAC_IAC, IACCommandHandler.IAC_COMMAND_SB, IAC_CODE_AUTHENTICATION, AUTHTYPE_KERBEROS_V4 | MODIFIER_AUTH_CLIENT_TO_SERVER, IACCommandHandler.IAC_IAC, IACCommandHandler.IAC_COMMAND_SE);
            break;
         case IACCommandHandler.IAC_COMMAND_WONT:
            logger.info("Received IAC WONT auth");
            break;
         case IACCommandHandler.IAC_COMMAND_SB:
            logger.info("Received IAC SB auth");
            final byte[] sn = readSubnegotiation(nvt);
            if (sn[0] == NAME) {
               readString(sn, 1, sn.length);
            } else if (sn[0] == SEND) {
               // send the termspeed
            }
            break;
         default:
            logger.info("Received Unknown IAC Command:" + cmd);
            break;
      }
   }

   @Override
   public int getCommand() {
      return IAC_CODE_AUTHENTICATION;
   }

   @Override
   public String getDescription() {
      return "AUTHENTICATION";
   }
}

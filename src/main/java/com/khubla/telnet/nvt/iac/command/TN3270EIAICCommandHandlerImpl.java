/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet.nvt.iac.command;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.khubla.telnet.nvt.IACCommandHandler;
import com.khubla.telnet.nvt.NVT;

/**
 * TN3270 Enhancements - RFC 2355
 *
 * @author tom
 */
public class TN3270EIAICCommandHandlerImpl extends AbstractIACCommandHandler {
   /**
    * logger
    */
   private static final Logger logger = LoggerFactory.getLogger(TN3270EIAICCommandHandlerImpl.class);
   /**
    * constants...
    */
   public static final int ASSOCIATE = 0;
   public static final int CONNECT = 1;
   public static final int DEVICE_TYPE = 2;
   public static final int FUNCTIONS = 3;
   public static final int IS = 4;
   public static final int REASON = 5;
   public static final int REJECT = 6;
   public static final int REQUEST = 7;
   public static final int SEND = 8;
   /**
    * Reason-codes
    */
   public static final int CONN_PARTNER = 0;
   public static final int DEVICE_IN_USE = 1;
   public static final int INV_ASSOCIATE = 2;
   public static final int INV_NAME = 3;
   public static final int INV_DEVICE_TYPE = 4;
   public static final int TYPE_NAME_ERROR = 5;
   public static final int UNKNOWN_ERROR = 6;
   public static final int UNSUPPORTED_REQ = 7;
   /**
    * Function Names
    */
   public static final int BIND_IMAGE = 0;
   public static final int DATA_STREAM_CTL = 1;
   public static final int RESPONSES = 2;
   public static final int SCS_CTL_CODES = 3;
   public static final int SYSREQ = 4;

   @Override
   public void process(NVT nvt, int cmd) throws IOException {
      switch (cmd) {
         case IACCommandHandler.IAC_COMMAND_DO:
            logger.info("Received IAC DO 3270E");
            break;
         case IACCommandHandler.IAC_COMMAND_DONT:
            logger.info("Received IAC DONT 3270E");
            break;
         case IACCommandHandler.IAC_COMMAND_WILL:
            logger.info("Received IAC WILL 3270E");
            break;
         case IACCommandHandler.IAC_COMMAND_WONT:
            logger.info("Received IAC WONT 3270E");
            break;
         case IACCommandHandler.IAC_COMMAND_SB:
            logger.info("Received IAC SB 3270E");
            break;
         default:
            logger.info("Received Unknown IAC Command:" + cmd);
            break;
      }
   }
}

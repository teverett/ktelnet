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
import com.khubla.telnet.nvt.iac.IACHandler;

/**
 * Telnet Data Encryption Option - RFC 2946
 *
 * @author tom
 */
public class EncryptIAICCommandHandlerImpl extends AbstractIACCommandHandler {
   /**
    * logger
    */
   private static final Logger logger = LoggerFactory.getLogger(EncryptIAICCommandHandlerImpl.class);
   /**
    * Encryption Commands
    */
   public static final int IS = 0;
   public static final int SUPPORT = 1;
   public static final int REPLY = 2;
   public static final int START = 3;
   public static final int END = 4;
   public static final int REQUEST_START = 5;
   public static final int REQUEST_END = 6;
   public static final int ENC_KEYID = 7;
   public static final int DEC_KEYID = 8;
   /**
    * Encryption Types
    */
   public static final int NULL = 0;
   public static final int DES_CFB64 = 1;
   public static final int DES_OFB64 = 2;
   public static final int DES3_CFB64 = 3;
   public static final int DES3_OFB64 = 4;
   public static final int CAST5_40_CFB64 = 8;
   public static final int CAST5_40_OFB64 = 9;
   public static final int CAST128_CFB64 = 10;
   public static final int CAST128_OFB64 = 11;

   @Override
   public void process(NVT nvt, int cmd) throws IOException {
      switch (cmd) {
         case IACCommandHandler.IAC_COMMAND_DO:
            logger.info("Received IAC DO encrypt");
            break;
         case IACCommandHandler.IAC_COMMAND_DONT:
            logger.info("Received IAC DONT encrypt");
            break;
         case IACCommandHandler.IAC_COMMAND_WILL:
            logger.info("Received IAC WILL encrypt");
            /*
             * great, here is what I support
             */
            nvt.getNvtStream().writeBytes(IACCommandHandler.IAC_IAC, IACCommandHandler.IAC_COMMAND_SB, IACHandler.IAC_CODE_ENCRYPT, REQUEST_START, IACCommandHandler.IAC_IAC,
                  IACCommandHandler.IAC_COMMAND_SE);
            nvt.getNvtStream().writeBytes(IACCommandHandler.IAC_IAC, IACCommandHandler.IAC_COMMAND_SB, IACHandler.IAC_CODE_ENCRYPT, SUPPORT, DES_CFB64, IACCommandHandler.IAC_IAC,
                  IACCommandHandler.IAC_COMMAND_SE);
            break;
         case IACCommandHandler.IAC_COMMAND_WONT:
            logger.info("Received IAC WONT encrypt");
            break;
         case IACCommandHandler.IAC_COMMAND_SB:
            logger.info("Received IAC SB encrypt");
            break;
         default:
            logger.info("Received Unknown IAC Command:" + cmd);
            break;
      }
   }
}

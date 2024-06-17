/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet.nvt.command.impl;

import com.khubla.telnet.nvt.NVT;
import com.khubla.telnet.nvt.command.AbstractIACCommandHandler;
import com.khubla.telnet.nvt.stream.IAC;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;

public class EnvvarIAICCommandHandlerImpl extends AbstractIACCommandHandler {
   /**
    * constants
    */
   public static final int IS = 0;
   public static final int SEND = 1;
   public static final int INFO = 2;
   public static final int VAR = 0;
   public static final int VALUE = 1;
   public static final int ESC = 2;
   public static final int USERVAR = 3;
   // RFC 1408
   public static final int IAC_CODE_ENVVAR = 36;
   /**
    * logger
    */
   private static final Logger logger = LogManager.getLogger(EnvvarIAICCommandHandlerImpl.class);

   @Override
   public void process(NVT nvt, int cmd) throws IOException {
      switch (cmd) {
         case IAC.IAC_COMMAND_DO:
            logger.info("Received IAC DO envvar");
            // we don't do envvars
            nvt.sendIACCommand(IAC.IAC_COMMAND_WONT, IAC_CODE_ENVVAR);
            break;
         case IAC.IAC_COMMAND_DONT:
            logger.info("Received IAC DONT envvar");
            break;
         case IAC.IAC_COMMAND_WILL:
            logger.info("Received IAC WILL envvar");
            nvt.getNvtOptions().setEnvvars(true);
            nvt.getNvtStream().writeBytes(IAC.IAC_IAC, IAC.IAC_COMMAND_SB, IAC_CODE_ENVVAR, SEND, IAC.IAC_IAC, IAC.IAC_COMMAND_SE);
            break;
         case IAC.IAC_COMMAND_WONT:
            logger.info("Received IAC WONT envvar");
            nvt.getNvtOptions().setEnvvars(false);
            break;
         case IAC.IAC_COMMAND_SB:
            logger.info("Received IAC SB envvar");
            final byte[] sn = readSubnegotiation(nvt);
            switch (sn[0]) {
               case IS:
                  final byte[] vars = readBytes(sn, 1, sn.length);
                  nvt.setEnvironment(parseVarString(vars));
                  break;
               default:
                  logger.info("Invalid ENV code: " + sn[0]);
            }
            break;
         default:
            logger.info("Received Unknown IAC Command:" + cmd);
            break;
      }
   }

   private HashMap<String, String> parseVarString(byte[] vars) {
      final HashMap<String, String> ret = new HashMap<>();
      int idx = 0;
      while (idx < vars.length) {
         // get the type
         byte type = vars[idx];
         if ((type >= VAR) && (type <= USERVAR)) {
            // get the name
            String name = readNullTerminatedString(vars, idx + 1);
            idx += name.length() + 1;
            // read the value
            String value = readValue(vars, idx + 1);
            idx += value.length() + 1;
            ret.put(name, value);
         } else {
            logger.info("Unknown env var tyoe:" + type);
         }
      }
      return ret;
   }

   private String readNullTerminatedString(byte[] buffer, int startIdx) {
      StringBuilder stringBuilder = new StringBuilder();
      int idx = startIdx;
      while (buffer[idx] != 0) {
         stringBuilder.append((char) buffer[idx]);
         idx = idx + 1;
      }
      return stringBuilder.toString();
   }

   // terminated by a type, or end of data
   private String readValue(byte[] buffer, int startIdx) {
      StringBuilder stringBuilder = new StringBuilder();
      int idx = startIdx;
      while ((idx < buffer.length) && (buffer[idx] > USERVAR)) {
         stringBuilder.append((char) buffer[idx]);
         idx = idx + 1;
      }
      return stringBuilder.toString();
   }

   @Override
   public int getCommand() {
      return IAC_CODE_ENVVAR;
   }

   @Override
   public String getDescription() {
      return "ENVVAR";
   }
}

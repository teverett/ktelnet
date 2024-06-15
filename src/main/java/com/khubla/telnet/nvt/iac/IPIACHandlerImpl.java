/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet.nvt.iac;

import com.khubla.telnet.nvt.NVT;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class IPIACHandlerImpl implements IACHandler {
   /**
    * logger
    */
   private static final Logger logger = LogManager.getLogger(IPIACHandlerImpl.class);

   @Override
   public void process(NVT nvt, int cmd, int option) throws IOException {
      logger.info("Received IP");
      // logout
      nvt.getNvtStream().setIpRequested(true);
   }
}

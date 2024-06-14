/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet.nvt.iac;

import com.khubla.telnet.nvt.NVT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class AYTIACHandlerImpl implements IACHandler {
   /**
    * logger
    */
   private static final Logger logger = LoggerFactory.getLogger(AYTIACHandlerImpl.class);

   @Override
   public void process(NVT nvt, int cmd, int option) throws IOException {
      logger.info("Received AYT");
   }
}

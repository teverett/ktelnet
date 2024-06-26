/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet.nvt.iac.impl;

import com.khubla.telnet.nvt.NVT;
import com.khubla.telnet.nvt.iac.IACHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class DatamarkIACHandlerImpl implements IACHandler {
   /**
    * logger
    */
   private static final Logger logger = LogManager.getLogger(DatamarkIACHandlerImpl.class);

   @Override
   public void process(NVT nvt, int cmd, int option) throws IOException {
      logger.info("Received datamark");
   }
}

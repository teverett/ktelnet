/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet.nvt.spy.impl;

import com.khubla.telnet.nvt.spy.NVTSpy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggingNVTSpyImpl implements NVTSpy {
   /**
    * logger
    */
   private static final Logger logger = LogManager.getLogger(LoggingNVTSpyImpl.class);

   @Override
   public void readbyte(int b) {
      logger.info(String.format("read: 0x%02x %02d %c", b, b, b));
   }

   @Override
   public void readshort(int s) {
      logger.info(String.format("read: 0x%02x %02d %c", s, s, s));
   }

   @Override
   public void writebyte(int b) {
      logger.info(String.format("wrote: 0x%02x %02d %c", b, b, b));
   }
}

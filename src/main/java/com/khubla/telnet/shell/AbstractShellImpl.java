/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet.shell;

import com.khubla.telnet.nvt.NVT;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class AbstractShellImpl implements Shell {
   /**
    * logger
    */
   private static final Logger logger = LogManager.getLogger(AbstractShellImpl.class);
   /**
    * nvt
    */
   private final NVT nvt;

   public AbstractShellImpl(NVT nvt) {
      super();
      this.nvt = nvt;
   }

   public NVT getNvt() {
      return nvt;
   }

   @Override
   public void run() {
      try {
         /*
          * go!
          */
         runShell();
      } catch (final Exception e) {
         logger.error(e.getMessage(), e);
      } finally {
         try {
            nvt.close();
         } catch (final Exception e) {
            logger.error(e.getMessage(), e);
         }
      }
   }

   protected abstract void runShell();
}

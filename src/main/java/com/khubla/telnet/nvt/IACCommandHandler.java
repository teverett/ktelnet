/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet.nvt;

import java.io.IOException;

public interface IACCommandHandler {
   void process(NVT nvt, int cmd) throws IOException;

   int getCommand();

   String getDescription();

   boolean negotiate();
}

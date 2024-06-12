/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet.nvt.iac;

import com.khubla.telnet.nvt.NVT;

import java.io.IOException;

public interface IACHandler {
   void process(NVT nvt, int cmd, int option) throws IOException;
}

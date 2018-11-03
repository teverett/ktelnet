/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet.shell;

import com.khubla.telnet.nvt.NVT;

public interface ShellFactory {
   Shell createShell(NVT nvt);
}

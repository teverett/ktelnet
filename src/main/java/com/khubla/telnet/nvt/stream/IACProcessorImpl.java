package com.khubla.telnet.nvt.stream;

import com.khubla.telnet.nvt.IACCommandHandler;
import com.khubla.telnet.nvt.NVT;
import com.khubla.telnet.nvt.iac.CommandIACHandlerImpl;
import com.khubla.telnet.nvt.iac.IACHandler;
import com.khubla.telnet.nvt.iac.IPIACHandlerImpl;
import com.khubla.telnet.nvt.iac.NOPIACHandlerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;

public class IACProcessorImpl implements IACProcessor {
   /**
    * logger
    */
   private static final Logger logger = LoggerFactory.getLogger(IACProcessorImpl.class);
   /**
    * IAC handlers
    */
   private final HashMap<Integer, IACHandler> iacHandlers = new HashMap<Integer, IACHandler>();
   /**
    * nvt
    */
   private final NVT nvt;

   public IACProcessorImpl(NVT nvt) {
      this.nvt = nvt;
      /*
       * IACs
       */
      iacHandlers.put(IACCommandHandler.IAC_COMMAND_WILL, new CommandIACHandlerImpl());
      iacHandlers.put(IACCommandHandler.IAC_COMMAND_WONT, new CommandIACHandlerImpl());
      iacHandlers.put(IACCommandHandler.IAC_COMMAND_DO, new CommandIACHandlerImpl());
      iacHandlers.put(IACCommandHandler.IAC_COMMAND_DONT, new CommandIACHandlerImpl());
      iacHandlers.put(IACCommandHandler.IAC_COMMAND_SB, new CommandIACHandlerImpl());
      iacHandlers.put(IACCommandHandler.IAC_COMMAND_NOP, new NOPIACHandlerImpl());
      iacHandlers.put(IACCommandHandler.IAC_COMMAND_IP, new IPIACHandlerImpl());
   }

   @Override
   public void processIAC() throws IOException {
      final int cmd = nvt.getNvtStream().readRawByte();
      final int option = nvt.getNvtStream().readRawByte();
      final IACHandler iacHandler = iacHandlers.get(cmd);
      if (null != iacHandler) {
         iacHandler.process(nvt, cmd, option);
      } else {
         logger.info("No handler for AIC command:" + cmd + " option:" + option);
      }
   }
}

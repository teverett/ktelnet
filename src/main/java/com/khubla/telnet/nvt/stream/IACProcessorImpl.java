package com.khubla.telnet.nvt.stream;

import com.khubla.telnet.nvt.NVT;
import com.khubla.telnet.nvt.iac.IACHandler;
import com.khubla.telnet.nvt.iac.impl.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;

public class IACProcessorImpl implements IACProcessor {
   /**
    * logger
    */
   private static final Logger logger = LogManager.getLogger(IACProcessorImpl.class);
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
      iacHandlers.put(IAC.IAC_COMMAND_WILL, new CommandIACHandlerImpl());
      iacHandlers.put(IAC.IAC_COMMAND_WONT, new CommandIACHandlerImpl());
      iacHandlers.put(IAC.IAC_COMMAND_DO, new CommandIACHandlerImpl());
      iacHandlers.put(IAC.IAC_COMMAND_DONT, new CommandIACHandlerImpl());
      iacHandlers.put(IAC.IAC_COMMAND_SB, new CommandIACHandlerImpl());
      iacHandlers.put(IAC.IAC_COMMAND_NOP, new NOPIACHandlerImpl());
      iacHandlers.put(IAC.IAC_COMMAND_IP, new IPIACHandlerImpl());
      iacHandlers.put(IAC.IAC_COMMAND_BREAK, new BRKIACHandlerImpl());
      iacHandlers.put(IAC.IAC_COMMAND_AYT, new AYTIACHandlerImpl());
      iacHandlers.put(IAC.IAC_COMMAND_GA, new GAIACHandlerImpl());
      iacHandlers.put(IAC.IAC_COMMAND_EC, new ECIACHandlerImpl());
      iacHandlers.put(IAC.IAC_COMMAND_EL, new ELIACHandlerImpl());
      iacHandlers.put(IAC.IAC_COMMAND_AO, new AOIACHandlerImpl());
      iacHandlers.put(IAC.IAC_COMMAND_DATAMARK, new DatamarkIACHandlerImpl());
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

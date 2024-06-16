package com.khubla.telnet.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
   private static final String CONFIG_FILE = "ktelnet.properties";
   /**
    * logger
    */
   private static final Logger logger = LogManager.getLogger(Config.class);
   private static Config instance;
   private Properties properties;

   private Config() {
   }

   public static Config getInstance() {
      try {
         if (instance == null) {
            instance = new Config();
            instance.readConfig();
         }
         return instance;
      } catch (Exception e) {
         logger.error(e);
         return instance;
      }
   }

   private void readConfig() throws IOException {
      this.properties = new Properties();
      properties.load(new FileInputStream(CONFIG_FILE));
   }

   public String getProperty(String key) {
      return properties.getProperty(key);
   }
}

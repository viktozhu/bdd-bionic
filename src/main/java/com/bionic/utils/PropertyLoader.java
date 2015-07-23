package com.bionic.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by viktozhu on 7/23/15.
 */
public class PropertyLoader {
    private static Logger logger = LoggerFactory.getLogger(PropertyLoader.class);

    private static final String[] PROPERTY_FILE = {"/application.properties","/thucydides.properties"};
    private static Properties properties = new Properties();

    public static void loadPropertys()
    {
        for (int i=0; i<PROPERTY_FILE.length; i++)
            loadPropertys(PROPERTY_FILE[i]);
    }

    public static void loadPropertys(String filePath)
    {
        logger.info("Load properties from file=" + filePath);
        try {
            properties.load(PropertyLoader.class.getResourceAsStream(filePath));
        } catch (IOException e) {
            logger.error("No such property file",e);
        }
    }

    public static String getProperty(String name){
        logger.debug("get property "+name);
        String value = "";
        if (name != null)
        {
            value = properties.getProperty(name);
        } else
        {
            logger.error("No such property",name);
        }
        return value;
    }
}

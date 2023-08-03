package com.real.time.publisher.common;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.real.time.publisher.amsterdam.parkings.App;

public class Configuration {

    private static Logger LOGGER = LogManager.getLogger(Configuration.class);

    private static final String fileName = "config.properties";

    public static Properties getProperties() {
        Properties props = null;

        if (props == null) {
            try (InputStream input = new FileInputStream("./" + fileName)) {
                LOGGER.info("load from ./" + fileName);
                props = new Properties();
                props.load(input);
            } catch (IOException ex) {
                //ex.printStackTrace();
            }
        }

        if (props == null) {
            try (InputStream input = App.class.getClassLoader().getResourceAsStream(fileName)) {
                LOGGER.info("load from resources = " + fileName);
                props = new Properties();
                props.load(input);
            } catch (IOException ex) {
                //ex.printStackTrace();
            }
        }

        return props;
    }

}

package com.real.time.publisher.amsterdam.parkings;

import com.migratorydata.client.MigratoryDataLogLevel;
import com.migratorydata.client.MigratoryDataLogListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MyMdLogListener implements MigratoryDataLogListener {
    private Logger LOGGER = LogManager.getLogger(MyMdLogListener.class);

    @Override
    public void onLog(String s, MigratoryDataLogLevel migratoryDataLogLevel) {
        if(migratoryDataLogLevel == MigratoryDataLogLevel.TRACE){
            LOGGER.trace(s);
        }
        if(migratoryDataLogLevel == MigratoryDataLogLevel.DEBUG){
            LOGGER.debug(s);
        }
        if(migratoryDataLogLevel == MigratoryDataLogLevel.INFO){
            LOGGER.info(s);
        }
        if(migratoryDataLogLevel == MigratoryDataLogLevel.WARN){
            LOGGER.warn(s);
        }
        if(migratoryDataLogLevel == MigratoryDataLogLevel.ERROR){
            LOGGER.error(s);
        }
    }
}

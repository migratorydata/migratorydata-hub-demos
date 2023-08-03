package com.real.time.publisher.brussels.traffic;

import com.migratorydata.client.MigratoryDataListener;
import com.migratorydata.client.MigratoryDataMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MyMdListener implements MigratoryDataListener {

    private Logger LOGGER = LogManager.getLogger(MyMdListener.class);

    @Override
    public void onMessage(MigratoryDataMessage migratoryDataMessage) {
        LOGGER.info(migratoryDataMessage);
    }

    @Override
    public void onStatus(String s, String s1) {
        LOGGER.info(s+":"+s1);
    }
}

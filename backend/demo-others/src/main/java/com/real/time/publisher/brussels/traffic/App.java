package com.real.time.publisher.brussels.traffic;

import java.util.Properties;

import com.real.time.publisher.common.Configuration;

public class App {

    public static void main(String[] args){
        Properties properties = Configuration.getProperties();

        String migratoryDataServer = properties.getProperty("migratory-data-server");
        String migratoryDataSubject = properties.getProperty("brussels-traffic-subject");
        String migratoryDataEntitlementToken = properties.getProperty("entitlement-token-brussels");
        boolean encryption = Boolean.valueOf(properties.getProperty("migratory-data-server-encryption", "false"));
        TrafficService trafficService = new TrafficService(migratoryDataServer, migratoryDataEntitlementToken, migratoryDataSubject, encryption);
    }
}

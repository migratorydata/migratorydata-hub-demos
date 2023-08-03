package com.real.time.publisher.amsterdam.parkings;

import java.util.Properties;

import com.real.time.publisher.common.Configuration;

public class App {

    public static void main(String[] args){
        Properties properties = Configuration.getProperties();

        String migratoryDataServer = properties.getProperty("migratory-data-server");
        String migratoryDataSubject = properties.getProperty("amsterdam-parkings-subject");
        String migratoryDataEntitlementToken = properties.getProperty("entitlement-token-amsterdam");
        boolean encryption = Boolean.valueOf(properties.getProperty("migratory-data-server-encryption", "false"));
        ParkingService parkingService = new ParkingService(migratoryDataServer, migratoryDataEntitlementToken, migratoryDataSubject, encryption);
    }
}

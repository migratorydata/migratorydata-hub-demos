package com.real.time.publisher.cryptocurrency;

import java.util.Properties;

import com.real.time.publisher.common.Configuration;

public class App {

    public static void main(String[] args){
        Properties properties = Configuration.getProperties();

        String migratoryDataServer = properties.getProperty("migratory-data-server");
        System.out.println(migratoryDataServer);
        String migratoryDataSubject = properties.getProperty("cryptocurrency-subject");
        String migratoryDataEntitlementToken = properties.getProperty("entitlement-token-cryptocurrency");
        boolean encryption = Boolean.valueOf(properties.getProperty("migratory-data-server-encryption", "false"));
        CryptocurrencyService cryptocurrencyService = new CryptocurrencyService(migratoryDataServer, migratoryDataEntitlementToken, migratoryDataSubject, encryption);

    }
}

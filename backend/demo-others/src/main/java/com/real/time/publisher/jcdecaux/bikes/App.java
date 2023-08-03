package com.real.time.publisher.jcdecaux.bikes;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.real.time.publisher.common.Configuration;

public class App {
    public static void main(String[] args) {
        Properties properties = Configuration.getProperties();

        String migratoryDataServer = properties.getProperty("migratory-data-server");
        String migratoryDataSubjectFirstPart = properties.getProperty("bikes-subject-first-part");
        String migratoryDataEntitlementToken = properties.getProperty("entitlement-token-jcdecaux");
        String apiKey = properties.getProperty("jcdecaux-api-key");
        boolean encryption = Boolean.valueOf(properties.getProperty("migratory-data-server-encryption", "false"));

        List<String> contracts = new ArrayList<>();
        contracts.add(properties.getProperty("rouen-bikes-subject"));
//            contracts.add(properties.getProperty("toulouse-bikes-subject"));
//            contracts.add(properties.getProperty("luxembourg-bikes-subject"));
//            contracts.add(properties.getProperty("dublin-bikes-subject"));
//            contracts.add(properties.getProperty("valence-bikes-subject"));
        contracts.add(properties.getProperty("santander-bikes-subject"));
        contracts.add(properties.getProperty("lund-bikes-subject"));
//            contracts.add(properties.getProperty("bruxelles-bikes-subject"));
        contracts.add(properties.getProperty("amiens-bikes-subject"));
//            contracts.add(properties.getProperty("mulhouse-bikes-subject"));
        contracts.add(properties.getProperty("lillestrom-bikes-subject"));
//            contracts.add(properties.getProperty("lyon-bikes-subject"));
//            contracts.add(properties.getProperty("ljubljana-bikes-subject"));
//            contracts.add(properties.getProperty("seville-bikes-subject"));
        contracts.add(properties.getProperty("nancy-bikes-subject"));
        contracts.add(properties.getProperty("namur-bikes-subject"));
//            contracts.add(properties.getProperty("creteil-bikes-subject"));
//            contracts.add(properties.getProperty("cergy-pontoise-bikes-subject"));
        contracts.add(properties.getProperty("vilnius-bikes-subject"));
        contracts.add(properties.getProperty("toyama-bikes-subject"));
//            contracts.add(properties.getProperty("marseille-bikes-subject"));
//            contracts.add(properties.getProperty("nantes-bikes-subject"));
//            contracts.add(properties.getProperty("brisbane-bikes-subject"));
        contracts.add(properties.getProperty("besancon-bikes-subject"));
        BikesService bikesService = new BikesService(migratoryDataServer, migratoryDataEntitlementToken, migratoryDataSubjectFirstPart, contracts, apiKey, encryption);

    }
}

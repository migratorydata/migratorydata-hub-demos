package com.real.time.publisher.zurich.parkings;

import com.migratorydata.client.MigratoryDataClient;
import com.migratorydata.client.MigratoryDataLogLevel;
import com.migratorydata.client.MigratoryDataMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ParkingService {
    private Logger LOGGER = LogManager.getLogger(ParkingService.class);

    private MigratoryDataClient migratoryDataClient;
    private String migratoryDataSubject;
    private RSSFeedParser rssFeedParser;

    private Map<String, ParkingInfo> parkingsMap = new HashMap<>();

    public ParkingService(String migratoryDataServer, String migratoryDataEntitlementToken,String migratoryDataSubject, boolean encryption) {
        startMigratoryDataClient(migratoryDataServer, migratoryDataEntitlementToken, encryption);
        rssFeedParser = new RSSFeedParser();
        this.migratoryDataSubject = migratoryDataSubject;
        start();
    }

    private void startMigratoryDataClient(String migratoryDataServer, String migratoryDataEntitlementToken, boolean encryption) {
        migratoryDataClient = new MigratoryDataClient();
        migratoryDataClient.setLogListener(new MyMdLogListener(), MigratoryDataLogLevel.DEBUG);
        migratoryDataClient.setEntitlementToken(migratoryDataEntitlementToken);
        migratoryDataClient.setListener(new MyMdListener());
        migratoryDataClient.setServers(new String[]{migratoryDataServer});
        migratoryDataClient.setEncryption(encryption);
        migratoryDataClient.connect();
    }

    private void start(){
        while(true){
            ParkingsFeed parkingsFeed = rssFeedParser.readFeed();
            if(parkingsFeed!=null){
                processParkingsFeed(parkingsFeed);
            }
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                LOGGER.error(e.getMessage());
            }
        }
    }

    private void processParkingsFeed(ParkingsFeed parkingsFeed){
        List<ParkingInfo> parkingInfoList = parkingsFeed.getEntries();
        boolean publishToMd = false;
        for(ParkingInfo parkingInfo: parkingInfoList){
            if(parkingsMap.containsKey(parkingInfo.getTitle())){
                if(!parkingsMap.get(parkingInfo.getTitle()).equals(parkingInfo)){
                    publishToMd = true;
                    parkingsMap.put(parkingInfo.getTitle(), parkingInfo);
                }
            } else {
                publishToMd = true;
                parkingsMap.put(parkingInfo.getTitle(), parkingInfo);
            }
        }

        if(publishToMd){
            publishMessageToMd();
        }
    }

    private void publishMessageToMd(){
        JSONArray jsonArray = new JSONArray();
        for(String key: parkingsMap.keySet()){
            ParkingInfo parkingInfo = parkingsMap.get(key);
            JSONObject parkingInfoResult = new JSONObject();
            try {
                Field map = parkingInfoResult.getClass().getDeclaredField("map");
                map.setAccessible(true);
                map.set(parkingInfoResult, new LinkedHashMap<>());
                map.setAccessible(false);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                LOGGER.warn(e.getMessage());
            }
            parkingInfoResult.put("name", parkingInfo.getTitle());
            parkingInfoResult.put("link*", parkingInfo.getLink());
            parkingInfoResult.put("status", parkingInfo.getStatus());
            parkingInfoResult.put("freeSpaces", parkingInfo.getFreeSpaces());
            jsonArray.put(parkingInfoResult);

        }
        String stringContent = jsonArray.toString();
        migratoryDataClient.publish(new MigratoryDataMessage(migratoryDataSubject, stringContent.getBytes(StandardCharsets.UTF_8)));

    }
}

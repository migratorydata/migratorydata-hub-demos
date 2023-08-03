package com.real.time.publisher.amsterdam.parkings;

import com.migratorydata.client.MigratoryDataClient;
import com.migratorydata.client.MigratoryDataLogLevel;
import com.migratorydata.client.MigratoryDataMessage;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

public class ParkingService {

    private Logger LOGGER = LogManager.getLogger(ParkingService.class);

    private MigratoryDataClient migratoryDataClient;
    private String migratoryDataSubject;
    private OkHttpClient httpClient;
    private Request requestParkings;

    private Map<String, ParkingInfo> parkingsMap = new LinkedHashMap<>();

    public ParkingService(String migratoryDataServer, String migratoryDataEntitlementToken,String migratoryDataSubject, boolean encryption) {
        this.migratoryDataSubject = migratoryDataSubject;
        startMigratoryDataClient(migratoryDataServer, migratoryDataEntitlementToken, encryption);
        httpClient = new OkHttpClient();
        requestParkings = new Request.Builder().url("https://p-info.vorin-amsterdam.nl/v1/ParkingLocation.json")
                .get().build();
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
            try {
                Response response = httpClient.newCall(requestParkings).execute();
                if (response.code() == 200) {
                    ResponseBody responseBody = response.body();
                    if (responseBody != null) {
                        processHttpResponseString(responseBody.string());
                    }
                } else {
                    LOGGER.error("Http request response code: " + response.code());
                }
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
            }
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                LOGGER.error(e.getMessage());
            }
        }
    }

    private void processHttpResponseString(String responseString){
        JSONObject parkings = new JSONObject(responseString);
        JSONArray features = parkings.getJSONArray("features");
        boolean publishToMd = false;
        for(int i=0;i<features.length();i++) {
            JSONObject feature = features.getJSONObject(i);
            JSONObject geometry = feature.getJSONObject("geometry");
            JSONArray coordinates = geometry.getJSONArray("coordinates");
            JSONObject properties = feature.getJSONObject("properties");

            String name = properties.getString("Name");
            Double latitude = coordinates.getDouble(1);
            Double longitude = coordinates.getDouble(0);
            String state = properties.getString("State");
            String freeSpaceShort = properties.getString("FreeSpaceShort");
            String freeSpaceLong = properties.getString("FreeSpaceLong");
            String shortCapacity = properties.getString("ShortCapacity");
            String longCapacity = properties.getString("LongCapacity");
            ParkingInfo parkingInfo = new ParkingInfo(name, latitude, longitude, state, freeSpaceShort, freeSpaceLong, shortCapacity, longCapacity);

            if(parkingsMap.containsKey(name)){
                if(!parkingsMap.get(name).equals(parkingInfo)){
                    publishToMd = true;
                    parkingsMap.put(name, parkingInfo);
                }
            } else {
                publishToMd = true;
                parkingsMap.put(name, parkingInfo);
            }
        }
        if(publishToMd){
            publishMessageToMd();
        }

    }

    private void publishMessageToMd() {
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
            parkingInfoResult.put("name", parkingInfo.getName());
            parkingInfoResult.put("state", parkingInfo.getState());
            parkingInfoResult.put("freeShort", parkingInfo.getFreeSpaceShort());
            parkingInfoResult.put("freeLong", parkingInfo.getFreeSpaceLong());
            parkingInfoResult.put("shortCapacity", parkingInfo.getShortCapacity());
            parkingInfoResult.put("longCapacity", parkingInfo.getLongCapacity());
            // with these exceeds 8kb
//            parkingInfoResult.put("latitude", parkingInfo.getLatitude());
//            parkingInfoResult.put("longitude", parkingInfo.getLongitude());
            jsonArray.put(parkingInfoResult);
        }
        String stringContent = jsonArray.toString();
        migratoryDataClient.publish(new MigratoryDataMessage(migratoryDataSubject, stringContent.getBytes()));
    }
}

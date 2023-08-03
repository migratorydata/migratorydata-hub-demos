package com.real.time.publisher.jcdecaux.bikes;

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
import java.nio.charset.StandardCharsets;
import java.util.*;

public class BikesService {
    private Logger LOGGER = LogManager.getLogger(BikesService.class);

    private MigratoryDataClient migratoryDataClient;
    private String migratoryDataSubjectFirstPart;
    private List<String> bikesContracts;
    private OkHttpClient httpClient;
    private Request requestBikes;

    private Map<String, Map<String, BikeStation>> currentRegionStations = new LinkedHashMap<>();

    public BikesService(String migratoryDataServer, String migratoryDataEntitlementToken, String migratoryDataSubjectFirstPart,
                        List<String> bikesContracts, String bikesApiKey, boolean encryption) {
        this.migratoryDataSubjectFirstPart = migratoryDataSubjectFirstPart;
        this.bikesContracts = bikesContracts;
        startMigratoryDataClient(migratoryDataServer, migratoryDataEntitlementToken, encryption);
        httpClient = new OkHttpClient();
        requestBikes = new Request.Builder().url("https://api.jcdecaux.com/vls/v1/stations?apiKey=" + bikesApiKey)
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

    public void start() {
        while (true) {
            try {
                Response response = httpClient.newCall(requestBikes).execute();
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

    private void processHttpResponseString(String responseString) {
        JSONArray bikesStations = new JSONArray(responseString);
        Set<String> toPublishContracts = new HashSet<>();
        for (int i = 0; i < bikesStations.length(); i++) {
            JSONObject station = bikesStations.getJSONObject(i);
            String contractName = station.getString("contract_name");
            if (contractName != null) {
                if (bikesContracts.contains(contractName)) {
                    String name = station.getString("name");
                    String address = station.getString("address");
                    JSONObject position = station.getJSONObject("position");
                    Double latitude = position.getDouble("lat");
                    Double longitude = position.getDouble("lng");
                    String paymentTerminal = String.valueOf(station.getBoolean("banking"));
                    int bikeStands = station.getInt("bike_stands");
                    int availableBikeStands = station.getInt("available_bike_stands");
                    int availableBikes = station.getInt("available_bikes");
                    String status = station.getString("status");

                    BikeStation bikeStation = new BikeStation(name, address, latitude, longitude, paymentTerminal,
                            bikeStands, availableBikeStands, availableBikes, status);

                    if (!currentRegionStations.containsKey(contractName)) {
                        currentRegionStations.put(contractName, new LinkedHashMap<>());
                    }
                    Map<String, BikeStation> bikeStationMap = currentRegionStations.get(contractName);
                    if (bikeStationMap.containsKey(bikeStation.getName())) {
                        if (!bikeStationMap.get(bikeStation.getName()).equals(bikeStation)) {
                            toPublishContracts.add(contractName);
                            bikeStationMap.put(bikeStation.getName(), bikeStation);
                        }
                    } else {
                        toPublishContracts.add(contractName);
                        bikeStationMap.put(bikeStation.getName(), bikeStation);
                    }
                } /*else {
                    LOGGER.error("Got a contract not found in configurations list: " + contractName + ". You need to actualize the contracts list from configuration.");
                }*/
            }
        }

        if (toPublishContracts.size() != 0) {
            publishMessagesToMd(toPublishContracts);
        }
    }

    private void publishMessagesToMd(Set<String> contractNames) {
        for (String contractName : contractNames) {
            Map<String, BikeStation> bikeStationMap = currentRegionStations.get(contractName);
            JSONArray jsonArray = new JSONArray();
            for (String key : bikeStationMap.keySet()) {
                BikeStation bikeStation = bikeStationMap.get(key);
                if (bikeStation.getName().contains("TEST DSI")) {
                    continue;
                }
                JSONObject bikeStationJson = new JSONObject();
                try {
                    Field map = bikeStationJson.getClass().getDeclaredField("map");
                    map.setAccessible(true);
                    map.set(bikeStationJson, new LinkedHashMap<>());
                    map.setAccessible(false);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    LOGGER.warn(e.getMessage());
                }
                bikeStationJson.put("name", bikeStation.getName());
//                bikeStationJson.put("bikeStands", bikeStation.getBikeStands());
                bikeStationJson.put("availableBikeStands", bikeStation.getAvailableBikeStands());
                bikeStationJson.put("&availableBikes", bikeStation.getAvailableBikes());
                bikeStationJson.put("status", bikeStation.getStatus());
                bikeStationJson.put("address", bikeStation.getAddress());
                bikeStationJson.put("lat*", bikeStation.getLatitude());
                bikeStationJson.put("long*", bikeStation.getLongitude());
                bikeStationJson.put("payTerminal", bikeStation.isPaymentTerminal());
                jsonArray.put(bikeStationJson);
            }
            String content = jsonArray.toString();
            migratoryDataClient.publish(new MigratoryDataMessage(migratoryDataSubjectFirstPart + contractName, content.getBytes(StandardCharsets.UTF_8), contractName));
        }
    }
}

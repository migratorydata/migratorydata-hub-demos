package com.real.time.publisher.brussels.traffic;

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

public class TrafficService {
    private Logger LOGGER = LogManager.getLogger(TrafficService.class);

    private MigratoryDataClient migratoryDataClient;
    private String migratoryDataSubject;
    private OkHttpClient httpClient;
    private Request requestTraffic;
    private List<String> trafficIntervalTimes;

    private Map<String, TrafficInfo> traverseTrafficMap = new HashMap<>();

    public TrafficService(String migratoryDataServer, String migratoryDataEntitlementToken, String migratoryDataSubject, boolean encryption) {
        trafficIntervalTimes = new ArrayList<>();
        trafficIntervalTimes.add("1m");
        trafficIntervalTimes.add("5m");
        trafficIntervalTimes.add("15m");
        trafficIntervalTimes.add("60m");
        this.migratoryDataSubject = migratoryDataSubject;
        startMigratoryDataClient(migratoryDataServer, migratoryDataEntitlementToken, encryption);
        httpClient = new OkHttpClient();
        requestTraffic = new Request.Builder().url("http://data-mobility.brussels/traffic/api/counts/?request=live")
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

    private void start() {
        while (true) {
            try {
                Response response = httpClient.newCall(requestTraffic).execute();
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
        JSONObject trafficResponse = new JSONObject(responseString);
        JSONObject trafficData = trafficResponse.getJSONObject("data");
        Set<String> traverseNames = trafficData.keySet();
        boolean publishToMd = false;
        for (String traverseName : traverseNames) {

            TrafficInfo trafficInfo = new TrafficInfo(traverseName);

            JSONObject results = trafficData.getJSONObject(traverseName).getJSONObject("results");
            for (String intervalTime : trafficIntervalTimes) {
                JSONObject trafficInfoPerTime = results.getJSONObject(intervalTime).getJSONObject("t1");
                if (!trafficInfoPerTime.isNull("count")) {
                    int count = trafficInfoPerTime.getInt("count");
                    Double speed = trafficInfoPerTime.getDouble("speed");
                    Double occupancy = trafficInfoPerTime.getDouble("occupancy");
                    String startTime = trafficInfoPerTime.getString("start_time");
                    String endTime = trafficInfoPerTime.getString("end_time");
                    IntervalInfo intervalInfo = new IntervalInfo(count, speed, occupancy, startTime, endTime);
                    if (intervalTime.equals("1m")) {
                        trafficInfo.setOneMinute(intervalInfo);
                    }
                    if (intervalTime.equals("5m")) {
                        trafficInfo.setFiveMinutes(intervalInfo);
                    }
                    if (intervalTime.equals("15m")) {
                        trafficInfo.setFifteenMinutes(intervalInfo);
                    }
                    if (intervalTime.equals("60m")) {
                        trafficInfo.setSixtyMinutes(intervalInfo);
                    }
                }
            }
            if (trafficInfo.getOneMinute() != null || trafficInfo.getFiveMinutes() != null || trafficInfo.getFifteenMinutes() != null
                    || trafficInfo.getSixtyMinutes() != null) {
                if (traverseTrafficMap.containsKey(trafficInfo.getTraverseName())) {
                    if (!traverseTrafficMap.get(trafficInfo.getTraverseName()).equals(trafficInfo)) {
                        publishToMd = true;
                        traverseTrafficMap.put(trafficInfo.getTraverseName(), trafficInfo);
                    }
                } else {
                    publishToMd = true;
                    traverseTrafficMap.put(trafficInfo.getTraverseName(), trafficInfo);
                }
            }
        }

        if (publishToMd) {
            publishMessageToMd();
        }
    }

    private void publishMessageToMd() {
        JSONArray trafficMdContent = new JSONArray();
        for (String key : traverseTrafficMap.keySet()) {
            JSONObject traverseInfoJson = new JSONObject();
            try {
                Field map = traverseInfoJson.getClass().getDeclaredField("map");
                map.setAccessible(true);
                map.set(traverseInfoJson, new LinkedHashMap<>());
                map.setAccessible(false);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                LOGGER.warn(e.getMessage());
            }
            TrafficInfo trafficInfo = traverseTrafficMap.get(key);
            traverseInfoJson.put("traverseName", trafficInfo.getTraverseName());
            IntervalInfo oneMinuteInterval = trafficInfo.getOneMinute();
            traverseInfoJson.put("count", oneMinuteInterval.getCount());
            traverseInfoJson.put("speed", oneMinuteInterval.getSpeed());
            traverseInfoJson.put("occupancy", oneMinuteInterval.getOccupancy());
            traverseInfoJson.put("startTime", oneMinuteInterval.getStartTime());
            traverseInfoJson.put("endTime", oneMinuteInterval.getEndTime());

//            if (trafficInfo.getOneMinute() != null) {
//                JSONObject oneMinuteIntervalJson = new JSONObject();
//                IntervalInfo oneMinuteInterval = trafficInfo.getOneMinute();
//                oneMinuteIntervalJson.put("count", oneMinuteInterval.getCount());
//                oneMinuteIntervalJson.put("speed", oneMinuteInterval.getSpeed());
//                oneMinuteIntervalJson.put("occupancy", oneMinuteInterval.getOccupancy());
//                oneMinuteIntervalJson.put("startTime", oneMinuteInterval.getStartTime());
//                oneMinuteIntervalJson.put("endTime", oneMinuteInterval.getEndTime());
//                traverseInfoJson.put("1m", oneMinuteIntervalJson);
//            }
//            if (trafficInfo.getFiveMinutes() != null) {
//                JSONObject fiveMinutesIntervalJson = new JSONObject();
//                IntervalInfo fiveMinutesInterval = trafficInfo.getFiveMinutes();
//                fiveMinutesIntervalJson.put("count", fiveMinutesInterval.getCount());
//                fiveMinutesIntervalJson.put("speed", fiveMinutesInterval.getSpeed());
//                fiveMinutesIntervalJson.put("occupancy", fiveMinutesInterval.getOccupancy());
//                fiveMinutesIntervalJson.put("startTime", fiveMinutesInterval.getStartTime());
//                fiveMinutesIntervalJson.put("endTime", fiveMinutesInterval.getEndTime());
//                traverseInfoJson.put("5m", fiveMinutesIntervalJson);
//            }
//            if (trafficInfo.getFifteenMinutes() != null) {
//                JSONObject fifteenMinutesIntervalJson = new JSONObject();
//                IntervalInfo fifteenMinutesInterval = trafficInfo.getFifteenMinutes();
//                fifteenMinutesIntervalJson.put("count", fifteenMinutesInterval.getCount());
//                fifteenMinutesIntervalJson.put("speed", fifteenMinutesInterval.getSpeed());
//                fifteenMinutesIntervalJson.put("occupancy", fifteenMinutesInterval.getOccupancy());
//                fifteenMinutesIntervalJson.put("startTime", fifteenMinutesInterval.getStartTime());
//                fifteenMinutesIntervalJson.put("endTime", fifteenMinutesInterval.getEndTime());
//                traverseInfoJson.put("15m", fifteenMinutesIntervalJson);
//            }
//            if (trafficInfo.getSixtyMinutes()!=null) {
//                JSONObject sixtyMinutesIntervalJson = new JSONObject();
//                IntervalInfo sixtyMinutesInterval = trafficInfo.getSixtyMinutes();
//                sixtyMinutesIntervalJson.put("count", sixtyMinutesInterval.getCount());
//                sixtyMinutesIntervalJson.put("speed", sixtyMinutesInterval.getSpeed());
//                sixtyMinutesIntervalJson.put("occupancy", sixtyMinutesInterval.getOccupancy());
//                sixtyMinutesIntervalJson.put("startTime", sixtyMinutesInterval.getStartTime());
//                sixtyMinutesIntervalJson.put("endTime", sixtyMinutesInterval.getEndTime());
//                traverseInfoJson.put("60m", sixtyMinutesIntervalJson);
//            }
            trafficMdContent.put(traverseInfoJson);
        }
        String mdMessageContent = trafficMdContent.toString();
        migratoryDataClient.publish(new MigratoryDataMessage(migratoryDataSubject, mdMessageContent.getBytes(StandardCharsets.UTF_8)));
    }
}

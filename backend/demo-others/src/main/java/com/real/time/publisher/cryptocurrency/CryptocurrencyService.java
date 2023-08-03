package com.real.time.publisher.cryptocurrency;

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
import java.util.*;

public class CryptocurrencyService {

    private Logger LOGGER = LogManager.getLogger(CryptocurrencyService.class);

    private MigratoryDataClient migratoryDataClient;
    private String migratoryDataSubject;
    private OkHttpClient httpClient;
    private Request requestPrices;

    private Map<Integer, CryptocurrencyInfo> currentSymbolCryptocurrencies = new LinkedHashMap<>();

    public CryptocurrencyService(String migratoryDataServer, String migratoryDataEntitlementToken,String migratoryDataSubject, boolean encryption) {
        this.migratoryDataSubject = migratoryDataSubject;
        startMigratoryDataClient(migratoryDataServer, migratoryDataEntitlementToken, encryption);
        httpClient = new OkHttpClient();
        requestPrices = new Request.Builder().url("https://api.alternative.me/v2/ticker/?limit=15&convert=EUR")
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
                Response response = httpClient.newCall(requestPrices).execute();
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
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                LOGGER.error(e.getMessage());
            }
        }
    }

    private void processHttpResponseString(String responseString) {
        JSONObject requestResult = new JSONObject(responseString);
        JSONObject data = requestResult.getJSONObject("data");
        Set<String> dataKeys = data.keySet();
        boolean publishToMd = false;
        for(String key: dataKeys){
            JSONObject cryptocurrency = data.getJSONObject(key);
            JSONObject quotes = cryptocurrency.getJSONObject("quotes");
            JSONObject usdQuote = quotes.getJSONObject("USD");
            JSONObject eurQuote = quotes.getJSONObject("EUR");
            Integer rank = cryptocurrency.getInt("rank");
            String name = cryptocurrency.getString("name");
            String symbol = cryptocurrency.getString("symbol");
            Double usdPrice = usdQuote.getDouble("price");
            Double eurPrice = eurQuote.getDouble("price");
            CryptocurrencyInfo cryptocurrencyInfo = new CryptocurrencyInfo(name, symbol, usdPrice, eurPrice);
            if(currentSymbolCryptocurrencies.containsKey(rank)){
                if(!currentSymbolCryptocurrencies.get(rank).equals(cryptocurrencyInfo)){
                    publishToMd = true;
                    currentSymbolCryptocurrencies.put(rank, cryptocurrencyInfo);
                }
            } else {
                publishToMd = true;
                currentSymbolCryptocurrencies.put(rank, cryptocurrencyInfo);
            }
        }
        if(publishToMd){
            publishMessageToMd();
        }
    }

    private void publishMessageToMd(){
        JSONArray mdContent = new JSONArray();
        Set<Integer> keys = currentSymbolCryptocurrencies.keySet();
        List<Integer> keyList = new ArrayList<>(keys);
        Collections.sort(keyList);
        for(Integer key: keyList){
            CryptocurrencyInfo cryptocurrencyInfo = currentSymbolCryptocurrencies.get(key);
            JSONObject cryptocurrencyResult = new JSONObject();
            try {
                Field map = cryptocurrencyResult.getClass().getDeclaredField("map");
                map.setAccessible(true);
                map.set(cryptocurrencyResult, new LinkedHashMap<>());
                map.setAccessible(false);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                LOGGER.warn(e.getMessage());
            }
            cryptocurrencyResult.put("name", cryptocurrencyInfo.getName());
            cryptocurrencyResult.put("symbol", cryptocurrencyInfo.getSymbol());
            cryptocurrencyResult.put("USD_price", cryptocurrencyInfo.getUsdPrice());
            cryptocurrencyResult.put("EUR_price", cryptocurrencyInfo.getEurPrice());
            mdContent.put(cryptocurrencyResult);
        }
        String mdContentString = mdContent.toString();
        migratoryDataClient.publish(new MigratoryDataMessage(migratoryDataSubject, mdContentString.getBytes()));
    }
}

package com.real.time.publisher.cryptocurrency;

import java.util.Objects;

public class CryptocurrencyInfo {

    private String name;
    private String symbol;
    private Double usdPrice;
    private Double eurPrice;

    public CryptocurrencyInfo(String name, String symbol, Double usdPrice, Double eurPrice) {
        this.name = name;
        this.symbol = symbol;
        this.usdPrice = usdPrice;
        this.eurPrice = eurPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Double getUsdPrice() {
        return usdPrice;
    }

    public void setUsdPrice(Double usdPrice) {
        this.usdPrice = usdPrice;
    }

    public Double getEurPrice() {
        return eurPrice;
    }

    public void setEurPrice(Double eurPrice) {
        this.eurPrice = eurPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CryptocurrencyInfo that = (CryptocurrencyInfo) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(symbol, that.symbol) &&
                Objects.equals(usdPrice, that.usdPrice) &&
                Objects.equals(eurPrice, that.eurPrice);
    }

    @Override
    public String toString() {
        return "CryptocurrencyInfo{" +
                "name='" + name + '\'' +
                ", symbol='" + symbol + '\'' +
                ", usdPrice=" + usdPrice +
                ", eurPrice=" + eurPrice +
                '}';
    }
}

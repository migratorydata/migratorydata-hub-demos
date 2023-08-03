package com.real.time.publisher.jcdecaux.bikes;

import java.util.Objects;

public class BikeStation {
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
    private String paymentTerminal;
    private int bikeStands;
    private int availableBikeStands;
    private int availableBikes;
    private String status;

    public BikeStation(String name, String address, Double latitude, Double longitude, String paymentTerminal,
                       int bikeStands, int availableBikeStands, int availableBikes, String status) {
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.paymentTerminal = paymentTerminal;
        this.bikeStands = bikeStands;
        this.availableBikeStands = availableBikeStands;
        this.availableBikes = availableBikes;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String isPaymentTerminal() {
        return paymentTerminal;
    }

    public void setPaymentTerminal(String paymentTerminal) {
        this.paymentTerminal = paymentTerminal;
    }

    public int getBikeStands() {
        return bikeStands;
    }

    public void setBikeStands(int bikeStands) {
        this.bikeStands = bikeStands;
    }

    public int getAvailableBikeStands() {
        return availableBikeStands;
    }

    public void setAvailableBikeStands(int availableBikeStands) {
        this.availableBikeStands = availableBikeStands;
    }

    public int getAvailableBikes() {
        return availableBikes;
    }

    public void setAvailableBikes(int availableBikes) {
        this.availableBikes = availableBikes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BikeStation that = (BikeStation) o;
        return paymentTerminal == that.paymentTerminal &&
                bikeStands == that.bikeStands &&
                availableBikeStands == that.availableBikeStands &&
                availableBikes == that.availableBikes &&
                Objects.equals(name, that.name) &&
                Objects.equals(address, that.address) &&
                Objects.equals(latitude, that.latitude) &&
                Objects.equals(longitude, that.longitude) &&
                Objects.equals(status, that.status);
    }

    @Override
    public String toString() {
        return "BikeStation{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", paymentTerminal=" + paymentTerminal +
                ", bikeStands=" + bikeStands +
                ", availableBikeStands=" + availableBikeStands +
                ", availableBikes=" + availableBikes +
                ", status='" + status + '\'' +
                '}';
    }
}

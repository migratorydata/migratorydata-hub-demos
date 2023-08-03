package com.real.time.publisher.amsterdam.parkings;

import java.util.Objects;

public class ParkingInfo {
    private String name;
    private Double latitude;
    private Double longitude;
    private String state;
    private String freeSpaceShort;
    private String freeSpaceLong;
    private String shortCapacity;
    private String longCapacity;

    public ParkingInfo(String name, Double latitude, Double longitude, String state,
                       String freeSpaceShort, String freeSpaceLong, String shortCapacity, String longCapacity) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.state = state;
        this.freeSpaceShort = freeSpaceShort;
        this.freeSpaceLong = freeSpaceLong;
        this.shortCapacity = shortCapacity;
        this.longCapacity = longCapacity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getFreeSpaceShort() {
        return freeSpaceShort;
    }

    public void setFreeSpaceShort(String freeSpaceShort) {
        this.freeSpaceShort = freeSpaceShort;
    }

    public String getFreeSpaceLong() {
        return freeSpaceLong;
    }

    public void setFreeSpaceLong(String freeSpaceLong) {
        this.freeSpaceLong = freeSpaceLong;
    }

    public String getShortCapacity() {
        return shortCapacity;
    }

    public void setShortCapacity(String shortCapacity) {
        this.shortCapacity = shortCapacity;
    }

    public String getLongCapacity() {
        return longCapacity;
    }

    public void setLongCapacity(String longCapacity) {
        this.longCapacity = longCapacity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParkingInfo that = (ParkingInfo) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(latitude, that.latitude) &&
                Objects.equals(longitude, that.longitude) &&
                Objects.equals(state, that.state) &&
                Objects.equals(freeSpaceShort, that.freeSpaceShort) &&
                Objects.equals(freeSpaceLong, that.freeSpaceLong) &&
                Objects.equals(shortCapacity, that.shortCapacity) &&
                Objects.equals(longCapacity, that.longCapacity);
    }

    @Override
    public String toString() {
        return "ParkingInfo{" +
                "name='" + name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", state='" + state + '\'' +
                ", freeSpaceShort='" + freeSpaceShort + '\'' +
                ", freeSpaceLong='" + freeSpaceLong + '\'' +
                ", shortCapacity='" + shortCapacity + '\'' +
                ", longCapacity='" + longCapacity + '\'' +
                '}';
    }
}

package com.real.time.publisher.brussels.traffic;

import java.util.Objects;

public class IntervalInfo {
    private int count;
    private Double speed;
    private Double occupancy;
    private String startTime;
    private String endTime;

    public IntervalInfo(int count, Double speed, Double occupancy, String startTime, String endTime) {
        this.count = count;
        this.speed = speed;
        this.occupancy = occupancy;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Double getOccupancy() {
        return occupancy;
    }

    public void setOccupancy(Double occupancy) {
        this.occupancy = occupancy;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntervalInfo that = (IntervalInfo) o;
        return count == that.count &&
                Objects.equals(speed, that.speed) &&
                Objects.equals(occupancy, that.occupancy) &&
                Objects.equals(startTime, that.startTime) &&
                Objects.equals(endTime, that.endTime);
    }

    @Override
    public String toString() {
        return "IntervalInfo{" +
                "count=" + count +
                ", speed=" + speed +
                ", occupancy=" + occupancy +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}

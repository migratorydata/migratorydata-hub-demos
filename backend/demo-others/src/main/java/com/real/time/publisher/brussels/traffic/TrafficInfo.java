package com.real.time.publisher.brussels.traffic;

import java.util.Objects;

public class TrafficInfo {
    private String traverseName;
    private IntervalInfo oneMinute = null;
    private IntervalInfo fiveMinutes = null;
    private IntervalInfo fifteenMinutes = null;
    private IntervalInfo sixtyMinutes = null;

    public TrafficInfo(String traverseName) {
        this.traverseName = traverseName;
    }

    public String getTraverseName() {
        return traverseName;
    }

    public void setTraverseName(String traverseName) {
        this.traverseName = traverseName;
    }

    public IntervalInfo getOneMinute() {
        return oneMinute;
    }

    public void setOneMinute(IntervalInfo oneMinute) {
        this.oneMinute = oneMinute;
    }

    public IntervalInfo getFiveMinutes() {
        return fiveMinutes;
    }

    public void setFiveMinutes(IntervalInfo fiveMinutes) {
        this.fiveMinutes = fiveMinutes;
    }

    public IntervalInfo getFifteenMinutes() {
        return fifteenMinutes;
    }

    public void setFifteenMinutes(IntervalInfo fifteenMinutes) {
        this.fifteenMinutes = fifteenMinutes;
    }

    public IntervalInfo getSixtyMinutes() {
        return sixtyMinutes;
    }

    public void setSixtyMinutes(IntervalInfo sixtyMinutes) {
        this.sixtyMinutes = sixtyMinutes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrafficInfo that = (TrafficInfo) o;
        return Objects.equals(traverseName, that.traverseName) &&
                Objects.equals(oneMinute, that.oneMinute) &&
                Objects.equals(fiveMinutes, that.fiveMinutes) &&
                Objects.equals(fifteenMinutes, that.fifteenMinutes) &&
                Objects.equals(sixtyMinutes, that.sixtyMinutes);
    }

    @Override
    public String toString() {
        return "TrafficInfo{" +
                "traverseName='" + traverseName + '\'' +
                ", oneMinute=" + oneMinute +
                ", fiveMinutes=" + fiveMinutes +
                ", fifteenMinutes=" + fifteenMinutes +
                ", sixty=" + sixtyMinutes +
                '}';
    }
}

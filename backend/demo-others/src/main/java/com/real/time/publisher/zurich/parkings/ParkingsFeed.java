package com.real.time.publisher.zurich.parkings;

import java.util.ArrayList;
import java.util.List;

public class ParkingsFeed {

    private String title;
    private String link;
    private String description;
    private String copyright;

    private List<ParkingInfo> entries = new ArrayList<>();

    public ParkingsFeed(String title, String link, String description, String copyright) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.copyright = copyright;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public String getCopyright() {
        return copyright;
    }

    public List<ParkingInfo> getEntries() {
        return entries;
    }

    @Override
    public String toString() {
        return "ParkingsFeed{" +
                "title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", description='" + description + '\'' +
                ", copyright='" + copyright + '\'' +
                ", entries=" + entries +
                '}';
    }
}

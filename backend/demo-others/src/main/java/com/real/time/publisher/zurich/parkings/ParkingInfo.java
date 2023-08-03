package com.real.time.publisher.zurich.parkings;

import java.util.Objects;

public class ParkingInfo {
    private String title;
    private String link;
    private String description;
    private String status = "";
    private String freeSpaces = "";

    public ParkingInfo(String title, String link, String description) {
        this.title = title;
        this.link = link;
        this.description = description;
        String[] descriptionParts = description.split("/");
        if(descriptionParts.length == 2) {
            status = descriptionParts[0].trim();
            freeSpaces = descriptionParts[1].trim();
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public String getFreeSpaces() {
        return freeSpaces;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParkingInfo that = (ParkingInfo) o;
        return Objects.equals(title, that.title) &&
                Objects.equals(link, that.link) &&
                Objects.equals(description, that.description) &&
                Objects.equals(status, that.status) &&
                Objects.equals(freeSpaces, that.freeSpaces);
    }

    @Override
    public String toString() {
        return "ParkingInfo{" +
                "title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", freeSpaces='" + freeSpaces + '\'' +
                '}';
    }
}

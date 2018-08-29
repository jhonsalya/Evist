package com.example.jhonsalya.evist.Model;

public class Event {
    private String name, location, price;
    private int thumbnail;

    public Event() {
    }

    public Event(String name, String location, String price, int thumbnail) {
        this.name = name;
        this.location = location;
        this.price = price;
        this.thumbnail = thumbnail;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}

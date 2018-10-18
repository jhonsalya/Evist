package com.example.jhonsalya.evist.Model;

public class Event {
    private String title, location, price;
    private String image;

    public Event() {
    }

    public Event(String title, String location, String price, String image) {
        this.title = title;
        this.location = location;
        this.price = price;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

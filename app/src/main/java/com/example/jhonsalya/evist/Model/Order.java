package com.example.jhonsalya.evist.Model;

/**
 * Created by jhonsalya on 18/11/18.
 */

public class Order {
    private String EventID;
    private String EventName;
    private String EventAddress;
    private String EventStartDate;
    private String EventStartTime;
    private String Quantity;
    private String Price;

    public Order() {
    }

    public Order(String eventID, String eventName, String eventAddress, String eventStartDate, String eventStartTime, String quantity, String price) {
        EventID = eventID;
        EventName = eventName;
        EventAddress = eventAddress;
        EventStartDate = eventStartDate;
        EventStartTime = eventStartTime;
        Quantity = quantity;
        Price = price;
    }

    public String getEventID() {
        return EventID;
    }

    public void setEventID(String eventID) {
        EventID = eventID;
    }

    public String getEventName() {
        return EventName;
    }

    public void setEventName(String eventName) {
        EventName = eventName;
    }

    public String getEventAddress() {
        return EventAddress;
    }

    public void setEventAddress(String eventAddress) {
        EventAddress = eventAddress;
    }

    public String getEventStartDate() {
        return EventStartDate;
    }

    public void setEventStartDate(String eventStartDate) {
        EventStartDate = eventStartDate;
    }

    public String getEventStartTime() {
        return EventStartTime;
    }

    public void setEventStartTime(String eventStartTime) {
        EventStartTime = eventStartTime;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }
}

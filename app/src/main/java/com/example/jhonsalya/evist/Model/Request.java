package com.example.jhonsalya.evist.Model;

import java.util.List;

/**
 * Created by jhonsalya on 19/11/18.
 */

public class Request {
    private String total;
    private List<Order> event;
    private String eventAddress;
    private String eventID;
    private String eventName;
    private String eventStartDate;
    private String eventStartTime;
    private String price;
    private String quantity;
    private String sellerid;
    private String buyerid;

    public Request() {
    }

    public Request(String total, String eventAddress, String eventID, String eventName, String eventStartDate, String eventStartTime, String price, String quantity, String sellerid, String buyerid) {
        this.total = total;
        this.eventAddress = eventAddress;
        this.eventID = eventID;
        this.eventName = eventName;
        this.eventStartDate = eventStartDate;
        this.eventStartTime = eventStartTime;
        this.price = price;
        this.quantity = quantity;
        this.sellerid = sellerid;
        this.buyerid = buyerid;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Order> getEvent() {
        return event;
    }

    public void setEvent(List<Order> event) {
        this.event = event;
    }

    public String getEventAddress() {
        return eventAddress;
    }

    public void setEventAddress(String eventAddress) {
        this.eventAddress = eventAddress;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventStartDate() {
        return eventStartDate;
    }

    public void setEventStartDate(String eventStartDate) {
        this.eventStartDate = eventStartDate;
    }

    public String getEventStartTime() {
        return eventStartTime;
    }

    public void setEventStartTime(String eventStartTime) {
        this.eventStartTime = eventStartTime;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getSellerid() {
        return sellerid;
    }

    public void setSellerid(String sellerid) {
        this.sellerid = sellerid;
    }

    public String getBuyerid() {
        return buyerid;
    }

    public void setBuyerid(String buyerid) {
        this.buyerid = buyerid;
    }
}

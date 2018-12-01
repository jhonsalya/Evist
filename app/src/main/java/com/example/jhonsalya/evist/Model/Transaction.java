package com.example.jhonsalya.evist.Model;

/**
 * Created by jhonsalya on 24/11/18.
 */

public class Transaction {
    private String buyerid;
    private String total;
    private String status;
    private String eventAddress;
    private String eventID;
    private String eventName;
    private String eventStartDate;
    private String eventStartTime;
    private String price;
    private String quantity;

    public Transaction() {
    }

    public Transaction(String buyerid, String total, String status, String eventAddress, String eventID, String eventName, String eventStartDate, String eventStartTime, String price, String quantity) {
        this.buyerid = buyerid;
        this.total = total;
        this.status = status;
        this.eventAddress = eventAddress;
        this.eventID = eventID;
        this.eventName = eventName;
        this.eventStartDate = eventStartDate;
        this.eventStartTime = eventStartTime;
        this.price = price;
        this.quantity = quantity;
    }

    public String getBuyerid() {
        return buyerid;
    }

    public void setBuyerid(String buyerid) {
        this.buyerid = buyerid;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
}

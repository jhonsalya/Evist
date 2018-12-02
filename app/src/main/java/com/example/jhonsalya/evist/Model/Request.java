package com.example.jhonsalya.evist.Model;

import java.util.List;

/**
 * Created by jhonsalya on 19/11/18.
 */

public class Request {
    private String total;
    private List<Order> event;
    private String address;
    private String id;
    private String name;
    private String startdate;
    private String starttime;
    private String price;
    private String quantity;
    private String sellerid;
    private String buyerid;

    public Request() {
    }

    public Request(String total, String address, String id, String name, String startdate, String starttime, String price, String quantity, String sellerid, String buyerid) {
        this.total = total;
        this.address = address;
        this.id = id;
        this.name = name;
        this.startdate = startdate;
        this.starttime = starttime;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String eventAddress) {
        this.address = eventAddress;
    }

    public String getId() {
        return id;
    }

    public void setId(String eventID) {
        this.id = eventID;
    }

    public String getName() {
        return name;
    }

    public void setName(String eventName) {
        this.name = eventName;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String eventStartDate) {
        this.startdate = eventStartDate;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String eventStartTime) {
        this.starttime = eventStartTime;
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

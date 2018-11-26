package com.example.jhonsalya.evist.Model;

import java.util.List;

/**
 * Created by jhonsalya on 19/11/18.
 */

public class Request {
    private String total;
    private List<Order> event;
    private String sellerid;
    private String buyerid;

    public Request() {
    }

    public Request(String total, List<Order> event, String buyerid) {
        this.total = total;
        this.event = event;
        this.buyerid = buyerid;
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
}

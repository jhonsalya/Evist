package com.example.jhonsalya.evist.Model;

import java.util.List;

/**
 * Created by jhonsalya on 19/11/18.
 */

public class Request {
    private String total;
    private List<Order> event;

    public Request() {
    }

    public Request(String total, List<Order> event) {
        this.total = total;
        this.event = event;
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

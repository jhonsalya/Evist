package com.example.jhonsalya.evist.Model;

/**
 * Created by jhonsalya on 24/11/18.
 */

public class Transaction {

    private String address;
    private String buyerid;
    private String id;
    private String name;
    private String price;
    private String quantity;
    private String startdate;
    private String starttime;
    private String total;

    private String bankaccount;
    private String bankaccountNumber;
    private String bankaccountName;

    private String statusbuyer;
    private String statusseller;

    public Transaction() {
    }

    public Transaction(String address, String buyerid, String id, String name, String price, String quantity, String startdate, String starttime, String total) {
        this.address = address;
        this.buyerid = buyerid;
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.startdate = startdate;
        this.starttime = starttime;
        this.total = total;
    }

    public Transaction(String address, String buyerid, String id, String name, String price, String quantity, String startdate, String starttime, String total, String bankaccount, String bankaccountNumber, String bankaccountName) {
        this.address = address;
        this.buyerid = buyerid;
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.startdate = startdate;
        this.starttime = starttime;
        this.total = total;
        this.bankaccount = bankaccount;
        this.bankaccountNumber = bankaccountNumber;
        this.bankaccountName = bankaccountName;
    }

    public String getBankaccount() {
        return bankaccount;
    }

    public void setBankaccount(String bankaccount) {
        this.bankaccount = bankaccount;
    }

    public String getBankaccountNumber() {
        return bankaccountNumber;
    }

    public void setBankaccountNumber(String bankaccountNumber) {
        this.bankaccountNumber = bankaccountNumber;
    }

    public String getBankaccountName() {
        return bankaccountName;
    }

    public void setBankaccountName(String bankaccountName) {
        this.bankaccountName = bankaccountName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBuyerid() {
        return buyerid;
    }

    public void setBuyerid(String buyerid) {
        this.buyerid = buyerid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getStatusbuyer() {
        return statusbuyer;
    }

    public void setStatusbuyer(String statusbuyer) {
        this.statusbuyer = statusbuyer;
    }

    public String getStatusseller() {
        return statusseller;
    }

    public void setStatusseller(String statusseller) {
        this.statusseller = statusseller;
    }
}

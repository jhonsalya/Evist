package com.example.jhonsalya.evist.Model;

public class Event {
    private String title, location, price;
    private String image;
    private String category, contact, description, finish_date, organizer, participant, start_date, start_time, target_age, uid;

    public Event() {
    }

    public Event(String title, String location, String price, String image, String category, String contact, String description, String finish_date, String organizer, String participant, String start_date, String start_time, String target_age, String uid) {
        this.title = title;
        this.location = location;
        this.price = price;
        this.image = image;
        this.category = category;
        this.contact = contact;
        this.description = description;
        this.finish_date = finish_date;
        this.organizer = organizer;
        this.participant = participant;
        this.start_date = start_date;
        this.start_time = start_time;
        this.target_age = target_age;
        this.uid = uid;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFinish_date() {
        return finish_date;
    }

    public void setFinish_date(String finish_date) {
        this.finish_date = finish_date;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public String getParticipant() {
        return participant;
    }

    public void setParticipant(String participant) {
        this.participant = participant;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getTarget_age() {
        return target_age;
    }

    public void setTarget_age(String target_age) {
        this.target_age = target_age;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

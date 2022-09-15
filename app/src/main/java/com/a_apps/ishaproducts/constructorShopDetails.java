package com.a_apps.ishaproducts;

public class constructorShopDetails {

    private String name,place;
    private String pending;
    private String phoneNumber;

    public constructorShopDetails () {

        this.name = name;
        this.place = place;
        this.phoneNumber = phoneNumber;
        this.pending=pending;
        // This is default constructor.
    }

    public String getShopName() {

        return name;
    }

    public void setShopName(String name) {

        this.name = name;
    }


    public String getShopPlace() {

        return place;
    }

    public void setShopPlace(String place) {

        this.place = place;
    }

    public String getPhoneNumber() {

        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {

        this.phoneNumber = phoneNumber;
    }

    public String getShopPending() {

        return pending;
    }

    public void setShopPending(String pending) {

        this.pending = pending;
    }

}



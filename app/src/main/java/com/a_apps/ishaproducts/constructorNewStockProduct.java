package com.a_apps.ishaproducts;

public class constructorNewStockProduct {

    String NSProduct, NSQnty;


    public constructorNewStockProduct(String NSQnty, String NSProduct) {


        this.NSQnty = NSQnty;
        this.NSProduct = NSProduct;


    }

    public String getNSQnty(){
        return NSQnty;

    }
    public void setNSQnty(String NSQnty){
        this.NSQnty = NSQnty;
    }


    public String getNSProduct(){
        return NSProduct;

    }
    public void setNSProduct(String NSProduct){
        this.NSProduct = NSProduct;
    }
}
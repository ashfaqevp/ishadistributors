package com.a_apps.ishaproducts;

public class constructorNewBill {



    String NSProduct, NSQnty,NSPrice,NSAmount;


    public constructorNewBill(String NSQnty, String NSProduct,String NSPrice,String NSAmount) {


        this.NSQnty = NSQnty;
        this.NSProduct = NSProduct;
        this.NSPrice=NSPrice;
        this.NSAmount=NSAmount;


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


    public String getNSPrice(){
        return NSPrice;

    }
    public void setNSPrice(String NSPrice){
        this.NSPrice = NSPrice;
    }








    public String getNSAmount(){
        return NSAmount;

    }
    public void setNSAmount(String NSAmount){
        this.NSAmount = NSAmount;
    }






}

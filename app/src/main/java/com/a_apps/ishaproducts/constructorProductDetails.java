package com.a_apps.ishaproducts;

public class constructorProductDetails {
    private String name;
    private String price;
    private String wholesale;

    public constructorProductDetails(){

        this.name = name;
        this.price= price;
        this.wholesale=wholesale;

    }


    public String getProductName() {

        return name;
    }

    public void setProductName(String name) {

        this.name = name;
    }


    public String getProductPrice() {

        return price;
    }

    public void setProductPrice(String price) {

        this.price = price;
    }


    public String getProductWholesale() {

        return wholesale;
    }

    public void setProductWholesale(String wholesale) {

        this.wholesale = wholesale;
    }
}

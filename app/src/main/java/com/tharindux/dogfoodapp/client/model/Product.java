package com.tharindux.dogfoodapp.client.model;

public class Product {

    private String name;
    private int qty;
    private double price;

    private String description;

    private String PicName;


    public Product(){

    }
    public Product( String name, int qty, double price, String description,String PicName) {
        this.name = name;
        this.qty = qty;
        this.price = price;
        this.description = description;
        this.PicName = PicName;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public String getPicName() {
        return PicName;
    }

    public void setPicName(String picName) {
        PicName = picName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

//    public static ArrayList<Product> getSampleProductList(){
//
//        ArrayList<Product> getSampleProductList= new ArrayList<>();
//        getSampleProductList.add(new Product("chipps",10,150.00,"this is good product"));
//        getSampleProductList.add(new Product("cola",5,100.00,"this is good product"));
//        getSampleProductList.add(new Product("bugger",8,250.00,"this is good product"));
//        return getSampleProductList;
//    }
}

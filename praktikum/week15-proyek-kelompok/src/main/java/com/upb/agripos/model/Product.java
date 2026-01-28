package com.upb.agripos.model;
public class Product {
    private String code, name; private double price; private int stock;
    public Product(String c,String n,double p,int s){code=c;name=n;price=p;stock=s;}
    public String getCode(){return code;} public String getName(){return name;}
    public double getPrice(){return price;} public int getStock(){return stock;}
}

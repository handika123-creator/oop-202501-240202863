package com.upb.agripos.model;
public class User {
    private String username, role;
    public User(String u,String r){username=u;role=r;}
    public String getUsername(){return username;}
    public String getRole(){return role;}
}

package com.example.martin.mangerresturant;

public class user_info {
   private String fname;
   private String lname;
   private String address;
   private String phone;



    public user_info() {
    }

    public user_info(String fname, String lname, String address, String phone) {
        this.fname = fname;
        this.lname = lname;
        this.address = address;
        this.phone = phone;
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }
}

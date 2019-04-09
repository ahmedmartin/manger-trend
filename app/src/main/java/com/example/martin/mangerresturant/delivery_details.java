package com.example.martin.mangerresturant;

public class delivery_details {

    private String phone ;
    private String address;
    private String ssn ;


    public delivery_details(String phone, String address, String ssn) {
        this.phone = phone;
        this.address = address;
        this.ssn = ssn;
    }

    public delivery_details() {
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getSsn() {
        return ssn;
    }
}

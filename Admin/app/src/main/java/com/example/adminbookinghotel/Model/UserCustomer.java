package com.example.adminbookinghotel.Model;

public class UserCustomer {
    private String email, phone, name;
    private boolean status;

    public UserCustomer() {
    }

    public UserCustomer(String email, String phone, String name, boolean status) {
        this.email = email;
        this.phone = phone;
        this.name = name;
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}

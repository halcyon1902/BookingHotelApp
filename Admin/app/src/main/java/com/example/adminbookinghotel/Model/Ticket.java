package com.example.adminbookinghotel.Model;

import java.io.Serializable;

public class Ticket implements Serializable {
    private String datecome, dateleave, typeroom, email, name, phone, staydate, amount, currentdate;
    private boolean status;

    public Ticket() {
    }

    public Ticket(String datecome, String dateleave, String typeroom, String email, String name, String phone, String staydate, String amount, String currentdate, boolean status) {
        this.datecome = datecome;
        this.dateleave = dateleave;
        this.typeroom = typeroom;
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.staydate = staydate;
        this.amount = amount;
        this.currentdate = currentdate;
        this.status = status;
    }

    public String getDatecome() {
        return datecome;
    }

    public void setDatecome(String datecome) {
        this.datecome = datecome;
    }

    public String getDateleave() {
        return dateleave;
    }

    public void setDateleave(String dateleave) {
        this.dateleave = dateleave;
    }

    public String getTyperoom() {
        return typeroom;
    }

    public void setTyperoom(String typeroom) {
        this.typeroom = typeroom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStaydate() {
        return staydate;
    }

    public void setStaydate(String staydate) {
        this.staydate = staydate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrentdate() {
        return currentdate;
    }

    public void setCurrentdate(String currentdate) {
        this.currentdate = currentdate;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}

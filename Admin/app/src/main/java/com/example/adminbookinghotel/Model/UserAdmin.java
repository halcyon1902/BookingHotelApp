package com.example.adminbookinghotel.Model;

public class UserAdmin {
    private String email, phone, name,image, permission;
    private boolean status;

    public UserAdmin() {
    }

    public UserAdmin(String email, String phone, String name, String image, String permission, boolean status) {
        this.email = email;
        this.phone = phone;
        this.name = name;
        this.image = image;
        this.permission = permission;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}

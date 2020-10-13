package com.coderpakistan.learningbank.HelperClasses;

public class Session {
    private String id;
    private String email,name,phone,image;

    public Session(String id, String email, String name, String phone, String image) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }
}

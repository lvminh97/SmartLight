package com.example.smartlight.models;

public class User {
    private int id;
    private String fullname;
    private String mobile;
    private String email;
    private boolean appControl;

    public User(int id, String fullname, String mobile, String email, boolean appControl) {
        this.id = id;
        this.fullname = fullname;
        this.mobile = mobile;
        this.email = email;
        this.appControl = appControl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAppControl() {
        return appControl;
    }

    public void setAppControl(boolean appControl) {
        this.appControl = appControl;
    }
}

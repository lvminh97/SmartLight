package com.example.smartlight.models;

import android.graphics.drawable.Drawable;

import com.example.smartlight.Factory;

public class Room {
    private int id;
    private Drawable img;
    private String name;

    public Room(int id, Drawable img, String name) {
        this.id = id;
        this.img = img;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Drawable getImg() {
        return img;
    }

    public void setImg(Drawable img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return Factory.engToVi.getOrDefault(name, name);
    }
}

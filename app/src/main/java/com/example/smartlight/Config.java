package com.example.smartlight;

import com.example.smartlight.models.Device;
import com.example.smartlight.models.Room;
import com.example.smartlight.models.User;

import java.util.List;

public class Config {

    public static boolean debug = true;

    public static String HOST = "https://smartlight02.000webhostapp.com";
    public static User user = null;
    public static List<Room> roomList = null;
    public static Room room = null;
    public static Device device = null;
}

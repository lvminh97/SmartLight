package com.example.smartlight;

import android.util.DisplayMetrics;

import com.example.smartlight.models.Device;
import com.example.smartlight.models.Room;
import com.example.smartlight.models.Type;
import com.example.smartlight.models.User;

import java.util.List;

public class Factory {

    public static String version = "1.0.7";
    public static boolean debug = true;
    public static DisplayMetrics displayMetrics = null;

    public static String HOST = "";

    public static User user = null;
    public static List<Room> roomList = null;
    public static Room room = null;
    public static List<Device> deviceList = null;
    public static Device device = null;

    public static List<Type> types = null;
}

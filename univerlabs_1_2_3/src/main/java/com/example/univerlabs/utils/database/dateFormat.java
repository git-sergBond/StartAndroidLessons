package com.example.univerlabs.utils.database;

import java.util.Date;
import java.text.SimpleDateFormat;

public final class dateFormat {
    public static String getCurrentTime() {
        SimpleDateFormat iso8601format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return iso8601format.format(new Date());
    }
}

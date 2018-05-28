package com.ibamb.udm.log;

import android.util.Log;

public class UdmLog {

    public  static void e(String tag, String message) {
        if (message != null && message.trim().length() > 0) {
            Log.e(tag,message);
        }
    }
}

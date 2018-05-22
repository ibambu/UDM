package com.ibamb.udm.core;

import java.util.Arrays;

public class TryUser {
    private static String[] TRY_USER;

    public static String[] getUser(int index) {
        if (TRY_USER != null && TRY_USER.length >= index * 2) {
            return Arrays.copyOfRange(TRY_USER, index * 2 - 2, index * 2);
        } else {
            return null;
        }
    }

    public static void setTryUser(String[] tryUser) {
        if(tryUser!=null){
            TRY_USER = Arrays.copyOfRange(tryUser,0,tryUser.length);
        }
    }

    public static int getUserCount() {
        return TRY_USER == null ? 0 : TRY_USER.length / 2;
    }
}

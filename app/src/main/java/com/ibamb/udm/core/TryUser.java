package com.ibamb.udm.core;

import java.util.Arrays;

public class TryUser {
    private static String [] TRY_USER ;

    public static String[] getUser(int index){
        if(TRY_USER.length>index*2){
            return Arrays.copyOfRange(TRY_USER,index*2-2,index*2);
        }else{
            return null;
        }
    }

    public static void setTryUser(String[] tryUser) {
        TRY_USER = tryUser;
    }

    public static int getUserCount(){
        return TRY_USER.length/2;
    }
}

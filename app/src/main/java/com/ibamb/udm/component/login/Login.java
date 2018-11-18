package com.ibamb.udm.component.login;

import android.util.Base64;

import com.ibamb.dnet.module.api.UdmClient;
import com.ibamb.dnet.module.log.UdmLog;
import com.ibamb.dnet.module.security.DefualtECryptValue;

import java.io.UnsupportedEncodingException;

public class Login {

    /**
     *
     * @param userName 登录设备的用户名
     * @param password 登录设备的密码
     * @param devMac   目标设备MAC
     * @param ip       目标设备IP，可选参数。
     * @return  登录成功返回TRUE，登录失败返回FALSE。
     */
    public static boolean  login(String userName, String password, String devMac,String ip){
        boolean isSuccessful = false;
        /**
         * 将用户名和密码采用BASE64加密并转成字节数组。注意用户名和密码之间要留一个空格，加密时需将空格和用户名一起加密。
         */
        try {
            String enUserName = Base64.encodeToString((userName + " ").getBytes(DefualtECryptValue.CHARSET), Base64.NO_WRAP);
            String enPassword = Base64.encodeToString(password.getBytes(DefualtECryptValue.CHARSET), Base64.NO_WRAP);
            isSuccessful = UdmClient.getInstance().login(enUserName,enPassword,devMac,ip);
        } catch (UnsupportedEncodingException e) {
            UdmLog.error(e);
        }
        return isSuccessful;
    }
}

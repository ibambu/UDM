package com.ibamb.udm.constants;

/**
 * Created by luotao on 18-3-27.
 */

public class UdmControl {

    //The command type is list below:
    public static final int GET_PARAMETERS = 0x81;
    public static final int SET_PARAMETERS = 0xC3;
    public static final int AUTH_USER = 0xC5;
    public static final int REBOOT_SYSTEM = 0x87;
    public static final int LOGOUT = 0xC9;
    public static final int UDPDATE_FIRMWARE = 0x8B;

    //The response type is list below:
    public static final byte ACKNOWLEDGE = 0x00;
    public static final byte NO_PERMISSION = 0x02;
    public static final byte OPTION_NOT_SUPPORT = 0x04;
    public static final byte VALUE_INVALID = 0x06;
    public static final byte AUTH_FAIL = 0x08;
}

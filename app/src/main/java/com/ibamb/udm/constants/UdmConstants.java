package com.ibamb.udm.constants;

/**
 * Created by luotao on 18-3-26.
 */
public class UdmConstants {

    //UDP DATA MODE
    public static final String UDP_DATA_MODE_UNI = "1";
    public static final String UDP_DATA_MODE_MUL = "2";
    //TCP WORK MODE
    public static final String TCP_WORK_MODE_CLIENT = "1";
    public static final String TCP_WORK_MODE_SERVER = "2";
    public static final String TCP_WORK_MODE_BOTH = "3";

    //NET CONNECT PROTOCOL
    public static final String CONN_NET_PROTOCOL_TCP = "1";
    public static final String CONN_NET_PROTOCOL_UDP = "2";
    public static final String CONN_NET_PROTOCOL_BOTH = "3";
    //CONN UDP ACCEPTION
    public static final String CONN_UDP_ACCEPTION_ON = "1";
    public static final String CONN_UDP_ACCEPTION_OFF = "0";

    public static final int UDM_CONTROL_LENGTH = 1;
    public static final int UDM_ID_LENGTH = 1;
    public static final int UDM_MAIN_FRAME_LENGTH = 2;
    public static final int UDM_IP_LENGTH = 4;
    public static final int UDM_MAC_LENGTH = 8;

    public static final int UDM_TYPE_LENGTH = 2;
    public static final int UDM_SUB_FRAME_LENGTH = 1;

    //UDP读写操作的端口号
    public static final int UDM_UDP_SERVER_PORT = 5000;
    //UDP广播地址
    public static final String UDM_BROADCAST_IP = "255.255.255.255";
    //登录成功返回值
    public static final int UDM_LOGIN_SUCCESS = 4;
    //读参数
    public static final int UDM_PARAM_READ = 0;
    //写参数
    public static final int UDM_PARAM_WRITE = 1;

}

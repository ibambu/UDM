package com.ibamb.udm.module.constants;

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
    public static final String CONN_NET_PROTOCOL_TCP = "TCP";
    public static final String CONN_NET_PROTOCOL_UDP = "UDP";
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

    //界面控件类型常量
    public static final int UDM_UI_SPECIAL = 0;
    public static final int UDM_UI_EDIT_TEXT = 1;
    public static final int UDM_UI_UDMSPINNER = 2;
    public static final int UDM_UI_SWITCH = 3;
    public static final int UDM_UI_BUTTON_TEXT = 4;


    //连接设置界面初始化时默认通道
    public static final String UDM_DEFAULT_CHNL = "1";
    public static final String UDM_IP_SETTING_CHNL = "0";

    public static final String UDM_SWITCH_ON = "1";
    public static final String UDM_SWITCH_OFF = "0";


    public static final int UDM_PARAM_TYPE_NUMBER = 1;
    public static final int UDM_PARAM_TYPE_IPADDR = 2;
    public static final int UDM_PARAM_TYPE_TIME = 3;
    public static final int UDM_PARAM_TYPE_CHAR = 4;
    //预留帐号文件
    public static final String TRY_USER_FILE = "tryusr";

    public static final int TASK_BAR_COLOR = 0xFB303132;

    public static final String WAIT_READ_PARAM=" loading...";
}

package com.ibamb.udm.module.constants;


public class Constants {

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
    //参数ID映射关系
    public static final String FILE_PARAM_MAPPING = "conextop-parameter-mapping.txt";
    public static final String FILE_PARAM_MAPPING_COLUMN_SPLIT = "#";
    public static final String DEFAULT_CHARSET = "UTF-8";
    public static final String FILE_ENUM_VALUE_SPLIT = ",";
    public static final String FILE_LOG_NAME = "udmlog.log";
    public static final int TASK_BAR_COLOR = 0xFB303132;

    public static final String WAIT_READ_PARAM = " loading...";

    public static final int RET_SUCCESS = 1;
    public static final int RET_FAIL = 0;

    //不同界面标题
    public static final String TITLE_IP_SETTING = "IP Setting";
    public static final String TITLE_ACCESS_SETTING = "Access Setting";
    public static final String TITLE_DEVICE_PROFILE = "Device Profile";
    public static final String TITLE_OTHER_SETTING = "Other Setting";
    public static final String TITLE_SERIAL_SETTING = "Serial Setting";
    public static final String TITLE_TCP_CONNECTION = "TCP Connection";
    public static final String TITLE_UDP_CONNECTION = "UDP Connection";
    public static final String TITLE_USER_PROFILE = "User Profile";


    public static final String INFO_SUCCESS = "successful";
    public static final String INFO_FAIL = "fail";
    public static final String INFO_READ_PARAM_FAIL = "Possible network delay. Please click title try again.";
    public static final String INFO_SEARCH_PROGRESS = "searching...";
    public static final String INFO_SEARCH_FAIL = "Possible network delay. Please try again.";
    public static final String INFO_LOGIN_FAIL = "login fail,try again.";

    public static final int FLAG_SPECIALLY_SEARCH = 1;
}

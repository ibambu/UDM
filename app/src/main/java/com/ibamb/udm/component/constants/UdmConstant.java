package com.ibamb.udm.component.constants;

public class UdmConstant {
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


    //预留帐号文件
    public static final String TRY_USER_FILE = "tryusr";
    //参数ID映射关系
    public static final String FILE_PARAM_MAPPING = "conextop-parameter-mapping.txt";
    public static final String FILE_PARAM_MAPPING_COLUMN_SPLIT = "#";
    //参数同步日志文件
    public static final String FILE_SYNC_TO_OTH_DEVICE_LOG = "device_sync.log";
    //Guide config file
    public static final String FILE_GUIDE_CONF = "guide_config.txt";

    //APP 运行错误日志
    public static final String FILE_UDM_RUN_ERR_LOG = "run_err.log";

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
    public static final String TITLE_DEVICE_UPGRADE = "Device Upgrade";


    public static final String INFO_SUCCESS = "successful";
    public static final String INFO_FAIL = "fail";
    public static final String INFO_READ_PARAM_FAIL = "Possible network delay. Please click title try again.";
    public static final String INFO_SEARCH_PROGRESS = "searching...";
    public static final String INFO_SEARCH_FAIL = "Device not found, Please try again.";
    public static final String INFO_LOGIN_FAIL = "login fail,try again.";

    public static final int FLAG_SPECIALLY_SEARCH = 1;
    public static final int FLAG_SCAN_QR_CODE = -1;

    public static final int MAX_CHANNEL = 33;

    public static final int ACTIVITY_RESULT_FOR_LOGIN = 10;
    public static final int ACTIVITY_RESULT_FOR_IMP_TYPE_FILE = 11;//导入TYPE ID 配置文件。

    //参数同步提示语
    public static final String SYNC_SUCCESS = "success";
    public static final String SYNC_NO_PERMISSION = "no permission";
    public static final String SYNC_OPTION_NOT_SUPPORT = "option not support";
    public static final String SYNC_NO_DATA_REPLY = "no-response";
    public static final String SYNC_VALUE_INVALID = "value invalid";
    public static final String SYNC_UNKNOWN_ERROR = "unknown error";
    public static final String SYNC_SAVE_REBOOT_FAIL = "save&reboot fail";
    public static final String SYNC_AUTH_FAIL = "auth fail";
}

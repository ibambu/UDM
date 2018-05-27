package com.ibamb.udm.module.instruct.beans;

/**
 * Created by luotao on 18-3-18.
 */

public class ChannelParamsID {

    public static String[] getTcpParamsId(String channelId) {
        String[] ids = {
                "CONN" + channelId + "_NET_PROTOCOL",
                "CONN" + channelId + "_TCP_WORK_MODE",
                "CONN" + channelId + "_TCP_CONN_RESPONS",
                "CONN" + channelId + "_TCP_HOST_IP0",
                "CONN" + channelId + "_TCP_HOST_PORT0",
                "CONN" + channelId + "_TCP_LOCAL_PORT",
                "UART" + channelId + "_STOPBIT",
                "UART" + channelId + "_DATABIT",
                "UART" + channelId + "_BDRATE",
                "UART" + channelId + "_PARITY",
                "UART" + channelId + "_FLOWRNT",
                "UART" + channelId + "_IDLE_TIME_PACKINGR",
                "CONN" + channelId + "_LINK_LATCH_TIMEOUT"
        };
        return ids;
    }

    public static String[] getUdpParamsId(String channelId) {
        String[] ids = {
                "CONN" + channelId + "_NET_PROTOCOL",
                "CONN" + channelId + "_UDP_ACCEPTION",
                "CONN" + channelId + "_UDP_DATA_MODE",
                "CONN" + channelId + "_UDP_TMP_HOST_EN",
                "CONN" + channelId + "_UDP_UNI_LOCAL_PORT",
                "CONN" + channelId + "_UDP_UNI_HOST_IP0",
                "CONN" + channelId + "_UDP_UNI_HOST_PORT0",
                "UART" + channelId + "_BDRATE",
                "UART" + channelId + "_DATABIT",
                "UART" + channelId + "_STOPBIT",
                "UART" + channelId + "_FLOWRNT",
                "UART" + channelId + "_PARITY",
                "UART" + channelId + "_IDLE_TIME_PACKINGR",
                "CONN" + channelId + "_LINK_LATCH_TIMEOUT",
                "CONN" + channelId + "_UDP_MUL_LOCAL_PORT",
                "CONN" + channelId + "_UDP_MUL_REMOTE_PORT",
                "CONN" + channelId + "_UDP_MUL_REMOTE_IP"
        };
        return ids;
    }
}

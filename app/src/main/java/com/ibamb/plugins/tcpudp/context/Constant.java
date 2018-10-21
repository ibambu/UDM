package com.ibamb.plugins.tcpudp.context;

public class Constant {
    public static final String CONN_TCP_SERVER = "A";
    public static final String CONN_TCP_CLIENT = "B";
    public static final String CONN_UDP_UNICAST_CLIENT = "CC";
    public static final String CONN_UDP_UNICAST_SERVER = "CS";
    public static final String CONN_UDP_MULCAST = "D";
    public static final String CONN_UDP_BROADCAST = "E";
    public static final String DEFAULT_CHARSET = "UTF-8";

    public static final int CONN_PROTOCOL_TCP = 1;
    public static final int CONN_PROTOCOL_UDP = 2;
    public static final int CONN_ROLE_CLIENT = 1;
    public static final int CONN_ROLE_SERVER = 2;
    public static final int CONN_UDP_TRANS_UNICAST = 1;
    public static final int CONN_UDP_TRANS_MULCAST = 2;
    public static final int CONN_UDP_TRANS_BROADCAST = 3;
}

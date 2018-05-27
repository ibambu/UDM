package com.ibamb.udm.module.net;

import java.net.DatagramSocket;

/**
 * Created by luotao on 18-4-21.
 */

public class UdmDatagramSocket {
    public static DatagramSocket datagramSocket;
    public static DatagramSocket searchSocekt;
    public static DatagramSocket getDatagramSocket() {
        return datagramSocket;
    }

    public static void setDatagramSocket(DatagramSocket datagramSocket) {
        UdmDatagramSocket.datagramSocket = datagramSocket;
    }

    public static DatagramSocket getSearchSocekt() {
        return searchSocekt;
    }

    public static void setSearchSocekt(DatagramSocket searchSocekt) {
        UdmDatagramSocket.searchSocekt = searchSocekt;
    }
}

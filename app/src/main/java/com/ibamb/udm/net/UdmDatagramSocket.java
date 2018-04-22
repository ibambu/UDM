package com.ibamb.udm.net;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.net.DatagramSocket;
import java.net.SocketException;

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

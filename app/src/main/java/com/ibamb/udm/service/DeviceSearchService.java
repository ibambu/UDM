package com.ibamb.udm.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class DeviceSearchService extends Service {

    public DeviceSearchServiceBinder searchServiceBinder = new DeviceSearchServiceBinder();

    public DeviceSearchService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return searchServiceBinder;
    }

    public class DeviceSearchServiceBinder extends Binder {
        /**
         * search device list by udp broadcast.
         *
         * @param broadcastAddress
         * @return
         */
        public ArrayList<InetAddress> searchDeviceByBroadcast(InetAddress broadcastAddress) {
            ArrayList inetAddresses = new ArrayList();
            try {
                inetAddresses.add(InetAddress.getByName("192.168.1.100"));
                inetAddresses.add(InetAddress.getByName("192.168.1.102"));
                inetAddresses.add(InetAddress.getByName("192.168.1.104"));
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            return inetAddresses;
        }
    }
}

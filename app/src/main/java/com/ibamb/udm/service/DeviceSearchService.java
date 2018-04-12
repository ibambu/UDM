package com.ibamb.udm.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;

import com.ibamb.udm.beans.DeviceInfo;
import com.ibamb.udm.search.DeviceSearch;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import static com.ibamb.udm.search.DeviceSearch.searchDevice;

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
        @RequiresApi(api = Build.VERSION_CODES.O)
        public ArrayList<DeviceInfo> searchDeviceByBroadcast(InetAddress broadcastAddress) {

            return searchDevice();
        }
    }
}

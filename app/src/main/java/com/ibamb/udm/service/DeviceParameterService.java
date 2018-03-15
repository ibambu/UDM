package com.ibamb.udm.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.ibamb.udm.beans.DeviceParameter;
import com.ibamb.udm.beans.TCPChannelParameter;
import com.ibamb.udm.beans.UDPChannelParameter;
import com.ibamb.udm.instruct.IParser;
import com.ibamb.udm.instruct.beans.ReplyFrame;
import com.ibamb.udm.instruct.impl.ReplyFrameParser;

public class DeviceParameterService extends Service {
    private DeviceParameterServiceBinder paramServiceBinder = new DeviceParameterServiceBinder();

    public DeviceParameterService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
       return paramServiceBinder;
    }

    public class DeviceParameterServiceBinder extends Binder {

        public TCPChannelParameter readTCPChannelParameter(String channelId,String mac){
            IParser parser = new ReplyFrameParser();
            return null;
        }
        public UDPChannelParameter readUDPChannelParameter(String channelId,String mac){
            return null;
        }

        public TCPChannelParameter writeTCPChannelParameter(TCPChannelParameter tcpChannelParameter){
            return null;
        }

        public UDPChannelParameter writeUDPChannelParameter(UDPChannelParameter udpChannelParameter){
            return null;
        }
    }
}

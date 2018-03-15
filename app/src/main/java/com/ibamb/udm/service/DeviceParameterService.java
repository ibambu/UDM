package com.ibamb.udm.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.ibamb.udm.beans.DeviceParameter;
import com.ibamb.udm.beans.TCPChannelParameter;
import com.ibamb.udm.beans.UDPChannelParameter;
import com.ibamb.udm.instruct.IParser;
import com.ibamb.udm.instruct.beans.InstructFrame;
import com.ibamb.udm.instruct.beans.ReplyFrame;
import com.ibamb.udm.instruct.impl.ReplyFrameParser;
import com.ibamb.udm.net.UDPSender;

import java.util.List;

public class DeviceParameterService extends Service {
    private DeviceParameterServiceBinder paramServiceBinder = new DeviceParameterServiceBinder();

    public DeviceParameterService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
       return paramServiceBinder;
    }

    public class DeviceParameterServiceBinder extends Binder {

        public TCPChannelParameter readTCPChannelParameter(String mac,String channelId,List<String> paramIds){
            UDPSender sender = new UDPSender();
            List<InstructFrame> instructFrames = null;//from paramIds
            List<ReplyFrame> replyFrames = sender.sendInstruct(mac,instructFrames);
            TCPChannelParameter tcpChannelParameter = null;//from replyFrames
            return tcpChannelParameter;
        }
        public UDPChannelParameter readUDPChannelParameter(String mac,String channelId,List<String> paramIds){
            UDPSender sender = new UDPSender();
            List<InstructFrame> instructFrames = null;//from paramIds
            List<ReplyFrame> replyFrames = sender.sendInstruct(mac,instructFrames);
            UDPChannelParameter udpChannelParameter = null;//from replyFrames
            return udpChannelParameter;
        }

        public TCPChannelParameter writeTCPChannelParameter(String mac,TCPChannelParameter tcpChannelParameter){
            UDPSender sender = new UDPSender();
            /**
             * 根据将界面参数生成指令
             */
            List<InstructFrame> instructFrames = null;//from tcpChannelParameter
            /**
             * 发送指令
             */
            List<ReplyFrame> replyFrames = sender.sendInstruct(mac,instructFrames);
            /**
             * 解析返回帧,读取返回的最新参数设置.
             */
            tcpChannelParameter = null;
            return tcpChannelParameter;
        }

        public UDPChannelParameter writeUDPChannelParameter(String mac,UDPChannelParameter udpChannelParameter){
            UDPSender sender = new UDPSender();
            /**
             * 根据将界面参数生成指令
             */
            List<InstructFrame> instructFrames = null;//from udpChannelParameter
            /**
             * 发送指令
             */
            List<ReplyFrame> replyFrames = sender.sendInstruct(mac,instructFrames);
            /**
             * 解析返回帧,读取返回的最新参数设置.
             */
            udpChannelParameter = null;
            return udpChannelParameter;
        }
    }
}

package com.ibamb.udm.module.core;

import com.ibamb.udm.module.beans.DeviceInfo;

import java.util.ArrayList;

public class ContextData {
    private static ContextData contextData;
    private static ArrayList<DeviceInfo> deviceInfos;

    public static synchronized ContextData getInstance(){
        if(contextData==null){
            contextData = new ContextData();
            deviceInfos = new ArrayList<>();
        }
        return contextData;
    }

    public  synchronized ArrayList<DeviceInfo> getDataInfos(){
        return deviceInfos;
    }

    public synchronized void addDevice(DeviceInfo deviceInfo){
        if(deviceInfo!=null){
            boolean isExists = false;
            for(DeviceInfo device:deviceInfos){
                if(device.getMac().equalsIgnoreCase(deviceInfo.getMac())){
                    isExists = true;
                    break;
                }
            }
            if(!isExists){
                deviceInfos.add(deviceInfo);
            }
        }
    }

    public synchronized void cleanDeviceList(){
            deviceInfos.clear();
    }

    public synchronized void addAllDevice(ArrayList<DeviceInfo> deviceInfos){
        deviceInfos.addAll(deviceInfos);
    }

    public synchronized void removeDevice(DeviceInfo deviceInfo){
        if(deviceInfo!=null){
            for(int i=0;i<deviceInfos.size();i++){
                DeviceInfo device = deviceInfos.get(i);
                if(device.getMac().equalsIgnoreCase(deviceInfo.getMac())){
                    deviceInfos.remove(i);
                    break;
                }
            }
        }
    }
}

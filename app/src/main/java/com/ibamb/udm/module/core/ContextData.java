package com.ibamb.udm.module.core;

import com.ibamb.udm.module.beans.DeviceInfo;
import com.ibamb.udm.module.beans.ParameterItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContextData {
    private static ContextData contextData;
    private static ArrayList<DeviceInfo> deviceInfos;
    private static Map<String,List<ParameterItem>> changedParamMap;//有改动的参数列表

    public List<ParameterItem> getChangedParam(String mac){
        return changedParamMap.get(mac);
    }

    public void addChangedParam(String mac,ParameterItem parameterItem){
        List<ParameterItem> parameterItems = changedParamMap.get(mac);
        if(parameterItems== null){
            parameterItems = new ArrayList<>();
        }
        boolean isExists = false;//注意避免重复添加改动参数。
        for(ParameterItem item:parameterItems){
            if(item.getParamId().equals(parameterItem.getParamId())){
                item.setParamValue(parameterItem.getParamValue());
                item.setDisplayValue(parameterItem.getDisplayValue());
                isExists = true;
                break;
            }
        }
        if(!isExists){
            parameterItems.add(parameterItem);
        }
    }

    public static synchronized ContextData getInstance(){
        if(contextData==null){
            contextData = new ContextData();
            deviceInfos = new ArrayList<>();
            changedParamMap = new HashMap<>();
        }
        return contextData;
    }

    public synchronized  void cleanChecked(){
        for(DeviceInfo device:deviceInfos){
            device.setChecked(false);
        }
    }

    public synchronized int getCheckedItems(){
        int count =0;
        for(DeviceInfo device:deviceInfos){
            if(device.isChecked()){
                count ++;
            }
        }
        return count;
    }

    public synchronized boolean isCheckAll(){
        return getCheckedItems()== deviceInfos.size();
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

    public synchronized void addAllDevice(ArrayList<DeviceInfo> deviceInfoList){
        deviceInfos.addAll(deviceInfoList);
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

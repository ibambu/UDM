package com.ibamb.udm.module.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luotao on 18-3-16.
 */

public class ChannelParameter {


    private boolean isSuccessful;
    private String mac;
    private String ip;
    private String channelId;
    private List<ParameterItem> paramItems;

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }

    public void pinrtParam(){
        for(ParameterItem item:paramItems){
            System.out.println(item.toString());
        }
    }

    public String getParamValueById(String paramId){
        String value="";
        for(ParameterItem item:paramItems){
            if(paramId.equals(item.getParamId())){
                value = item.getParamValue();
                break;
            }
        }
        return value;
    }

    public void updateParamValueById(String paramId,String value){
        boolean isExist = false;
        for(ParameterItem item:paramItems){
            if(paramId.equals(item.getParamId())){
                item.setParamValue(value);
                isExist = true;
                break;
            }
        }
        if(!isExist){
            paramItems.add(new ParameterItem(paramId,value));
        }
    }
    public ChannelParameter(){

    }
    public ChannelParameter(String mac,String channelId){
        this.mac = mac;
        this.channelId = channelId;
        paramItems = new ArrayList<>();
    }

    public ChannelParameter(String mac, String ip, String channelId) {
        this.mac = mac;
        this.ip = ip;
        this.channelId = channelId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public List<ParameterItem> getParamItems() {
        return paramItems;
    }

    public void setParamItems(List<ParameterItem> paramItems) {
        this.paramItems = paramItems;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }
}
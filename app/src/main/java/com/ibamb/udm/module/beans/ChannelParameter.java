package com.ibamb.udm.module.beans;

import com.ibamb.udm.module.constants.Control;

import java.util.ArrayList;
import java.util.List;


public class ChannelParameter {


    private boolean isSuccessful;//读取参数值是否成功返回数据
    private String mac;
    private String ip;
    private String channelId;
    private List<ParameterItem> paramItems;
    private int resultCode;

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    private boolean isNoPermission;//是否禁止访问，一般在没有登录的情况下访问设备返回。

    public boolean isNoPermission() {
        return resultCode== Control.NO_PERMISSION;
    }

    public void setNoPermission(boolean noPermission) {
        isNoPermission = noPermission;
    }

    public boolean isSuccessful() {
        return resultCode==Control.ACKNOWLEDGE;
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

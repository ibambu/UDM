package com.ibamb.udm.module.beans;

public class DeviceSyncMessage {
    private int index;
    private String ip;
    private String mac;
    private String resultInfo;

    public String getResultInfo() {
        return resultInfo;
    }

    public void setResultInfo(String resultInfo) {
        this.resultInfo = resultInfo;
    }

    public DeviceSyncMessage(String ip, String mac) {
        this.ip = ip;
        this.mac = mac;
    }

    public DeviceSyncMessage(int index, String ip, String mac) {
        this.index = index;
        this.ip = ip;
        this.mac = mac;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }
}

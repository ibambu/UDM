/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ibamb.udm.beans;

/**
 *
 * @author Luo Tao
 */
public class DeviceInfo {
    private String ip;
    private String mac;
    private int index;

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

    public DeviceInfo(String ip, String mac) {
        this.ip = ip;
        this.mac = mac;
    }

    @Override
    public String toString() {
        return "DeviceInfo{" + "ip=" + ip + ", mac=" + mac + '}';
    }
}


package com.ibamb.udm.module.beans;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeviceInfo that = (DeviceInfo) o;

        if (index != that.index) return false;
        if (!ip.equals(that.ip)) return false;
        return mac.equals(that.mac);
    }

    @Override
    public int hashCode() {
        int result = ip.hashCode();
        result = 31 * result + mac.hashCode();
        result = 31 * result + index;
        return result;
    }

    @Override
    public String toString() {
        return "DeviceInfo{" + "ip=" + ip + ", mac=" + mac + '}';
    }
}

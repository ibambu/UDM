
package com.ibamb.udm.module.beans;

import com.ibamb.udm.beans.Device;

public class DeviceModel {
    private String ip;
    private String mac;
    private String deviceName;
    private String firmwareVersion;
    private int index;
    private int upgradeProgress;
    private int upgradeCode;
    private boolean isChecked;

    public String getDeviceName() {
        return (deviceName==null||deviceName.trim().length()==0||deviceName.equalsIgnoreCase("null"))?"No device Name":deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getFirmwareVersion() {
        return (firmwareVersion==null||firmwareVersion.equalsIgnoreCase("null")
                ||firmwareVersion.trim().length()==0)?"unknown version":firmwareVersion;
    }

    public void setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getUpgradeCode() {
        return upgradeCode;
    }

    public void setUpgradeCode(int upgradeCode) {
        this.upgradeCode = upgradeCode;
    }

    public int getUpgradeProgress() {
        return upgradeProgress;
    }

    public void setUpgradeProgress(int upgradeProgress) {
        this.upgradeProgress = upgradeProgress;
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

    public DeviceModel(String ip, String mac) {
        this.ip = ip;
        this.mac = mac;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeviceModel that = (DeviceModel) o;

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
        return "DeviceInfo{" +
                "ip='" + ip + '\'' +
                ", mac='" + mac + '\'' +
                ", deviceName='" + deviceName + '\'' +
                ", firmwareVersion='" + firmwareVersion + '\'' +
                ", index=" + index +
                ", upgradeProgress=" + upgradeProgress +
                ", upgradeCode=" + upgradeCode +
                ", isChecked=" + isChecked +
                '}';
    }

    public Device toDevice(){
        Device device = new Device();
        device.setIndex(this.index);
        device.setDeviceName(this.deviceName);
        device.setIp(this.ip);
        device.setMac(this.mac);
        device.setFirmwareVersion(this.firmwareVersion);
        return device;
    }
}

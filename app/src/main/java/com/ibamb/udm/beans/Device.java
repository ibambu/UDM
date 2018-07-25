package com.ibamb.udm.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.ibamb.udm.module.beans.DeviceModel;

public class Device implements Parcelable {
    private int index;//序号，如 001,002,003,...
    private String deviceName;//设备名称
    private String ip;//IP地址
    private String mac;//MAC 地址，格式 XX：XX：XX：XX：XX：XX
    private String firmwareVersion;//设备固件版本

    public static final Creator<Device> CREATOR = new Creator<Device>() {
        @Override
        public Device createFromParcel(Parcel in) {
            Device device = new Device();
            device.index = in.readInt();
            device.deviceName = in.readString();
            device.ip = in.readString();
            device.mac = in.readString();
            device.firmwareVersion = in.readString();
            return device;
        }

        @Override
        public Device[] newArray(int size) {
            return new Device[size];
        }
    };

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
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

    public String getFirmwareVersion() {
        return (firmwareVersion==null||firmwareVersion.equals("null")
                ||firmwareVersion.trim().length()==0)? "unknown version":firmwareVersion;
    }

    public void setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(index);
        dest.writeString(deviceName);
        dest.writeString(ip);
        dest.writeString(mac);
        dest.writeString(firmwareVersion);
    }

    @Override
    public String toString() {
        return  index +"#"+ deviceName +"#" + ip + "#" + mac + "#" +firmwareVersion;
    }

    public static Device toDevice(DeviceModel deviceModel){
        Device device = new Device();
        device.setIndex(deviceModel.getIndex());
        device.setDeviceName(deviceModel.getDeviceName());
        device.setIp(deviceModel.getIp());
        device.setMac(deviceModel.getMac());
        device.setFirmwareVersion(deviceModel.getFirmwareVersion());
        return device;
    }
}

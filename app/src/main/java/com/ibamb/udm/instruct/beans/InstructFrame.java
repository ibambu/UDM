package com.ibamb.udm.instruct.beans;

/**
 * Created by luotao on 18-3-13.
 */

public class InstructFrame {
    private byte control;// 1 octet
    private byte id;// 1 octet
    private byte[] length;// 2 octets
    private byte[] ip;// 4 octets
    private byte[] mac;// 8 octets
    private Information information;

    public InstructFrame(byte control,String mac,String type,String value){
        this.control = control;
        information = new Information(type,value);
    }

    public byte getControl() {
        return control;
    }

    public void setControl(byte control) {
        this.control = control;
    }

    public byte getId() {
        return id;
    }

    public void setId(byte id) {
        this.id = id;
    }

    public byte[] getLength() {
        return length;
    }

    public void setLength(byte[] length) {
        this.length = length;
    }

    public byte[] getIp() {
        return ip;
    }

    public void setIp(byte[] ip) {
        this.ip = ip;
    }

    public byte[] getMac() {
        return mac;
    }

    public void setMac(byte[] mac) {
        this.mac = mac;
    }

    public Information getInformation() {
        return information;
    }

    public void setInformation(Information information) {
        this.information = information;
    }
}

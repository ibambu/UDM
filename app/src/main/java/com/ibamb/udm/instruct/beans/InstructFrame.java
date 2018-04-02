package com.ibamb.udm.instruct.beans;

/**
 * Created by luotao on 18-3-13.
 */

public class InstructFrame {
    private int control;// 1 octet
    private int id;// 1 octet
    private int length;// 2 octets
    private String ip;// 4 octets
    private String mac;// 8 octets
    private Information information;

    public InstructFrame(int control,String mac,String type,String value){
        this.control = control;
        information = new Information(type,value);
    }

    public int getControl() {
        return control;
    }

    public void setControl(byte control) {
        this.control = control;
    }
    public Information getInformation() {
        return information;
    }

    public void setInformation(Information information) {
        this.information = information;
    }

    public void setControl(int control) {
        this.control = control;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
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

package com.ibamb.udm.instruct.beans;

/**
 * Created by luotao on 18-3-14.
 */

public class ReplyFrame {
    private byte control;// 1 octet
    private byte id;// 1 octet
    private byte[] length;// 2 octets
    private Information information;

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

    public Information getInformation() {
        return information;
    }

    public void setInformation(Information information) {
        this.information = information;
    }
}
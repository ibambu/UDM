package com.ibamb.udm.instruct.beans;

/**
 * Created by luotao on 18-3-14.
 */

public class Information {
    private byte[] type;// 2 octets
    private byte[] length;// 1 octet
    private byte[] data;// zero or more octets

    public byte[] getType() {
        return type;
    }

    public void setType(byte[] type) {
        this.type = type;
    }

    public byte[] getLength() {
        return length;
    }

    public void setLength(byte[] length) {
        this.length = length;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getTypeString(){
        return new String(type,0,type.length);
    }
    public String getDataString(){
        return new String(data,0,data.length);
    }
}

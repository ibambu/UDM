package com.ibamb.udm.instruct.beans;

/**
 * Created by luotao on 18-3-14.
 */

public class Information {
    private byte[] type;
    private byte[] length;
    private byte[] data;

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
}

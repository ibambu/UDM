package com.ibamb.udm.instruct.beans;

/**
 * Created by luotao on 18-3-14.
 */

public class Information {
    private String type;// 2 octets
    private int length;// 1 octet
    private String data;// zero or more octets

    public Information(String type,String value){
        this.type = type;
        this.data = value;
    }
    public Information(){

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Information{" + "type=" + type + ", length=" + length + ", data=" + data + '}';
    }
    
}

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
}

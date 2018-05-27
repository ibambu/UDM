package com.ibamb.udm.module.instruct.beans;

import java.util.List;

/**
 * Created by luotao on 18-3-14.
 */

public class ReplyFrame {
    private int control;// 1 octet
    private int id;// 1 octet
    private int length;// 2 octets
    private Information information;
    private List<Information> infoList;

    public List<Information> getInfoList() {
        return infoList;
    }

    public void setInfoList(List<Information> infoList) {
        this.infoList = infoList;
    }

    public int getControl() {
        return control;
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

    public Information getInformation() {
        return information;
    }

    public void setInformation(Information information) {
        this.information = information;
    }
}

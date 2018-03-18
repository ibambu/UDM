package com.ibamb.udm.beans;

/**
 * Created by luotao on 18-2-4.
 */

public class ParameterItem {
    private String paramId;
    private String paramValue;
    private String remoteParamId;

    public ParameterItem(String paramId, String paramValue, String remoteParamId) {
        this.paramId = paramId;
        this.paramValue = paramValue;
        this.remoteParamId = remoteParamId;
    }

    public ParameterItem(String paramId,String paramValue) {
        this.paramId = paramId;
        this.paramValue = paramValue;
    }

    public String getRemoteParamId() {
        return remoteParamId;
    }

    public void setRemoteParamId(String remoteParamId) {
        this.remoteParamId = remoteParamId;
    }

    public String getParamId() {
        return paramId;
    }

    public void setParamId(String paramId) {
        this.paramId = paramId;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    @Override
    public String toString() {
        return "ParameterItem{" +
                "paramId='" + paramId + '\'' +
                ", paramValue='" + paramValue + '\'' +
                ", remoteParamId='" + remoteParamId + '\'' +
                '}';
    }
}

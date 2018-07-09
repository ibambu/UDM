package com.ibamb.udm.module.beans;


public class ParameterItem {
    private String paramId;
    private String paramValue;
    private String displayValue;

    public String getDisplayValue() {
        return displayValue;
    }

    public void setDisplayValue(String displayValue) {
        this.displayValue = displayValue;
    }



    public ParameterItem(String paramId,String paramValue) {
        this.paramId = paramId;
        this.paramValue = paramValue;
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
                ", displayValue='" + displayValue + '\'' +
                '}';
    }
}

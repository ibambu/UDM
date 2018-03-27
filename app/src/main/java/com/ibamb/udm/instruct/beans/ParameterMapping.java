package com.ibamb.udm.instruct.beans;

/**
 * Created by luotao on 18-3-27.
 */

public class ParameterMapping {
    private String paramId;
    private String displayParamId;
    private String paramValue;
    private int dexId;
    private int hexId;
    private byte length;
    private String convertType;
    private ParameterValueMapping valueMapping;

    public String getConvertType() {
        return convertType;
    }

    public void setConvertType(String convertType) {
        this.convertType = convertType;
    }

    public String getParamId() {
        return paramId;
    }

    public void setParamId(String paramId) {
        this.paramId = paramId;
    }

    public String getDisplayParamId() {
        return displayParamId;
    }

    public void setDisplayParamId(String displayParamId) {
        this.displayParamId = displayParamId;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public int getDexId() {
        return dexId;
    }

    public void setDexId(int dexId) {
        this.dexId = dexId;
    }

    public int getHexId() {
        return hexId;
    }

    public void setHexId(int hexId) {
        this.hexId = hexId;
    }

    public byte getLength() {
        return length;
    }

    public void setLength(byte length) {
        this.length = length;
    }

    public ParameterValueMapping getValueMapping() {
        return valueMapping;
    }

    public void setValueMapping(ParameterValueMapping valueMapping) {
        this.valueMapping = valueMapping;
    }
}

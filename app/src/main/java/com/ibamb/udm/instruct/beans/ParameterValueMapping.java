package com.ibamb.udm.instruct.beans;

/**
 * Created by luotao on 18-3-27.
 */

public class ParameterValueMapping {
    private String paramId;
    private String value;
    private String displayValue;

    public ParameterValueMapping(String paramId, String value, String displayValue) {
        this.paramId = paramId;
        this.value = value;
        this.displayValue = displayValue;
    }

    public ParameterValueMapping() {

    }

    public String getParamId() {
        return paramId;
    }

    public void setParamId(String paramId) {
        this.paramId = paramId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDisplayValue() {
        return displayValue;
    }

    public void setDisplayValue(String displayValue) {
        this.displayValue = displayValue;
    }
}

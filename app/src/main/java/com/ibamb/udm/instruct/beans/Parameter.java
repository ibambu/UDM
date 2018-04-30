package com.ibamb.udm.instruct.beans;


import java.util.List;

/**
 * Created by luotao on 18-3-28.
 */

public class Parameter {
    private int channelId;
    private String id;
    private int decId;
    private int hexId;
    private int byteLength;
    // UI component ID
    private int viewId;

    private int covertType;

    private int elementType;
    private int paramType;
    private String viewTagId;

    public String getViewTagId() {
        return viewTagId;
    }

    public void setViewTagId(String viewTagId) {
        this.viewTagId = viewTagId;
    }

    public int getParamType() {
        return paramType;
    }

    public void setParamType(int paramType) {
        this.paramType = paramType;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public int getElementType() {
        return elementType;
    }

    public void setElementType(int elementType) {
        this.elementType = elementType;
    }

    public int getCovertType() {
        return covertType;
    }

    public void setCovertType(int covertType) {
        this.covertType = covertType;
    }
    
    
    private List<ValueMapping> valueMappings;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getDecId() {
        return decId;
    }

    public void setDecId(int decId) {
        this.decId = decId;
    }

    public int getHexId() {
        return hexId;
    }

    public void setHexId(int hexId) {
        this.hexId = hexId;
    }

    public int getByteLength() {
        return byteLength;
    }

    public void setByteLength(int byteLength) {
        this.byteLength = byteLength;
    }

    public int getViewId() {
        return viewId;
    }

    public void setViewId(int viewId) {
        this.viewId = viewId;
    }

    public List<ValueMapping> getValueMappings() {
        return valueMappings;
    }

    public void setValueMappings(List<ValueMapping> valueMappings) {
        this.valueMappings = valueMappings;
    }

    public String getDisplayValue(String value){
        String displayValue = value;
        for(ValueMapping mapping:valueMappings){
            if(mapping.getValue().equalsIgnoreCase(value)){
                displayValue = mapping.getDisplayValue();
                break;
            }
        }
        return displayValue;
    }

    public String getValue(String displayValue){
        String value = displayValue;
        for(ValueMapping mapping:valueMappings){
            if(mapping.getDisplayValue().equalsIgnoreCase(displayValue)){
                value = mapping.getValue();
                break;
            }
        }
        return value;
    }

    @Override
    public String toString() {
        return "Parameter{" + "id=" + id + ", decId=" + decId + ", hexId=" + hexId + ", byteLength=" + byteLength + ", viewId=" + viewId + ", covertType=" + covertType + ", valueMappings=" + valueMappings + '}';
    }
    
    
}

package com.ibamb.udm.instruct.beans;

import android.view.View;

import java.util.List;

/**
 * Created by luotao on 18-3-28.
 */

public class Parameter {
    private String id;
    private int decId;
    private int hexId;
    private int byteLength;
    // UI component ID
    private String viewId;

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

    public String getViewId() {
        return viewId;
    }

    public void setViewId(String viewId) {
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
}

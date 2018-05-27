package com.ibamb.udm.module.beans;

import java.util.List;

/**
 * Created by luotao on 18-2-4.
 */

public class UDPChannelParameter {
    private int channelId;

    private List<ParameterItem> baseParamItems;
    private List<ParameterItem> uniParamItems;
    private List<ParameterItem> multiParamItems;

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public List<ParameterItem> getBaseParamItems() {
        return baseParamItems;
    }

    public void setBaseParamItems(List<ParameterItem> baseParamItems) {
        this.baseParamItems = baseParamItems;
    }

    public List<ParameterItem> getUniParamItems() {
        return uniParamItems;
    }

    public void setUniParamItems(List<ParameterItem> uniParamItems) {
        this.uniParamItems = uniParamItems;
    }

    public List<ParameterItem> getMultiParamItems() {
        return multiParamItems;
    }

    public void setMultiParamItems(List<ParameterItem> multiParamItems) {
        this.multiParamItems = multiParamItems;
    }

    public ParameterItem getBaseParamItemById(String paramId) {
        ParameterItem parameterItem = null;
        for (ParameterItem item : baseParamItems) {
            if (item.getParamId().equals(paramId)) {
                parameterItem = item;
                break;
            }
        }
        return parameterItem;
    }

    public ParameterItem getUniParamItemById(String paramId) {
        ParameterItem parameterItem = null;
        for (ParameterItem item : uniParamItems) {
            if (item.getParamId().equals(paramId)) {
                parameterItem = item;
                break;
            }
        }
        return parameterItem;
    }

    public ParameterItem getMutiParamItemById(String paramId) {
        ParameterItem parameterItem = null;
        for (ParameterItem item : multiParamItems) {
            if (item.getParamId().equals(paramId)) {
                parameterItem = item;
                break;
            }
        }
        return parameterItem;
    }

    public void setBaseParamItemById(String paramId, String paramValue) {
        for (ParameterItem item : baseParamItems) {
            if (item.getParamId().equals(paramId)) {
                item.setParamValue(paramValue);
                break;
            }
        }

    }

    public void settUniParamItemById(String paramId, String paramValue) {
        for (ParameterItem item : uniParamItems) {
            if (item.getParamId().equals(paramId)) {
                item.setParamValue(paramValue);
                break;
            }
        }
    }

    public void setUniParamItemById(String paramId, String paramValue) {
        for (ParameterItem item : multiParamItems) {
            if (item.getParamId().equals(paramId)) {
                item.setParamValue(paramValue);
                break;
            }
        }
    }

}

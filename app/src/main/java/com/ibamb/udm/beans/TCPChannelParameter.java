package com.ibamb.udm.beans;

import java.util.List;

/**
 * Created by luotao on 18-2-4.
 */

public class TCPChannelParameter {
    private String channelId;

    private List<ParameterItem> paramItems;

    public String getChannelId() {

        return channelId;
    }

    public void setChannelId(String channelId)
    {
        this.channelId = channelId;
    }

    public List<ParameterItem> getParameterItems() {
        return paramItems;
    }

    public void setParameterItems(List<ParameterItem> parameterItems) {
        this.paramItems = parameterItems;
    }

    public ParameterItem getParamItemById(String paramId){
        ParameterItem parameterItem = null;
        for(ParameterItem item:paramItems){
            if(item.getParamId().equals(paramId)){
                parameterItem = item;
                break;
            }
        }
        return parameterItem;
    }

    public void setParamItemValue(String paramId,String paramValue){
        for(ParameterItem item:paramItems){
            if(item.getParamId().equals(paramId)){
                item.setParamValue(paramValue);
                break;
            }
        }
    }

    public void print(){
        for(ParameterItem item:paramItems){
           System.out.println(item.toString());
        }
    }
}

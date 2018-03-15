package com.ibamb.udm.beans;

import java.util.List;

/**
 * Created by luotao on 18-3-16.
 */

public class ChannelParameter {
    private String channelId;

    private List<ParameterItem> paramItems;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public List<ParameterItem> getParamItems() {
        return paramItems;
    }

    public void setParamItems(List<ParameterItem> paramItems) {
        this.paramItems = paramItems;
    }
}

package com.ibamb.udm.module.instruct;

import com.ibamb.udm.module.beans.ChannelParameter;


public interface IParamWriter {
    /**
     * 写参数值
     * @param channelParameter
     * @return
     */
    public ChannelParameter writeChannelParam(ChannelParameter channelParameter);
}

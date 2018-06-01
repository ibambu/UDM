package com.ibamb.udm.module.instruct;

import com.ibamb.udm.module.beans.ChannelParameter;


public interface IParamReader {
    /**
     * 读取参数值
     * @param channelParameter
     * @return
     */
    public ChannelParameter readChannelParam(ChannelParameter channelParameter);
}

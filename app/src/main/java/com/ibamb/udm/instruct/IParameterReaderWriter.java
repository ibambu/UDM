package com.ibamb.udm.instruct;

import com.ibamb.udm.beans.ChannelParameter;
import com.ibamb.udm.beans.TCPChannelParameter;
import com.ibamb.udm.beans.UDPChannelParameter;

import java.util.List;

/**
 * Created by luotao on 18-3-16.
 */

public interface IParameterReaderWriter {
    public  ChannelParameter readChannelParam(String channelId, String mac,String[] paramIds);
    public  ChannelParameter writeChannelParam(ChannelParameter channelParameter);
}

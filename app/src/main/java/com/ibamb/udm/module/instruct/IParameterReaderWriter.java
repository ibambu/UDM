package com.ibamb.udm.module.instruct;

import com.ibamb.udm.module.beans.ChannelParameter;
import java.net.DatagramSocket;

/**
 * Created by luotao on 18-3-16.
 */

public interface IParameterReaderWriter {
    public  ChannelParameter readChannelParam(DatagramSocket datagramSocket,ChannelParameter channelParameter);
    public  ChannelParameter writeChannelParam(DatagramSocket datagramSocket,ChannelParameter channelParameter);
}

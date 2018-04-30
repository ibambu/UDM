package com.ibamb.udm.instruct;

import com.ibamb.udm.beans.ChannelParameter;

import java.net.DatagramSocket;

/**
 * Created by luotao on 18-4-30.
 */

public interface IParamWriter {
    public ChannelParameter writeChannelParam(DatagramSocket datagramSocket, ChannelParameter channelParameter);
}

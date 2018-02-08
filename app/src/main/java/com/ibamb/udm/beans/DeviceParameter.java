package com.ibamb.udm.beans;

import java.util.List;

/**
 * Created by luotao on 18-2-4.
 */

public class DeviceParameter {
    private String ip;
    private String mac;
    private String netProtocol;
    private List<TCPChannelParameter> tcpChannelParamList;
    private List<UDPChannelParameter> udpChannelParamList;

    private TCPChannelParameter getTcpChannelParameters(String channelId) {
        TCPChannelParameter channelParam = null;
        for (TCPChannelParameter channelParameter : tcpChannelParamList) {
            if (channelParameter.getChannelId().equals(channelId)) {
                channelParam = channelParameter;
                break;
            }
        }
        return channelParam;
    }

    private UDPChannelParameter getUdpChannelParameters(int channelId) {
        UDPChannelParameter channelParam = null;
        for (UDPChannelParameter channelParameter : udpChannelParamList) {
            if (channelParameter.getChannelId() == channelId) {
                channelParam = channelParameter;
                break;
            }
        }
        return channelParam;
    }


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getNetProtocol() {
        return netProtocol;
    }

    public void setNetProtocol(String netProtocol) {
        this.netProtocol = netProtocol;
    }
}

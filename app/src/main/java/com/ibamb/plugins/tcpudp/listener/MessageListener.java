package com.ibamb.plugins.tcpudp.listener;

public interface MessageListener {
    public void onReceive(String hostAddress,byte[] message);
}

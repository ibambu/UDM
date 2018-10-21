package com.ibamb.plugins.tcpudp.context;

public class ConnectionContext {
    private TCPClient tcpClient;
    private TCPServer tcpServer;
    private UDPUnicastClient udpUnicastClient;
    private UDPUnicastServer udpUnicastServer;
    private UDPMulticast udpMulticast;
    private UDPBroadcast udpBroadcast;


    public void closeAll() {
        try {
            if (tcpServer != null) {
                tcpServer.close();
            }
            if (tcpClient != null) {
                tcpClient.close();
            }
            if (udpUnicastClient != null) {
                udpUnicastClient.close();
            }
            if (udpUnicastServer != null) {
                udpUnicastServer.close();
            }
            if (udpMulticast != null) {
                udpMulticast.close();
            }
            if (udpBroadcast != null) {
                udpBroadcast.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public UDPBroadcast getUdpBroadcast() {
        return udpBroadcast;
    }

    public void setUdpBroadcast(UDPBroadcast udpBroadcast) {
        this.udpBroadcast = udpBroadcast;
    }

    public UDPMulticast getUdpMulticast() {
        return udpMulticast;
    }

    public void setUdpMulticast(UDPMulticast udpMulticast) {
        this.udpMulticast = udpMulticast;
    }

    public TCPClient getTcpClient() {
        return tcpClient;
    }

    public void setTcpClient(TCPClient tcpClient) {
        this.tcpClient = tcpClient;
    }

    public TCPServer getTcpServer() {
        return tcpServer;
    }

    public void setTcpServer(TCPServer tcpServer) {
        this.tcpServer = tcpServer;
    }


    public UDPUnicastClient getUdpUnicastClient() {
        return udpUnicastClient;
    }

    public void setUdpUnicastClient(UDPUnicastClient udpUnicastClient) {
        this.udpUnicastClient = udpUnicastClient;
    }

    public UDPUnicastServer getUdpUnicastServer() {
        return udpUnicastServer;
    }

    public void setUdpUnicastServer(UDPUnicastServer udpUnicastServer) {
        this.udpUnicastServer = udpUnicastServer;
    }
}

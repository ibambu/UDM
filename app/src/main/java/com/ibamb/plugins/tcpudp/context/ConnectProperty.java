package com.ibamb.plugins.tcpudp.context;

public class ConnectProperty {
    private String tcpRemoteHost;
    private int tcpRemotePort;
    private int tcpLocalPort;

    private String udpUniRemoteHost;
    private int udpUniRemotePort;
    private int udpUniLocalPort;

    private String udpMulAddress;
    private int udpMulPort;

    private int udpBroadcastPort;

    private String connectType;
    private int workRole;

    public int getWorkRole() {
        return workRole;
    }

    public void setWorkRole(int workRole) {
        this.workRole = workRole;
    }

    public String getConnectType() {
        return connectType;
    }

    public void setConnectType(String connectType) {
        this.connectType = connectType;
    }

    public String getTcpRemoteHost() {
        return tcpRemoteHost;
    }

    public void setTcpRemoteHost(String tcpRemoteHost) {
        this.tcpRemoteHost = tcpRemoteHost;
    }

    public int getTcpRemotePort() {
        return tcpRemotePort;
    }

    public void setTcpRemotePort(String tcpRemotePort) {
        if (tcpRemotePort != null && tcpRemotePort.trim().length() != 0) {
            this.tcpRemotePort = Integer.parseInt(tcpRemotePort);
        }
    }

    public int getTcpLocalPort() {
        return tcpLocalPort;
    }

    public void setTcpLocalPort(String tcpLocalPort) {
        if (tcpLocalPort != null && tcpLocalPort.trim().length() != 0) {
            this.tcpLocalPort = Integer.parseInt(tcpLocalPort);
        }
    }

    public String getUdpUniRemoteHost() {
        return udpUniRemoteHost;
    }

    public void setUdpUniRemoteHost(String udpUniRemoteHost) {
        this.udpUniRemoteHost = udpUniRemoteHost;
    }

    public int getUdpUniRemotePort() {
        return udpUniRemotePort;
    }

    public void setUdpUniRemotePort(String udpUniRemotePort) {
        if (udpUniRemotePort != null && udpUniRemotePort.trim().length() != 0) {
            this.udpUniRemotePort = Integer.parseInt(udpUniRemotePort);
        }
    }

    public int getUdpUniLocalPort() {
        return udpUniLocalPort;
    }

    public void setUdpUniLocalPort(String udpUniLocalPort) {
        if (udpUniLocalPort != null && udpUniLocalPort.trim().length() > 0) {
            this.udpUniLocalPort = Integer.parseInt(udpUniLocalPort);
        }

    }

    public String getUdpMulAddress() {
        return udpMulAddress;
    }

    public void setUdpMulAddress(String udpMulAddress) {
        this.udpMulAddress = udpMulAddress;
    }

    public int getUdpMulPort() {
        return udpMulPort;
    }

    public void setUdpMulPort(String udpMulPort) {
        if (udpMulPort != null && udpMulPort.trim().length() > 0) {
            this.udpMulPort = Integer.parseInt(udpMulPort);
        }
    }

    public int getUdpBroadcastPort() {
        return udpBroadcastPort;
    }

    public void setUdpBroadcastPort(String udpBroadcastPort) {
        if (udpBroadcastPort != null && udpBroadcastPort.trim().length() > 0) {
            this.udpBroadcastPort = Integer.parseInt(udpBroadcastPort);
        }
    }
}

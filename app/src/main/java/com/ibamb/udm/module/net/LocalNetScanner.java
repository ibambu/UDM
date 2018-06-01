package com.ibamb.udm.module.net;


import com.ibamb.udm.log.UdmLog;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;


public class LocalNetScanner {
    public List<String> scanDevices(String wifiIp) {
        List<String> hostList = new ArrayList();//升级主机，通过扫描获得。
        try {

            Enumeration nis = NetworkInterface.getNetworkInterfaces();
            while (nis.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) nis.nextElement();
                List<InterfaceAddress> list = ni.getInterfaceAddresses();
                for (InterfaceAddress localAddress : list) {
                    InetAddress inetAddress = localAddress.getAddress();
                    if (inetAddress instanceof Inet4Address
                            && !inetAddress.isLoopbackAddress()
                            && inetAddress.isSiteLocalAddress()
                            && inetAddress.getHostAddress().equals(wifiIp)) {
                        String startIp = IPUtil.getBeginIpStr(inetAddress.getHostAddress(), String.valueOf(localAddress.getNetworkPrefixLength()));
                        String endIp = IPUtil.getEndIpStr(inetAddress.getHostAddress(), String.valueOf(localAddress.getNetworkPrefixLength()));
                        System.out.println(startIp + "--" + endIp);
                        List<String> ipList = IPUtil.getPossibleIP(startIp, endIp);
                        hostList.addAll(ipList);
                    }

                }
            }

        } catch (Exception e) {

        }
        return hostList;
    }

    /**
     * Find broadcast address by local ip.
     *
     * @param localIP
     * @return
     */
    public InetAddress findBroadCastAddress(String localIP) {
        InetAddress broadcastAddress = null;
        try {
            Enumeration nis = NetworkInterface.getNetworkInterfaces();
            while (nis.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) nis.nextElement();
                List<InterfaceAddress> list = ni.getInterfaceAddresses();
                boolean found = false;
                for (InterfaceAddress localAddress : list) {
                    InetAddress inetAddress = localAddress.getAddress();
                    if (inetAddress instanceof Inet4Address
                            && !inetAddress.isLoopbackAddress()
                            && inetAddress.isSiteLocalAddress()
                            && inetAddress.getHostAddress().equals(localIP)) {
                        broadcastAddress = localAddress.getBroadcast();
                        found = true;
                        break;
                    }
                }
                if (found) {
                    break;
                }
            }
        } catch (Exception e) {

        }
        return broadcastAddress;
    }
}

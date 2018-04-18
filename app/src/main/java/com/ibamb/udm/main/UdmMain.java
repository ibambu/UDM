/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ibamb.udm.main;

import com.ibamb.udm.beans.ChannelParameter;
import com.ibamb.udm.beans.ParameterItem;
import com.ibamb.udm.instruct.IParameterReaderWriter;
import com.ibamb.udm.instruct.impl.ParameterReaderWriter;
import com.ibamb.udm.net.LocalNetScanner;
import com.ibamb.udm.net.UDPMessageSender;
import com.ibamb.udm.security.UserAuth;
import com.ibamb.udm.util.DataTypeConvert;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author luotao
 */
public class UdmMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        InputStream in = null;
        Properties prop = new Properties();
        try {
            List<String> hexCmdList = new ArrayList();
            String confFile = "test.properties";
            /**
             * 加重配置文件 conf.properties
             */
            //2A-B2-BD-76-0D-95
            in = new BufferedInputStream(new FileInputStream(confFile));
            prop.load(new InputStreamReader(in, "UTF-8"));

            String userName = prop.getProperty("user");
            String localIp = prop.getProperty("local_ip");
            String password = prop.getProperty("password");
            String mac = prop.getProperty("remote_mac");
            System.out.println("login with:" + userName + "/" + password + "/" + mac);

            String channelId = "1";
            LocalNetScanner scanner = new LocalNetScanner();
            System.out.println("local ip:" + localIp);

            String broadcastIp = "255.255.255.255";//scanner.findBroadCastAddress(localIp).getHostAddress();
            System.out.println("broadcast ip:" + broadcastIp);
            System.out.println("start login ...");
            byte[] test1 = new byte[2];
            test1[0] = DataTypeConvert.intToUnsignedByte(0x30);
            test1[1] = DataTypeConvert.intToUnsignedByte(0x00);
            System.out.println("aaa:" + DataTypeConvert.bytes2int(test1));
            /**
             * search device
             */
            byte[] serachBytes = new byte[23];
            serachBytes[0] = DataTypeConvert.intToUnsignedByte(0x81);
            serachBytes[1] = DataTypeConvert.intToUnsignedByte(0xff);
            serachBytes[2] = DataTypeConvert.intToUnsignedByte(0x00);
            serachBytes[3] = DataTypeConvert.intToUnsignedByte(0x17);
            serachBytes[4] = DataTypeConvert.intToUnsignedByte(0xff);
            serachBytes[5] = DataTypeConvert.intToUnsignedByte(0xff);
            serachBytes[6] = DataTypeConvert.intToUnsignedByte(0xff);
            serachBytes[7] = DataTypeConvert.intToUnsignedByte(0xff);
            serachBytes[8] = DataTypeConvert.intToUnsignedByte(0x00);
            serachBytes[9] = DataTypeConvert.intToUnsignedByte(0xbf);
            serachBytes[10] = DataTypeConvert.intToUnsignedByte(0x03);
            serachBytes[11] = DataTypeConvert.intToUnsignedByte(0x00);
            serachBytes[12] = DataTypeConvert.intToUnsignedByte(0x21);
            serachBytes[13] = DataTypeConvert.intToUnsignedByte(0x03);
            serachBytes[14] = DataTypeConvert.intToUnsignedByte(0x01);
            serachBytes[15] = DataTypeConvert.intToUnsignedByte(0x61);
            serachBytes[16] = DataTypeConvert.intToUnsignedByte(0x03);
            serachBytes[17] = DataTypeConvert.intToUnsignedByte(0x00);
            serachBytes[18] = DataTypeConvert.intToUnsignedByte(0x07);
            serachBytes[19] = DataTypeConvert.intToUnsignedByte(0x03);
            serachBytes[20] = DataTypeConvert.intToUnsignedByte(0x00);
            serachBytes[21] = DataTypeConvert.intToUnsignedByte(0x00);
            serachBytes[22] = DataTypeConvert.intToUnsignedByte(0x03);

            byte[] serachBytes1 = new byte[31];
            serachBytes1[0] = DataTypeConvert.intToUnsignedByte(0x81);
            serachBytes1[1] = DataTypeConvert.intToUnsignedByte(0xff);
            serachBytes1[2] = DataTypeConvert.intToUnsignedByte(0x00);
            serachBytes1[3] = DataTypeConvert.intToUnsignedByte(0x11);
            serachBytes1[4] = DataTypeConvert.intToUnsignedByte(0xff);
            serachBytes1[5] = DataTypeConvert.intToUnsignedByte(0xff);
            serachBytes1[6] = DataTypeConvert.intToUnsignedByte(0xff);
            serachBytes1[7] = DataTypeConvert.intToUnsignedByte(0xff);
            serachBytes1[8] = DataTypeConvert.intToUnsignedByte(0xa0);
            serachBytes1[9] = DataTypeConvert.intToUnsignedByte(0xc5);
            serachBytes1[10] = DataTypeConvert.intToUnsignedByte(0x89);
            serachBytes1[11] = DataTypeConvert.intToUnsignedByte(0x32);
            serachBytes1[12] = DataTypeConvert.intToUnsignedByte(0x65);
            serachBytes1[13] = DataTypeConvert.intToUnsignedByte(0x9a);
            serachBytes1[14] = DataTypeConvert.intToUnsignedByte(0x01);
            serachBytes1[15] = DataTypeConvert.intToUnsignedByte(0x61);
            serachBytes1[16] = DataTypeConvert.intToUnsignedByte(0x03);

            serachBytes1[17 + 0] = DataTypeConvert.intToUnsignedByte(0x81);
            serachBytes1[17 + 1] = DataTypeConvert.intToUnsignedByte(0xff);
            serachBytes1[17 + 2] = DataTypeConvert.intToUnsignedByte(0x00);
            serachBytes1[17 + 3] = DataTypeConvert.intToUnsignedByte(0x0e);
            serachBytes1[17 + 4] = DataTypeConvert.intToUnsignedByte(0xff);
            serachBytes1[17 + 5] = DataTypeConvert.intToUnsignedByte(0xff);
            serachBytes1[17 + 6] = DataTypeConvert.intToUnsignedByte(0xff);
            serachBytes1[17 + 7] = DataTypeConvert.intToUnsignedByte(0xff);
            serachBytes1[17 + 8] = DataTypeConvert.intToUnsignedByte(0xa0);
            serachBytes1[17 + 9] = DataTypeConvert.intToUnsignedByte(0xc5);
            serachBytes1[17 + 10] = DataTypeConvert.intToUnsignedByte(0x89);
            serachBytes1[17 + 11] = DataTypeConvert.intToUnsignedByte(0x32);
            serachBytes1[17 + 12] = DataTypeConvert.intToUnsignedByte(0x65);
            serachBytes1[17 + 13] = DataTypeConvert.intToUnsignedByte(0x9a);

//            serachBytes[14] = DataTypeConvert.intToUnsignedByte(0x00);
//            serachBytes[15] = DataTypeConvert.intToUnsignedByte(0x00);
            for (int i = 0; i < serachBytes1.length; i++) {
                System.out.print(Integer.toHexString(serachBytes1[i]) + " ");
            }
//            for (int i = 0; i < serachBytes1.length; i++) {
//                System.out.print(Integer.toHexString(serachBytes1[i]) + " ");
//            }
            System.out.println();
            UDPMessageSender sender = new UDPMessageSender();
            InetAddress address = InetAddress.getByName(broadcastIp);
            UserAuth userAuth = new UserAuth();

            DatagramSocket datagramSocket = new DatagramSocket();
            byte[] replyData = sender.send(datagramSocket, serachBytes, 100);
//            for (int i = 0; i < replyData.length; i++) {
//                System.out.print(Integer.toHexString(replyData[i]) + " ");
//            }
//            System.out.println(Arrays.toString(replyData));
//            datagramSocket = userAuth.login(userName, password, mac);
//            byte[] replyBytes = Login.login(datagramSocket,broadcastIp, mac, userName, password);
            IParameterReaderWriter readerWriter = new ParameterReaderWriter();

            ChannelParameter channelParameter = new ChannelParameter();
            channelParameter.setChannelId(channelId);
            channelParameter.setMac(mac);
            List<ParameterItem> alist = new ArrayList<>();
            channelParameter.setParamItems(alist);
            ParameterItem item1 = new ParameterItem("ETH_AUTO_OBTAIN_IP",null);
            ParameterItem item2 = new ParameterItem("PREFERRED_DNS_SERVER",null);
            ParameterItem item3 = new ParameterItem("ALTERNATE_DNS_SERVE",null);
            ParameterItem item4 = new ParameterItem("BASIC_WEB_CONSOLE",null);
            ParameterItem item5 = new ParameterItem("BASIC_RTC_TIME",null);
            ParameterItem item6 = new ParameterItem("ETH_IP_ADDR",null);
            ParameterItem item7 = new ParameterItem("ETH_NETMASK_ADDR",null);
            ParameterItem item8 = new ParameterItem("ETH_GATEWAY_ADDR",null);
            ParameterItem item9 = new ParameterItem("BASIC_TELNET_CONSOLE",null);
            ParameterItem item10 = new ParameterItem("BASIC_CMD_TCP_CONSOL",null);
            ParameterItem item11 = new ParameterItem("BASIC_TIMER_SERVER",null);
            alist.add(item1);
            alist.add(item2);
            alist.add(item3);
            alist.add(item4);
            alist.add(item5);
            alist.add(item6);
            alist.add(item7);
            alist.add(item8);
            alist.add(item9);
            alist.add(item10);
            alist.add(item11);
            readerWriter.readChannelParam(datagramSocket, channelParameter);
            for (ParameterItem item : channelParameter.getParamItems()) {
                System.out.println(item.toString());
            }
//            item.setParamValue("SERVER");
//            channelParameter = readerWriter.writeChannelParam(datagramSocket, channelParameter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

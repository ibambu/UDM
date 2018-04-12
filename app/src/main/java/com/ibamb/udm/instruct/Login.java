/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ibamb.udm.instruct;

import com.ibamb.udm.constants.UdmConstants;
import com.ibamb.udm.constants.UdmControl;
import com.ibamb.udm.net.UDPSender;
import com.ibamb.udm.util.DataTypeConvert;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author luotao
 */
public class Login {

    public static byte[] login(String broadcsatIp, String mac, String userName, String password) {
        Base64.Decoder decoder = Base64.getDecoder();
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] replyData = new byte[4];
        try {
            String enUserName = encoder.encodeToString(userName.getBytes("UTF-8"));
            String enPassword = encoder.encodeToString(password.getBytes("UTF-8"));
            System.out.println("base64 encrypted userName:" + enUserName + " -> " + str2HexStr(enUserName));
            System.out.println("base64 encrypted password:" + enPassword + " -> " + str2HexStr(enPassword));
            byte[] byteUserName = DataTypeConvert.hexStringtoBytes(str2HexStr(enUserName));
            byte[] bytePassword = DataTypeConvert.hexStringtoBytes(str2HexStr(enPassword));
            byte[] macData = DataTypeConvert.hexStringtoBytes(mac.replaceAll(":", ""));
            System.out.println("mac bytes:" + Arrays.toString(macData));
            System.out.println("user bytes:" + Arrays.toString(byteUserName));
            System.out.println("password bytes:" + Arrays.toString(bytePassword));
            //c5 00 0020 00000000 00f0111111110000 5957527461573467 595752746157343d
            int frameLength = 1 + 1 + 2 + 4 + 8 + byteUserName.length + bytePassword.length;
            byte[] sendFrame = new byte[frameLength];
            sendFrame[0] = DataTypeConvert.intToUnsignedByte(UdmControl.AUTH_USER);
            sendFrame[1] = 0;
            byte[] byteFrameLength = DataTypeConvert.shortToBytes(DataTypeConvert.intToUnsignedShort(frameLength));
            sendFrame[2] = byteFrameLength[0];
            sendFrame[3] = byteFrameLength[1];
            //ip
            sendFrame[4] = 0;
            sendFrame[5] = 0;
            sendFrame[6] = 0;
            sendFrame[7] = 0;
            //mac
            sendFrame[8] = macData[0];
            sendFrame[9] = macData[1];
            sendFrame[10] = macData[2];
            sendFrame[11] = macData[3];
            sendFrame[12] = macData[4];
            sendFrame[13] = macData[5];
            sendFrame[14] = 0;
            sendFrame[15] = 0;
            int pos = 16;
            for (int i = 0; i < byteUserName.length; i++) {
                sendFrame[16 + i] = byteUserName[i];
                pos++;
            }
            for (int i = 0; i < bytePassword.length; i++) {
                sendFrame[pos + i] = bytePassword[i];
            }

            UDPSender sender = new UDPSender();
            InetAddress address = InetAddress.getByName(broadcsatIp);

            DatagramSocket datagramSocket = new DatagramSocket();

           replyData = sender.send(datagramSocket, address, UdmConstants.UDM_UDP_SERVER_PORT, sendFrame, 4);

        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        } catch (UnknownHostException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        return replyData;
    }

    public static String str2HexStr(String str) {
        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;
        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
            // sb.append(' ');
        }
        return sb.toString().trim();
    }
}

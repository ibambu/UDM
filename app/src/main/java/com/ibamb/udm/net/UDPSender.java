package com.ibamb.udm.net;

import com.ibamb.udm.beans.TCPChannelParameter;
import com.ibamb.udm.instruct.IEncoder;
import com.ibamb.udm.instruct.IParser;
import com.ibamb.udm.instruct.beans.InstructFrame;
import com.ibamb.udm.instruct.beans.ReplyFrame;
import com.ibamb.udm.instruct.impl.InstructFrameEncoder;
import com.ibamb.udm.instruct.impl.ReplyFrameParser;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by luotao on 18-3-15.
 */

public class UDPSender {
    public List<ReplyFrame> sendInstruct(String host, List<InstructFrame> instructFrames) {
        IEncoder encoder = new InstructFrameEncoder();
        IParser parser = new ReplyFrameParser();

        DatagramSocket datagramSocket = null;
        List<ReplyFrame> replyFrameList = new ArrayList<>();

        try {
            datagramSocket = new DatagramSocket();
            InetAddress address = InetAddress.getByName(host);
            for(InstructFrame instructFrame:instructFrames){
                byte[] sendData = encoder.encode(instructFrame);
                byte[] retData = send(datagramSocket, address, 5000, sendData);
                ReplyFrame replyFrame = parser.parse(retData);
                replyFrameList.add(replyFrame);
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭socket
            if (datagramSocket != null) {
                datagramSocket.close();
            }
        }
        return replyFrameList;
    }

    /**
     * 发送数据包
     *
     * @param datagramSocket
     * @param address
     * @param port
     * @param sendData
     * @return
     * @throws IOException
     */
    private byte[] send(DatagramSocket datagramSocket, InetAddress address, int port, byte[] sendData) throws IOException {

        DatagramPacket sendDataPacket = new DatagramPacket(sendData, sendData.length, address, port);
        // 发送数据
        datagramSocket.send(sendDataPacket);
        System.out.println("local send:" + new String(sendData, 0, sendData.length));
        // 接收数据
        byte[] recevBytes = new byte[1024];
        DatagramPacket recevPacket = new DatagramPacket(recevBytes, recevBytes.length);
        datagramSocket.receive(recevPacket);
        String responseMessage = new String(recevPacket.getData(), 0, recevPacket.getLength());
        System.out.println("remote resonse:" + responseMessage);
        return recevPacket.getData();
    }
}

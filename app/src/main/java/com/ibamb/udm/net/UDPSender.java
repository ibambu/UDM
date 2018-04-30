package com.ibamb.udm.net;

import com.ibamb.udm.constants.UdmConstants;
import com.ibamb.udm.core.ParameterMapping;
import com.ibamb.udm.instruct.IEncoder;
import com.ibamb.udm.instruct.IParser;
import com.ibamb.udm.instruct.beans.InstructFrame;
import com.ibamb.udm.instruct.beans.Parameter;
import com.ibamb.udm.instruct.beans.ReplyFrame;
import com.ibamb.udm.instruct.impl.ParamReadEncoder;
import com.ibamb.udm.instruct.impl.ReplyFrameParser;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by luotao on 18-3-15.
 */
public class UDPSender {

    public List<ReplyFrame> sendInstruct(String broadcastIp, List<InstructFrame> instructFrames) {
        IEncoder encoder = new ParamReadEncoder();
        IParser parser = new ReplyFrameParser();

        DatagramSocket datagramSocket = null;
        List<ReplyFrame> replyFrameList = new ArrayList<>();
        try {
            datagramSocket = new DatagramSocket();
            int seq = 1;
            InetAddress address = InetAddress.getByName(broadcastIp);
            for (InstructFrame instructFrame : instructFrames) {

                instructFrame.setId(seq++);

                Parameter param = ParameterMapping.getMapping(instructFrame.getInformation().getType());
                byte[] sendData = encoder.encode(instructFrame,1);

                byte[] retData = send(datagramSocket, address, UdmConstants.UDM_UDP_SERVER_PORT, sendData, param.getByteLength()
                        + UdmConstants.UDM_CONTROL_LENGTH
                        + UdmConstants.UDM_ID_LENGTH
                        + UdmConstants.UDM_MAIN_FRAME_LENGTH
                        + UdmConstants.UDM_TYPE_LENGTH
                        + UdmConstants.UDM_SUB_FRAME_LENGTH);
                
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
     * @param length
     * @return
     * @throws IOException
     */
    public  byte[] send(DatagramSocket datagramSocket, InetAddress address, int port, byte[] sendData, int length) throws IOException {

        DatagramPacket sendDataPacket = new DatagramPacket(sendData, sendData.length, address, port);

        // 发送数据
        datagramSocket.send(sendDataPacket);
        
        System.out.println("send:" + Arrays.toString(sendData));

        // 接收数据
        byte[] recevBuffer = new byte[length];
        DatagramPacket recevPacket = new DatagramPacket(recevBuffer, recevBuffer.length);

        try {
            Thread.sleep(200);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        datagramSocket.receive(recevPacket);
        byte[] recevData = recevPacket.getData();

        System.out.println("repy:" + Arrays.toString(recevData));

        return recevPacket.getData();
    }
}

package com.ibamb.plugins.tcpudp.context;

import com.ibamb.dnet.module.log.UdmLog;
import com.ibamb.dnet.module.net.IPUtil;
import com.ibamb.plugins.tcpudp.listener.MessageListener;
import com.ibamb.plugins.tcpudp.listener.ResultListener;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class UDPUnicastServer {

    private ConnectProperty connectProperty;
    private DatagramSocket socket;
    private Map<String, UnicastClient> clientMap = new HashMap<>();//所有客户端信息
    private MessageListener listener;

    public UDPUnicastServer(ConnectProperty connectProperty, MessageListener listener) {
        this.connectProperty = connectProperty;
        this.listener = listener;
    }

    public void create(final ResultListener resultListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String code ="0";
                try {
                    socket = new DatagramSocket(connectProperty.getUdpUniLocalPort());
                    code ="1";
                    while (!socket.isClosed()) {
                        /* * 服务器端接受客户端的数据 */
                        byte[] data = new byte[1024];
                        //指定用于接受数据报的大小
                        DatagramPacket packet = new DatagramPacket(data, data.length);
                        try {
                            socket.receive(packet);
                            //接受到数据之前该方法处于阻塞状态
                            byte[] receiveData =Arrays.copyOfRange(packet.getData(), 0, packet.getLength());
                            InetAddress address = packet.getAddress();
                            int port = packet.getPort();
                            clientMap.put(address.getHostAddress(), new UnicastClient(address.getHostAddress(), port));
                            listener.onReceive(address.getHostAddress() , receiveData);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }finally {
                    resultListener.onResult(code);
                }
            }
        }).start();
    }
    public boolean isReady() {
        return socket == null || socket.isClosed() ? false : true;
    }
    public void send(final String toAddress, final byte[] message, final MessageListener sendListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String localAddress = IPUtil.getLocalAddress().getHostAddress();
                    UnicastClient client = clientMap.get(toAddress);
                    if (client != null) {
                        if (!socket.isClosed()) {
                            InetAddress address = InetAddress.getByName(client.getAddress());
//                            byte[] sendData = message.getBytes();
                            DatagramPacket sendDataPacket = new DatagramPacket(message,message.length, address, client.getPort());
                            // 发送数据
                            socket.send(sendDataPacket);
                            sendListener.onReceive(localAddress ,message);
                        } else {
                            sendListener.onReceive(localAddress ,Constant.SOCKET_IS_CLOSED.getBytes());
                        }
                    } else {
                        sendListener.onReceive(localAddress ,":Unknown address".getBytes());
                    }

                } catch (Exception ex) {
                    UdmLog.error(ex);
                }
            }
        }).start();
    }

    public void close() {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class UnicastClient {
        private String address;
        private int port;

        public UnicastClient(String address, int port) {
            this.address = address;
            this.port = port;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }
    }

    public List<String> getAllUnicastClient() {
        List<String> clients = new ArrayList<>();
        for (Iterator<String> it = clientMap.keySet().iterator(); it.hasNext(); ) {
            String address = it.next();
            UnicastClient client = clientMap.get(address);
            clients.add(client.getAddress());
        }
        return clients;
    }
}

package com.ibamb.plugins.tcpudp.context;

import com.ibamb.dnet.module.net.IPUtil;
import com.ibamb.plugins.tcpudp.listener.MessageListener;
import com.ibamb.plugins.tcpudp.thread.TCPServerHandler;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class TCPServer {
    // 分配给socket连接的id，用于区分不同的socket连接
    private String currentAddress;//当前通信地址
    // 存储socket连接，发送消息的时候从这里取出对应的socket连接
    private HashMap<String, TCPServerHandler> socketHandlerMap = new HashMap<>();
    // 服务器对象，用于监听TCP端口
    private ServerSocket server;
    private MessageListener recvListener;//监控客户端发送过来的信息

    public TCPServer(MessageListener recvListener) {
        this.recvListener = recvListener;
    }

    public void create(final int serverPort) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    server = new ServerSocket(serverPort);
                    start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    public void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (server != null) {
                        while (!server.isClosed()) {
                            Socket socket = server.accept();
                            TCPServerHandler handler = new TCPServerHandler(socket, new MessageListener() {
                                @Override
                                public void onReceive(String message) {
                                    recvListener.onReceive(message);
                                }
                            });
                            addTCPServerHandler(handler);
                            handler.run();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void send(final String toAddress, final String message, final MessageListener listener) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TCPServerHandler serverHandler = getTCPServerHandler(toAddress);
                    InetAddress localAddress = IPUtil.getLocalAddress();
                    if (serverHandler != null) {
                        Socket socket = serverHandler.getSocket();
                        if (!socket.isClosed()) {
                            BufferedWriter bufferedWriter = new BufferedWriter(
                                    new OutputStreamWriter(socket.getOutputStream(), Constant.DEFAULT_CHARSET));
                            bufferedWriter.write(message);
                            bufferedWriter.flush();
                            listener.onReceive(localAddress.getHostAddress() + ":" + message);
                        } else {
                            listener.onReceive(localAddress.getHostAddress() + ":Socket is closed");
                        }
                    } else {
                        listener.onReceive(localAddress.getHostAddress() + ":Network disconnected");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public ServerSocket getServer() {
        return server;
    }

    public void setServer(ServerSocket server) {
        this.server = server;
    }


    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    public HashMap<String, TCPServerHandler> getSocketHandlerMap() {
        return socketHandlerMap;
    }

    public void setSocketHandlerMap(HashMap<String, TCPServerHandler> socketHandlerMap) {
        this.socketHandlerMap = socketHandlerMap;
    }

    public void addTCPServerHandler(TCPServerHandler tcpServerHandler) {
        this.socketHandlerMap.put(tcpServerHandler.getHostAddress(), tcpServerHandler);
    }

    public TCPServerHandler getTCPServerHandler(String hostAddress) {
        return socketHandlerMap.get(hostAddress);
    }

    public void removeTCPServerHandler(String hostAddress) {
        socketHandlerMap.remove(hostAddress);
    }

    public List<String> getConnectedClient() {
        List<String> aList = new ArrayList<>();
        for (Iterator it = socketHandlerMap.keySet().iterator(); it.hasNext(); ) {
            TCPServerHandler handler = socketHandlerMap.get(it.next());
            if (!handler.getSocket().isClosed()) {
                aList.add(handler.getHostAddress());
            }
        }
        for (int i = 0; i < 5; i++) {
            aList.add("192.168.0." + i);
        }
        return aList;
    }

    public void close() {
        try {
            for (Iterator it = socketHandlerMap.keySet().iterator(); it.hasNext(); ) {
                TCPServerHandler handler = socketHandlerMap.get(it.next());
                if (!handler.getSocket().isClosed()) {
                    handler.getSocket().close();
                }
            }
            server.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.ibamb.plugins.tcpudp.context;

import com.ibamb.dnet.module.net.IPUtil;
import com.ibamb.plugins.tcpudp.listener.MessageListener;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class TCPClient {
    private Socket socket;
    private BufferedInputStream inputStream;
    private BufferedWriter bufferedWriter;
    private MessageListener listener;
    private String remoteAddress;
    private String localAddress;

    public TCPClient(MessageListener listener) {
        this.listener = listener;
    }

    public void create(final ConnectProperty connectProperty) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket(connectProperty.getTcpRemoteHost(), connectProperty.getTcpRemotePort());
                    inputStream = new BufferedInputStream(socket.getInputStream());
                    bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), Constant.DEFAULT_CHARSET));
                    remoteAddress = connectProperty.getTcpRemoteHost();
                    localAddress = IPUtil.getLocalAddress().getHostAddress();
                    reading();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void reading() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String line = "";
                    byte[] buffer = new byte[1024];
                    int length = 0;
                    while ((length = inputStream.read(buffer)) != -1) {
                        String data = new String(buffer, 0, length, Constant.DEFAULT_CHARSET);
                        if (line != null) {
                            listener.onReceive(remoteAddress + ":" + data);
                        }
                    }
                    listener.onReceive(remoteAddress + ":Server is stopped.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void send(final String message, final MessageListener sendListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (!socket.isClosed()) {
                        bufferedWriter.write(message);
                        bufferedWriter.flush();
                        sendListener.onReceive(localAddress + ":" + message);
                    } else {
                        sendListener.onReceive(localAddress + ":Socket is colsed");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void close() {
        try {
            if (socket != null) {
                socket.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

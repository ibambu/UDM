package com.ibamb.plugins.tcpudp.context;

import com.ibamb.dnet.module.net.IPUtil;
import com.ibamb.plugins.tcpudp.listener.MessageListener;
import com.ibamb.plugins.tcpudp.listener.ResultListener;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Arrays;

public class TCPClient {
    private Socket socket;
    private BufferedInputStream inputStream;
    private BufferedWriter bufferedWriter;
    private BufferedOutputStream outputStream;
    private MessageListener listener;
    private String remoteAddress;
    private String localAddress;

    public TCPClient(MessageListener listener) {
        this.listener = listener;
    }

    public void create(final ConnectProperty connectProperty, final ResultListener resultListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String code ="0";
                try {
                    socket = new Socket(connectProperty.getTcpRemoteHost(), connectProperty.getTcpRemotePort());
                    code ="1";
                    inputStream = new BufferedInputStream(socket.getInputStream());
                    outputStream = new BufferedOutputStream(socket.getOutputStream());
                    bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), Constant.DEFAULT_CHARSET));
                    remoteAddress = connectProperty.getTcpRemoteHost();
                    localAddress = IPUtil.getLocalAddress().getHostAddress();
                    reading();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    resultListener.onResult(code);
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
                        byte[] readData = Arrays.copyOfRange(buffer, 0, length);
                        if (line != null) {
                            listener.onReceive(remoteAddress, readData);
                        }
                    }
                    listener.onReceive(remoteAddress, Constant.SOCKET_IS_CLOSED.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public boolean isReady() {
        return socket == null || socket.isClosed() ? false : true;
    }
    public void send(final byte[] message, final MessageListener sendListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (!socket.isClosed()) {
                        outputStream.write(message);
                        outputStream.flush();
                        sendListener.onReceive(localAddress , message);
                    } else {
                        sendListener.onReceive(localAddress ,Constant.SOCKET_IS_CLOSED.getBytes());
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
            if(outputStream!=null){
                outputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

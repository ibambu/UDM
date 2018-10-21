package com.ibamb.plugins.tcpudp.thread;

import com.ibamb.plugins.tcpudp.listener.MessageListener;

import java.net.Socket;

public class TCPServerHandler implements Runnable {
    private Socket socket;
    private MessageListener listener;
    private String hostAddress;

    public Socket getSocket() {
        return socket;
    }

    public String getHostAddress() {
        return hostAddress;
    }

    public TCPServerHandler(Socket socket, MessageListener listener) {
        this.socket = socket;
        this.listener = listener;
        this.hostAddress = socket.getInetAddress().getHostAddress();
    }

    @Override
    public void run() {
        //开启独立线程，接收客户端发送过来的信息。
        TCPReaderThread tcpReaderThread = new TCPReaderThread(socket, listener);
        tcpReaderThread.start();
    }
}

package com.ibamb.plugins.tcpudp.thread;

import com.ibamb.plugins.tcpudp.listener.MessageListener;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

public class TCPReaderThread extends Thread{

    private MessageListener listener;
    private BufferedInputStream inputStream;
    private String address;

    public TCPReaderThread(Socket socket, MessageListener listener) {
        this.listener = listener;
        this.address = socket.getInetAddress().getHostAddress();
        try {
            inputStream = new BufferedInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            String line = "";
            byte[] buffer = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buffer)) != -1) {
                byte[] data = Arrays.copyOfRange(buffer,0,length);
//                String data = new String(buffer, 0, length,"gbk");
                if (line != null) {
                    listener.onReceive(address,data);
                }
            }
            System.out.println("Server is stopped.");
        } catch (IOException e) {
            System.out.println("Connection is closed.");
        }
    }

}

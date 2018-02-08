package com.ibamb.udm.task;

import android.os.Handler;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by luotao on 17-12-20.
 */

public class NetScanTask implements Callable {

    private String ip;//主机地址

    private Socket socket;//套接字
    private DataInputStream dataReader;//数据读
    private DataOutputStream dataWriter;//数据写

    private Handler handler;
    public NetScanTask(String ip,Handler handler) {
        this.ip = ip;
        this.handler = handler;
    }

    @Override
    public Object call() throws Exception {

        InetAddress inetAddress = InetAddress.getByName(ip);
        boolean isReachable = inetAddress.isReachable(2000);
        if(isReachable){
            System.out.println("[OK] Found "+inetAddress.getHostAddress());
            handler.sendMessage(handler.obtainMessage(0, inetAddress));
        }
        return isReachable;
    }
}

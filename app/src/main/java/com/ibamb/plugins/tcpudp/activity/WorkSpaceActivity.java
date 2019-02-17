package com.ibamb.plugins.tcpudp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import com.ibamb.dnet.module.log.UdmLog;
import com.ibamb.dnet.module.util.Convert;
import com.ibamb.plugins.tcpudp.adapter.RecordListAdapter;
import com.ibamb.plugins.tcpudp.context.ConnectProperty;
import com.ibamb.plugins.tcpudp.context.ConnectionContext;
import com.ibamb.plugins.tcpudp.context.Constant;
import com.ibamb.plugins.tcpudp.context.TCPClient;
import com.ibamb.plugins.tcpudp.context.TCPServer;
import com.ibamb.plugins.tcpudp.context.UDPBroadcast;
import com.ibamb.plugins.tcpudp.context.UDPMulticast;
import com.ibamb.plugins.tcpudp.context.UDPUnicastClient;
import com.ibamb.plugins.tcpudp.context.UDPUnicastServer;
import com.ibamb.plugins.tcpudp.listener.MessageListener;
import com.ibamb.plugins.tcpudp.listener.ResultListener;
import com.ibamb.udm.R;
import com.ibamb.udm.component.constants.UdmConstant;
import com.ibamb.udm.util.TaskBarQuiet;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class WorkSpaceActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;//通信记录列表控件
    private EditText messageToSendView;//发送内容编辑控件
    private ImageView vMoreObjectListView;//更多目标列表
    private TextView mSendButton;//发送信息按钮
    private Switch mConnectSwitch;//连接切换控件
    private CheckBox mAutoSendIntervalBox;
    private EditText mInterval;
    private RadioButton rbHex;
    private TextView vTitle;
    private TextView sendCountView;
    private TextView recvCountView;
    private TextView tvClean;

    private ConnectProperty configuration;//连接配置信息
    private ConnectionContext connContext = new ConnectionContext();//存放所有连接资源
    private List<String> communicateList;//存储服务端和客户端通信记录
    private RecordListAdapter adapter;

    private String currentTargetIp;
    private long revLength;
    private long sendLength;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_space);
        TaskBarQuiet.setStatusBarColor(this, UdmConstant.TASK_BAR_COLOR);

        mRecyclerView = findViewById(R.id.record_recycler_view);
        messageToSendView = findViewById(R.id.message_to_send);
        mAutoSendIntervalBox = findViewById(R.id.cb_auto_send_interval);
        mInterval = findViewById(R.id.et_auto_send_interval);
        vMoreObjectListView = findViewById(R.id.img_more_object_list);
        recvCountView = findViewById(R.id.received_count);
        sendCountView = findViewById(R.id.send_count);
        rbHex = findViewById(R.id.rb_hex);
        tvClean = findViewById(R.id.tv_clean);
        tvClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                revLength = 0;
                sendLength = 0;
                recvCountView.setText("Received:0");
                sendCountView.setText("Send:0");
                if (communicateList != null) {
                    communicateList.clear();
                    adapter.notifyDataSetChanged();
                }
            }
        });
        //作为服务端时点击显示更多连接的客户端
        vMoreObjectListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> clients = null;
                final String[] arrayAddress;
                if (configuration.getConnectType().equals(Constant.CONN_UDP_UNICAST_SERVER)) {
                    clients = connContext.getUdpUnicastServer().getAllUnicastClient();
                } else if (configuration.getConnectType().equals(Constant.CONN_TCP_SERVER)) {
                    clients = connContext.getTcpServer().getConnectedClient();
                }
                if (clients != null) {
                    arrayAddress = new String[clients.size()];
                    clients.toArray(arrayAddress);
                    if (arrayAddress.length > 0) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        builder.setTitle("Client connected:" + clients.size());
                        int checkItemIdx = 0;
                        if (arrayAddress != null) {
                            for (int i = 0; i < clients.size(); i++) {
                                if (arrayAddress[i].equalsIgnoreCase(currentTargetIp)) {
                                    checkItemIdx = i;
                                    break;
                                }
                            }
                            builder.setSingleChoiceItems(arrayAddress, checkItemIdx, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    currentTargetIp = arrayAddress[which];
                                    vTitle.setText("To " + currentTargetIp);
                                    dialog.dismiss();
                                }
                            });
                            builder.show();
                        }
                    }
                }
            }
        });
        vTitle = findViewById(R.id.title);
        vTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent connIntent = new Intent(v.getContext(), ConnectionActivity.class);
//                startActivity(connIntent);
            }
        });

        findViewById(R.id.go_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.do_commit).setVisibility(View.GONE);

        communicateList = new ArrayList<>();//初始化录通信记录


        adapter = new RecordListAdapter(this, communicateList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);


        Intent intent = getIntent();

        configuration = new ConnectProperty();
        configuration.setTcpRemoteHost(intent.getStringExtra("TCP_REMOTE_HOST"));
        configuration.setTcpRemotePort(intent.getStringExtra("TCP_REMOTE_PORT"));
        configuration.setTcpLocalPort(intent.getStringExtra("TCP_LOCAL_PORT"));

        configuration.setUdpUniRemoteHost(intent.getStringExtra("UDP_UNI_TARGET_HOST"));
        configuration.setUdpUniRemotePort(intent.getStringExtra("UDP_UNI_TARGET_PORT"));
        configuration.setUdpUniLocalPort(intent.getStringExtra("UDP_UNI_LOCAL_PORT"));

        configuration.setUdpMulAddress(intent.getStringExtra("UDP_MUL_ADDRESS"));
        configuration.setUdpMulPort(intent.getStringExtra("UDP_MUL_PORT"));
        configuration.setUdpBroadcastPort(intent.getStringExtra("UDP_BROADCAST_PORT"));

        configuration.setConnectType(intent.getStringExtra("CONNECTION_TYPE"));
        configuration.setWorkRole(intent.getIntExtra("WORK_ROLE", Constant.CONN_ROLE_CLIENT));
        if (configuration.getConnectType().equals(Constant.CONN_TCP_CLIENT)) {
            vTitle.setText("To " + configuration.getTcpRemoteHost());
        } else if (configuration.getConnectType().equals(Constant.CONN_UDP_UNICAST_CLIENT)) {
            vTitle.setText("To " + configuration.getUdpUniRemoteHost());
        } else if (configuration.getConnectType().equals(Constant.CONN_UDP_BROADCAST)) {
            vTitle.setText("UDP Broadcast");
        } else if (configuration.getConnectType().equals(Constant.CONN_UDP_MULCAST)) {
            vTitle.setText("UDP Multicast");
        }
        if (configuration.getWorkRole() == Constant.CONN_ROLE_SERVER &&
                !configuration.getConnectType().equals(Constant.CONN_UDP_BROADCAST)
                && !configuration.getConnectType().equals(Constant.CONN_UDP_MULCAST)) {
            findViewById(R.id.img_more_object_list).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.img_more_object_list).setVisibility(View.GONE);
        }

        connect(configuration);//开始连接

        mConnectSwitch = findViewById(R.id.toggle_state_btn);
        mConnectSwitch.setChecked(true);
        mConnectSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mConnectSwitch.isChecked()) {
                    disconnect();
                } else {
                    connect(configuration);
                    mConnectSwitch.setText("connect");
                    mSendButton.setEnabled(true);
                }
            }
        });
        /**
         * 发送信息事件
         */

        mSendButton = findViewById(R.id.send_button);
        mSendButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mAutoSendIntervalBox.isChecked()) {
                    if (mSendButton.getText().toString().equals("Send")) {
                        String intval = mInterval.getText().toString();
                        if (intval != null && intval.trim().length() > 0) {
                            mSendButton.setText("Pause");
                            autoSendByInterval();
                        } else {
                            Snackbar.make(mSendButton, "Interval can't be empty.", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }

                    } else {
                        mSendButton.setText("Send");
                        stopAutoSend();
                    }
                } else {
                    String message = messageToSendView.getText().toString();
                    send(message);
                }
            }
        });

    }

    private void disconnect(){
        stopAutoSend();
        connContext.closeAll();
        mConnectSwitch.setText("disconnect");
        mSendButton.setText("Send");
        mConnectSwitch.setChecked(false);
        mSendButton.setEnabled(false);
    }

    Timer timer;
    TimerTask task;

    private void autoSendByInterval() {
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                send(messageToSendView.getText().toString());
            }
        };
        long period = Long.parseLong(mInterval.getText().toString());
        timer.schedule(task, 0, period);
    }

    private void stopAutoSend() {
        if (task != null) {
            task.cancel();
        }
    }

    @Override
    public void finish() {
        connContext.closeAll();
        super.finish();
    }

    private boolean isSocketClosed(byte[] message) {
        boolean isClosed = bytesToString(message).contains(Constant.SOCKET_IS_CLOSED);
        if(isClosed){
            Snackbar.make(this.getWindow().getDecorView(), "Socket is closed", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    disconnect();
                }
            });
        }
        return isClosed;
    }


    /**
     * 连接
     */
    private void connect(ConnectProperty connectProperty) {
        try {
            switch (connectProperty.getConnectType()) {
                case Constant.CONN_TCP_SERVER:
                    TCPServer tcpServer = new TCPServer(new MessageListener() {
                        @Override
                        public void onReceive(String hostAddress, byte[] message) {
                            if (isSocketClosed(message)) {
                                stopAutoSend();
                            } else {
                                updateRecord(hostAddress, message);
                                countRecvBytes(message);
                            }
                        }
                    });
                    tcpServer.create(connectProperty.getTcpLocalPort(), new ResultListener() {
                        @Override
                        public void onResult(String code) {
                            if(code.equals("0")){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        disconnect();
                                    }
                                });
                            }
                        }
                    });
                    connContext.setTcpServer(tcpServer);
                    break;
                case Constant.CONN_TCP_CLIENT:
                    TCPClient tcpClient = new TCPClient(new MessageListener() {
                        @Override
                        public void onReceive(String hostAddress, byte[] message) {
                            if (isSocketClosed(message)) {
                                stopAutoSend();
                            } else {
                                updateRecord(hostAddress, message);
                                countRecvBytes(message);
                            }
                        }
                    });
                    tcpClient.create(connectProperty, new ResultListener() {
                        @Override
                        public void onResult(String code) {
                            if(code.equals("0")){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        disconnect();
                                    }
                                });
                            }
                        }
                    });
                    connContext.setTcpClient(tcpClient);
                    break;
                case Constant.CONN_UDP_UNICAST_CLIENT:
                    UDPUnicastClient unicastClient = new UDPUnicastClient(connectProperty, new MessageListener() {
                        @Override
                        public void onReceive(String hostAddress, byte[] message) {
                            if (isSocketClosed(message)) {
                                stopAutoSend();
                            } else {
                                updateRecord(hostAddress, message);
                                countRecvBytes(message);
                            }
                        }
                    });
                    unicastClient.create(new ResultListener() {
                        @Override
                        public void onResult(String code) {
                            if(code.equals("0")){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        disconnect();
                                    }
                                });
                            }
                        }
                    });
                    connContext.setUdpUnicastClient(unicastClient);
                    break;
                case Constant.CONN_UDP_UNICAST_SERVER:
                    UDPUnicastServer unicastServer = new UDPUnicastServer(connectProperty, new MessageListener() {
                        @Override
                        public void onReceive(String hostAddress, byte[] message) {
                            if (isSocketClosed(message)) {
                                stopAutoSend();
                            } else {
                                updateRecord(hostAddress, message);
                                countRecvBytes(message);
                            }
                        }
                    });
                    unicastServer.create(new ResultListener() {
                        @Override
                        public void onResult(String code) {
                            if(code.equals("0")){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        disconnect();
                                    }
                                });
                            }
                        }
                    });
                    connContext.setUdpUnicastServer(unicastServer);
                    break;
                case Constant.CONN_UDP_MULCAST:
                    UDPMulticast udpMulticast = new UDPMulticast(connectProperty, new MessageListener() {
                        @Override
                        public void onReceive(String hostAddress, byte[] message) {
                            if(isSocketClosed(message)){
                                stopAutoSend();
                            }else{
                                updateRecord(hostAddress, message);
                                countRecvBytes(message);
                            }

                        }
                    });
                    udpMulticast.create(new ResultListener() {
                        @Override
                        public void onResult(String code) {
                            if(code.equals("0")){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        disconnect();
                                    }
                                });
                            }
                        }
                    });
                    connContext.setUdpMulticast(udpMulticast);
                    break;
                case Constant.CONN_UDP_BROADCAST:
                    UDPBroadcast broadcast = new UDPBroadcast(connectProperty, new MessageListener() {
                        @Override
                        public void onReceive(String hostAddress, byte[] message) {
                            if(isSocketClosed(message)){
                                stopAutoSend();
                            }else{
                                updateRecord(hostAddress, message);
                                countRecvBytes(message);
                            }
                        }
                    });
                    broadcast.create(new ResultListener() {
                        @Override
                        public void onResult(String code) {
                            if(code.equals("0")){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        disconnect();
                                    }
                                });
                            }
                        }
                    });
                    connContext.setUdpBroadcast(broadcast);
                    break;
            }
        } catch (Exception e) {
            UdmLog.error(e);
        }
    }


    /**
     * 发送信息
     */
    private void send(String message) {
        try {
            int dataType = rbHex.isChecked() ? 1 : 0;

            byte[] sendData = null;
            if (dataType == 0) {//character
                sendData = message.getBytes(Constant.DEFAULT_CHARSET);
            } else {//Hex
                sendData = hexStringToBytes(message);
            }

            if (configuration.getConnectType().equals(Constant.CONN_TCP_CLIENT)) {
                connContext.getTcpClient().send(sendData, new MessageListener() {
                    @Override
                    public void onReceive(String hostAddress, byte[] message) {
//                        updateRecord(hostAddress, message);
                        countSendBytes(message);

                    }
                });
            } else if (configuration.getConnectType().equals(Constant.CONN_TCP_SERVER)) {
                //通过界面选择要发送的目标地址。
                if (currentTargetIp != null) {
                    connContext.getTcpServer().send(currentTargetIp, sendData, new MessageListener() {
                        @Override
                        public void onReceive(String hostAddress, byte[] message) {
//                            updateRecord(hostAddress, message);
                            countSendBytes(message);
                        }
                    });
                }
            } else if (configuration.getConnectType().equals(Constant.CONN_UDP_UNICAST_CLIENT)) {
                connContext.getUdpUnicastClient().send(sendData, new MessageListener() {
                    @Override
                    public void onReceive(String hostAddress, byte[] message) {
//                        updateRecord(hostAddress, message);
                        countSendBytes(message);
                    }
                });
            } else if (configuration.getConnectType().equals(Constant.CONN_UDP_UNICAST_SERVER)) {
                connContext.getUdpUnicastServer().send(currentTargetIp, sendData, new MessageListener() {
                    @Override
                    public void onReceive(String hostAddress, byte[] message) {
//                        updateRecord(hostAddress, message);
                        countSendBytes(message);
                    }
                });
            } else if (configuration.getConnectType().equals(Constant.CONN_UDP_MULCAST)) {
                connContext.getUdpMulticast().send(sendData, new MessageListener() {
                    @Override
                    public void onReceive(String hostAdress, byte[] message) {
//                        updateRecord(hostAdress, message);
                        countSendBytes(message);
                    }
                });
            } else if (configuration.getConnectType().equals(Constant.CONN_UDP_BROADCAST)) {
                connContext.getUdpBroadcast().send(sendData, new MessageListener() {
                    @Override
                    public void onReceive(String hostAddress, byte[] message) {
//                        updateRecord(hostAddress, message);
                        countSendBytes(message);
                    }
                });
            }
        } catch (Exception e) {
            UdmLog.error(e);
        }
    }

    private void countSendBytes(final byte[] message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sendLength += message.length;
                sendCountView.setText("Send:" + sendLength);
            }
        });


    }

    private void countRecvBytes(final byte[] message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                revLength += message.length;
                recvCountView.setText("Received:" + revLength);
            }
        });

    }

    /**
     * 更新界面
     *
     * @param message
     */
    private void updateRecord(final String hostAddress, final byte[] message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String displayData = "";
                if (rbHex.isChecked()) {
                    displayData = bytesToHexString(message);
                } else {
                    displayData = bytesToString(message);
                }
                communicateList.add(hostAddress + "->" + displayData);
                adapter.notifyDataSetChanged();
                mRecyclerView.scrollToPosition(communicateList.size() - 1);
            }
        });
    }

    /**
     * string to bytes
     *
     * @param message
     * @return
     */
    private byte[] stringToBytes(String message) {
        char[] cData = message.toCharArray();
        byte[] rBytes = new byte[cData.length];
        for (int k = 0; k < cData.length; k++) {
            rBytes[k] = (byte) cData[k];
        }
        return rBytes;
    }

    /**
     * bytes to string
     *
     * @param message
     * @return
     */
    private String bytesToString(byte[] message) {
        //默认当作文本处理
        String retString = "";
        try {
            retString = new String(message, 0, message.length, Constant.DEFAULT_CHARSET);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
//        StringBuilder buffer = new StringBuilder();
//        for (int k = 0; k < message.length; k++) {
//            char c = (char) message[k];
//            buffer.append(c);
//        }
        return retString;
    }


    /**
     * bytes to hex
     *
     * @param message
     * @return
     */
    private String bytesToHexString(byte[] message) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < message.length; i++) {
            buffer.append(Convert.toHexString(message[i]));
        }
        return buffer.toString();

    }

    /**
     * hex to bytes
     *
     * @param message
     * @return
     */
    private byte[] hexStringToBytes(String message) {
        byte[] retbytes = Convert.hexStringtoBytes(message);
        if (retbytes.length == 0) {
            String hexstr = str2HexStr(message);
            retbytes = Convert.hexStringtoBytes(hexstr);
        }
        return retbytes;
    }

    /**
     * 字符串转换成为16进制(无需Unicode编码)
     *
     * @param str
     * @return
     */
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

    /**
     * 16进制直接转换成为字符串(无需Unicode解码)
     *
     * @param hexStr
     * @return
     */
    public static String hexStr2Str(String hexStr) {
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;
        for (int i = 0; i < bytes.length; i++) {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes);
    }
}

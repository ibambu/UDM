package com.ibamb.plugins.tcpudp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import com.ibamb.udm.R;
import com.ibamb.udm.component.constants.UdmConstant;
import com.ibamb.udm.util.TaskBarQuiet;

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
    RadioButton rbHex;
    private TextView vTitle;

    private ConnectProperty configuration;//连接配置信息
    private ConnectionContext connContext = new ConnectionContext();//存放所有连接资源
    private List<String> communicateList;//存储服务端和客户端通信记录
    private RecordListAdapter adapter;

    private String currentTargetIp;


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
        rbHex = findViewById(R.id.rb_hex);
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
                Intent connIntent = new Intent(v.getContext(), ConnectionActivity.class);
                startActivity(connIntent);
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
                    connContext.closeAll();
                    mConnectSwitch.setText("disconnect");
                } else {
                    connect(configuration);
                    mConnectSwitch.setText("connect");
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
                        mSendButton.setText("Stop");
                        autoSendByInterval();
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

    Timer timer;

    private void autoSendByInterval() {
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                send(messageToSendView.getText().toString());
            }
        };
        long period = Long.parseLong(mInterval.getText().toString());
        timer.schedule(task, 0, period);
    }

    private void stopAutoSend() {
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    public void finish() {
        connContext.closeAll();
        super.finish();
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
                            updateRecord(hostAddress, message);
                        }
                    });
                    tcpServer.create(connectProperty.getTcpLocalPort());
                    connContext.setTcpServer(tcpServer);
                    break;
                case Constant.CONN_TCP_CLIENT:
                    TCPClient tcpClient = new TCPClient(new MessageListener() {
                        @Override
                        public void onReceive(String hostAddress, byte[] message) {
                            updateRecord(hostAddress, message);
                        }
                    });
                    tcpClient.create(connectProperty);
                    connContext.setTcpClient(tcpClient);
                    break;
                case Constant.CONN_UDP_UNICAST_CLIENT:
                    UDPUnicastClient unicastClient = new UDPUnicastClient(connectProperty, new MessageListener() {
                        @Override
                        public void onReceive(String hostAddress, byte[] message) {
                            updateRecord(hostAddress, message);
                        }
                    });
                    unicastClient.create();
                    connContext.setUdpUnicastClient(unicastClient);
                    break;
                case Constant.CONN_UDP_UNICAST_SERVER:
                    UDPUnicastServer unicastServer = new UDPUnicastServer(connectProperty, new MessageListener() {
                        @Override
                        public void onReceive(String hostAddress, byte[] message) {
                            updateRecord(hostAddress, message);
                        }
                    });
                    unicastServer.create();
                    connContext.setUdpUnicastServer(unicastServer);
                    break;
                case Constant.CONN_UDP_MULCAST:
                    UDPMulticast udpMulticast = new UDPMulticast(connectProperty, new MessageListener() {
                        @Override
                        public void onReceive(String hostAddress, byte[] message) {
                            updateRecord(hostAddress, message);
                        }
                    });
                    udpMulticast.create();
                    connContext.setUdpMulticast(udpMulticast);
                    break;
                case Constant.CONN_UDP_BROADCAST:
                    UDPBroadcast broadcast = new UDPBroadcast(connectProperty, new MessageListener() {
                        @Override
                        public void onReceive(String hostAddress, byte[] message) {
                            updateRecord(hostAddress, message);
                        }
                    });
                    broadcast.create();
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
                sendData = stringToBytes(message);
            } else {//Hex
                sendData = hexStringToBytes(message);
            }

            if (configuration.getConnectType().equals(Constant.CONN_TCP_CLIENT)) {
                connContext.getTcpClient().send(sendData, new MessageListener() {
                    @Override
                    public void onReceive(String hostAddress, byte[] message) {
                        updateRecord(hostAddress, message);
                    }
                });
            } else if (configuration.getConnectType().equals(Constant.CONN_TCP_SERVER)) {
                //通过界面选择要发送的目标地址。
                if (currentTargetIp != null) {
                    connContext.getTcpServer().send(currentTargetIp, sendData, new MessageListener() {
                        @Override
                        public void onReceive(String hostAddress, byte[] message) {
                            updateRecord(hostAddress, message);
                        }
                    });
                }
            } else if (configuration.getConnectType().equals(Constant.CONN_UDP_UNICAST_CLIENT)) {
                connContext.getUdpUnicastClient().send(sendData, new MessageListener() {
                    @Override
                    public void onReceive(String hostAddress, byte[] message) {
                        updateRecord(hostAddress, message);
                    }
                });
            } else if (configuration.getConnectType().equals(Constant.CONN_UDP_UNICAST_SERVER)) {
                connContext.getUdpUnicastServer().send(currentTargetIp, sendData, new MessageListener() {
                    @Override
                    public void onReceive(String hostAddress, byte[] message) {
                        updateRecord(hostAddress, message);
                    }
                });
            } else if (configuration.getConnectType().equals(Constant.CONN_UDP_MULCAST)) {
                connContext.getUdpMulticast().send(sendData, new MessageListener() {
                    @Override
                    public void onReceive(String hostAdress, byte[] message) {
                        updateRecord(hostAdress, message);
                    }
                });
            } else if (configuration.getConnectType().equals(Constant.CONN_UDP_BROADCAST)) {
                connContext.getUdpBroadcast().send(sendData, new MessageListener() {
                    @Override
                    public void onReceive(String hostAddress, byte[] message) {
                        updateRecord(hostAddress, message);
                    }
                });
            }
        } catch (Exception e) {
            UdmLog.error(e);
        }
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
                communicateList.add(hostAddress + ":" + displayData);
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
        StringBuilder buffer = new StringBuilder();
        for (int k = 0; k < message.length; k++) {
            char c = (char) message[k];
            buffer.append(c);
        }
        return buffer.toString();
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
        return Convert.hexStringtoBytes(message);
    }

}

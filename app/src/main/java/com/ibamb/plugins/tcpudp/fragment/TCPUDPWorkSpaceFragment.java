package com.ibamb.plugins.tcpudp.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TCPUDPWorkSpaceFragment extends Fragment {
    public static final String PARAM = "PARAM";
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
    private View fragmentView;

    private String param;

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public TCPUDPWorkSpaceFragment() {
        // Required empty public constructor
    }

    public static TCPUDPWorkSpaceFragment newInstance(String param) {
        TCPUDPWorkSpaceFragment fragment = new TCPUDPWorkSpaceFragment();
        Bundle args = new Bundle();
        args.putString(PARAM, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle!=null){
            String aa = bundle.getString(PARAM);
            configuration = new ConnectProperty();
            configuration.setTcpRemoteHost(bundle.getString("TCP_REMOTE_HOST"));
            configuration.setTcpRemotePort(bundle.getString("TCP_REMOTE_PORT"));
            configuration.setTcpLocalPort(bundle.getString("TCP_LOCAL_PORT"));

            configuration.setUdpUniRemoteHost(bundle.getString("UDP_UNI_TARGET_HOST"));
            configuration.setUdpUniRemotePort(bundle.getString("UDP_UNI_TARGET_PORT"));
            configuration.setUdpUniLocalPort(bundle.getString("UDP_UNI_LOCAL_PORT"));

            configuration.setUdpMulAddress(bundle.getString("UDP_MUL_ADDRESS"));
            configuration.setUdpMulPort(bundle.getString("UDP_MUL_PORT"));
            configuration.setUdpBroadcastPort(bundle.getString("UDP_BROADCAST_PORT"));

            configuration.setConnectType(bundle.getString("CONNECTION_TYPE"));
            configuration.setWorkRole(bundle.getInt("WORK_ROLE", Constant.CONN_ROLE_CLIENT));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tcpudpwork_space, container, false);

        mRecyclerView = view.findViewById(R.id.record_recycler_view);
        messageToSendView = view.findViewById(R.id.message_to_send);
        mAutoSendIntervalBox = view.findViewById(R.id.cb_auto_send_interval);
        mInterval = view.findViewById(R.id.et_auto_send_interval);
        vMoreObjectListView = view.findViewById(R.id.img_more_object_list);
        recvCountView = view.findViewById(R.id.received_count);
        sendCountView = view.findViewById(R.id.send_count);
        vTitle = getActivity().findViewById(R.id.title);
        rbHex = view.findViewById(R.id.rb_hex);
        tvClean = view.findViewById(R.id.tv_clean);
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


        communicateList = new ArrayList<>();//初始化录通信记录
        adapter = new RecordListAdapter(getActivity(), communicateList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(adapter);


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
            fragmentView.findViewById(R.id.img_more_object_list).setVisibility(View.VISIBLE);
        } else {
            fragmentView.findViewById(R.id.img_more_object_list).setVisibility(View.GONE);
        }

        connect(configuration);//开始连接

        mConnectSwitch = fragmentView.findViewById(R.id.toggle_state_btn);
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


        /**
         * 发送信息事件
         */

        mSendButton = view.findViewById(R.id.send_button);
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
        this.fragmentView = view;
        return view;
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
    public void onDestroy() {
        super.onDestroy();
        connContext.closeAll();
    }

    /**
     * 连接
     */
    private void connect(ConnectProperty connectProperty) {
//        try {
//            switch (connectProperty.getConnectType()) {
//                case Constant.CONN_TCP_SERVER:
//                    TCPServer tcpServer = new TCPServer(new MessageListener() {
//                        @Override
//                        public void onReceive(String hostAddress, byte[] message) {
//                            updateRecord(hostAddress, message);
//                            countRecvBytes(message);
//                        }
//                    });
//                    tcpServer.create(connectProperty.getTcpLocalPort());
//                    connContext.setTcpServer(tcpServer);
//                    break;
//                case Constant.CONN_TCP_CLIENT:
//                    TCPClient tcpClient = new TCPClient(new MessageListener() {
//                        @Override
//                        public void onReceive(String hostAddress, byte[] message) {
//                            updateRecord(hostAddress, message);
//                            countRecvBytes(message);
//                        }
//                    });
//                    tcpClient.create(connectProperty);
//                    connContext.setTcpClient(tcpClient);
//                    break;
//                case Constant.CONN_UDP_UNICAST_CLIENT:
//                    UDPUnicastClient unicastClient = new UDPUnicastClient(connectProperty, new MessageListener() {
//                        @Override
//                        public void onReceive(String hostAddress, byte[] message) {
//                            updateRecord(hostAddress, message);
//                            countRecvBytes(message);
//                        }
//                    });
//                    unicastClient.create();
//                    connContext.setUdpUnicastClient(unicastClient);
//                    break;
//                case Constant.CONN_UDP_UNICAST_SERVER:
//                    UDPUnicastServer unicastServer = new UDPUnicastServer(connectProperty, new MessageListener() {
//                        @Override
//                        public void onReceive(String hostAddress, byte[] message) {
//                            updateRecord(hostAddress, message);
//                            countRecvBytes(message);
//                        }
//                    });
//                    unicastServer.create();
//                    connContext.setUdpUnicastServer(unicastServer);
//                    break;
//                case Constant.CONN_UDP_MULCAST:
//                    UDPMulticast udpMulticast = new UDPMulticast(connectProperty, new MessageListener() {
//                        @Override
//                        public void onReceive(String hostAddress, byte[] message) {
//                            updateRecord(hostAddress, message);
//                            countRecvBytes(message);
//                        }
//                    });
//                    udpMulticast.create();
//                    connContext.setUdpMulticast(udpMulticast);
//                    break;
//                case Constant.CONN_UDP_BROADCAST:
//                    UDPBroadcast broadcast = new UDPBroadcast(connectProperty, new MessageListener() {
//                        @Override
//                        public void onReceive(String hostAddress, byte[] message) {
//                            updateRecord(hostAddress, message);
//                            countRecvBytes(message);
//                        }
//                    });
//                    broadcast.create();
//                    connContext.setUdpBroadcast(broadcast);
//                    break;
//            }
//        } catch (Exception e) {
//            UdmLog.error(e);
//        }
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
                        updateRecord(hostAddress, message);
                        countSendBytes(message);
                    }
                });
            } else if (configuration.getConnectType().equals(Constant.CONN_TCP_SERVER)) {
                //通过界面选择要发送的目标地址。
                if (currentTargetIp != null) {
                    connContext.getTcpServer().send(currentTargetIp, sendData, new MessageListener() {
                        @Override
                        public void onReceive(String hostAddress, byte[] message) {
                            updateRecord(hostAddress, message);
                            countSendBytes(message);
                        }
                    });
                }
            } else if (configuration.getConnectType().equals(Constant.CONN_UDP_UNICAST_CLIENT)) {
                connContext.getUdpUnicastClient().send(sendData, new MessageListener() {
                    @Override
                    public void onReceive(String hostAddress, byte[] message) {
                        updateRecord(hostAddress, message);
                        countSendBytes(message);
                    }
                });
            } else if (configuration.getConnectType().equals(Constant.CONN_UDP_UNICAST_SERVER)) {
                connContext.getUdpUnicastServer().send(currentTargetIp, sendData, new MessageListener() {
                    @Override
                    public void onReceive(String hostAddress, byte[] message) {
                        updateRecord(hostAddress, message);
                        countSendBytes(message);
                    }
                });
            } else if (configuration.getConnectType().equals(Constant.CONN_UDP_MULCAST)) {
                connContext.getUdpMulticast().send(sendData, new MessageListener() {
                    @Override
                    public void onReceive(String hostAdress, byte[] message) {
                        updateRecord(hostAdress, message);
                        countSendBytes(message);
                    }
                });
            } else if (configuration.getConnectType().equals(Constant.CONN_UDP_BROADCAST)) {
                connContext.getUdpBroadcast().send(sendData, new MessageListener() {
                    @Override
                    public void onReceive(String hostAddress, byte[] message) {
                        updateRecord(hostAddress, message);
                        countSendBytes(message);
                    }
                });
            }
        } catch (Exception e) {
            UdmLog.error(e);
        }
    }

    private void countSendBytes(byte[] message) {
        sendLength += message.length;
        sendCountView.setText("Send:" + sendLength);

    }

    private void countRecvBytes(byte[] message) {
        revLength += message.length;
        recvCountView.setText("Received:" + revLength);
    }

    /**
     * 更新界面
     *
     * @param message
     */
    private void updateRecord(final String hostAddress, final byte[] message) {
        getActivity().runOnUiThread(new Runnable() {
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

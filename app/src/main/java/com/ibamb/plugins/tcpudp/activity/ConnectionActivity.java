package com.ibamb.plugins.tcpudp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ibamb.dnet.module.net.IPUtil;
import com.ibamb.plugins.tcpudp.context.Constant;
import com.ibamb.udm.R;
import com.ibamb.udm.component.constants.UdmConstant;
import com.ibamb.udm.util.TaskBarQuiet;

public class ConnectionActivity extends AppCompatActivity {


    RadioGroup vProtocolRadioGroup;
    RadioGroup vRoleRadioGroup;
    RadioGroup vUdpTransModeRadioGroup;

    Button connectButton;

    private int protocol = Constant.CONN_PROTOCOL_TCP;// 1:tcp 2:udp 默认TCP
    private int role = Constant.CONN_ROLE_CLIENT;// 1:client 2:server 默认客户端
    private int udpTransmissionMode = Constant.CONN_UDP_TRANS_UNICAST;//默认单播

    private void bindViewElement() {

        //协议选择事件
        vProtocolRadioGroup = findViewById(R.id.protocol_radio_group);
        vProtocolRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.tcp_radio) {
                    protocol = Constant.CONN_PROTOCOL_TCP;
                } else if (checkedId == R.id.udp_radio) {
                    protocol = Constant.CONN_PROTOCOL_UDP;
                }
                changeViewToDisplay();
            }
        });
        //工作角色事件（选择本地作为客户端或者服务端）
        vRoleRadioGroup = findViewById(R.id.role_radio_group);
        vRoleRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.client_radio) {
                    role = Constant.CONN_ROLE_CLIENT;
                } else if (checkedId == R.id.server_radio) {
                    role = Constant.CONN_ROLE_SERVER;
                }
                changeViewToDisplay();
            }
        });
        //UDP 传输方式切换事件
        vUdpTransModeRadioGroup = findViewById(R.id.udp_trans_mode_group);
        vUdpTransModeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.udp_uni_radio) {
                    udpTransmissionMode = Constant.CONN_UDP_TRANS_UNICAST;
                } else if (checkedId == R.id.udp_mul_radio) {
                    udpTransmissionMode = Constant.CONN_UDP_TRANS_MULCAST;
                } else if (checkedId == R.id.udp_broad_radio) {
                    udpTransmissionMode = Constant.CONN_UDP_TRANS_BROADCAST;
                }
                changeViewToDisplay();
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);
        bindViewElement();
        TaskBarQuiet.setStatusBarColor(this, UdmConstant.TASK_BAR_COLOR);
        findViewById(R.id.go_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.do_commit).setVisibility(View.GONE);
        TextView title = findViewById(R.id.title);
        title.setText("Connection property");
        connectButton = findViewById(R.id.connect_btn);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean canContinue = true;
                // TCP
                EditText vRemoteAddr = findViewById(R.id.tcp_remote_host);
                EditText vRemotePort = findViewById(R.id.tcp_remote_port);
                EditText vLocalPort = findViewById(R.id.tcp_local_port);
                // UDP UNICAST
                EditText vUniTargetAddr = findViewById(R.id.udp_uni_target_address);
                EditText vUniTargetPort = findViewById(R.id.udp_uni_target_port);
                EditText vUniLocalPort = findViewById(R.id.udp_uni_local_port);
                // UDP MULTICAST
                EditText vMulAddress = findViewById(R.id.multicast_address);
                EditText vMulPort = findViewById(R.id.multicast_port);
                //UDP BROADCAST
                EditText vBroadcastPort = findViewById(R.id.broadcast_port);


                String checkNotice = "";
                if (canContinue) {
                    String connectionType = "";
                    if (protocol == Constant.CONN_PROTOCOL_TCP) {
                        if (role == Constant.CONN_ROLE_CLIENT) {
                            connectionType = Constant.CONN_TCP_CLIENT;
                            String remoteAddr = vRemoteAddr.getText().toString();
                            String reportPort = vRemotePort.getText().toString();
                            if (remoteAddr == null || remoteAddr.trim().length() == 0 || !IPUtil.isIPAddress(remoteAddr)) {
                                canContinue = false;
                                checkNotice = "Invalid remote host or the value is empty.";
                            } else if (reportPort == null || reportPort.trim().length() == 0) {
                                canContinue = false;
                                checkNotice = "Remote port can't be mepty.";
                            }
                        } else if (role == Constant.CONN_ROLE_SERVER) {
                            connectionType = Constant.CONN_TCP_SERVER;
                            String localPort = vLocalPort.getText().toString();
                            if (localPort == null || localPort.trim().length() == 0) {
                                canContinue = false;
                                checkNotice = "Local port can't be empty.";
                            }
                        }
                    } else if (protocol == Constant.CONN_PROTOCOL_UDP) {
                        if (role == Constant.CONN_ROLE_CLIENT) {
                            if (udpTransmissionMode == Constant.CONN_UDP_TRANS_UNICAST) {
                                connectionType = Constant.CONN_UDP_UNICAST_CLIENT;
                                String uniTarget = vUniTargetAddr.getText().toString();
                                String uniTargetPort = vUniTargetPort.getText().toString();
                                if (isEmptyValue(uniTarget) || !IPUtil.isIPAddress(uniTarget)) {
                                    canContinue = false;
                                    checkNotice = "Invalid remote address or empty value";
                                } else if (isEmptyValue(uniTargetPort)) {
                                    canContinue = false;
                                    checkNotice = "Remote port cant't be empty.";
                                }
                            } else if (udpTransmissionMode == Constant.CONN_UDP_TRANS_MULCAST) {
                                connectionType = Constant.CONN_UDP_MULCAST;
                                String mulAddress = vMulAddress.getText().toString();
                                String mulPort = vMulPort.getText().toString();
                                if (isEmptyValue(mulAddress)) {
                                    canContinue = false;
                                    checkNotice = "Multicast address can't be empty.";
                                } else if (isEmptyValue(mulPort)) {
                                    canContinue = false;
                                    checkNotice = "Multicast port can't be empty.";
                                }
                            } else if (udpTransmissionMode == Constant.CONN_UDP_TRANS_BROADCAST) {
                                connectionType = Constant.CONN_UDP_BROADCAST;
                                String broadcastPort = vBroadcastPort.getText().toString();
                                if (isEmptyValue(broadcastPort)) {
                                    canContinue = false;
                                    checkNotice = "Broadcast port can't be empty.";
                                }
                            }
                        } else if (role == Constant.CONN_ROLE_SERVER) {
                            if (udpTransmissionMode == Constant.CONN_UDP_TRANS_UNICAST) {
                                connectionType = Constant.CONN_UDP_UNICAST_SERVER;
                                String uniLocalPort = vUniLocalPort.getText().toString();
                                if (isEmptyValue(uniLocalPort)) {
                                    canContinue = false;
                                    checkNotice = "Broadcast port can't be empty.";
                                }
                            } else if (udpTransmissionMode == Constant.CONN_UDP_TRANS_MULCAST) {
                                connectionType = Constant.CONN_UDP_MULCAST;
                                String mulAddress = vMulAddress.getText().toString();
                                String mulPort = vMulPort.getText().toString();
                                if (isEmptyValue(mulAddress)) {
                                    canContinue = false;
                                    checkNotice = "Multicast address can't be empty.";
                                } else if (isEmptyValue(mulPort)) {
                                    canContinue = false;
                                    checkNotice = "Multicast port can't be empty.";
                                }
                            } else if (udpTransmissionMode == Constant.CONN_UDP_TRANS_BROADCAST) {
                                connectionType = Constant.CONN_UDP_BROADCAST;
                                String broadcastPort = vBroadcastPort.getText().toString();
                                if (isEmptyValue(broadcastPort)) {
                                    canContinue = false;
                                    checkNotice = "Broadcast port can't be empty.";
                                }
                            }
                        }
                    }
                    if (canContinue) {
                        Intent intent = new Intent(ConnectionActivity.this, WorkSpaceActivity.class);
                        intent.putExtra("TCP_REMOTE_HOST", vRemoteAddr.getText().toString());
                        intent.putExtra("TCP_REMOTE_PORT", vRemotePort.getText().toString());
                        intent.putExtra("TCP_LOCAL_PORT", vLocalPort.getText().toString());

                        intent.putExtra("UDP_UNI_TARGET_HOST", vUniTargetAddr.getText().toString());
                        intent.putExtra("UDP_UNI_TARGET_PORT", vUniTargetPort.getText().toString());
                        intent.putExtra("UDP_UNI_LOCAL_PORT", vUniLocalPort.getText().toString());

                        intent.putExtra("UDP_MUL_ADDRESS", vMulAddress.getText().toString());
                        intent.putExtra("UDP_MUL_PORT", vMulPort.getText().toString());

                        intent.putExtra("UDP_BROADCAST_PORT", vBroadcastPort.getText().toString());

                        intent.putExtra("CONNECTION_TYPE", connectionType);

                        intent.putExtra("WORK_ROLE", role);
                        startActivity(intent);
                    } else {
                        Snackbar.make(connectButton, checkNotice, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }
            }
        });
    }

    private boolean isEmptyValue(String value) {
        return value == null || value.trim().length() == 0;
    }

    /**
     * 根据 role 和 protocol 以及 udp 通信方式决定展现哪些控件。
     */
    private void changeViewToDisplay() {


        if (role == Constant.CONN_ROLE_SERVER) {
            connectButton.setText("Start");
        } else {
            connectButton.setText("Connect");
        }
        if (protocol == Constant.CONN_PROTOCOL_TCP) {
            findViewById(R.id.tcp_view_container).setVisibility(View.VISIBLE);
            findViewById(R.id.udp_view_container).setVisibility(View.GONE);
            if (role == Constant.CONN_ROLE_SERVER) {
                findViewById(R.id.tcp_remote_host_ly).setVisibility(View.GONE);
                findViewById(R.id.tcp_remote_port_ly).setVisibility(View.GONE);
            } else if (role == Constant.CONN_ROLE_CLIENT) {
                findViewById(R.id.tcp_remote_host_ly).setVisibility(View.VISIBLE);
                findViewById(R.id.tcp_remote_port_ly).setVisibility(View.VISIBLE);
            }
        } else if (protocol == Constant.CONN_PROTOCOL_UDP) {
            findViewById(R.id.tcp_view_container).setVisibility(View.GONE);
            findViewById(R.id.udp_view_container).setVisibility(View.VISIBLE);
            if (role == Constant.CONN_ROLE_CLIENT) {
                if (udpTransmissionMode == Constant.CONN_UDP_TRANS_UNICAST) {
                    findViewById(R.id.udp_uni_view_container).setVisibility(View.VISIBLE);
                    findViewById(R.id.udp_mul_view_container).setVisibility(View.GONE);
                    findViewById(R.id.udp_broad_view_container).setVisibility(View.GONE);
                    findViewById(R.id.udp_uni_target_address_ly).setVisibility(View.VISIBLE);
                    findViewById(R.id.udp_uni_target_port_ly).setVisibility(View.VISIBLE);
                } else if (udpTransmissionMode == Constant.CONN_UDP_TRANS_MULCAST) {
                    findViewById(R.id.udp_uni_view_container).setVisibility(View.GONE);
                    findViewById(R.id.udp_mul_view_container).setVisibility(View.VISIBLE);
                    findViewById(R.id.udp_broad_view_container).setVisibility(View.GONE);
                } else if (udpTransmissionMode == Constant.CONN_UDP_TRANS_BROADCAST) {
                    findViewById(R.id.udp_uni_view_container).setVisibility(View.GONE);
                    findViewById(R.id.udp_mul_view_container).setVisibility(View.GONE);
                    findViewById(R.id.udp_broad_view_container).setVisibility(View.VISIBLE);
                }

            } else if (role == Constant.CONN_ROLE_SERVER) {
                if (udpTransmissionMode == Constant.CONN_UDP_TRANS_UNICAST) {
                    findViewById(R.id.udp_uni_view_container).setVisibility(View.VISIBLE);
                    findViewById(R.id.udp_mul_view_container).setVisibility(View.GONE);
                    findViewById(R.id.udp_broad_view_container).setVisibility(View.GONE);
                    findViewById(R.id.udp_uni_target_address_ly).setVisibility(View.GONE);
                    findViewById(R.id.udp_uni_target_port_ly).setVisibility(View.GONE);

                } else if (udpTransmissionMode == Constant.CONN_UDP_TRANS_MULCAST) {
                    findViewById(R.id.udp_uni_view_container).setVisibility(View.GONE);
                    findViewById(R.id.udp_mul_view_container).setVisibility(View.VISIBLE);
                    findViewById(R.id.udp_broad_view_container).setVisibility(View.GONE);

                } else if (udpTransmissionMode == Constant.CONN_UDP_TRANS_BROADCAST) {
                    findViewById(R.id.udp_uni_view_container).setVisibility(View.GONE);
                    findViewById(R.id.udp_mul_view_container).setVisibility(View.GONE);
                    findViewById(R.id.udp_broad_view_container).setVisibility(View.VISIBLE);

                }
            }
        }
    }
}

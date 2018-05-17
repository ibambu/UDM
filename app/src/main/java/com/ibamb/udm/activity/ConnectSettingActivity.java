package com.ibamb.udm.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.ibamb.udm.R;

public class ConnectSettingActivity extends AppCompatActivity {

    private TextView vTcpSetting;
    private TextView vUdpSetting;
    private TextView vSerailSetting;


    private ImageView vTcpMore;
    private ImageView vUdpMore;
    private ImageView vSerailMore;

    private Switch vTcpEnabled;
    private Switch vUdpEnabled;



    private String ip;
    private String mac;

    private static final String NET_PROTOCOL_ID="CONN_NET_PROTOCOL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_setting);

        Bundle bundle = getIntent().getExtras();
        ip = (String) bundle.get("HOST_ADDRESS");
        mac = (String) bundle.get("HOST_MAC");

        vTcpSetting = findViewById(R.id.tcp_connection_setting);
        vUdpSetting = findViewById(R.id.udp_connection_setting);
        vSerailSetting = findViewById(R.id.serial_setting);

        vTcpMore = findViewById(R.id.tcp_connection_more);
        vUdpMore = findViewById(R.id.udp_connection_more);
        vSerailMore = findViewById(R.id.serial_setting_more);


        vTcpEnabled = findViewById(R.id.tcp_enanbled_switch);
        vUdpEnabled = findViewById(R.id.udp_enanbled_switch);

        vTcpSetting.setOnClickListener(menuItemClickListener);
        vUdpSetting.setOnClickListener(menuItemClickListener);
        vSerailSetting.setOnClickListener(menuItemClickListener);
        vTcpEnabled.setOnClickListener(menuItemClickListener);
        vUdpEnabled.setOnClickListener(menuItemClickListener);

        vTcpMore.setOnClickListener(menuItemClickListener);
        vUdpMore.setOnClickListener(menuItemClickListener);
        vSerailMore.setOnClickListener(menuItemClickListener);
    }

    private View.OnClickListener menuItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = null;

            switch (v.getId()) {
                case R.id.tcp_connection_setting:
                    intent = new Intent(v.getContext(), TCPConnectionActivity.class);
                    break;
                case R.id.udp_connection_setting:
                    intent = new Intent(v.getContext(), UDPConnectionActivity.class);
                    break;
                case R.id.serial_setting:
                    intent = new Intent(v.getContext(), SerialActivity.class);
                    break;
                default:
                    break;
            }
            Bundle params = new Bundle();
            params.putString("HOST_ADDRESS", ip);
            params.putString("HOST_MAC", mac);
            intent.putExtras(params);
            if(intent!=null){
                startActivity(intent);
            }
        }
    };
}

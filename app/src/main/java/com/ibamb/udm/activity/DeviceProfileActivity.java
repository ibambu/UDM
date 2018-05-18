package com.ibamb.udm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ibamb.udm.R;

public class DeviceProfileActivity extends AppCompatActivity {

    private String ip;
    private String mac;

    private ImageView icSettingIp;

    private TextView vSettingIP;
    private TextView vSettingConnect;
    private TextView vSettingOther;
    private TextView vSettingAccess;

    private ImageView icSettingConnect;
    private ImageView icSettingOther;
    private ImageView icSettingAccess;

    private View.OnClickListener profileMenuClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = null;

            String channelId = "0";
            switch (v.getId()) {
                case R.id.profile_ip_more:
                case R.id.profile_ip_setting:
                    intent = new Intent(v.getContext(), IPSettingActivity.class);
                    break;
                case R.id.profile_access_more:
                case R.id.profile_access_setting:
                    intent = new Intent(v.getContext(), AccessSettingActivity.class);
                    break;
                case R.id.profile_connect_setting:
                case R.id.profile_connect_more:
                    intent = new Intent(v.getContext(), ConnectSettingActivity.class);
                    channelId ="1";
                    break;
                case R.id.profile_other_setting:
                case R.id.profile_other_more:
                    intent = new Intent(v.getContext(), OtherSettingActivity.class);
                    break;
                default:
                    break;
            }
            Bundle params = new Bundle();
            params.putString("HOST_ADDRESS", ip);
            params.putString("HOST_MAC", mac);
            params.putString("CHNL_ID",channelId);
            if(intent!=null){
                intent.putExtras(params);
                startActivity(intent);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_profile);
        Bundle bundle = getIntent().getExtras();
        ip = (String) bundle.get("HOST_ADDRESS");
        mac = (String) bundle.get("HOST_MAC");
        TextView vIp = findViewById(R.id.profile_ip);
        TextView vMac = findViewById(R.id.profile_mac);
        vIp.setText(ip);
        vMac.setText(mac);

        vSettingIP = findViewById(R.id.profile_ip_setting);
        vSettingConnect = findViewById(R.id.profile_connect_setting);
        vSettingOther = findViewById(R.id.profile_other_setting);
        vSettingAccess = findViewById(R.id.profile_access_setting);

        icSettingIp = findViewById(R.id.profile_ip_more);
        icSettingConnect = findViewById(R.id.profile_connect_more);
        icSettingOther=findViewById(R.id.profile_other_more);
        icSettingAccess=findViewById(R.id.profile_access_more);

        vSettingIP.setOnClickListener(profileMenuClickListener);
        vSettingConnect.setOnClickListener(profileMenuClickListener);
        vSettingOther.setOnClickListener(profileMenuClickListener);
        vSettingAccess.setOnClickListener(profileMenuClickListener);

        icSettingIp.setOnClickListener(profileMenuClickListener);
        icSettingConnect.setOnClickListener(profileMenuClickListener);
        icSettingOther.setOnClickListener(profileMenuClickListener);
        icSettingAccess.setOnClickListener(profileMenuClickListener);

    }


}

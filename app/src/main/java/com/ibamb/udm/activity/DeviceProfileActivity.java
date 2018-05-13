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

    private ImageView vSettingIp;
    private TextView vSettingConnect;
    private TextView vSettingOther;

    private View.OnClickListener profileMenuClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = null;

            switch (v.getId()) {
                case R.id.profile_ip_more:
                    intent = new Intent(v.getContext(), IPSettingActivity.class);
                    break;
                case R.id.profile_connect_setting:
                    intent = new Intent(v.getContext(), ConnectSettingActivity.class);
                    break;
                case R.id.profile_other_setting:
                    intent = new Intent(v.getContext(), OtherSettingActivity.class);
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

        vSettingIp = findViewById(R.id.profile_ip_more);
        vSettingConnect = findViewById(R.id.profile_connect_setting);
        vSettingOther = findViewById(R.id.profile_other_setting);

        vSettingIp.setOnClickListener(profileMenuClickListener);
        vSettingConnect.setOnClickListener(profileMenuClickListener);
        vSettingOther.setOnClickListener(profileMenuClickListener);

    }


}

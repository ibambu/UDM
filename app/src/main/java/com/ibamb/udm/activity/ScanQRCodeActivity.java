package com.ibamb.udm.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.WriterException;
import com.google.zxing.client.android.Intents;
import com.ibamb.udm.R;
import com.ibamb.udm.component.LoginComponent;
import com.ibamb.udm.module.constants.Constants;
import com.ibamb.udm.util.BitmapUtil;
import com.ibamb.udm.util.TaskBarQuiet;
import com.journeyapps.barcodescanner.CaptureActivity;

public class ScanQRCodeActivity extends AppCompatActivity {
    private TextView resultTextView;
    private Button scanBarCodeButton;
    private ImageView iv_qr_image;

    protected int mScreenWidth ;
    private ImageView commit;
    private ImageView back;
    private TextView title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qrcode);
        TaskBarQuiet.setStatusBarColor(this, Constants.TASK_BAR_COLOR);

        resultTextView = (TextView) this.findViewById(R.id.scan_result);
        scanBarCodeButton = (Button) this.findViewById(R.id.bt_bigin_scan);
        iv_qr_image = (ImageView)findViewById(R.id.iv_qr_image);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;

        scanBarCodeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 调用ZXIng开源项目源码  扫描二维码
                Intent openCameraIntent = new Intent(ScanQRCodeActivity.this, CaptureActivity.class);
                startActivityForResult(openCameraIntent, 0);
            }
        });

        back = findViewById(R.id.go_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        title = findViewById(R.id.title);
        title.setText("Scan QR code");
        commit = findViewById(R.id.do_commit);
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 取得返回信息
        if (resultCode == RESULT_OK) {

            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString(Intents.Scan.RESULT);
            if(isMac(scanResult)){
                resultTextView.setText("");
                LoginComponent loginComponent = new LoginComponent(this,scanResult,null);
                loginComponent.setToProfile(true);
                loginComponent.setSyncMenuEnabled(true);
                loginComponent.login();
            }else{
                resultTextView.setText(scanResult);
            }
        }
    }
    private boolean isMac(String val) {
        String regxMacAddress = "([A-Fa-f0-9]{2}:){5}[A-Fa-f0-9]{2}";
        if (val.matches(regxMacAddress)) {
            return true;
        } else {
            return false;
        }
    }
    //调用浏览器打开，功能尚未完善、、、
    public void checkResult(View v){
        String result = resultTextView.getText().toString();
////      Intent intent = new Intent(ZxingFrame.this,
////              CheckResult.class);
////      intent.putExtra("result", result);
////      startActivity(intent);
//
//        Intent i= new Intent();
//        i.setAction("android.intent.action.VIEW");
//        Uri content_url = Uri.parse(result);
//        i.setData(content_url);
//        i.setClassName("com.android.browser","com.android.browser.BrowserActivity");
//        startActivity(i);
    }
    //生成二维码
    public void Create2QR(View v){
        String uri = resultTextView.getText().toString();
//      Bitmap bitmap = BitmapUtil.create2DCoderBitmap(uri, mScreenWidth/2, mScreenWidth/2);
        Bitmap bitmap;
        try {
            bitmap = BitmapUtil.createQRCode(uri, mScreenWidth);

            if(bitmap != null){
                iv_qr_image.setImageBitmap(bitmap);
            }

        } catch (WriterException e) {

        }
    }
}

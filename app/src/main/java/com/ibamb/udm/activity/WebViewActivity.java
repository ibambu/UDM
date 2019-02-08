package com.ibamb.udm.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ibamb.udm.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebViewActivity extends AppCompatActivity {

    private WebView vWebView;
    private ProgressBar progressbar;
    private ImageView commit;
    private ImageView back;
    private TextView title;
    private TextView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        content = findViewById(R.id.content);

        vWebView = findViewById(R.id.mywebview);
        progressbar = findViewById(R.id.progressbar);
        commit = findViewById(R.id.do_commit);
        back = findViewById(R.id.go_back);

        commit.setVisibility(View.GONE);
        back.setVisibility(View.GONE);
        title=findViewById(R.id.title);
        title.setText("Scan Result");
        Intent intent = getIntent();
        final String webUrl = intent.getStringExtra("WEB_URL");
        //是否URL
        Pattern pattern = Pattern.compile("^((ht|f)tps?):\\/\\/[\\w\\-]+(\\.[\\w\\-]+)+([\\w\\-\\.,@?^=%&:\\/~\\+#]*[\\w\\-\\@?^=%&\\/~\\+#])?$");
        Matcher match = pattern.matcher(webUrl);
        if(match.find()){
            vWebView.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    super.onProgressChanged(view, newProgress);
                    if (newProgress == 100) {
                        progressbar.setVisibility(View.GONE);
                    } else {
                        if (progressbar.getVisibility() == View.GONE) {
                            progressbar.setVisibility(View.VISIBLE);
                        }
                        progressbar.setProgress(newProgress);
                    }

                }
            });

            vWebView.setWebViewClient(new WebViewClient());
            vWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            vWebView.getSettings().setJavaScriptEnabled(true);
            vWebView.getSettings().setDomStorageEnabled(true);
            vWebView.getSettings().setDatabaseEnabled(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //混合加载http和https
                vWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            }
            //声明WebSettings子类
            WebSettings webSettings = vWebView.getSettings();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                webSettings.setSafeBrowsingEnabled(false);
            }
            content.setVisibility(View.GONE);
            vWebView.loadUrl(webUrl);
        }else{
            content.setText(webUrl);
            content.setVisibility(View.VISIBLE);
        }
    }
}

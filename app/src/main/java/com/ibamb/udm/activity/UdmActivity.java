package com.ibamb.udm.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.ibamb.udm.R;
import com.ibamb.udm.listener.UdmToolbarMenuClickListener;

import java.lang.reflect.Method;

/**
 * 带有顶部导航的Activity,其他需要有导航条的地方可以继承该类.
 */
public class UdmActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    public void setParentContentView(int layoutResId) {
        /**
         * v_content是在基类布局文件中预定义的layout区域
         */
        LinearLayout vContent = (LinearLayout) findViewById(R.id.udm_content);
        /**
         * 通过LayoutInflater填充基类的layout区域
         */
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(layoutResId, null);
        vContent.addView(v);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_udm);

        //将ActionBar位置改放Toolbar.
        mToolbar = (Toolbar) findViewById(R.id.udm_toolbar);
        mToolbar.setTitle("udm");
        setSupportActionBar(mToolbar);

        //设置右上角的填充菜单
        mToolbar.inflateMenu(R.menu.tool_bar_menu);
        //这句代码使启用Activity回退功能，并显示Toolbar上的左侧回退图标
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        //绑定菜单点击事件
        mToolbar.setOnMenuItemClickListener(new UdmToolbarMenuClickListener());

        //此处需要优化,将请求网络资源的操作放在一个单独的线程里面执行.避免网络延时界面假死.
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        //沉静式工具栏,将任务栏的背景改为与Toolbar背景一致.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tool_bar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected boolean onPrepareOptionsPanel(View view, Menu menu) {
        if (menu != null) {
            if (menu.getClass() == MenuBuilder.class) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                }
            }
        }
        return super.onPrepareOptionsPanel(view, menu);
    }
}

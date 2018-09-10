package com.ibamb.udm.component.guide;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ibamb.udm.R;
import com.ibamb.udm.component.constants.UdmConstant;
import com.ibamb.udm.guide.guideview.Component;
import com.ibamb.dnet.module.constants.Constants;
import com.ibamb.dnet.module.log.UdmLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.content.Context.MODE_APPEND;


public class MainActivityGuide implements Component {

    private String guideDesc;
    private int xOffset;

    public void setxOffset(int xOffset) {
        this.xOffset = xOffset;
    }

    public MainActivityGuide(String guideDesc) {
        this.guideDesc = guideDesc;
    }

    @Override
    public View getView(final LayoutInflater inflater) {
        final LinearLayout ll = new LinearLayout(inflater.getContext());
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setLayoutParams(param);
        TextView textView = new TextView(inflater.getContext());
        textView.setText(guideDesc);
        textView.setTextColor(inflater.getContext().getResources().getColor(R.color.udm_white));
        textView.setTextSize(15);
        ImageView imageView = new ImageView(inflater.getContext());
        imageView.setImageResource(R.mipmap.ic_action_guide);
        ll.removeAllViews();
        ll.addView(textView);
        ll.addView(imageView);

        final CheckBox checkBox = new CheckBox(inflater.getContext());
        checkBox.setText("show on startup");
        checkBox.setTextColor(inflater.getContext().getResources().getColor(R.color.udm_light_black));
        checkBox.setTextSize(15);
        checkBox.setBackgroundColor(inflater.getContext().getResources().getColor(R.color.text_gray));
        checkBox.setChecked(true);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                FileOutputStream outputStream = null;
                String flag = isChecked ? "1" : "0";
                try {
                    File[] files = inflater.getContext().getFilesDir().listFiles();
                    for (File file : files) {
                        if (file.getName().equals(UdmConstant.FILE_GUIDE_CONF)) {
                            file.delete();//删除已有文件
                            break;
                        }
                    }
                    outputStream = inflater.getContext().openFileOutput(UdmConstant.FILE_GUIDE_CONF, MODE_APPEND);
                    outputStream.write(flag.getBytes());//写入新文件
                } catch (Exception e) {
                    UdmLog.error(e);
                } finally {
                    try {
                        if (outputStream != null) {
                            outputStream.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        ll.addView(checkBox);

        return ll;
    }

    @Override
    public int getAnchor() {
        return Component.ANCHOR_BOTTOM;
    }

    @Override
    public int getFitPosition() {
        return Component.FIT_CENTER;
    }

    @Override
    public int getXOffset() {
        return xOffset;
    }

    @Override
    public int getYOffset() {
        return 20;
    }
}

package com.ibamb.udm.component.guide;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ibamb.udm.R;
import com.ibamb.udm.guide.guideview.Component;

public class SimpleComponent implements Component {
    @Override public View getView(LayoutInflater inflater) {

        RelativeLayout ll = (RelativeLayout) inflater.inflate(R.layout.activity_main, null);
        ll.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                Toast.makeText(view.getContext(), "引导层被点击了", Toast.LENGTH_SHORT).show();
            }
        });
        return ll;
    }

    @Override public int getAnchor() {
        return Component.ANCHOR_BOTTOM;
    }

    @Override public int getFitPosition() {
        return Component.FIT_END;
    }

    @Override public int getXOffset() {
        return 0;
    }

    @Override public int getYOffset() {
        return 10;
    }
}

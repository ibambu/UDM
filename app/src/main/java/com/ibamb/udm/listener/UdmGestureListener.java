package com.ibamb.udm.listener;

import android.app.Activity;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.ibamb.dnet.module.beans.ChannelParameter;
import com.ibamb.udm.task.ChannelParamReadAsyncTask;

public class UdmGestureListener extends SimpleOnGestureListener {

    private int verticalMinDistance = 20;
    private int minVelocity         = 0;

    private ChannelParameter channelParameter;
    private View currentView;
    private Activity activity;

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return super.onDoubleTap(e);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return super.onDown(e);
    }

    public UdmGestureListener(Activity activity,ChannelParameter channelParameter, View currentView) {
        this.activity = activity;
        this.channelParameter = channelParameter;
        this.currentView = currentView;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        if (((e1.getX() - e2.getX() > verticalMinDistance||e2.getX() - e1.getX() > verticalMinDistance) && Math.abs(velocityX) > minVelocity)
                ||(e1.getY() - e2.getY() > verticalMinDistance||e2.getY() - e1.getY() > verticalMinDistance && Math.abs(velocityY) > minVelocity)) {

            ChannelParamReadAsyncTask readerAsyncTask = new ChannelParamReadAsyncTask(activity,currentView,channelParameter);
            readerAsyncTask.execute(channelParameter.getMac());
            Toast.makeText(currentView.getContext(), "Refreshed.", Toast.LENGTH_SHORT).show();
        }
        return super.onFling(e1, e2, velocityX, velocityY);
    }

    @Override
    public void onLongPress(MotionEvent e){
        super.onLongPress(e);
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2,
                            float distanceX, float distanceY) {
        return super.onScroll(e1, e2, distanceX, distanceY);
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e){
        return super.onSingleTapUp(e);
    }

}

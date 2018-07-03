package com.ibamb.udm.task;

import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ibamb.udm.R;
import com.ibamb.udm.module.beans.ChannelParameter;
import com.ibamb.udm.module.constants.Constants;
import com.ibamb.udm.module.instruct.IParamReader;
import com.ibamb.udm.module.instruct.ParamReader;
import com.ibamb.udm.util.ViewElementDataUtil;


public class ChannelParamReadAsyncTask extends AsyncTask<String, String, ChannelParameter> {

    private View view;
    private ChannelParameter channelParameter;

    public ChannelParamReadAsyncTask(View view,ChannelParameter channelParameter) {
        this.view = view;
        this.channelParameter = channelParameter;
    }

    @Override
    protected ChannelParameter doInBackground(String... strings) {
        try {

            IParamReader reader = new ParamReader();
            int tryCount = 0;
            /**
             * 如果无数据返回，重试3次。
             */
            reader.readChannelParam(channelParameter);
            while (!channelParameter.isSuccessful() && tryCount < 3) {
                publishProgress(Constants.WAIT_READ_PARAM);
                reader.readChannelParam(channelParameter);
                tryCount++;
            }

        } catch (Exception e) {

        }
        return channelParameter;
    }

    @Override
    protected void onPostExecute(ChannelParameter channelParameter) {
        super.onPostExecute(channelParameter);
        //更新界面数据
        String notice = "";
        if(!channelParameter.isSuccessful()){
            notice = "Possible network delay. Please click title try again.";
            Toast.makeText(view.getContext(), notice,Toast.LENGTH_SHORT).show();
//            Snackbar.make(view.findViewById(R.id.anchor),  notice, Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show();
        }else{
            ViewElementDataUtil.setData(channelParameter, view);
        }
        TextView title = view.findViewById(R.id.title);
        if(title!=null){
            String titleValue = title.getText().toString();
            title.setText(titleValue.replaceAll(Constants.WAIT_READ_PARAM,""));
        }
    }

    @Override
    protected void onProgressUpdate(String... values) {
        TextView title = view.findViewById(R.id.title);
        if(title!=null && !title.getText().toString().contains(Constants.WAIT_READ_PARAM)){
            title.setText(title.getText().toString()+ Constants.WAIT_READ_PARAM);
        }
        super.onProgressUpdate(values);
    }
}

package com.ibamb.udm.task;

import android.app.Activity;
import android.os.AsyncTask;

import com.ibamb.dnet.module.api.UdmClient;
import com.ibamb.dnet.module.core.ParameterMapping;
import com.ibamb.udm.component.security.ParameterMappingLoader;
import com.ibamb.dnet.module.instruct.beans.Parameter;

import java.util.Map;

/**
 * 初始化应用数据
 */

public class UdmInitAsyncTask extends AsyncTask<String, Void, Map<String,Parameter>> {

    private Activity activity;

    public UdmInitAsyncTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected Map<String, Parameter> doInBackground(String...strings) {

        Map<String,Parameter> mapping  = ParameterMappingLoader.loadParameterMapping(activity);
        if(mapping!=null&& !mapping.isEmpty()){
            ParameterMapping.setCustParameterMapping(mapping);
        }else{
            UdmClient.getInstance();
        }
        return mapping;
    }
}

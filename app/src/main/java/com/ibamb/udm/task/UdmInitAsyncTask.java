package com.ibamb.udm.task;

import android.content.res.AssetManager;
import android.os.AsyncTask;

import com.ibamb.udm.module.core.ParameterMapping;
import com.ibamb.udm.module.core.ParameterMappingManager;
import com.ibamb.udm.module.instruct.beans.Parameter;

import java.util.Map;

/**
 * 初始化应用数据
 * Created by luotao on 18-4-21.
 */

public class UdmInitAsyncTask extends AsyncTask<AssetManager, Void, Map<String,Parameter>> {

    @Override
    protected Map<String, Parameter> doInBackground(AssetManager... assetManagers) {
        Map<String,Parameter> mapping = ParameterMappingManager.loadParameterMapping(assetManagers[0]);
        ParameterMapping.setParameterMap(mapping);
        return mapping;
    }
}

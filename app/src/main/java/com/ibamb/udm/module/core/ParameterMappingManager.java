package com.ibamb.udm.module.core;

import android.content.res.AssetManager;

import com.ibamb.udm.module.constants.Constants;
import com.ibamb.udm.module.instruct.beans.Parameter;
import com.ibamb.udm.module.instruct.beans.ValueMapping;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParameterMappingManager {



    public static Map<String,Parameter> loadParameterMapping(AssetManager assetManager) {
        Map<String,Parameter> mapping = new HashMap();
        BufferedReader bufreader = null;
        try {
            InputStream in = assetManager.open(Constants.FILE_PARAM_MAPPING);
            bufreader = new BufferedReader(new InputStreamReader(in, Constants.DEFAULT_CHARSET));
            String readline = null;
            while ((readline = bufreader.readLine()) != null) {
                String[] dataArray = readline.split(Constants.FILE_PARAM_MAPPING_COLUMN_SPLIT);
                int cellCount = 0;
                Parameter param = new Parameter();
                param.setParamType(Integer.parseInt(dataArray[cellCount++]));//参数类型
                param.setChannelId(Integer.parseInt(dataArray[cellCount++]));//通道ID
                param.setId(dataArray[cellCount++]);//字符参数ID
                param.setViewTagId(dataArray[cellCount++]);//现实参数的界面控件ID
                param.setElementType(Integer.parseInt(dataArray[cellCount++]));//界面控件类型
                param.setDecId(Integer.parseInt(dataArray[cellCount++]));//参数对应的十进制ID
                param.setHexId(Integer.parseInt(dataArray[cellCount++],16));//参数对应的十六进制ID
                param.setCovertType(Integer.parseInt(dataArray[cellCount++]));//参数转换类型
                param.setByteLength(Integer.parseInt(dataArray[cellCount++]));//参数所占长度
                List<ValueMapping> vMappings = new ArrayList<>();
                param.setValueMappings(vMappings);
                //如果是枚举值，则将枚举值对应的显示值都放入VaueMapping对象中。
                if(dataArray.length>9){
                    String enumValues = dataArray[cellCount++];
                    String displayEnumValues = dataArray[cellCount++];
                    if (enumValues!=null && displayEnumValues!=null) {
                        String[] values = enumValues.split(Constants.FILE_ENUM_VALUE_SPLIT);
                        String[] names = displayEnumValues.split(Constants.FILE_ENUM_VALUE_SPLIT);
                        if(values.length==names.length){
                            for (int i = 0; i < values.length; i++) {
                                ValueMapping vmapping = new ValueMapping();
                                vmapping.setValue(values[i]);
                                vmapping.setDisplayValue(names[i]);
                                vMappings.add(vmapping);
                                vmapping.setParamId(param.getId());
                            }
                        }
                    }
                }
                mapping.put(param.getId(), param);
            }
        } catch (IOException e) {
        }finally {
            if(bufreader!=null){
                try {
                    bufreader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return mapping;
    }
}

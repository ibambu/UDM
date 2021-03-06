package com.ibamb.udm.component.security;

import android.app.Activity;

import com.ibamb.dnet.module.instruct.beans.Parameter;
import com.ibamb.dnet.module.instruct.beans.ValueMapping;
import com.ibamb.dnet.module.log.UdmLog;
import com.ibamb.dnet.module.security.AESUtil;
import com.ibamb.udm.component.constants.UdmConstant;
import com.ibamb.udm.component.file.FileDirManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParameterMappingLoader {

    public static Map<String, Parameter> loadParameterMapping(Activity activity) {
        Map<String, Parameter> mapping = new HashMap();
        BufferedReader bufferedReader = null;
        try {

            FileDirManager fileDirManager = new FileDirManager(activity);
            File mappingFile = fileDirManager.getFileByName(UdmConstant.FILE_PARAM_MAPPING);
            if (mappingFile != null) {
                FileInputStream inputStream = activity.openFileInput(UdmConstant.FILE_PARAM_MAPPING);
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream, UdmConstant.DEFAULT_CHARSET));
                String readLine = null;
                StringBuilder typeBuffer = new StringBuilder();
                while ((readLine = bufferedReader.readLine()) != null) {
                    typeBuffer.append(readLine);
                }
                String decodeTypeString = AESUtil.aesDecrypt(typeBuffer.toString(), "1qaz2wsx3edc4rfv");//解密
                if (decodeTypeString != null) {
                    String[] typeLines = decodeTypeString.split("&&");
                    for (String typeLine : typeLines) {
                        String[] dataArray = typeLine.split(UdmConstant.FILE_PARAM_MAPPING_COLUMN_SPLIT);
                        int cellCount = 0;
                        Parameter param = new Parameter();
                        param.setParamType(Integer.parseInt(dataArray[cellCount++]));//参数类型
                        param.setChannelId(Integer.parseInt(dataArray[cellCount++]));//通道ID
                        param.setId(dataArray[cellCount++]);//字符参数ID
                        param.setViewTagId(dataArray[cellCount++]);//现实参数的界面控件ID
                        param.setElementType(Integer.parseInt(dataArray[cellCount++]));//界面控件类型
                        param.setDecId(Integer.parseInt(dataArray[cellCount++]));//参数对应的十进制ID
                        param.setHexId(Integer.parseInt(dataArray[cellCount++], 16));//参数对应的十六进制ID
                        param.setCovertType(Integer.parseInt(dataArray[cellCount++]));//参数转换类型
                        param.setByteLength(Integer.parseInt(dataArray[cellCount++]));//参数所占长度
                        List<ValueMapping> vMappings = new ArrayList<>();
                        param.setValueMappings(vMappings);
                        //如果是枚举值，则将枚举值对应的显示值都放入VaueMapping对象中。
                        String enumValues = dataArray[cellCount++];
                        String displayEnumValues = dataArray[cellCount++];
                        if (dataArray.length > 9) {
                            if (enumValues != null && displayEnumValues != null) {
                                String[] values = enumValues.split(UdmConstant.FILE_ENUM_VALUE_SPLIT);
                                String[] names = displayEnumValues.split(UdmConstant.FILE_ENUM_VALUE_SPLIT);
                                if (values.length == names.length) {
                                    for (int i = 0; i < values.length; i++) {
                                        if (values[i].equals("NULL")) {
                                            continue;
                                        }
                                        ValueMapping vmapping = new ValueMapping();
                                        vmapping.setValue(values[i]);
                                        vmapping.setDisplayValue(names[i]);
                                        vMappings.add(vmapping);
                                        vmapping.setParamId(param.getId());
                                    }
                                }
                            }
                        }
                        String pubFlag = dataArray[cellCount++];
                        boolean isPublic = "1".equals(pubFlag) ? true : false;
                        param.setPublic(isPublic);
                        mapping.put(param.getId(), param);
                    }
                }
            }
        } catch (Exception e) {
            UdmLog.error(e);
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {

                }
            }
        }
        return mapping;
    }
}

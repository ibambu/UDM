package com.ibamb.udm.core;

import com.ibamb.udm.instruct.beans.Parameter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by luotao on 18-3-27.
 */

public class ParameterMapping {
    private static Map<String,Parameter> parameterMap;

    public static void setParameterMap(Map<String, Parameter> parameterMap) {
        ParameterMapping.parameterMap = parameterMap;
    }

    public static List<Parameter> getChannelParamDef(int channelId){
        List<Parameter> channelParamList = new ArrayList<>();
        for(Iterator<String> it = parameterMap.keySet().iterator(); it.hasNext();){
            String paramId = it.next();
            Parameter parameter = parameterMap.get(paramId);
            if (parameter.getChannelId()==channelId){
                channelParamList.add(parameter);
            }
        }
        return channelParamList;
    }
    

    public static Parameter getMapping(String paramId){
        return parameterMap.get(paramId);
    }
    
    public static Parameter getMappingByDecId(int decId){
        Parameter parameter = null;
        for(Iterator<String> it = parameterMap.keySet().iterator(); it.hasNext();){
            String paramId = it.next();
            Parameter parameter1 = parameterMap.get(paramId);
            if(parameter1.getDecId()==decId){
                parameter = parameter1;
                break;
            }
        }
        return parameter;
    }
    public static Parameter getMappingByTagId(String tagId){
        Parameter parameter = null;
        for(Iterator<String> it = parameterMap.keySet().iterator(); it.hasNext();){
            String paramId = it.next();
            Parameter parameter1 = parameterMap.get(paramId);
            if(parameter1.getViewTagId().equals(tagId)){
                parameter = parameter1;
                break;
            }
        }
        return parameter;
    }
}

package com.ibamb.udm.instruct.beans;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by luotao on 18-3-27.
 */

public class ParameterMapping {
    private static Map<String,Parameter> parameterMap;

    public Parameter getMapping(String paramId){
        return parameterMap.get(paramId);
    }
    public Parameter getMappingByDecId(int decId){
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

}

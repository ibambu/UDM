package com.ibamb.udm.instruct;

import com.ibamb.udm.instruct.beans.ParameterMapping;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by luotao on 18-3-27.
 */

public class ParameterMappingManager {
    private static Map<String,ParameterMapping> parameterMapping ;

    private static void loadParameterMapping(){
        parameterMapping = null;
    }
    public static Map<String,ParameterMapping> getInstance(){
        if(parameterMapping==null){
            ParameterMappingManager.loadParameterMapping();;
        }
        return parameterMapping;
    }
    public static ParameterMapping getParameterMapping(int decParamId){
        ParameterMapping mapping = null;
        for(Iterator<String> it = parameterMapping.keySet().iterator();it.hasNext();){
            String paramId = it.next();
            ParameterMapping paramMapping = parameterMapping.get(paramId);
            if(paramMapping.getDexId()==decParamId){
                mapping = paramMapping;
                break;
            }
        }
        return mapping;
    }
}

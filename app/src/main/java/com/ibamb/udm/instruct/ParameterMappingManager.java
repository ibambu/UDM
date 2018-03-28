package com.ibamb.udm.instruct;

import com.ibamb.udm.instruct.beans.Parameter;
import com.ibamb.udm.instruct.beans.ParameterMapping;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by luotao on 18-3-27.
 */

public class ParameterMappingManager {
    private static ParameterMapping parameterMapping ;

    private static void loadParameterMapping(){
        parameterMapping = null;
    }
    public static ParameterMapping getInstance(){
        if(parameterMapping==null){
            ParameterMappingManager.loadParameterMapping();;
        }
        return parameterMapping;
    }
}

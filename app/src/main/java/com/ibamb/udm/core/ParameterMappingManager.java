package com.ibamb.udm.core;

import com.ibamb.udm.instruct.beans.Parameter;
import com.ibamb.udm.instruct.beans.ParameterMapping;
import com.ibamb.udm.instruct.beans.ValueMapping;
import com.ibamb.udm.util.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by luotao on 18-3-27.
 */
public class ParameterMappingManager {

    private static ParameterMapping parameterMapping;

    private static void loadParameterMapping() {
        parameterMapping = new ParameterMapping();
        Map<String,Parameter> mapping = new HashMap();
        parameterMapping.setParameterMap(mapping);
        String usrdir = System.getProperty("user.dir");
        List<String> dataLines = FileReader.readTxtFileToList(usrdir+"/conextop-parameter-mapping.txt", true);
        for (String dataLine : dataLines) {
            String[] dataArray = dataLine.split("#");
            Parameter param = new Parameter();
            param.setId(dataArray[0]);
            param.setDecId(Integer.parseInt(dataArray[1]));
            param.setHexId(Integer.parseInt(dataArray[2],16));
            System.out.println(param.getHexId());
            param.setByteLength(Integer.parseInt(dataArray[3]));
            param.setViewId(Integer.parseInt(dataArray[4]));
            param.setCovertType(Integer.parseInt(dataArray[5]));
            
            List<ValueMapping> vMappings = new ArrayList<>();
            param.setValueMappings(vMappings);
            
            if (param.getCovertType() == 1) {
                String[] displayEnumValues = dataArray[6].split(",");
                String[] enumValues = dataArray[7].split(",");
                for (int i = 0; i < displayEnumValues.length; i++) {
                     ValueMapping vmapping = new ValueMapping();
                     vmapping.setDisplayValue(displayEnumValues[i]);
                     vmapping.setValue(enumValues[i]);
                     vMappings.add(vmapping);
                     vmapping.setParamId(param.getId());
                }
            }
            mapping.put(param.getId(), param);
        }
    }

    public static ParameterMapping getInstance() {
        if (parameterMapping == null) {
            ParameterMappingManager.loadParameterMapping();
        }
        return parameterMapping;
    }
}

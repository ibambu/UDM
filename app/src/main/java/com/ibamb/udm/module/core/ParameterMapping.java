package com.ibamb.udm.module.core;

import com.ibamb.udm.module.instruct.beans.Parameter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by luotao on 18-3-27.
 */

public class ParameterMapping {
    private static Map<String, Parameter> parameterMap;

    public static void setParameterMap(Map<String, Parameter> parameterMap) {
        ParameterMapping.parameterMap = parameterMap;
    }

    public static List<Parameter> getChannelParamDef(int channelId) {
        List<Parameter> channelParamList = new ArrayList<>();
        for (Iterator<String> it = parameterMap.keySet().iterator(); it.hasNext(); ) {
            String paramId = it.next();
            Parameter parameter = parameterMap.get(paramId);
            if (parameter.getChannelId() == channelId) {
                channelParamList.add(parameter);
            }
        }
        return channelParamList;
    }

    /**
     * 读取支持的通道
     * @return
     */
    public static List<String> getSupportedChannels(){
        List<String> supportedChannels = new ArrayList<>();
        for (Iterator<String> it = parameterMap.keySet().iterator(); it.hasNext(); ) {
            String paramId = it.next();
            Parameter parameter = parameterMap.get(paramId);
            String channelId = String.valueOf(parameter.getChannelId());
            if(parameter.getChannelId()!=0&& !supportedChannels.contains(channelId)){
                supportedChannels.add(channelId);
            }
        }
        return supportedChannels;
    }

    public static Parameter getMapping(String paramId) {
        return parameterMap.get(paramId);
    }

    /**
     * 根据十进制参数ID获取某个参数定义信息。
     * @param decId
     * @return
     */
    public static Parameter getMappingByDecId(int decId) {
        Parameter parameter = null;
        for (Iterator<String> it = parameterMap.keySet().iterator(); it.hasNext(); ) {
            String paramId = it.next();
            Parameter parameter1 = parameterMap.get(paramId);
            if (parameter1.getDecId() == decId) {
                parameter = parameter1;
                break;
            }
        }
        return parameter;
    }

    /**
     * 当通道ID为空时，建议用于获取参数对应的枚举选项值。因为之是根据TAG随机获取一个通道的某个参数枚举值。
     * 理论上各个通道相同类型的参数枚举选项值是一致的。
     * @param tagId
     * @param channelId
     * @return
     */
    public static Parameter getMappingByTagId(String tagId, String channelId) {
        Parameter parameter = null;
        for (Iterator<String> it = parameterMap.keySet().iterator(); it.hasNext(); ) {
            String paramId = it.next();
            Parameter parameter1 = parameterMap.get(paramId);
            if (channelId == null) {
                if (parameter1.getViewTagId().equals(tagId)) {
                    parameter = parameter1;
                    break;
                }
            } else if (parameter1.getViewTagId().equals(tagId)
                    &&String.valueOf(parameter1.getChannelId()).equals(channelId)) {
                parameter = parameter1;
                break;
            }
        }
        return parameter;
    }

    /**
     * 用户读写参数时获取某个通道的参数定义信息。
     * @param tagIds
     * @param channelId
     * @return
     */
    public static List<Parameter> getMappingByTags(String[] tagIds, String channelId) {
        List<Parameter> paramList = new ArrayList<>();

        for (String tagId : tagIds) {
            for (Iterator<String> it = parameterMap.keySet().iterator(); it.hasNext(); ) {
                String paramId = it.next();
                Parameter parameter1 = parameterMap.get(paramId);
                if (parameter1.getViewTagId().equals(tagId)
                        && String.valueOf(parameter1.getChannelId()).equals(channelId)) {
                    paramList.add(parameter1);
                    break;
                }
            }
        }
        return paramList;
    }
}

package com.ibamb.udm.module.sync;

import com.ibamb.udm.module.beans.ChannelParameter;
import com.ibamb.udm.module.beans.ParameterItem;
import com.ibamb.udm.module.instruct.IParamWriter;
import com.ibamb.udm.module.instruct.ParamWriter;

import java.util.List;

public class DeviceParamSynchronize {
    /**
     * 同步设备参数值，返回目标设备所有参数最新值。
     * @param srcChannelParameter
     * @param distChannelParameter
     * @return
     */
    public void syncDeviceChannelParam(ChannelParameter srcChannelParameter, ChannelParameter distChannelParameter) {
        try {
            List<ParameterItem> srcParamItems = srcChannelParameter.getParamItems();
            List<ParameterItem> distParamItems = distChannelParameter.getParamItems();
            copyParamValue(srcParamItems,distParamItems);
            IParamWriter writer = new ParamWriter();
            writer.writeChannelParam(distChannelParameter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 复制参数值
     *
     * @param srcParamList
     * @param distParamList
     */
    private void copyParamValue(List<ParameterItem> srcParamList, List<ParameterItem> distParamList) {
        for (ParameterItem srcParamItem : srcParamList) {
            for (ParameterItem distParamItem : distParamList) {
                if (srcParamItem.getParamId().equals(distParamItem.getParamId())) {
                    distParamItem.setParamValue(srcParamItem.getParamValue());
                    break;
                }
            }
        }
    }
}

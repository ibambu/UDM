package com.ibamb.udm.instruct.impl;

import com.ibamb.udm.beans.ChannelParameter;
import com.ibamb.udm.beans.ParameterItem;
import com.ibamb.udm.constants.UdmConstants;
import com.ibamb.udm.constants.UdmControl;
import com.ibamb.udm.instruct.IEncoder;
import com.ibamb.udm.instruct.IParameterReaderWriter;
import com.ibamb.udm.instruct.ParameterMappingManager;
import com.ibamb.udm.instruct.beans.InstructFrame;
import com.ibamb.udm.instruct.beans.Parameter;
import com.ibamb.udm.instruct.beans.ParameterMapping;
import com.ibamb.udm.instruct.beans.ReplyFrame;
import com.ibamb.udm.net.UDPSender;
import com.ibamb.udm.util.DataTypeConvert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by luotao on 18-3-26.
 */

public class ParameterReaderWriter implements IParameterReaderWriter {
    @Override
    public ChannelParameter readChannelParam(String channelId,String mac, String[] paramIds) {
        List<InstructFrame> cmdList = new ArrayList<>();
        ParameterMapping parameterMapping = ParameterMappingManager.getInstance();
        for(String paramId:paramIds){
            Parameter mapping = parameterMapping.getMapping(paramId);
            InstructFrame instructFrame = new InstructFrame(UdmControl.GET_PARAMETERS, mac, paramId, null);
            cmdList.add(instructFrame);
        }
        ChannelParameter channelParameter = new ChannelParameter(channelId,mac);
        List<ParameterItem> parameterItems = new ArrayList<>();
        channelParameter.setParamItems(parameterItems);
        UDPSender sender = new UDPSender();
        List<ReplyFrame> replyFrames = sender.sendInstruct(mac, cmdList);
        for(ReplyFrame replyFrame:replyFrames){
            String paramId = replyFrame.getInformation().getType();
            String paramValiue = replyFrame.getInformation().getData();
            ParameterItem parameterItem = new ParameterItem(paramId,paramValiue);
            parameterItems.add(parameterItem);
        }
        return channelParameter;
    }

    @Override
    public ChannelParameter writeChannelParam(ChannelParameter channelParameter) {
        UDPSender sender = new UDPSender();
        ParameterMapping parameterMapping = ParameterMappingManager.getInstance();
        List<ParameterItem> parameterItems = channelParameter.getParamItems();
        List<InstructFrame> cmdList = new ArrayList<>();
        for (ParameterItem parameterItem : parameterItems) {
            Parameter mapping = parameterMapping.getMapping(parameterItem.getParamId());
            String typeId = mapping.getId();
            String typeValue = mapping.getValue(parameterItem.getParamValue());
            InstructFrame instructFrame = new InstructFrame(UdmControl.SET_PARAMETERS, channelParameter.getMac(), typeId, typeValue);
            cmdList.add(instructFrame);
        }
        List<ReplyFrame> replyFrames = sender.sendInstruct(channelParameter.getMac(), cmdList);
        for(ParameterItem item:parameterItems){
            String paramId = item.getParamId();
            Parameter mapping = parameterMapping.getMapping(paramId);
            for(ReplyFrame replyFrame:replyFrames){
                if(paramId.equalsIgnoreCase(mapping.getId())){
                    item.setParamValue(mapping.getDisplayValue(replyFrame.getInformation().getData()));//从返回帧中解析最新参数值,更新到界面对象中.
                    break;
                }
            }
        }
        return channelParameter;
    }
}

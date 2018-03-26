package com.ibamb.udm.instruct.impl;

import com.ibamb.udm.beans.ChannelParameter;
import com.ibamb.udm.beans.ParameterItem;
import com.ibamb.udm.instruct.IEncoder;
import com.ibamb.udm.instruct.IParameterReaderWriter;
import com.ibamb.udm.instruct.beans.InstructFrame;
import com.ibamb.udm.instruct.beans.ReplyFrame;
import com.ibamb.udm.net.UDPSender;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luotao on 18-3-26.
 */

public class ParameterReaderWriter implements IParameterReaderWriter {
    @Override
    public ChannelParameter readChannelParam(String channelId, String[] paramIds) {
        return null;
    }

    @Override
    public ChannelParameter writeChannelParam(ChannelParameter channelParameter) {
        UDPSender sender = new UDPSender();
        IEncoder encoder = new InstructFrameEncoder();
        List<ParameterItem> parameterItems = channelParameter.getParamItems();
        List<InstructFrame> cmdList = new ArrayList<>();
        for (ParameterItem parameterItem : parameterItems) {
            String typeId = parameterItem.getRemoteParamId();
            String typeValue = parameterItem.getParamValue();
            byte control = 0;
            InstructFrame instructFrame = new InstructFrame(control, channelParameter.getMac(), typeId, typeValue);
            cmdList.add(instructFrame);
        }
        List<ReplyFrame> replyFrames = sender.sendInstruct(channelParameter.getMac(), cmdList);
        for(ParameterItem item:parameterItems){
            String typeId = item.getParamId();
            for(ReplyFrame replyFrame:replyFrames){
                byte[] remoteTypeId = replyFrame.getInformation().getType();
                if(typeId.equals("")){
                    item.setParamValue(replyFrame.getInformation().getDataString());//TODO 从返回帧中解析最新参数值,更新到界面对象中.
                    break;
                }
            }
        }
        return channelParameter;
    }
}

package com.ibamb.udm.module.instruct.impl;

import android.util.Log;

import com.ibamb.udm.module.beans.ChannelParameter;
import com.ibamb.udm.module.beans.ParameterItem;
import com.ibamb.udm.module.constants.UdmConstants;
import com.ibamb.udm.module.constants.UdmControl;
import com.ibamb.udm.module.core.ParameterMapping;
import com.ibamb.udm.module.instruct.IEncoder;
import com.ibamb.udm.module.instruct.IParamWriter;
import com.ibamb.udm.module.instruct.IParser;
import com.ibamb.udm.module.instruct.beans.Information;
import com.ibamb.udm.module.instruct.beans.InstructFrame;
import com.ibamb.udm.module.instruct.beans.Parameter;
import com.ibamb.udm.module.instruct.beans.ReplyFrame;
import com.ibamb.udm.module.net.UDPMessageSender;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luotao on 18-4-30.
 */

public class ParamWriter implements IParamWriter {
    @Override
    public ChannelParameter writeChannelParam(ChannelParameter channelParameter) {
        channelParameter = sendStructure(channelParameter);
        return channelParameter;
    }

    /**
     * 发送读/写参数指令
     *
     * @param channelParameter
     * @return
     */
    private ChannelParameter sendStructure(ChannelParameter channelParameter) {
        try {
            //根据传入的读/写标志设置控制位。
            UDPMessageSender sender = new UDPMessageSender();
            List<ParameterItem> parameterItems = channelParameter.getParamItems();
            //主帧固定结构长度
            int mainStructLen = UdmConstants.UDM_CONTROL_LENGTH
                    + UdmConstants.UDM_ID_LENGTH
                    + UdmConstants.UDM_MAIN_FRAME_LENGTH
                    + UdmConstants.UDM_IP_LENGTH
                    + UdmConstants.UDM_MAC_LENGTH;
            //子帧固定结构长度
            int subStructLen = UdmConstants.UDM_TYPE_LENGTH
                    + UdmConstants.UDM_SUB_FRAME_LENGTH;
            List<Information> informationList = new ArrayList<>();//存放本次读/写的所有参数

            //先生成帧对象
            InstructFrame instructFrame = new InstructFrame(UdmControl.SET_PARAMETERS, channelParameter.getMac());
            instructFrame.setInfoList(informationList);
            instructFrame.setId(1);

            int sendFrameLength = mainStructLen;//所有子帧总长度
            int replyFrameLength = mainStructLen;//期望返回帧的总长度。
            //遍历通道参数，将要读/写参数存入帧对象中。
            for (ParameterItem parameterItem : parameterItems) {
                Parameter param = ParameterMapping.getMapping(parameterItem.getParamId());
                String typeId = param.getId();
                Information dataField = new Information(typeId, parameterItem.getParamValue());
                //如果是读取参数值，则参数值长度设置为0. 如果是写参数，则参数值的长度设置约定长度。
                dataField.setLength(subStructLen + param.getByteLength());
                sendFrameLength += dataField.getLength();//增加发送的主帧长度
                replyFrameLength += subStructLen + param.getByteLength();//增加返回帧的长度
                informationList.add(dataField);
            }
            instructFrame.setLength(sendFrameLength);
            IEncoder encoder = new ParamWriteEncoder();
            //生成发送报文
            byte[] sendData = encoder.encode(instructFrame, UdmControl.SET_PARAMETERS);
            //发送报文
            byte[] replyData = sender.sendByBroadcast(sendData, replyFrameLength);
            //解析返回报文
            IParser parser = new ReplyFrameParser();
            ReplyFrame replyFrame = parser.parse(replyData);
            /**
             * 对于修改参数值值的时候，2,4字节长度高位和低位位置是反的，
             * 所以对于读取参数只判断返回码是否成功即可，后面再重新读取一次。
             */
            boolean isSuccessful = replyFrame.getControl() == UdmControl.ACKNOWLEDGE ? true : false;
            channelParameter.setSuccessful(isSuccessful);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(this.getClass().getName(), e.getMessage());
        }
        return channelParameter;
    }
}

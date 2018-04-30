package com.ibamb.udm.instruct.impl;

import com.ibamb.udm.beans.ChannelParameter;
import com.ibamb.udm.beans.ParameterItem;
import com.ibamb.udm.constants.UdmConstants;
import com.ibamb.udm.constants.UdmControl;
import com.ibamb.udm.core.ParameterMapping;
import com.ibamb.udm.instruct.IEncoder;
import com.ibamb.udm.instruct.IParamReader;
import com.ibamb.udm.instruct.IParser;
import com.ibamb.udm.instruct.beans.Information;
import com.ibamb.udm.instruct.beans.InstructFrame;
import com.ibamb.udm.instruct.beans.Parameter;
import com.ibamb.udm.instruct.beans.ReplyFrame;
import com.ibamb.udm.net.IPUtil;
import com.ibamb.udm.net.UDPMessageSender;

import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by luotao on 18-4-30.
 */

public class ParamReader implements IParamReader {
    @Override
    public ChannelParameter readChannelParam(DatagramSocket datagramSocket, ChannelParameter channelParameter) {
        return this.sendStructure(datagramSocket, channelParameter);
    }
    /**
     *
     * @param datagramSocket
     * @param channelParameter
     * @return
     */
    private ChannelParameter sendStructure(DatagramSocket datagramSocket, ChannelParameter channelParameter) {
        try {
            UDPMessageSender sender = new UDPMessageSender();
            List<ParameterItem> parameterItems = channelParameter.getParamItems();
            IEncoder encoder = new ParamReadEncoder();
            IParser parser = new ReplyFrameParser();
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
            InstructFrame instructFrame = new InstructFrame(UdmControl.GET_PARAMETERS, channelParameter.getMac());
            instructFrame.setInfoList(informationList);
            instructFrame.setId(1);

            int sendFrameLength = mainStructLen;//所有子帧总长度
            int replyFrameLength = mainStructLen;//期望返回帧的总长度。
            //遍历通道参数，将要读/写参数存入帧对象中。
            for (ParameterItem parameterItem : parameterItems) {
                Parameter param = ParameterMapping.getMapping(parameterItem.getParamId());
                String typeId = param.getId();

                Information dataField = new Information(typeId, null);
                //如果是读取参数值，则参数值长度设置为0. 如果是写参数，则参数值的长度设置约定长度。
                dataField.setLength(subStructLen);
                sendFrameLength += dataField.getLength();//增加发送的主帧长度
                replyFrameLength += subStructLen + param.getByteLength();//增加返回帧的长度
                informationList.add(dataField);
            }
            instructFrame.setLength(sendFrameLength);
            //生成发送报文
            byte[] sendData = encoder.encode(instructFrame,UdmControl.GET_PARAMETERS);
            //发送报文
            byte[] replyData = sender.send(datagramSocket, sendData, replyFrameLength);
            //解析返回报文
            ReplyFrame replyFrame = parser.parse(replyData);
            for (ParameterItem parameterItem : parameterItems) {
                for (Information info : replyFrame.getInfoList()) {
                    if (parameterItem.getParamId().equals(info.getType())) {
                        String displayValue = info.getData();
                        Parameter paramDef = ParameterMapping.getMapping(parameterItem.getParamId());
                        /**
                         * 根据参数值的转换类型，对值进行转换。
                         */
                        String elementTagId = paramDef.getViewTagId().toLowerCase();
                        if (displayValue != null) {
                            int convertType = paramDef.getCovertType();
                            switch (convertType) {
                                case UdmConstants.UDM_PARAM_TYPE_NUMBER://数值类型
                                    parameterItem.setParamValue(info.getData());
                                    parameterItem.setDisplayValue(paramDef.getDisplayValue(info.getData()));
                                    break;
                                case UdmConstants.UDM_PARAM_TYPE_IPADDR://IP地址
                                    displayValue = IPUtil.getIpFromLong(Long.parseLong(displayValue));
                                    parameterItem.setParamValue(info.getData());
                                    parameterItem.setDisplayValue(displayValue);
                                    break;
                                case UdmConstants.UDM_PARAM_TYPE_TIME://时间类型
                                    parameterItem.setParamValue(info.getData());
                                    parameterItem.setDisplayValue(displayValue);
                                    break;
                                default://字符类型
                                    parameterItem.setParamValue(info.getData());
                                    parameterItem.setDisplayValue(paramDef.getDisplayValue(info.getData()));
                                    break;
                            }
                        }
                        break;
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return channelParameter;
    }
}

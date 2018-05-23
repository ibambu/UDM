package com.ibamb.udm.instruct.impl;

import com.ibamb.udm.constants.UdmConstants;
import com.ibamb.udm.constants.UdmControl;
import com.ibamb.udm.core.ParameterMapping;
import com.ibamb.udm.instruct.IEncoder;
import com.ibamb.udm.instruct.beans.Information;
import com.ibamb.udm.instruct.beans.InstructFrame;
import com.ibamb.udm.instruct.beans.Parameter;
import com.ibamb.udm.net.IPUtil;
import com.ibamb.udm.util.DataTypeConvert;

import java.util.Arrays;
import java.util.List;

/**
 * Created by luotao on 18-4-30.
 */

public class ParamWriteEncoder implements IEncoder {
    @Override
    public byte[] encode(InstructFrame instructFrame, int control) {

        byte[] byteFrame = new byte[instructFrame.getLength()];
        // frame of control
        byteFrame[0] = (byte) instructFrame.getControl();
        // frame of id
        byteFrame[1] = (byte) instructFrame.getId();
        // frame for length
        byte[] frameLength = DataTypeConvert.shortToBytes((short) instructFrame.getLength());
        byteFrame[2] = frameLength[0];
        byteFrame[3] = frameLength[1];
        //frame of ip
        byteFrame[4] = 0;
        byteFrame[5] = 0;
        byteFrame[6] = 0;
        byteFrame[7] = 0;
        //frame of mac
        byte[] macBytes = DataTypeConvert.hexStringtoBytes(instructFrame.getMac().replaceAll(":", ""));
        byteFrame[8] = macBytes[0];
        byteFrame[9] = macBytes[1];
        byteFrame[10] = macBytes[2];
        byteFrame[11] = macBytes[3];
        byteFrame[12] = macBytes[4];
        byteFrame[13] = macBytes[5];
        byteFrame[14] = 0;
        byteFrame[15] = 0;
        // information of type
        List<Information> infoList = instructFrame.getInfoList();
        int pos = 16;//前面16位是固定值，从17位开始计数。
        for (int i = 0; i < infoList.size(); i++) {
            Information info = infoList.get(i);
            Parameter parameter = ParameterMapping.getMapping(info.getType());
            int convertType = parameter.getCovertType();
            byte[] typeBytes = DataTypeConvert.shortToBytes((short) parameter.getDecId());
            byteFrame[pos++] = typeBytes[0];// bit 16
            byteFrame[pos++] = typeBytes[1];// bit 17
            // information of length
            byteFrame[pos++] = (byte) info.getLength();
            // information of data
            String data = info.getData();
            if (control == UdmControl.SET_PARAMETERS) {
                if (data != null) {
                    switch (convertType) {
                        case UdmConstants.UDM_PARAM_TYPE_NUMBER://数值类型
                            break;
                        case UdmConstants.UDM_PARAM_TYPE_IPADDR://IP地址
                            data = String.valueOf(IPUtil.getIpFromString(data));
                            break;
                        case UdmConstants.UDM_PARAM_TYPE_TIME://时间类型
                            break;
                        default://字符类型
                            break;
                    }
                    //读参数的时候参数值为null，所以长度为0.
                    int dataLength = data != null ? info.getLength() - UdmConstants.UDM_TYPE_LENGTH
                            - UdmConstants.UDM_SUB_FRAME_LENGTH : 0;

                    switch (dataLength) {
                        case 1: {
                            byteFrame[pos++] = (byte) Integer.parseInt(data);
                            break;
                        }
                        case 2: {
                            byte[] dataBytes = DataTypeConvert.shortToBytes((short) (Integer.parseInt(data)));
                            byteFrame[pos++] = dataBytes[0];
                            byteFrame[pos++] = dataBytes[1];
                            break;
                        }
                        case 4: {
                            if(convertType!=UdmConstants.UDM_PARAM_TYPE_CHAR){
                                byte[] dataBytes = DataTypeConvert.int2bytes((int) (Long.parseLong(data)));
                                byteFrame[pos++] = dataBytes[0];
                                byteFrame[pos++] = dataBytes[1];
                                byteFrame[pos++] = dataBytes[2];
                                byteFrame[pos++] = dataBytes[3];
                            }else{
                                //有些参数会把IP地址当作字符串存储
                                char[] cData = data.toCharArray();
                                for (int k = 0; k < cData.length; k++) {
                                    byteFrame[pos++] = (byte) cData[k];
                                }
                            }
                            break;
                        }
                        case 8: {
                            break;
                        }
                        default: {
                            char[] cData = data.toCharArray();
                            for (int k = 0; k < cData.length; k++) {
                                byteFrame[pos++] = (byte) cData[k];
                            }
                            break;
                        }
                    }
                }
            }
        }
        return byteFrame;
    }
}

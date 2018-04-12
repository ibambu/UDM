package com.ibamb.udm.instruct.impl;

import com.ibamb.udm.constants.UdmConstants;
import com.ibamb.udm.instruct.IEncoder;
import com.ibamb.udm.core.ParameterMappingManager;
import com.ibamb.udm.instruct.beans.Information;
import com.ibamb.udm.instruct.beans.InstructFrame;
import com.ibamb.udm.instruct.beans.Parameter;
import com.ibamb.udm.instruct.beans.ParameterMapping;
import com.ibamb.udm.util.DataTypeConvert;
import java.util.List;

/**
 * Created by luotao on 18-3-14.
 */
public class InstructFrameEncoder implements IEncoder {

    @Override
    public byte[] encode(InstructFrame instructFrame) {

        ParameterMapping parameterMapping = ParameterMappingManager.getInstance();
        byte[] byteFrame = new byte[instructFrame.getLength()];
        // frame of control
        byteFrame[0] = DataTypeConvert.intToUnsignedByte(instructFrame.getControl());
        // frame of id
        byteFrame[1] = DataTypeConvert.intToUnsignedByte(instructFrame.getId());
        // frame for length
        byte[] frameLength = DataTypeConvert.shortToBytes(DataTypeConvert.intToUnsignedShort(instructFrame.getLength()));
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
            System.out.println(info.toString());
            Parameter parameter = parameterMapping.getMapping(info.getType());
            System.out.println(parameter.toString());
            byte[] typeBytes = DataTypeConvert.shortToBytes(DataTypeConvert.intToUnsignedShort(parameter.getDecId()));
            byteFrame[pos++] = typeBytes[0];// bit 16
            byteFrame[pos++] = typeBytes[1];// bit 17
            // information of length
            byteFrame[pos++] = DataTypeConvert.intToUnsignedByte(info.getLength());
            // information of data
            String data = info.getData();
            //读参数的时候参数值为null，所以长度为0. 
            //TODO 在写参数的时候，如果参数值为空时需要再测试。
            int dataLength = data != null ? info.getLength() - UdmConstants.UDM_TYPE_LENGTH
                    - UdmConstants.UDM_SUB_FRAME_LENGTH : 0;
            switch (dataLength) {
                case 1: {
                    byteFrame[pos++] = DataTypeConvert.intToUnsignedByte((Integer.parseInt(data)));
                    break;
                }
                case 2: {
                    byte[] dataBytes = DataTypeConvert.shortToBytes(DataTypeConvert.intToUnsignedShort(Integer.parseInt(data)));
                    byteFrame[pos++] = dataBytes[0];
                    byteFrame[pos++] = dataBytes[1];
                    break;
                }
                case 4: {
                    byte[] dataBytes = DataTypeConvert.int2bytes(DataTypeConvert.LongToUnsignedInt(Long.parseLong(data)));
                    byteFrame[pos++] = dataBytes[0];
                    byteFrame[pos++] = dataBytes[1];
                    byteFrame[pos++] = dataBytes[2];
                    byteFrame[pos++] = dataBytes[3];
                    break;
                }
                case 8: {
                    break;
                }
                default: {
                    break;
                }
            }
        }
        return byteFrame;
    }
}

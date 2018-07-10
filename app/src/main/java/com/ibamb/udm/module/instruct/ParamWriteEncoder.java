package com.ibamb.udm.module.instruct;

import com.ibamb.udm.module.constants.Constants;
import com.ibamb.udm.module.constants.Control;
import com.ibamb.udm.module.core.ParameterMapping;
import com.ibamb.udm.module.instruct.beans.Information;
import com.ibamb.udm.module.instruct.beans.InstructFrame;
import com.ibamb.udm.module.instruct.beans.Parameter;
import com.ibamb.udm.module.net.IPUtil;
import com.ibamb.udm.module.util.Convert;

import java.util.List;


public class ParamWriteEncoder implements IEncoder {
    @Override
    public byte[] encode(InstructFrame instructFrame, int control) {

        byte[] byteFrame = new byte[instructFrame.getLength()];
        // frame of control
        byteFrame[0] = (byte) instructFrame.getControl();
        // frame of id
        byteFrame[1] = (byte) instructFrame.getId();
        // frame for length
        byte[] frameLength = Convert.shortToBytes((short) instructFrame.getLength());
        byteFrame[2] = frameLength[0];
        byteFrame[3] = frameLength[1];
        //frame of ip
        byteFrame[4] = 0;
        byteFrame[5] = 0;
        byteFrame[6] = 0;
        byteFrame[7] = 0;
        //frame of mac
        byte[] macBytes = Convert.hexStringtoBytes(instructFrame.getMac().replaceAll(":", ""));
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
            Parameter parameter = ParameterMapping.getInstance().getMapping(info.getType());
            int convertType = parameter.getCovertType();
            byte[] typeBytes = Convert.shortToBytes((short) parameter.getDecId());
            byteFrame[pos++] = typeBytes[0];// bit 16
            byteFrame[pos++] = typeBytes[1];// bit 17
            // information of length
            byteFrame[pos++] = (byte) info.getLength();
            // information of data
            String data = info.getData();
            if (control == Control.SET_PARAMETERS) {
                if (data != null) {

                    //读参数的时候参数值为null，所以长度为0.
                    int dataLength = data != null ? info.getLength() - Constants.UDM_TYPE_LENGTH
                            - Constants.UDM_SUB_FRAME_LENGTH : 0;
                    switch (dataLength) {
                        case 1: {
                            byteFrame[pos++] = (byte) Integer.parseInt(data);
                            break;
                        }
                        case 2: {
                            byte[] dataBytes = Convert.shortToBytes((short) (Integer.parseInt(data)));
                            byteFrame[pos++] = dataBytes[0];
                            byteFrame[pos++] = dataBytes[1];
                            break;
                        }
                        case 4: {
                            if (convertType != Constants.UDM_PARAM_TYPE_CHAR) {
                                byte[] dataBytes = Convert.int2bytes((int) (Long.parseLong(data)));
                                byteFrame[pos++] = dataBytes[0];
                                byteFrame[pos++] = dataBytes[1];
                                byteFrame[pos++] = dataBytes[2];
                                byteFrame[pos++] = dataBytes[3];
                            } else {
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

                            if (dataLength > cData.length) {
                                for (int j = 0; j < dataLength - cData.length; j++) {
                                    byteFrame[pos++] = 0;
                                }
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

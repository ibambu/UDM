package com.ibamb.udm.module.instruct;

import com.ibamb.udm.module.core.ParameterMapping;
import com.ibamb.udm.module.instruct.beans.Information;
import com.ibamb.udm.module.instruct.beans.InstructFrame;
import com.ibamb.udm.module.instruct.beans.Parameter;
import com.ibamb.udm.module.util.Convert;

import java.util.List;


public class ParamReadEncoder implements IEncoder {

    /**
     *  对读参数值指令进行编码
     * @param instructFrame
     * @param control
     * @return
     */
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
        }
        return byteFrame;
    }
}

package com.ibamb.udm.instruct.impl;

import com.ibamb.udm.instruct.IEncoder;
import com.ibamb.udm.instruct.beans.InstructFrame;
import com.ibamb.udm.util.DataTypeConvert;

/**
 * Created by luotao on 18-3-14.
 */

public class InstructFrameEncoder implements IEncoder {

    @Override
    public byte[] encode(InstructFrame instructFrame) {
        int frameLength = DataTypeConvert.bytes2int(instructFrame.getLength());
        byte[] cmdByteBuffer = new byte[frameLength];
        cmdByteBuffer[0] = instructFrame.getControl();
        cmdByteBuffer[1] = instructFrame.getId();
        cmdByteBuffer[2] = instructFrame.getLength()[0];
        cmdByteBuffer[3] = instructFrame.getLength()[1];
        //IP
        cmdByteBuffer[4] = 0;
        cmdByteBuffer[5] = 0;
        cmdByteBuffer[6] = 0;
        cmdByteBuffer[7] = 0;
        //MAC
        cmdByteBuffer[8] = instructFrame.getMac()[0];
        cmdByteBuffer[9] = instructFrame.getMac()[1];
        cmdByteBuffer[10] = instructFrame.getMac()[2];
        cmdByteBuffer[11] = instructFrame.getMac()[3];
        cmdByteBuffer[12] = instructFrame.getMac()[4];
        cmdByteBuffer[13] = instructFrame.getMac()[5];
        cmdByteBuffer[14] = instructFrame.getMac()[6];
        cmdByteBuffer[15] = instructFrame.getMac()[7];
        //TYPE
        cmdByteBuffer[16] = instructFrame.getInformation().getType()[0];
        cmdByteBuffer[17] = instructFrame.getInformation().getType()[1];
        //LENGTH
        cmdByteBuffer[17] = instructFrame.getInformation().getLength();
        //DATA
        for (int i = 0; i < instructFrame.getInformation().getData().length; i++) {
            cmdByteBuffer[18 + i] = instructFrame.getInformation().getData()[i];
        }

        return cmdByteBuffer;
    }
}

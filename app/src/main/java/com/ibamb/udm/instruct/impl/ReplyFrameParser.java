package com.ibamb.udm.instruct.impl;

import com.ibamb.udm.instruct.IParser;
import com.ibamb.udm.instruct.ParameterMappingManager;
import com.ibamb.udm.instruct.beans.Information;
import com.ibamb.udm.instruct.beans.Parameter;
import com.ibamb.udm.instruct.beans.ParameterMapping;
import com.ibamb.udm.instruct.beans.ReplyFrame;
import com.ibamb.udm.util.DataTypeConvert;

import java.util.Arrays;

/**
 * Created by luotao on 18-3-14.
 */

public class ReplyFrameParser implements IParser {
    @Override
    public ReplyFrame parse(byte[] replyBuffer) {
        ParameterMapping parameterMapping = ParameterMappingManager.getInstance();
        ReplyFrame replyFrame = new ReplyFrame();
        replyFrame.setControl(replyBuffer[0]);
        replyFrame.setId(replyBuffer[1]);
        replyFrame.setLength(DataTypeConvert.bytesToShort(Arrays.copyOfRange(replyBuffer,2,4)));

        Information information = new Information();
        int decId = DataTypeConvert.bytesToShort(Arrays.copyOfRange(replyBuffer,4,6));
        Parameter parameter = parameterMapping.getMappingByDecId(decId);
        information.setType(parameter.getId());
        information.setLength(replyBuffer[7]);
        information.setData(new String(replyBuffer,8,replyBuffer.length));

        replyFrame.setInformation(information);
        return replyFrame;
    }
}

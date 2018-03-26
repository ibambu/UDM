package com.ibamb.udm.instruct.impl;

import com.ibamb.udm.instruct.IParser;
import com.ibamb.udm.instruct.beans.Information;
import com.ibamb.udm.instruct.beans.ReplyFrame;

import java.util.Arrays;

/**
 * Created by luotao on 18-3-14.
 */

public class ReplyFrameParser implements IParser {
    @Override
    public ReplyFrame parse(byte[] replyBuffer) {
        ReplyFrame replyFrame = new ReplyFrame();
        replyFrame.setControl(replyBuffer[0]);
        replyFrame.setId(replyBuffer[1]);
        replyFrame.setLength(Arrays.copyOfRange(replyBuffer,2,4));

        Information information = new Information();
        information.setType(Arrays.copyOfRange(replyBuffer,4,6));
        information.setLength(replyBuffer[7]);
        information.setData(Arrays.copyOfRange(replyBuffer,8,replyBuffer.length));

        replyFrame.setInformation(information);
        return replyFrame;
    }
}

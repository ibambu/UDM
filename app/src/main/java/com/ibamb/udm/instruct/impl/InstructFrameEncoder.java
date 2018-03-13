package com.ibamb.udm.instruct.impl;

import com.ibamb.udm.instruct.IEncoder;
import com.ibamb.udm.instruct.IParser;
import com.ibamb.udm.instruct.beans.InstructFrame;
import com.ibamb.udm.instruct.beans.ReplyFrame;

/**
 * Created by luotao on 18-3-14.
 */

public class InstructFrameEncoder implements IEncoder {

    @Override
    public byte[] encode(InstructFrame instructFrame) {
        return new byte[0];
    }
}

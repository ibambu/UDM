package com.ibamb.udm.instruct;

import com.ibamb.udm.instruct.beans.InstructFrame;

/**
 * Created by luotao on 18-3-13.
 */

public interface IEncoder {
    public byte[] encode(InstructFrame instructFrame);
}

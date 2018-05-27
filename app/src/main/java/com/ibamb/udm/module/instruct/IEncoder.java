package com.ibamb.udm.module.instruct;

import com.ibamb.udm.module.instruct.beans.InstructFrame;

/**
 * Created by luotao on 18-3-13.
 */

public interface IEncoder {
    public byte[] encode(InstructFrame instructFrame,int control);

}

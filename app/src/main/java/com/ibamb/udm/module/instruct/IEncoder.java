package com.ibamb.udm.module.instruct;

import com.ibamb.udm.module.instruct.beans.InstructFrame;


public interface IEncoder {
    /**
     * 对指令按字节编码
     * @param instructFrame
     * @param control
     * @return
     */
    public byte[] encode(InstructFrame instructFrame,int control);

}

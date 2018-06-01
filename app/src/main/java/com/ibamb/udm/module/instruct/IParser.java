package com.ibamb.udm.module.instruct;

import com.ibamb.udm.module.instruct.beans.ReplyFrame;



public interface IParser {
    /**
     * 读/写参数值后解析返回字节数据
     * @param replyBuffer
     * @return
     */
    public ReplyFrame parse(byte[] replyBuffer);
}

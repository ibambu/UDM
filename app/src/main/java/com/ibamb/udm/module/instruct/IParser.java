package com.ibamb.udm.module.instruct;

import com.ibamb.udm.module.instruct.beans.ReplyFrame;

/**
 * Created by luotao on 18-3-13.
 */

public interface IParser {
    public ReplyFrame parse(byte[] replyBuffer);
}

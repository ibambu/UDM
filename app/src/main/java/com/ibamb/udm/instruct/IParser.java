package com.ibamb.udm.instruct;

import com.ibamb.udm.instruct.beans.ReplyFrame;

/**
 * Created by luotao on 18-3-13.
 */

public interface IParser {
    public ReplyFrame parse(byte[] replyBuffer);
}

package com.ibamb.udm.instruct.impl;

import com.ibamb.udm.constants.UdmConstants;
import com.ibamb.udm.instruct.IParser;
import com.ibamb.udm.core.ParameterMappingManager;
import com.ibamb.udm.instruct.beans.Information;
import com.ibamb.udm.instruct.beans.Parameter;
import com.ibamb.udm.instruct.beans.ParameterMapping;
import com.ibamb.udm.instruct.beans.ReplyFrame;
import com.ibamb.udm.util.DataTypeConvert;
import java.util.ArrayList;

import java.util.Arrays;
import java.util.List;

/**
 * Created by luotao on 18-3-14.
 */
public class ReplyFrameParser implements IParser {

    @Override
    public ReplyFrame parse(byte[] replyData) {
        ParameterMapping parameterMapping = ParameterMappingManager.getInstance();
        ReplyFrame replyFrame = new ReplyFrame();
        replyFrame.setControl(replyData[0]);// 控制位
        replyFrame.setId(replyData[1]);// 通信ID
        replyFrame.setLength(DataTypeConvert.bytesToShort(Arrays.copyOfRange(replyData, 2, 4)));//帧总长度
        List<Information> informationList = new ArrayList<>();//存放本次返回的所有参数
        replyFrame.setInfoList(informationList);
        int offset = 0;
        for (int i = 0; i < replyData.length; i++) {
            Information information = new Information();
            int decId = DataTypeConvert.bytesToShort(Arrays.copyOfRange(replyData, offset + 4, offset + 6));
            Parameter parameter = parameterMapping.getMappingByDecId(decId);
            if (parameter != null) {
                information.setType(parameter.getId());
                information.setLength(Byte.toUnsignedInt(replyData[offset + 6]));//返回数据长度
                //读取返回数据
                byte[] dataBytes = Arrays.copyOfRange(replyData, offset + 7,
                        offset + 7 + information.getLength()
                        - UdmConstants.UDM_TYPE_LENGTH - UdmConstants.UDM_SUB_FRAME_LENGTH);
                int dataLength = dataBytes.length;//实际参数取值长度
                switch (dataLength) {
                    case 1: {
                        information.setData(String.valueOf(Byte.toUnsignedInt(dataBytes[0])));
                        break;
                    }
                    case 2: {
                        information.setData(String.valueOf(Short.toUnsignedInt(DataTypeConvert.bytesToShort(dataBytes))));
                        break;
                    }
                    case 4: {
                        information.setData((Integer.toUnsignedString(DataTypeConvert.bytes2int(dataBytes))));
                        break;
                    }
                    case 8: {
                        break;
                    }
                    default: {
                        break;
                    }
                }
                offset = +information.getLength();
                informationList.add(information);
            }
        }
        return replyFrame;
    }
}

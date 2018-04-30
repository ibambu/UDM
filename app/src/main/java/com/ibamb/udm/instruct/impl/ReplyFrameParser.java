package com.ibamb.udm.instruct.impl;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.ibamb.udm.constants.UdmConstants;
import com.ibamb.udm.instruct.IParser;
import com.ibamb.udm.core.ParameterMappingManager;
import com.ibamb.udm.instruct.beans.Information;
import com.ibamb.udm.instruct.beans.Parameter;
import com.ibamb.udm.core.ParameterMapping;
import com.ibamb.udm.instruct.beans.ReplyFrame;
import com.ibamb.udm.util.DataTypeConvert;

import java.util.ArrayList;

import java.util.Arrays;
import java.util.List;

/**
 * Created by luotao on 18-3-14.
 */
public class ReplyFrameParser implements IParser {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public ReplyFrame parse(byte[] replyData) {
        ReplyFrame replyFrame = new ReplyFrame();
        replyFrame.setControl(replyData[0]);// 控制位
        replyFrame.setId(replyData[1]);// 通信ID
        replyFrame.setLength((int)DataTypeConvert.bytesToShort(Arrays.copyOfRange(replyData, 2, 4)));//帧总长度
        List<Information> informationList = new ArrayList<>();//存放本次返回的所有参数
        replyFrame.setInfoList(informationList);
        int offset = 0;
        for (int i = offset; i < replyData.length; i = i + offset) {
            Information information = new Information();
            int decId = DataTypeConvert.bytesToShort(Arrays.copyOfRange(replyData, i + 4, i + 6));
            Parameter parameter = ParameterMapping.getMappingByDecId(decId);

            if (parameter != null) {
                information.setType(parameter.getId());
                information.setLength((int) (replyData[i + 6]));//返回数据长度
                //读取返回数据
                byte[] dataBytes = Arrays.copyOfRange(replyData, i + 7,
                        i + 7 + information.getLength()
                                - UdmConstants.UDM_TYPE_LENGTH - UdmConstants.UDM_SUB_FRAME_LENGTH);
                int dataLength = dataBytes.length;//实际参数取值长度
                /**
                 * 对于长度不超过4个字节的参数先按照数值类型处理，超过4个字节的则根据字节则按照字符文本处理。
                 */
                switch (dataLength) {
                    case 1: {
                        information.setData(String.valueOf((int) (dataBytes[0])));
                        break;
                    }
                    case 2: {
                        short value = DataTypeConvert.bytesToShort(dataBytes);
                        information.setData(String.valueOf((int) (value)));
                        break;
                    }
                    case 4: {
                        information.setData(String.valueOf(DataTypeConvert.bytes2int(dataBytes)));
                        break;
                    }
                    default: {
                        //默认当作文本处理
                        StringBuilder buffer = new StringBuilder();
                        for(int k=0;k<dataBytes.length;k++){
                            char c = (char)dataBytes[k];
                            buffer.append(c);
                        }
                        information.setData(buffer.toString());
                        break;
                    }
                }
                System.out.println("reply information:" + information.toString());
            }else{
                break;//
            }
            informationList.add(information);
            offset = information.getLength();
        }
        return replyFrame;
    }
}

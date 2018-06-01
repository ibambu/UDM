package com.ibamb.udm.module.instruct;

import com.ibamb.udm.module.constants.Constants;
import com.ibamb.udm.module.constants.Control;
import com.ibamb.udm.module.core.ParameterMapping;
import com.ibamb.udm.module.instruct.beans.Information;
import com.ibamb.udm.module.instruct.beans.Parameter;
import com.ibamb.udm.module.instruct.beans.ReplyFrame;
import com.ibamb.udm.module.util.Convert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ReplyFrameParser implements IParser {

    @Override
    public ReplyFrame parse(byte[] replyData) {
        ReplyFrame replyFrame = new ReplyFrame();
        List<Information> informationList = new ArrayList<>();//存放本次返回的所有参数
        replyFrame.setInfoList(informationList);
        if(replyData==null){
            replyFrame.setControl(Control.NO_DATA_REPLY);
            return replyFrame;
        }
        replyFrame.setControl(replyData[0]);// 控制位
        replyFrame.setId(replyData[1]);// 通信ID
        replyFrame.setLength((int) Convert.bytesToShort(Arrays.copyOfRange(replyData, 2, 4)));//帧总长度

        int offset = 0;
        for (int i = offset; i < replyData.length; i = i + offset) {
            Information information = new Information();
            int decId = Convert.bytesToShort(Arrays.copyOfRange(replyData, i + 4, i + 6));
            Parameter parameter = ParameterMapping.getMappingByDecId(decId);

            if (parameter != null) {
                information.setType(parameter.getId());
                information.setLength((int) (replyData[i + 6]));//返回数据长度
                //读取返回数据
                byte[] dataBytes = Arrays.copyOfRange(replyData, i + 7,
                        i + 7 + information.getLength()
                                - Constants.UDM_TYPE_LENGTH - Constants.UDM_SUB_FRAME_LENGTH);
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
                        short value = Convert.bytesToShort(dataBytes);
                        information.setData(String.valueOf((int) (value)));
                        break;
                    }
                    case 4: {

                        if(parameter.getCovertType()!= Constants.UDM_PARAM_TYPE_CHAR){
                            information.setData(String.valueOf(Convert.bytes2int(dataBytes)));
                        }else{
                            //有些参数存储IP地址是当作字符串的。
                            StringBuilder buffer = new StringBuilder();
                            for(int k=0;k<dataLength;k++){
                                if(dataBytes[k]!=0){
                                    char c = (char)dataBytes[k];
                                    buffer.append(c);
                                }
                            }
                            information.setData(buffer.toString());
                        }

                        break;
                    }
                    default: {
                        //默认当作文本处理
                        StringBuilder buffer = new StringBuilder();
                        for(int k=0;k<dataLength;k++){
                            if(dataBytes[k]!=0){
                                char c = (char)dataBytes[k];
                                buffer.append(c);
                            }
                        }
                        information.setData(buffer.toString());
                        break;
                    }
                }
            }else{
                break;//
            }
            informationList.add(information);
            offset = information.getLength();
        }
        return replyFrame;
    }
}

package com.ibamb.udm.util;

import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

import com.ibamb.udm.beans.ChannelParameter;
import com.ibamb.udm.beans.ParameterItem;
import com.ibamb.udm.constants.UdmConstants;
import com.ibamb.udm.core.ParameterMappingManager;
import com.ibamb.udm.instruct.beans.Parameter;
import com.ibamb.udm.core.ParameterMapping;
import com.ibamb.udm.net.IPUtil;
import com.ibamb.udm.tag.UdmSpinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by luotao on 18-4-21.
 */

public class ViewElementDataUtil {

    public static void setData(ChannelParameter channelParameter, View view) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (ParameterItem item : channelParameter.getParamItems()) {
            //根据 paramId 找对应的界面元素 ID，并赋值。
            Parameter paramdef = ParameterMapping.getMapping(item.getParamId());
            String value = item.getParamValue();
            if(paramdef==null){
                continue;
            }
            String elementTagId = paramdef.getViewTagId().toLowerCase();
            if(value!=null){
                int convertType = paramdef.getCovertType();
                switch (convertType) {
                    case 1:
                        break;
                    case 2:
                        value = IPUtil.getIpFromLong(Long.parseLong(value));
                        System.out.println("element tag value 2=" + value);
                        break;
                    case 3:
//                        Calendar c = Calendar.getInstance();
//                        c.setTimeInMillis(Long.parseLong(value));
//                        value = sdf.format(c.getTime());
//                        System.out.println("element tag value 3=" + value);
                }

            }


            int elementType = paramdef.getElementType();
            System.out.println("================"+paramdef.getViewTagId()+"->"+value);
            switch (elementType) {
                case UdmConstants.UDM_UI_EDIT_TEXT:
                    EditText vEditText = view.findViewWithTag(elementTagId);
                    vEditText.setText(value);
                    break;
                case UdmConstants.UDM_UI_UDMSPINNER:
                    UdmSpinner vSpinner = view.findViewWithTag(elementTagId);
                    vSpinner.setValue(item.getDisplayValue());
                    break;
                case UdmConstants.UDM_UI_SWITCH:
                    Switch vSwitch = view.findViewWithTag(elementTagId);
                    if(item.getParamValue()=="1"){
                        vSwitch.setChecked(true);
                    }else {
                        vSwitch.setChecked(false);
                    }
                    break;
                default:
                    break;
            }
        }
    }
}

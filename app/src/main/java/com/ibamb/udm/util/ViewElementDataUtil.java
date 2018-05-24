package com.ibamb.udm.util;

import android.support.v7.widget.AppCompatCheckBox;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

import com.ibamb.udm.R;
import com.ibamb.udm.beans.ChannelParameter;
import com.ibamb.udm.beans.ParameterItem;
import com.ibamb.udm.constants.UdmConstants;
import com.ibamb.udm.core.ParameterMapping;
import com.ibamb.udm.instruct.beans.Parameter;
import com.ibamb.udm.tag.UdmButtonTextEdit;
import com.ibamb.udm.tag.UdmSpinner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luotao on 18-4-21.
 */

public class ViewElementDataUtil {

    /**
     * 将读取到的参数值更新到界面。
     * @param channelParameter
     * @param view
     */
    public static void setData(ChannelParameter channelParameter, View view) {
        try {
            for (ParameterItem item : channelParameter.getParamItems()) {
                //根据 paramId 找对应的界面元素 ID，并赋值。
                Parameter paramdef = ParameterMapping.getMapping(item.getParamId());
                String value = item.getParamValue();
                if (paramdef == null) {
                    continue;
                }
                String elementTagId = paramdef.getViewTagId().toLowerCase();
                int elementType = paramdef.getElementType();
                switch (elementType) {
                    case UdmConstants.UDM_UI_SPECIAL:
                        if ("CONN_NET_PROTOCOL".equalsIgnoreCase(paramdef.getViewTagId())) {
                            Switch tcp = view.findViewById(R.id.tcp_enanbled_switch);
                            Switch udp = view.findViewById(R.id.udp_enanbled_switch);
                            if(tcp!=null && udp!= null){
                                if ("0".equalsIgnoreCase(value)) {
                                    udp.setChecked(true);
                                    tcp.setChecked(false);
                                } else if ("1".equals(value)) {
                                    udp.setChecked(false);
                                    tcp.setChecked(true);
                                } else if ("2".equals(value)) {
                                    udp.setChecked(true);
                                    tcp.setChecked(true);
                                }
                            }
                        }
                        break;
                    case UdmConstants.UDM_UI_EDIT_TEXT:
                        EditText vEditText = view.findViewWithTag(elementTagId);
                        if(vEditText!=null){
                            vEditText.setText(item.getDisplayValue());
                        }
                        break;
                    case UdmConstants.UDM_UI_UDMSPINNER:
                        UdmSpinner vSpinner = view.findViewWithTag(elementTagId);
                        if(vSpinner!=null){
                            vSpinner.setValue(item.getDisplayValue());
                        }
                        break;
                    case UdmConstants.UDM_UI_SWITCH:
                        Switch vSwitch = view.findViewWithTag(elementTagId);
                        if(vSwitch!=null){
                            if (UdmConstants.UDM_SWITCH_ON.equals(item.getParamValue())) {
                                vSwitch.setChecked(true);
                            } else {
                                vSwitch.setChecked(false);
                            }
                        }
                        break;
                    case UdmConstants.UDM_UI_BUTTON_TEXT:
                        UdmButtonTextEdit buttonTextEdit = view.findViewWithTag(elementTagId);
                        if(buttonTextEdit!=null){
                            buttonTextEdit.setValue(item.getDisplayValue());
                        }
                        break;
                    default:
                        break;
                }
            }
        } catch (Exception ex) {
            Log.e(ViewElementDataUtil.class.getName(),ex.getMessage());
        }
    }

    /**
     * 与设置前的参数比较，获取界面发生改变的参数。
     * @param view
     * @param oldChannelParam
     * @param channelId
     * @return
     */
    public static ChannelParameter getChangedData(View view, ChannelParameter oldChannelParam, String channelId) {
        ChannelParameter channelParameter = new ChannelParameter(oldChannelParam.getMac(),
                oldChannelParam.getChannelId(),oldChannelParam.getIp());
        List<Parameter> parameters = ParameterMapping.getChannelParamDef(Integer.parseInt(channelId));
        List<ParameterItem> items = new ArrayList<>();
        channelParameter.setParamItems(items);
        for (Parameter parameter : parameters) {
            String viewTagId = parameter.getViewTagId().toLowerCase();
            int vElementType = parameter.getElementType();
            String value = null;
            String displayValue = null;
            switch (vElementType) {
                case UdmConstants.UDM_UI_SPECIAL:
                    if ("CONN_NET_PROTOCOL".equalsIgnoreCase(parameter.getViewTagId())) {
                        Switch tcp = view.findViewById(R.id.tcp_enanbled_switch);
                        Switch udp = view.findViewById(R.id.udp_enanbled_switch);
                        if(tcp!=null && udp!=null){
                            if (tcp.isChecked() && udp.isChecked()) {
                                value = "2";
                            } else if (tcp.isChecked()) {
                                value = "1";
                            } else if (udp.isChecked()) {
                                value = "0";
                            }
                        }
                    }
                    break;
                case UdmConstants.UDM_UI_EDIT_TEXT:
                    EditText vEditText = view.findViewWithTag(viewTagId);
                    if(vEditText!=null){
                        value = parameter.getValue(vEditText.getText().toString());
                    }
                    break;
                case UdmConstants.UDM_UI_UDMSPINNER:
                    UdmSpinner vSpinner = view.findViewWithTag(viewTagId);
                    if(vSpinner!=null){
                        value = parameter.getValue(vSpinner.getValue());
                    }
                    break;
                case UdmConstants.UDM_UI_SWITCH:
                    Switch vSwitch = view.findViewWithTag(viewTagId);
                    if(vSwitch!=null){
                        if (vSwitch.isChecked()) {
                            value = UdmConstants.UDM_SWITCH_ON;
                        } else {
                            value = UdmConstants.UDM_SWITCH_OFF;
                        }
                    }
                    break;
                case UdmConstants.UDM_UI_BUTTON_TEXT:
                    UdmButtonTextEdit buttonTextEdit = view.findViewWithTag(viewTagId);
                    if(buttonTextEdit!=null){
                        value = parameter.getValue(buttonTextEdit.getValue());
                    }
                    break;
                default:
                    break;
            }
            displayValue = parameter.getDisplayValue(value);
            if (oldChannelParam != null && oldChannelParam.getChannelId().equals(channelId)) {
                List<ParameterItem> paramItems = oldChannelParam.getParamItems();
                for (ParameterItem parameterItem : paramItems) {
                    //参数ID一致且值不一样，则认为是本次有修改的参数
                    if(parameterItem.getParamId().equals(parameter.getId())
                            && !parameterItem.getDisplayValue().equals(displayValue)){
                        items.add(new ParameterItem(parameter.getId(), value));
                        break;
                    }
                }
            }else  if (oldChannelParam == null) {
                //如果没有旧参数，则认为是最新设置的。
                items.add(new ParameterItem(parameter.getId(), value));
            }

        }
        return channelParameter;
    }
}

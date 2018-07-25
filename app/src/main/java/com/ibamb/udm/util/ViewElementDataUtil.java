package com.ibamb.udm.util;

import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

import com.ibamb.udm.R;
import com.ibamb.udm.module.beans.ChannelParameter;
import com.ibamb.udm.module.beans.ParameterItem;
import com.ibamb.udm.module.constants.Constants;
import com.ibamb.udm.module.core.ContextData;
import com.ibamb.udm.module.core.ParameterMapping;
import com.ibamb.udm.module.instruct.beans.Parameter;
import com.ibamb.udm.module.log.UdmLog;
import com.ibamb.udm.module.net.IPUtil;
import com.ibamb.udm.tag.UdmButtonTextEdit;
import com.ibamb.udm.tag.UdmSpinner;

import java.util.ArrayList;
import java.util.List;


public class ViewElementDataUtil {

    /**
     * 将读取到的参数值更新到界面。
     *
     * @param channelParameter
     * @param view
     */
    public static void setData(ChannelParameter channelParameter, View view) {
        try {
            for (ParameterItem item : channelParameter.getParamItems()) {
                //根据 paramId 找对应的界面元素 ID，并赋值。
                Parameter paramdef = ParameterMapping.getInstance().getMapping(item.getParamId());
                String value = item.getParamValue();
                if (paramdef == null) {
                    continue;
                }
                String elementTagId = paramdef.getViewTagId().toLowerCase();
                int elementType = paramdef.getElementType();
                switch (elementType) {
                    case Constants.UDM_UI_SPECIAL:
                        if ("CONN_NET_PROTOCOL".equalsIgnoreCase(paramdef.getViewTagId())) {
                            Switch tcp = view.findViewById(R.id.tcp_enanbled_switch);
                            Switch udp = view.findViewById(R.id.udp_enanbled_switch);
                            Switch both = view.findViewById(R.id.tcp_udp_enanbled_switch);

                            if (tcp != null && udp != null & both!=null ) {
                                if ("0".equalsIgnoreCase(value)) {
                                    udp.setChecked(true);
                                    tcp.setChecked(false);
                                    both.setChecked(false);
                                } else if ("1".equals(value)) {
                                    udp.setChecked(false);
                                    tcp.setChecked(true);
                                    both.setChecked(false);
                                } else if ("2".equals(value)) {
                                    udp.setChecked(false);
                                    tcp.setChecked(false);
                                    both.setChecked(true);
                                }
                            }
                        }
                        break;
                    case Constants.UDM_UI_EDIT_TEXT:
                        EditText vEditText = view.findViewWithTag(elementTagId);
                        if (vEditText != null) {

                            if (elementTagId.equalsIgnoreCase("basic_rtc_time")) {
                                EditText vDate = view.findViewById(R.id.id_local_date);
                                EditText vTime = view.findViewById(R.id.id_local_time);
                                if (item.getDisplayValue() != null && item.getDisplayValue().length() > 10) {
                                    if (vDate != null) {
                                        vDate.setText(item.getDisplayValue().substring(0, 10));
                                    }
                                    if (vTime != null) {
                                        vTime.setText(item.getDisplayValue().substring(10, item.getDisplayValue().length()));
                                    }
                                }
                            } else {
                                vEditText.setText(item.getDisplayValue());
                            }
                        }
                        break;
                    case Constants.UDM_UI_UDMSPINNER:
                        UdmSpinner vSpinner = view.findViewWithTag(elementTagId);
                        if (vSpinner != null) {
                            vSpinner.setValue(item.getDisplayValue());
                        }
                        break;
                    case Constants.UDM_UI_SWITCH:
                        Switch vSwitch = view.findViewWithTag(elementTagId);
                        if (vSwitch != null) {
                            if (Constants.UDM_SWITCH_ON.equals(item.getParamValue())) {
                                vSwitch.setChecked(true);
                            } else {
                                vSwitch.setChecked(false);
                            }
                        }
                        break;
                    case Constants.UDM_UI_BUTTON_TEXT:
                        UdmButtonTextEdit buttonTextEdit = view.findViewWithTag(elementTagId);
                        if (buttonTextEdit != null) {
                            buttonTextEdit.setValue(item.getDisplayValue());
                        }
                        break;
                    default:
                        break;
                }
            }
        } catch (Exception ex) {
            UdmLog.error(ex);
        }
    }

    /**
     * 与设置前的参数比较，获取界面发生改变的参数。
     *
     * @param view
     * @param oldChannelParam
     * @param channelId
     * @return
     */
    public static ChannelParameter getChangedData(View view, ChannelParameter oldChannelParam, String channelId) {
        ChannelParameter channelParameter = new ChannelParameter(oldChannelParam.getMac(),
                oldChannelParam.getChannelId(), oldChannelParam.getIp());
        List<Parameter> parameters = ParameterMapping.getInstance().getChannelParamDef(Integer.parseInt(channelId));
        List<ParameterItem> items = new ArrayList<>();
        channelParameter.setParamItems(items);
        for (Parameter parameter : parameters) {
            String viewTagId = parameter.getViewTagId().toLowerCase();
            int vElementType = parameter.getElementType();
            String value = null;
            String displayValue = null;
            boolean hasElement = false;
            switch (vElementType) {
                case Constants.UDM_UI_SPECIAL:
                    if ("CONN_NET_PROTOCOL".equalsIgnoreCase(parameter.getViewTagId())) {
                        Switch tcp = view.findViewById(R.id.tcp_enanbled_switch);
                        Switch udp = view.findViewById(R.id.udp_enanbled_switch);
                        Switch both = view.findViewById(R.id.tcp_udp_enanbled_switch);
                        if (tcp != null && udp != null & both!=null) {
                            if (both.isChecked()) {
                                value = "2";
                            } else if (tcp.isChecked()) {
                                value = "1";
                            } else if (udp.isChecked()) {
                                value = "0";
                            }
                            hasElement = true;
                        }

                    }
                    break;
                case Constants.UDM_UI_EDIT_TEXT:
                    EditText vEditText = view.findViewWithTag(viewTagId);
                    if (vEditText != null) {
                        value = parameter.getValue(vEditText.getText().toString());
                        if (parameter.getViewTagId().equalsIgnoreCase("basic_rtc_time")) {
                            EditText vDate = view.findViewById(R.id.id_local_date);
                            EditText vTime = view.findViewById(R.id.id_local_time);
                            if (vDate != null) {
                                value = vDate.getText().toString();
                            }
                            if (vTime != null) {
                                value += " " + vTime.getText().toString();
                            }
                        }
                        hasElement = true;
                    }
                    break;
                case Constants.UDM_UI_UDMSPINNER:
                    UdmSpinner vSpinner = view.findViewWithTag(viewTagId);
                    if (vSpinner != null) {
                        value = parameter.getValue(vSpinner.getValue());
                        hasElement = true;
                    }
                    break;
                case Constants.UDM_UI_SWITCH:
                    Switch vSwitch = view.findViewWithTag(viewTagId);
                    if (vSwitch != null) {
                        if (vSwitch.isChecked()) {
                            value = Constants.UDM_SWITCH_ON;
                        } else {
                            value = Constants.UDM_SWITCH_OFF;
                        }
                        hasElement = true;
                    }
                    break;
                case Constants.UDM_UI_BUTTON_TEXT:
                    UdmButtonTextEdit buttonTextEdit = view.findViewWithTag(viewTagId);
                    if (buttonTextEdit != null) {
                        value = parameter.getValue(buttonTextEdit.getValue());
                        hasElement = true;
                    }
                    break;
                default:
                    break;
            }
            displayValue = parameter.getDisplayValue(value);
            if (hasElement) {
                switch (parameter.getCovertType()) {
                    case Constants.UDM_PARAM_TYPE_NUMBER://数值类型
                        break;
                    case Constants.UDM_PARAM_TYPE_IPADDR://IP地址
                        value = String.valueOf(IPUtil.getIpFromString(value));
                        break;
                    case Constants.UDM_PARAM_TYPE_TIME://时间类型
                        break;
                    default://字符类型
                        break;
                }
            }
            if (oldChannelParam != null && oldChannelParam.getChannelId().equals(channelId)) {
                List<ParameterItem> paramItems = oldChannelParam.getParamItems();
                for (ParameterItem parameterItem : paramItems) {
                    //参数ID一致且值不一样，则认为是本次有修改的参数
                    if (parameterItem.getParamId().equals(parameter.getId())) {
                        if (parameterItem.getDisplayValue() != null && !parameterItem.getDisplayValue().equals(displayValue)) {
                            ParameterItem changedItem = new ParameterItem(parameter.getId(), value);
                            items.add(changedItem);
                            ContextData.getInstance().addChangedParam(oldChannelParam.getMac(), changedItem);//将有变动的参数保存到上下文中。
                            break;
                        }
                    }
                }
            } else if (oldChannelParam == null) {
                //如果没有旧参数，则认为是最新设置的。
                ParameterItem changedItem = new ParameterItem(parameter.getId(), value);
                items.add(changedItem);
                ContextData.getInstance().addChangedParam(oldChannelParam.getMac(), changedItem);//将有变动的参数保存到上下文中。
            }

        }
        return channelParameter;
    }

}

package com.ibamb.udm.dto;

import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;

import com.ibamb.udm.R;
import com.ibamb.udm.beans.ChannelParameter;
import com.ibamb.udm.beans.ParameterItem;
import com.ibamb.udm.instruct.beans.ChannelParamsID;
import com.ibamb.udm.tag.UdmButtonTextEdit;
import com.ibamb.udm.tag.UdmSpinner;

import java.util.List;

/**
 * Created by luotao on 18-3-18.
 */

public class ParameterTransfer {
    /**
     * transfer device tcp parameter to ui.
     *
     * @param view
     * @param channelParameter
     */
    public static void transTcpParamToView(View view, ChannelParameter channelParameter) {
        String channelId = channelParameter.getChannelId();
        String[] tcpParamIds = ChannelParamsID.getTcpParamsId(channelId);
        for (String paramId : tcpParamIds) {
            String value = channelParameter.getParamValueById(paramId);
            if (paramId.equals("CONN" + channelId + "_NET_PROTOCOL")) {
                CheckBox tcpCheckBox = (CheckBox) view.findViewById(R.id.udm_conn_net_protocol_tcp);
                CheckBox udpCheckBox = (CheckBox) view.findViewById(R.id.udm_conn_net_protocol_udp);
                if (value.equals("TCP")) {
                    tcpCheckBox.setChecked(true);
                    udpCheckBox.setChecked(false);
                } else if (value.equals("UDP")) {
                    tcpCheckBox.setChecked(false);
                    udpCheckBox.setChecked(true);
                } else if (value.equals("Both")) {
                    tcpCheckBox.setChecked(true);
                    udpCheckBox.setChecked(true);
                }
            } else if (paramId.equals("CONN" + channelId + "_TCP_WORK_MODE")) {
                ((UdmSpinner) view.findViewById(R.id.udm_conn_tcp_work_mode)).setValue(value);
            } else if (paramId.equals("CONN" + channelId + "_TCP_CONN_RESPONS")) {
                ((UdmSpinner) view.findViewById(R.id.udm_conn_tcp_conn_respons)).setValue(value);
            } else if (paramId.equals("CONN" + channelId + "_TCP_HOST_IP0")) {
                ((EditText) view.findViewById(R.id.udm_conn_tcpudp_host_ip0)).setText(value);
            } else if (paramId.equals("CONN" + channelId + "_TCP_HOST_PORT0")) {
                ((UdmButtonTextEdit) view.findViewById(R.id.udm_conn_tcpudp_host_port0)).setValue(value);
            } else if (paramId.equals("CONN" + channelId + "_TCP_LOCAL_PORT")) {
                ((UdmButtonTextEdit) view.findViewById(R.id.udm_conn_tcpudp_local_port)).setValue(value);
            } else if (paramId.equals("UART" + channelId + "_STOPBIT")) {
                ((UdmSpinner) view.findViewById(R.id.udm_uart_stopbit)).setValue(value);
            } else if (paramId.equals("UART" + channelId + "_DATABIT")) {
                ((UdmSpinner) view.findViewById(R.id.udm_uart_databit)).setValue(value);
            } else if (paramId.equals("UART" + channelId + "_BDRATE")) {
                ((UdmSpinner) view.findViewById(R.id.udm_uart_bdrate)).setValue(value);
            } else if (paramId.equals("UART" + channelId + "_PARITY")) {
                ((UdmSpinner) view.findViewById(R.id.udm_uart_parity)).setValue(value);
            } else if (paramId.equals("UART" + channelId + "_FLOWRNT")) {
                ((UdmSpinner) view.findViewById(R.id.udm_uart_flowrnt)).setValue(value);
            } else if (paramId.equals("UART" + channelId + "_IDLE_TIME_PACKINGR")) {
                ((UdmButtonTextEdit) view.findViewById(R.id.udm_uart_idle_time_packingr)).setValue(value);
            } else if (paramId.equals("CONN" + channelId + "_LINK_LATCH_TIMEOUT")) {
                ((UdmButtonTextEdit) view.findViewById(R.id.udm_conn_link_latch_timeout)).setValue(value);
            }
        }
    }

    /**
     * transfer udp parameter to ui.
     *
     * @param view
     * @param channelParameter
     */
    public static void transUdpParamToView(View view, ChannelParameter channelParameter) {
        String channelId = channelParameter.getChannelId();
        String[] udpParamIds = ChannelParamsID.getUdpParamsId(channelId);
        for (String paramId : udpParamIds) {
            String value = channelParameter.getParamValueById(paramId);
            if (paramId.equals("CONN" + channelId + "_NET_PROTOCOL")) {
                CheckBox tcpCheckBox = (CheckBox) view.findViewById(R.id.udm_conn_net_protocol_tcp);
                CheckBox udpCheckBox = (CheckBox) view.findViewById(R.id.udm_conn_net_protocol_udp);
                if (value.equals("TCP")) {
                    tcpCheckBox.setChecked(true);
                    udpCheckBox.setChecked(false);
                } else if (value.equals("UDP")) {
                    tcpCheckBox.setChecked(false);
                    udpCheckBox.setChecked(true);
                } else if (value.equals("Both")) {
                    tcpCheckBox.setChecked(true);
                    udpCheckBox.setChecked(true);
                }
            } else if (paramId.equals("CONN" + channelId + "_UDP_ACCEPTION")) {
                Switch udpAcception = (Switch) view.findViewById(R.id.udm_conn_udp_acception);
                if (value.equals("0")) {
                    udpAcception.setChecked(false);
                } else if (value.equals("1")) {
                    udpAcception.setChecked(true);
                }
            } else if (paramId.equals("CONN" + channelId + "_UDP_DATA_MODE")) {
                UdmSpinner udpDataMode = (UdmSpinner) view.findViewById(R.id.udm_conn_udp_data_mode);
                udpDataMode.setValue(value);
                if ("uni".equals(value)) {

                } else if ("mutil".equals(value)) {

                }
            } else if (paramId.equals("CONN" + channelId + "_UDP_TMP_HOST_EN")) {
                ((UdmSpinner) view.findViewById(R.id.udm_conn_tcp_conn_respons)).setValue(value);//TODO 与TCP标签公用. 需要分开处理.
            } else if (paramId.equals("CONN" + channelId + "_UDP_UNI_LOCAL_PORT")) {
                ((UdmButtonTextEdit) view.findViewById(R.id.udm_conn_tcpudp_local_port)).setValue(value);//TODO 与TCP标签公用. 需要分开处理.
            } else if (paramId.equals("CONN" + channelId + "_UDP_UNI_HOST_IP0")) {
                ((EditText) view.findViewById(R.id.udm_conn_tcpudp_host_ip0)).setText(value);//TODO 与TCP标签公用. 需要分开处理.
            } else if (paramId.equals("CONN" + channelId + "_UDP_UNI_HOST_IP0")) {
                ((EditText) view.findViewById(R.id.udm_conn_tcpudp_host_ip0)).setText(value);//TODO 与TCP标签公用. 需要分开处理.
            } else if (paramId.equals("CONN" + channelId + "_UDP_UNI_HOST_PORT0")) {
                ((UdmButtonTextEdit) view.findViewById(R.id.udm_conn_tcpudp_host_port0)).setValue(value);//TODO 与TCP标签公用. 需要分开处理.
            } else if (paramId.equals("UART" + channelId + "_STOPBIT")) {
                ((UdmSpinner) view.findViewById(R.id.udm_uart_stopbit)).setValue(value);
            } else if (paramId.equals("UART" + channelId + "_DATABIT")) {
                ((UdmSpinner) view.findViewById(R.id.udm_uart_databit)).setValue(value);
            } else if (paramId.equals("UART" + channelId + "_BDRATE")) {
                ((UdmSpinner) view.findViewById(R.id.udm_uart_bdrate)).setValue(value);
            } else if (paramId.equals("UART" + channelId + "_PARITY")) {
                ((UdmSpinner) view.findViewById(R.id.udm_uart_parity)).setValue(value);
            } else if (paramId.equals("UART" + channelId + "_FLOWRNT")) {
                ((UdmSpinner) view.findViewById(R.id.udm_uart_flowrnt)).setValue(value);
            } else if (paramId.equals("UART" + channelId + "_IDLE_TIME_PACKINGR")) {
                ((UdmButtonTextEdit) view.findViewById(R.id.udm_uart_idle_time_packingr)).setValue(value);
            } else if (paramId.equals("CONN" + channelId + "_LINK_LATCH_TIMEOUT")) {
                ((UdmButtonTextEdit) view.findViewById(R.id.udm_conn_link_latch_timeout)).setValue(value);
            } else if (paramId.equals("CONN" + channelId + "_UDP_MUL_LOCAL_PORT")) {
                ((UdmButtonTextEdit) view.findViewById(R.id.udm_conn_tcpudp_local_port)).setValue(value);//TODO 与TCP标签公用. 需要分开处理.
            } else if (paramId.equals("CONN" + channelId + "_UDP_MUL_REMOTE_PORT")) {
                ((UdmButtonTextEdit) view.findViewById(R.id.udm_conn_tcpudp_host_port0)).setValue(value);//TODO 与TCP标签公用. 需要分开处理.
            } else if (paramId.equals("CONN" + channelId + "_UDP_MUL_REMOTE_IP")) {
                ((UdmButtonTextEdit) view.findViewById(R.id.udm_conn_tcpudp_host_ip0)).setValue(value);//TODO 与TCP标签公用. 需要分开处理.
            }
        }
    }

    /**
     * Get device tcp parameter from ui.
     *
     * @param view
     * @param mac
     * @return
     */
    public static ChannelParameter getTcpParamFromView(View view, String mac) {
        String channelId = ((UdmSpinner) view.findViewById(R.id.udm_conn_net_protocol_set)).getValue();
        ChannelParameter channelParameter = new ChannelParameter(mac, channelId);

        List<ParameterItem> paramItemList = channelParameter.getParamItems();

        CheckBox tcpCheckBox = (CheckBox) view.findViewById(R.id.udm_conn_net_protocol_tcp);
        CheckBox udpCheckBox = (CheckBox) view.findViewById(R.id.udm_conn_net_protocol_udp);
        String value = "";
        if (tcpCheckBox.isChecked() && udpCheckBox.isChecked()) {
            value = "Both";
        } else if (tcpCheckBox.isChecked()) {
            value = "TCP";
        } else if (udpCheckBox.isChecked()) {
            value = "UDP";
        }
        paramItemList.add(new ParameterItem("CONN" + channelId + "_NET_PROTOCOL", value));

        UdmSpinner workMode = view.findViewById(R.id.udm_conn_tcp_work_mode);
        paramItemList.add(new ParameterItem("CONN" + channelId + "_TCP_WORK_MODE", workMode.getValue()));

        UdmSpinner tcpConnRespons = (UdmSpinner) view.findViewById(R.id.udm_conn_tcp_conn_respons);
        paramItemList.add(new ParameterItem("CONN" + channelId + "_TCP_CONN_RESPONS", tcpConnRespons.getValue()));

        EditText hostIp = (EditText) view.findViewById(R.id.udm_conn_tcpudp_host_ip0);
        paramItemList.add(new ParameterItem("CONN" + channelId + "_TCP_HOST_IP0", hostIp.getText().toString()));

        UdmButtonTextEdit hostPort = (UdmButtonTextEdit) view.findViewById(R.id.udm_conn_tcpudp_host_port0);
        paramItemList.add(new ParameterItem("CONN" + channelId + "_TCP_HOST_PORT0", hostPort.getValue()));

        UdmButtonTextEdit localPort = (UdmButtonTextEdit) view.findViewById(R.id.udm_conn_tcpudp_local_port);
        paramItemList.add(new ParameterItem("CONN" + channelId + "_TCP_LOCAL_PORT", localPort.getValue()));

        UdmSpinner stopBit = (UdmSpinner) view.findViewById(R.id.udm_uart_stopbit);
        paramItemList.add(new ParameterItem("UART" + channelId + "_STOPBIT", stopBit.getValue()));

        UdmSpinner dataBit = (UdmSpinner) view.findViewById(R.id.udm_uart_databit);
        paramItemList.add(new ParameterItem("UART" + channelId + "_DATABIT", dataBit.getValue()));

        UdmSpinner bdRate = (UdmSpinner) view.findViewById(R.id.udm_uart_bdrate);
        paramItemList.add(new ParameterItem("UART" + channelId + "_BDRATE", bdRate.getValue()));

        UdmSpinner parity = (UdmSpinner) view.findViewById(R.id.udm_uart_parity);
        paramItemList.add(new ParameterItem("UART" + channelId + "_PARITY", parity.getValue()));

        UdmSpinner flowrnt = (UdmSpinner) view.findViewById(R.id.udm_uart_flowrnt);
        paramItemList.add(new ParameterItem("UART" + channelId + "_FLOWRNT", flowrnt.getValue()));

        UdmButtonTextEdit idleTimePackingr = (UdmButtonTextEdit) view.findViewById(R.id.udm_uart_idle_time_packingr);
        paramItemList.add(new ParameterItem("UART" + channelId + "_IDLE_TIME_PACKINGR", idleTimePackingr.getValue()));

        UdmButtonTextEdit linkLatchTimeOut = (UdmButtonTextEdit) view.findViewById(R.id.udm_conn_link_latch_timeout);
        paramItemList.add(new ParameterItem("CONN" + channelId + "_LINK_LATCH_TIMEOUT", linkLatchTimeOut.getValue()));

        return channelParameter;
    }

    /**
     * Get device udp parameter from ui.
     *
     * @param view
     * @param mac
     * @return
     */
    public static ChannelParameter getUdpParamFromView(View view, String mac) {
        String channelId = ((UdmSpinner) view.findViewById(R.id.udm_conn_net_protocol_set)).getValue();
        ChannelParameter channelParameter = new ChannelParameter(mac, channelId);
        List<ParameterItem> paramItemList = channelParameter.getParamItems();

        CheckBox tcpCheckBox = (CheckBox) view.findViewById(R.id.udm_conn_net_protocol_tcp);
        CheckBox udpCheckBox = (CheckBox) view.findViewById(R.id.udm_conn_net_protocol_udp);
        String value = "";
        if (tcpCheckBox.isChecked() && udpCheckBox.isChecked()) {
            value = "Both";
        } else if (tcpCheckBox.isChecked()) {
            value = "TCP";
        } else if (udpCheckBox.isChecked()) {
            value = "UDP";
        }
        paramItemList.add(new ParameterItem("CONN" + channelId + "_NET_PROTOCOL", value));

        Switch udpAcception = (Switch) view.findViewById(R.id.udm_conn_udp_acception);
        value = udpAcception.isChecked() ? "1" : "0";
        paramItemList.add(new ParameterItem("CONN" + channelId + "_UDP_ACCEPTION", value));

        UdmSpinner udpDataMode = (UdmSpinner) view.findViewById(R.id.udm_conn_udp_data_mode);
        paramItemList.add(new ParameterItem("CONN" + channelId + "_UDP_DATA_MODE", udpDataMode.getValue()));

        UdmSpinner udpTmpHostEn = (UdmSpinner) view.findViewById(R.id.udm_conn_tcp_conn_respons);
        paramItemList.add(new ParameterItem("CONN" + channelId + "_UDP_TMP_HOST_EN", udpTmpHostEn.getValue()));

        UdmButtonTextEdit udpLocalPort = (UdmButtonTextEdit) view.findViewById(R.id.udm_conn_tcpudp_local_port);
        paramItemList.add(new ParameterItem("CONN" + channelId + "_UDP_UNI_LOCAL_PORT", udpLocalPort.getValue()));

        EditText hostIp = (EditText) view.findViewById(R.id.udm_conn_tcpudp_host_ip0);
        paramItemList.add(new ParameterItem("CONN" + channelId + "_UDP_UNI_HOST_IP0", hostIp.getText().toString()));

        UdmButtonTextEdit  hostPort = (UdmButtonTextEdit) view.findViewById(R.id.udm_conn_tcpudp_host_port0);
        paramItemList.add(new ParameterItem("CONN" + channelId + "_UDP_UNI_HOST_PORT0", hostPort.getValue()));

        UdmSpinner stopBit = (UdmSpinner) view.findViewById(R.id.udm_uart_stopbit);
        paramItemList.add(new ParameterItem("UART" + channelId + "_STOPBIT", stopBit.getValue()));

        UdmSpinner dataBit = (UdmSpinner) view.findViewById(R.id.udm_uart_databit);
        paramItemList.add(new ParameterItem("UART" + channelId + "_DATABIT", dataBit.getValue()));

        UdmSpinner bdRate = (UdmSpinner) view.findViewById(R.id.udm_uart_bdrate);
        paramItemList.add(new ParameterItem("UART" + channelId + "_BDRATE", bdRate.getValue()));

        UdmSpinner parity = (UdmSpinner) view.findViewById(R.id.udm_uart_parity);
        paramItemList.add(new ParameterItem("UART" + channelId + "_PARITY", parity.getValue()));

        UdmSpinner flowrnt = (UdmSpinner) view.findViewById(R.id.udm_uart_flowrnt);
        paramItemList.add(new ParameterItem("UART" + channelId + "_FLOWRNT", flowrnt.getValue()));

        UdmButtonTextEdit idleTimePackingr = (UdmButtonTextEdit) view.findViewById(R.id.udm_uart_idle_time_packingr);
        paramItemList.add(new ParameterItem("UART" + channelId + "_IDLE_TIME_PACKINGR", idleTimePackingr.getValue()));

        UdmButtonTextEdit linkLatchTimeOut = (UdmButtonTextEdit) view.findViewById(R.id.udm_conn_link_latch_timeout);
        paramItemList.add(new ParameterItem("CONN" + channelId + "_LINK_LATCH_TIMEOUT", linkLatchTimeOut.getValue()));

        UdmButtonTextEdit  udpMulLocalPort = (UdmButtonTextEdit) view.findViewById(R.id.udm_conn_tcpudp_local_port);
        paramItemList.add(new ParameterItem("CONN" + channelId + "_UDP_MUL_LOCAL_PORT", udpMulLocalPort.getValue()));

        UdmButtonTextEdit udmMulHostPort = (UdmButtonTextEdit) view.findViewById(R.id.udm_conn_tcpudp_host_port0);
        paramItemList.add(new ParameterItem("CONN" + channelId + "_UDP_MUL_REMOTE_PORT", udmMulHostPort.getValue()));

        UdmButtonTextEdit udmMulHostIp = (UdmButtonTextEdit) view.findViewById(R.id.udm_conn_tcpudp_host_ip0);
        paramItemList.add(new ParameterItem("CONN" + channelId + "_UDP_MUL_REMOTE_IP", udmMulHostIp.getValue()));

        return channelParameter;
    }
}

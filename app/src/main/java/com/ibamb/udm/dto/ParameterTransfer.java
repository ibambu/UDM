package com.ibamb.udm.dto;

import android.view.View;

import com.ibamb.udm.R;
import com.ibamb.udm.module.beans.ChannelParameter;
import com.ibamb.udm.module.instruct.beans.ChannelParamsID;
import com.ibamb.udm.tag.UdmSpinner;

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
//
//        //设置设备是否支持TCP和UDP
//        String value = channelParameter.getParamValueById("CONN" + channelId + "_NET_PROTOCOL");
//        CheckBox tcpCheckBox = (CheckBox) view.findViewById(R.id.udm_conn_net_protocol_tcp);
//        CheckBox udpCheckBox = (CheckBox) view.findViewById(R.id.udm_conn_net_protocol_udp);
//        if (value.equals(UdmConstants.CONN_NET_PROTOCOL_TCP)) {
//            tcpCheckBox.setChecked(true);
//            udpCheckBox.setChecked(false);
//        } else if (value.equals(UdmConstants.CONN_NET_PROTOCOL_UDP)) {
//            tcpCheckBox.setChecked(false);
//            udpCheckBox.setChecked(true);
//        } else if (value.equals(UdmConstants.CONN_NET_PROTOCOL_BOTH)) {
//            tcpCheckBox.setChecked(true);
//            udpCheckBox.setChecked(true);
//        }
//        //设置 TCP_WORK_MODE
//        value = channelParameter.getParamValueById("CONN" + channelId + "_TCP_WORK_MODE");
//        ((UdmSpinner) view.findViewById(R.id.udm_conn_tcp_work_mode)).setValue(value);
//        //设置 TCP_CONN_RESPONS
//        value = channelParameter.getParamValueById("CONN" + channelId + "_TCP_CONN_RESPONS");
//        ((UdmSpinner) view.findViewById(R.id.udm_conn_tcp_conn_respons)).setValue(value);
//        //设置 TCP HOST IP
//        value = channelParameter.getParamValueById("CONN" + channelId + "_TCP_HOST_IP0");
//        ((EditText) view.findViewById(R.id.udm_conn_host_ip0)).setText(value);
//        //设置 TCP local port
//        value = channelParameter.getParamValueById("CONN" + channelId + "_TCP_LOCAL_PORT");
//        ((UdmButtonTextEdit) view.findViewById(R.id.udm_conn_local_port)).setValue(value);
//
//        value = channelParameter.getParamValueById("UART" + channelId + "_STOPBIT");
//        ((UdmSpinner) view.findViewById(R.id.udm_uart_stopbit)).setValue(value);
//
//        value = channelParameter.getParamValueById("UART" + channelId + "_DATABIT");
//        ((UdmSpinner) view.findViewById(R.id.udm_uart_databit)).setValue(value);
//
//        value = channelParameter.getParamValueById("UART" + channelId + "_BDRATE");
//        ((UdmSpinner) view.findViewById(R.id.udm_uart_bdrate)).setValue(value);
//
//        value = channelParameter.getParamValueById("UART" + channelId + "_PARITY");
//        ((UdmSpinner) view.findViewById(R.id.udm_uart_parity)).setValue(value);
//
//        value = channelParameter.getParamValueById("UART" + channelId + "_FLOWRNT");
//        ((UdmSpinner) view.findViewById(R.id.udm_uart_flowrnt)).setValue(value);
//
//        value = channelParameter.getParamValueById("UART" + channelId + "_IDLE_TIME_PACKINGR");
//        ((UdmButtonTextEdit) view.findViewById(R.id.udm_uart_idle_time_packingr)).setValue(value);
//
//        value = channelParameter.getParamValueById("CONN" + channelId + "_LINK_LATCH_TIMEOUT");
//        ((UdmButtonTextEdit) view.findViewById(R.id.udm_conn_link_latch_timeout)).setValue(value);

    }

    /**
     * transfer udp parameter to ui.
     *
     * @param view
     * @param channelParameter
     */
    public static void transUdpParamToView(View view, ChannelParameter channelParameter) {
//
//        String channelId = channelParameter.getChannelId();
//        String[] udpParamIds = ChannelParamsID.getUdpParamsId(channelId);
//        //设置设备是否支持TCP和UDP
//        String value = channelParameter.getParamValueById("CONN" + channelId + "_NET_PROTOCOL");
//        CheckBox tcpCheckBox = (CheckBox) view.findViewById(R.id.udm_conn_net_protocol_tcp);
//        CheckBox udpCheckBox = (CheckBox) view.findViewById(R.id.udm_conn_net_protocol_udp);
//
//        if (value.equals(UdmConstants.CONN_NET_PROTOCOL_TCP)) {
//            tcpCheckBox.setChecked(true);
//            udpCheckBox.setChecked(false);
//        } else if (value.equals(UdmConstants.CONN_NET_PROTOCOL_UDP)) {
//            tcpCheckBox.setChecked(false);
//            udpCheckBox.setChecked(true);
//        } else if (value.equals(UdmConstants.CONN_NET_PROTOCOL_BOTH)) {
//            tcpCheckBox.setChecked(true);
//            udpCheckBox.setChecked(true);
//        }
//        value = channelParameter.getParamValueById("CONN" + channelId + "_UDP_ACCEPTION");
//        Switch udpAcception = (Switch) view.findViewById(R.id.udm_conn_udp_acception);
//        if (value.equals("0")) {
//            udpAcception.setChecked(false);
//        } else if (value.equals("1")) {
//            udpAcception.setChecked(true);
//        }
//
//        value = channelParameter.getParamValueById("CONN" + channelId + "_UDP_DATA_MODE");
//        UdmSpinner udpDataMode = (UdmSpinner) view.findViewById(R.id.udm_conn_udp_data_mode);
//        udpDataMode.setValue(value);
//
//        value = channelParameter.getParamValueById("CONN" + channelId + "_UDP_TMP_HOST_EN");
//        ((UdmSpinner) view.findViewById(R.id.udm_conn_udp_tmp_host_en)).setValue(value);
//
//        if (UdmConstants.UDP_DATA_MODE_UNI.equals(udpDataMode.getValue())) {
//            value = channelParameter.getParamValueById("CONN" + channelId + "_UDP_UNI_LOCAL_PORT");
//            ((UdmButtonTextEdit) view.findViewById(R.id.udm_conn_local_port)).setValue(value);//
//
//            value = channelParameter.getParamValueById("CONN" + channelId + "_UDP_UNI_HOST_IP0");
//            ((EditText) view.findViewById(R.id.udm_conn_host_ip0)).setText(value);//
//
//            value = channelParameter.getParamValueById("CONN" + channelId + "_UDP_UNI_HOST_PORT0");
//            ((EditText) view.findViewById(R.id.udm_conn_host_port0)).setText(value);//
//        } else if (UdmConstants.UDP_DATA_MODE_MUL.equals(udpDataMode.getValue())) {
//            value = channelParameter.getParamValueById("CONN" + channelId + "_UDP_MUL_LOCAL_PORT");
//            ((UdmButtonTextEdit) view.findViewById(R.id.udm_conn_local_port)).setValue(value);//TODO与TCP标签公用.需要分开处理.
//
//            value = channelParameter.getParamValueById("CONN" + channelId + "_UDP_MUL_REMOTE_PORT");
//            ((UdmButtonTextEdit) view.findViewById(R.id.udm_conn_host_port0)).setValue(value);//TODO与TCP标签公用.需要分开处理.
//
//            value = channelParameter.getParamValueById("CONN" + channelId + "_UDP_MUL_REMOTE_IP");
//            ((UdmButtonTextEdit) view.findViewById(R.id.udm_conn_host_ip0)).setValue(value);//TODO与TCP标签公用.需要分开处理.
//        }
//
//        value = channelParameter.getParamValueById("UART" + channelId + "_STOPBIT");
//        ((UdmSpinner) view.findViewById(R.id.udm_uart_stopbit)).setValue(value);
//
//        value = channelParameter.getParamValueById("UART" + channelId + "_DATABIT");
//        ((UdmSpinner) view.findViewById(R.id.udm_uart_databit)).setValue(value);
//
//        value = channelParameter.getParamValueById("UART" + channelId + "_BDRATE");
//        ((UdmSpinner) view.findViewById(R.id.udm_uart_bdrate)).setValue(value);
//
//        value = channelParameter.getParamValueById("UART" + channelId + "_PARITY");
//        ((UdmSpinner) view.findViewById(R.id.udm_uart_parity)).setValue(value);
//
//        value = channelParameter.getParamValueById("UART" + channelId + "_FLOWRNT");
//        ((UdmSpinner) view.findViewById(R.id.udm_uart_flowrnt)).setValue(value);
//
//        value = channelParameter.getParamValueById("UART" + channelId + "_IDLE_TIME_PACKINGR");
//        ((UdmButtonTextEdit) view.findViewById(R.id.udm_uart_idle_time_packingr)).setValue(value);
//
//        value = channelParameter.getParamValueById("CONN" + channelId + "_LINK_LATCH_TIMEOUT");
//        ((UdmButtonTextEdit) view.findViewById(R.id.udm_conn_link_latch_timeout)).setValue(value);

    }

    /**
     * Get device tcp parameter from ui.
     *
     * @param view
     * @param channelParameter
     * @return
     */
    public static ChannelParameter getTcpParamFromView(View view, ChannelParameter channelParameter) {
//        String channelId = ((UdmSpinner) view.findViewById(R.id.udm_conn_net_protocol_set)).getValue();
//
//
//        List<ParameterItem> paramItemList = channelParameter.getParamItems();
//
//        CheckBox tcpCheckBox = (CheckBox) view.findViewById(R.id.udm_conn_net_protocol_tcp);
//        CheckBox udpCheckBox = (CheckBox) view.findViewById(R.id.udm_conn_net_protocol_udp);
//        String value = "";
//        if (tcpCheckBox.isChecked() && udpCheckBox.isChecked()) {
//            value = UdmConstants.CONN_NET_PROTOCOL_BOTH;
//        } else if (tcpCheckBox.isChecked()) {
//            value = UdmConstants.CONN_NET_PROTOCOL_TCP;
//        } else if (udpCheckBox.isChecked()) {
//            value = UdmConstants.CONN_NET_PROTOCOL_UDP;
//        }
//        channelParameter.updateParamValueById("CONN" + channelId + "_NET_PROTOCOL", value);
//
//        UdmSpinner workMode = view.findViewById(R.id.udm_conn_tcp_work_mode);
//        channelParameter.updateParamValueById("CONN" + channelId + "_TCP_WORK_MODE", workMode.getValue());
//
//        UdmSpinner tcpConnRespons = (UdmSpinner) view.findViewById(R.id.udm_conn_tcp_conn_respons);
//        channelParameter.updateParamValueById("CONN" + channelId + "_TCP_CONN_RESPONS", tcpConnRespons.getValue());
//
//        EditText hostIp = (EditText) view.findViewById(R.id.udm_conn_host_ip0);
//        channelParameter.updateParamValueById("CONN" + channelId + "_TCP_HOST_IP0", hostIp.getText().toString());
//
//        UdmButtonTextEdit hostPort = (UdmButtonTextEdit) view.findViewById(R.id.udm_conn_host_port0);
//        channelParameter.updateParamValueById("CONN" + channelId + "_TCP_HOST_PORT0", hostPort.getValue());
//
//        UdmButtonTextEdit localPort = (UdmButtonTextEdit) view.findViewById(R.id.udm_conn_local_port);
//        channelParameter.updateParamValueById("CONN" + channelId + "_TCP_LOCAL_PORT", localPort.getValue());
//
//        UdmSpinner stopBit = (UdmSpinner) view.findViewById(R.id.udm_uart_stopbit);
//        channelParameter.updateParamValueById("UART" + channelId + "_STOPBIT", stopBit.getValue());
//
//        UdmSpinner dataBit = (UdmSpinner) view.findViewById(R.id.udm_uart_databit);
//        channelParameter.updateParamValueById("UART" + channelId + "_DATABIT", dataBit.getValue());
//
//        UdmSpinner bdRate = (UdmSpinner) view.findViewById(R.id.udm_uart_bdrate);
//        channelParameter.updateParamValueById("UART" + channelId + "_BDRATE", bdRate.getValue());
//
//        UdmSpinner parity = (UdmSpinner) view.findViewById(R.id.udm_uart_parity);
//        channelParameter.updateParamValueById("UART" + channelId + "_PARITY", parity.getValue());
//
//        UdmSpinner flowrnt = (UdmSpinner) view.findViewById(R.id.udm_uart_flowrnt);
//        channelParameter.updateParamValueById("UART" + channelId + "_FLOWRNT", flowrnt.getValue());
//
//        UdmButtonTextEdit idleTimePackingr = (UdmButtonTextEdit) view.findViewById(R.id.udm_uart_idle_time_packingr);
//        channelParameter.updateParamValueById("UART" + channelId + "_IDLE_TIME_PACKINGR", idleTimePackingr.getValue());
//
//        UdmButtonTextEdit linkLatchTimeOut = (UdmButtonTextEdit) view.findViewById(R.id.udm_conn_link_latch_timeout);
//        channelParameter.updateParamValueById("CONN" + channelId + "_LINK_LATCH_TIMEOUT", linkLatchTimeOut.getValue());

        return channelParameter;
    }

    /**
     * Get device udp parameter from ui.
     *
     * @param view
     * @param channelParameter
     * @return
     */
    public static ChannelParameter getUdpParamFromView(View view, ChannelParameter channelParameter) {
        String channelId = ((UdmSpinner) view.findViewById(R.id.udm_conn_net_protocol_set)).getValue();
//
//        CheckBox tcpCheckBox = (CheckBox) view.findViewById(R.id.udm_conn_net_protocol_tcp);
//        CheckBox udpCheckBox = (CheckBox) view.findViewById(R.id.udm_conn_net_protocol_udp);
//        String value = "";
//        if (tcpCheckBox.isChecked() && udpCheckBox.isChecked()) {
//            value = UdmConstants.CONN_NET_PROTOCOL_BOTH;
//        } else if (tcpCheckBox.isChecked()) {
//            value = UdmConstants.CONN_NET_PROTOCOL_TCP;
//        } else if (udpCheckBox.isChecked()) {
//            value = UdmConstants.CONN_NET_PROTOCOL_UDP;
//        }
//        channelParameter.updateParamValueById("CONN" + channelId + "_NET_PROTOCOL", value);
//
//        Switch udpAcception = (Switch) view.findViewById(R.id.udm_conn_udp_acception);
//        value = udpAcception.isChecked() ? UdmConstants.CONN_UDP_ACCEPTION_ON : UdmConstants.CONN_UDP_ACCEPTION_OFF;
//        channelParameter.updateParamValueById("CONN" + channelId + "_UDP_ACCEPTION", value);
//
//        UdmSpinner udpDataMode = (UdmSpinner) view.findViewById(R.id.udm_conn_udp_data_mode);
//        channelParameter.updateParamValueById("CONN" + channelId + "_UDP_DATA_MODE", udpDataMode.getValue());
//
//        UdmSpinner udpTmpHostEn = (UdmSpinner) view.findViewById(R.id.udm_conn_tcp_conn_respons);
//        channelParameter.updateParamValueById("CONN" + channelId + "_UDP_TMP_HOST_EN", udpTmpHostEn.getValue());
//
//        if(UdmConstants.UDP_DATA_MODE_UNI.equals(udpDataMode.getValue())){
//            UdmButtonTextEdit udpLocalPort = (UdmButtonTextEdit) view.findViewById(R.id.udm_conn_local_port);
//            channelParameter.updateParamValueById("CONN" + channelId + "_UDP_UNI_LOCAL_PORT", udpLocalPort.getValue());
//
//            EditText hostIp = (EditText) view.findViewById(R.id.udm_conn_host_ip0);
//            channelParameter.updateParamValueById("CONN" + channelId + "_UDP_UNI_HOST_IP0", hostIp.getText().toString());
//
//            UdmButtonTextEdit hostPort = (UdmButtonTextEdit) view.findViewById(R.id.udm_conn_host_port0);
//            channelParameter.updateParamValueById("CONN" + channelId + "_UDP_UNI_HOST_PORT0", hostPort.getValue());
//        }else if(UdmConstants.UDP_DATA_MODE_MUL.equals(udpDataMode.getValue())){
//
//            UdmButtonTextEdit udpMulLocalPort = (UdmButtonTextEdit) view.findViewById(R.id.udm_conn_local_port);
//            channelParameter.updateParamValueById("CONN" + channelId + "_UDP_MUL_LOCAL_PORT", udpMulLocalPort.getValue());
//
//            UdmButtonTextEdit udmMulHostPort = (UdmButtonTextEdit) view.findViewById(R.id.udm_conn_host_port0);
//            channelParameter.updateParamValueById("CONN" + channelId + "_UDP_MUL_REMOTE_PORT", udmMulHostPort.getValue());
//
//            UdmButtonTextEdit udmMulHostIp = (UdmButtonTextEdit) view.findViewById(R.id.udm_conn_host_ip0);
//            channelParameter.updateParamValueById("CONN" + channelId + "_UDP_MUL_REMOTE_IP", udmMulHostIp.getValue());
//        }
//
//        UdmSpinner stopBit = (UdmSpinner) view.findViewById(R.id.udm_uart_stopbit);
//        channelParameter.updateParamValueById("UART" + channelId + "_STOPBIT", stopBit.getValue());
//
//        UdmSpinner dataBit = (UdmSpinner) view.findViewById(R.id.udm_uart_databit);
//        channelParameter.updateParamValueById("UART" + channelId + "_DATABIT", dataBit.getValue());
//
//        UdmSpinner bdRate = (UdmSpinner) view.findViewById(R.id.udm_uart_bdrate);
//        channelParameter.updateParamValueById("UART" + channelId + "_BDRATE", bdRate.getValue());
//
//        UdmSpinner parity = (UdmSpinner) view.findViewById(R.id.udm_uart_parity);
//        channelParameter.updateParamValueById("UART" + channelId + "_PARITY", parity.getValue());
//
//        UdmSpinner flowrnt = (UdmSpinner) view.findViewById(R.id.udm_uart_flowrnt);
//        channelParameter.updateParamValueById("UART" + channelId + "_FLOWRNT", flowrnt.getValue());
//
//        UdmButtonTextEdit idleTimePackingr = (UdmButtonTextEdit) view.findViewById(R.id.udm_uart_idle_time_packingr);
//        channelParameter.updateParamValueById("UART" + channelId + "_IDLE_TIME_PACKINGR", idleTimePackingr.getValue());
//
//        UdmButtonTextEdit linkLatchTimeOut = (UdmButtonTextEdit) view.findViewById(R.id.udm_conn_link_latch_timeout);
//        channelParameter.updateParamValueById("CONN" + channelId + "_LINK_LATCH_TIMEOUT", linkLatchTimeOut.getValue());
//

        return channelParameter;
    }

    public static  void updateChannelUdpUniParams(View view, ChannelParameter channelParameter){
        UdmSpinner udpDataMode = (UdmSpinner) view.findViewById(R.id.udm_conn_udp_data_mode);
//        String channelId = ((UdmSpinner) view.findViewById(R.id.udm_conn_net_protocol_set)).getValue();
//        if(UdmConstants.UDP_DATA_MODE_UNI.equals(udpDataMode.getValue())){
//            UdmButtonTextEdit udpLocalPort = (UdmButtonTextEdit) view.findViewById(R.id.udm_conn_local_port);
//            channelParameter.updateParamValueById("CONN" + channelId + "_UDP_UNI_LOCAL_PORT", udpLocalPort.getValue());
//
//            EditText hostIp = (EditText) view.findViewById(R.id.udm_conn_host_ip0);
//            channelParameter.updateParamValueById("CONN" + channelId + "_UDP_UNI_HOST_IP0", hostIp.getText().toString());
//
//            UdmButtonTextEdit hostPort = (UdmButtonTextEdit) view.findViewById(R.id.udm_conn_host_port0);
//            channelParameter.updateParamValueById("CONN" + channelId + "_UDP_UNI_HOST_PORT0", hostPort.getValue());
//        }
    }

    public static void updateChannelUdpMutilParams(View view, ChannelParameter channelParameter){
        UdmSpinner udpDataMode = (UdmSpinner) view.findViewById(R.id.udm_conn_udp_data_mode);
//        String channelId = ((UdmSpinner) view.findViewById(R.id.udm_conn_net_protocol_set)).getValue();
//        if(UdmConstants.UDP_DATA_MODE_MUL.equals(udpDataMode.getValue())){
//
//            UdmButtonTextEdit udpMulLocalPort = (UdmButtonTextEdit) view.findViewById(R.id.udm_conn_local_port);
//            channelParameter.updateParamValueById("CONN" + channelId + "_UDP_MUL_LOCAL_PORT", udpMulLocalPort.getValue());
//
//            UdmButtonTextEdit udmMulHostPort = (UdmButtonTextEdit) view.findViewById(R.id.udm_conn_host_port0);
//            channelParameter.updateParamValueById("CONN" + channelId + "_UDP_MUL_REMOTE_PORT", udmMulHostPort.getValue());
//
//            UdmButtonTextEdit udmMulHostIp = (UdmButtonTextEdit) view.findViewById(R.id.udm_conn_host_ip0);
//            channelParameter.updateParamValueById("CONN" + channelId + "_UDP_MUL_REMOTE_IP", udmMulHostIp.getValue());
//        }
    }
}

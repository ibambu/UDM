package com.ibamb.udm.dto;

import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.ibamb.udm.R;
import com.ibamb.udm.beans.ParameterItem;
import com.ibamb.udm.beans.UDPChannelParameter;
import com.ibamb.udm.tag.UdmButtonTextEdit;

import java.util.ArrayList;

/**
 * Created by luotao on 18-2-26.
 */

public class UDPChannelParameterDTO {
    private String channelId;
    private View view;

    public UDPChannelParameterDTO(String channelId, View view) {
        this.channelId = channelId;
        this.view = view;
    }

    public UDPChannelParameter getParamFromView() {
        UDPChannelParameter channelParameter = new UDPChannelParameter();
        channelParameter.setChannelId(Integer.parseInt(channelId));
        channelParameter.setBaseParamItems(new ArrayList<ParameterItem>());
        channelParameter.setUniParamItems(new ArrayList<ParameterItem>());
        channelParameter.setMultiParamItems(new ArrayList<ParameterItem>());
//
//
//        boolean isAcceptIncome = ((CheckBox) view.findViewById(R.id.id_accepting_income)).isChecked();
//        ParameterItem acceptIncome = new ParameterItem("CONN" + channelId + "_UDP_ACCEPTION", String.valueOf(isAcceptIncome));
//        channelParameter.getBaseParamItems().add(acceptIncome);
//
//
//        String udpDataMode = (String) ((Spinner) view.findViewById(R.id.id_connect_uni_multi)).getSelectedItem();//Option
//        ParameterItem udpDataModeItem = new ParameterItem("CONN" + channelId + "_UDP_DATA_MODE", udpDataMode);
//        channelParameter.getUniParamItems().add(udpDataModeItem);
//
//        String connResons = (String) ((Spinner) view.findViewById(R.id.id_connect_rts)).getSelectedItem();//Option
//        ParameterItem connResonsItem = new ParameterItem("CONN" + channelId + "__UDP_TMP_HOST_EN", connResons);
//        channelParameter.getUniParamItems().add(connResonsItem);
//
//        String localPort = ((UdmButtonTextEdit) view.findViewById(R.id.id_local_port)).getValueEditText();//local port
//        ParameterItem localPortItem = new ParameterItem("CONN" + channelId + "_UDP_UNI_LOCAL_PORT", localPort);
//        channelParameter.getUniParamItems().add(localPortItem);
//
//        String remotePort = ((UdmButtonTextEdit) view.findViewById(R.id.id_remort_port)).getValueEditText();//remote port
//        ParameterItem remotePortItem = new ParameterItem("CONN" + channelId + "_UDP_UNI_HOST_PORT0", remotePort);
//        channelParameter.getUniParamItems().add(remotePortItem);
//
//
//        String remoteHost = (String) ((EditText) view.findViewById(R.id.id_remort_host)).getText().toString();//remote host
//        ParameterItem remoteHostItem = new ParameterItem("CONN" + channelId + "_UDP_UNI_HOST_IP0", remoteHost);
//        channelParameter.getUniParamItems().add(remoteHostItem);
//        //TODO wait find parameter id.
//        String serialProtocol = (String) ((Spinner) view.findViewById(R.id.id_serial_protocol)).getSelectedItem();//protocol
//        ParameterItem protoclItem = new ParameterItem("UART" + channelId + "_BDRATE", serialProtocol);
//        channelParameter.getUniParamItems().add(protoclItem);
//
//        String bautRate = (String) ((Spinner) view.findViewById(R.id.id_baud_rate)).getSelectedItem();//baud rate
//        ParameterItem bdrateItem = new ParameterItem("UART" + channelId + "_BDRATE", bautRate);
//        channelParameter.getUniParamItems().add(bdrateItem);
//
//        String dataBits = (String) ((Spinner) view.findViewById(R.id.id_data_bits)).getSelectedItem();//data bits
//        ParameterItem dataBitItem = new ParameterItem("UART" + channelId + "_DATABIT", bautRate);
//        channelParameter.getUniParamItems().add(dataBitItem);
//
//        String stopBits = (String) ((Spinner) view.findViewById(R.id.id_stop_bits)).getSelectedItem();//stop bits
//        ParameterItem stopBitItem = new ParameterItem("UART" + channelId + "_STOPBIT", stopBits);
//        channelParameter.getUniParamItems().add(stopBitItem);
//
//        String flowControl = (String) ((Spinner) view.findViewById(R.id.id_flow_control)).getSelectedItem();//flow control
//        ParameterItem flowControlItem = new ParameterItem("UART" + channelId + "_FLOWRNT", flowControl);
//        channelParameter.getUniParamItems().add(flowControlItem);
//
//        String serialParity = (String) ((Spinner) view.findViewById(R.id.id_serial_parity)).getSelectedItem();//serial parity
//        ParameterItem serialParitylItem = new ParameterItem("UART" + channelId + "_PARITY", flowControl);
//        channelParameter.getUniParamItems().add(serialParitylItem);
//
//        String uartLdleTime = ((UdmButtonTextEdit) view.findViewById(R.id.id_uart_ldle_time)).getValueEditText();//uart ldle time
//        ParameterItem uartLdleTimeItem = new ParameterItem("UART" + channelId + "_IDLE_TIME_PACKINGR", uartLdleTime);
//        channelParameter.getUniParamItems().add(uartLdleTimeItem);
//        //TODO wait conform
//        String uartOutIfg = ((UdmButtonTextEdit) view.findViewById(R.id.id_uart_out_ifg)).getValueEditText();// rart out ifg
//        ParameterItem uartOutIfgItem = new ParameterItem("CONN" + channelId + "_MERGE_TIMEOUT", uartOutIfg);
//        channelParameter.getUniParamItems().add(uartOutIfgItem);
//
//        String linkLatch = ((UdmButtonTextEdit) view.findViewById(R.id.id_link_latch)).getValueEditText();//link latch
//        ParameterItem linkLatchItem = new ParameterItem("CONN" + channelId + "_LINK_LATCH_TIMEOUT", linkLatch);
//        channelParameter.getUniParamItems().add(linkLatchItem);

        return channelParameter;

    }

}

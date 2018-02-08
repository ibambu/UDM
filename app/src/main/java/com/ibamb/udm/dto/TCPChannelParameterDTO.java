package com.ibamb.udm.dto;

import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.ibamb.udm.R;
import com.ibamb.udm.beans.ParameterItem;
import com.ibamb.udm.beans.TCPChannelParameter;
import com.ibamb.udm.tag.IncDecTextEdit;

import java.util.ArrayList;

/**
 * Created by luotao on 18-2-4.
 */

public class TCPChannelParameterDTO {

    private String channelId;
    private View view;

    public TCPChannelParameterDTO(View view) {
        this.view = view;
        this.channelId = "1";//((Spinner)view.findViewById(R.id.id_connect_channel)).getSelectedItem().toString();
    }

    public TCPChannelParameter getParamFromView() {
        TCPChannelParameter channelParameter = new TCPChannelParameter();
        channelParameter.setChannelId(channelId);
        channelParameter.setParameterItems(new ArrayList<ParameterItem>());

//        boolean isTCPPermit = ((CheckBox) view.findViewById(R.id.id_tcp_check_box)).isChecked();
//        boolean isUDPPermit = ((CheckBox) view.findViewById(R.id.id_udp_check_box)).isChecked();
//        String netProtocol = "";
//        if (isTCPPermit && isUDPPermit) {
//            netProtocol = "Both";
//        } else if (isTCPPermit) {
//            netProtocol = "TCP";
//        } else if (isUDPPermit) {
//            netProtocol = "UDP";
//        }
//        ParameterItem netProtocolItem = new ParameterItem("CONN" + channelId + "_NET_PROTOCOL", netProtocol);
//        channelParameter.getParameterItems().add(netProtocolItem);
        Spinner spinner = view.findViewById(R.id.id_work_role);
        spinner.setSelection(1);
        String workMode = (String) ((Spinner) view.findViewById(R.id.id_work_role)).getSelectedItem();//work mode
        ParameterItem workModeItem = new ParameterItem("CONN" + channelId + "_TCP_WORK_MODE", workMode);
        channelParameter.getParameterItems().add(workModeItem);

        String connResons = (String) ((Spinner) view.findViewById(R.id.id_connect_rts)).getSelectedItem();//Option
        ParameterItem connResonsItem = new ParameterItem("CONN" + channelId + "_TCP_CONN_RESPONS", connResons);
        channelParameter.getParameterItems().add(connResonsItem);

        String localPort = ((IncDecTextEdit) view.findViewById(R.id.id_local_port)).getValueEditText();//local port
        ParameterItem localPortItem = new ParameterItem("CONN" + channelId + "_TCP_LOCAL_PORT", localPort);
        channelParameter.getParameterItems().add(localPortItem);

        String remotePort = ((IncDecTextEdit) view.findViewById(R.id.id_remort_port)).getValueEditText();//remote port
        ParameterItem remotePortItem = new ParameterItem("CONN" + channelId + "_TCP_HOST_PORT0", remotePort);
        channelParameter.getParameterItems().add(remotePortItem);


        String remoteHost = (String) ((EditText) view.findViewById(R.id.id_remort_host)).getText().toString();//remote host
        ParameterItem remoteHostItem = new ParameterItem("CONN" + channelId + "_TCP_HOST_IP0", remoteHost);
        channelParameter.getParameterItems().add(remoteHostItem);
        //TODO wait find parameter id.
        String serialProtocol = (String) ((Spinner) view.findViewById(R.id.id_serial_protocol)).getSelectedItem();//protocol
        ParameterItem protoclItem = new ParameterItem("UART" + channelId + "_BDRATE", serialProtocol);
        channelParameter.getParameterItems().add(protoclItem);

        String bautRate = (String) ((Spinner) view.findViewById(R.id.id_baud_rate)).getSelectedItem();//baud rate
        ParameterItem bdrateItem = new ParameterItem("UART" + channelId + "_BDRATE", bautRate);
        channelParameter.getParameterItems().add(bdrateItem);

        String dataBits = (String) ((Spinner) view.findViewById(R.id.id_data_bits)).getSelectedItem();//data bits
        ParameterItem dataBitItem = new ParameterItem("UART" + channelId + "_DATABIT", bautRate);
        channelParameter.getParameterItems().add(dataBitItem);

        String stopBits = (String) ((Spinner) view.findViewById(R.id.id_stop_bits)).getSelectedItem();//stop bits
        ParameterItem stopBitItem = new ParameterItem("UART" + channelId + "_STOPBIT", stopBits);
        channelParameter.getParameterItems().add(stopBitItem);

        String flowControl = (String) ((Spinner) view.findViewById(R.id.id_flow_control)).getSelectedItem();//flow control
        ParameterItem flowControlItem = new ParameterItem("UART" + channelId + "_FLOWRNT", flowControl);
        channelParameter.getParameterItems().add(flowControlItem);

        String serialParity = (String) ((Spinner) view.findViewById(R.id.id_serial_parity)).getSelectedItem();//serial parity
        ParameterItem serialParitylItem = new ParameterItem("UART" + channelId + "_PARITY", flowControl);
        channelParameter.getParameterItems().add(serialParitylItem);

        String uartLdleTime = ((IncDecTextEdit) view.findViewById(R.id.id_uart_ldle_time)).getValueEditText();//uart ldle time
        ParameterItem uartLdleTimeItem = new ParameterItem("UART" + channelId + "_IDLE_TIME_PACKINGR", uartLdleTime);
        channelParameter.getParameterItems().add(uartLdleTimeItem);
        //TODO wait conform
        String uartOutIfg = ((IncDecTextEdit) view.findViewById(R.id.id_uart_out_ifg)).getValueEditText();// rart out ifg
        ParameterItem uartOutIfgItem = new ParameterItem("CONN" + channelId + "_MERGE_TIMEOUT", uartOutIfg);
        channelParameter.getParameterItems().add(uartOutIfgItem);

        String linkLatch = ((IncDecTextEdit) view.findViewById(R.id.id_link_latch)).getValueEditText();//link latch
        ParameterItem linkLatchItem = new ParameterItem("CONN" + channelId + "_LINK_LATCH_TIMEOUT", linkLatch);
        channelParameter.getParameterItems().add(linkLatchItem);

        channelParameter.print();
        return channelParameter;

    }
}

package com.ibamb.udm.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.ibamb.udm.R;
import com.ibamb.udm.beans.ChannelParameter;
import com.ibamb.udm.constants.UdmConstants;
import com.ibamb.udm.dto.ParameterTransfer;
import com.ibamb.udm.instruct.IParameterReaderWriter;
import com.ibamb.udm.instruct.beans.ChannelParamsID;
import com.ibamb.udm.tag.UdmButtonTextEdit;
import com.ibamb.udm.tag.UdmSpinner;


public class ConnectSettingFragment extends Fragment {

    private static final String HOST_IP = "IP";
    private static final String HOST_MAC = "MAC";

    private IParameterReaderWriter parameterReaderWriter;
    private View currentView;

    private String ip;
    private String mac;

    private UdmSpinner toSetProtocol;//tup/udp
    private UdmSpinner toSetChannel;
    private UdmSpinner toSetUdpDataMode;
    private Button commitButton;

    private ChannelParameter channelParameter;


    private class CommitButtonListener implements Button.OnClickListener {

        @Override
        public void onClick(View v) {

        }
    }


    public ConnectSettingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param ip  Parameter 1.
     * @param mac Parameter 2.
     * @return A new instance of fragment ConnectSettingFragment.
     */
    public static ConnectSettingFragment newInstance(String ip, String mac) {
        ConnectSettingFragment fragment = new ConnectSettingFragment();
        Bundle args = new Bundle();
        args.putString(HOST_IP, ip);
        args.putString(HOST_MAC, mac);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ip = getArguments().getString(HOST_IP);
            mac = getArguments().getString(HOST_MAC);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        currentView = inflater.inflate(R.layout.fragment_connect_setting, container, false);
        toSetProtocol = (UdmSpinner) currentView.findViewById(R.id.udm_conn_net_protocol_set);
        toSetChannel = (UdmSpinner) currentView.findViewById(R.id.udm_connect_channel_set);
        toSetUdpDataMode = (UdmSpinner) currentView.findViewById(R.id.udm_conn_udp_data_mode);
        commitButton = currentView.findViewById(R.id.id_conect_setting_commit);

        String[] parmaIds = ChannelParamsID.getTcpParamsId("1");// read default channel 1. default protocol tcp.
        ChannelParameter initChannelParam = null;//parameterReaderWriter.readChannelParam("1",parmaIds);
//        ParameterTransfer.transTcpParamToView(currentView,initChannelParam);
//        initParamView("TCP");// init param view element.
        bindParamChangeEvent();// init event.
        commitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChannelParameter channelParameter = null;
                if (UdmConstants.CONN_NET_PROTOCOL_TCP.equals(toSetProtocol.getValue())) {
                    channelParameter = ParameterTransfer.getTcpParamFromView(currentView, channelParameter);
                    channelParameter = parameterReaderWriter.writeChannelParam(channelParameter);
                    ParameterTransfer.transTcpParamToView(currentView, channelParameter);
                } else if (UdmConstants.CONN_NET_PROTOCOL_UDP.equals(toSetProtocol.getValue())) {
                    channelParameter = ParameterTransfer.getUdpParamFromView(currentView, channelParameter);
                    channelParameter = parameterReaderWriter.writeChannelParam(channelParameter);
                    ParameterTransfer.transUdpParamToView(currentView, channelParameter);
                }
            }
        });
        return currentView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IParameterReaderWriter) {
            parameterReaderWriter = (IParameterReaderWriter) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        parameterReaderWriter = null;
    }


    private void switchTcpOrUdpView(String seletedProtocl){
        switch(seletedProtocl){
            case UdmConstants.CONN_NET_PROTOCOL_TCP:{

                currentView.findViewById(R.id.label_work_as).setVisibility(View.VISIBLE);
                currentView.findViewById(R.id.udm_conn_tcp_work_mode).setVisibility(View.VISIBLE);

                currentView.findViewById(R.id.label_accepting_income).setVisibility(View.GONE);
                currentView.findViewById(R.id.udm_conn_udp_acception).setVisibility(View.GONE);

                currentView.findViewById(R.id.label_connect_uni_multi).setVisibility(View.GONE);
                currentView.findViewById(R.id.udm_conn_udp_data_mode).setVisibility(View.GONE);

                currentView.findViewById(R.id.udm_conn_tcp_conn_respons).setVisibility(View.VISIBLE);
                currentView.findViewById(R.id.id_tcp_conn_respons).setVisibility(View.VISIBLE);

                currentView.findViewById(R.id.udm_conn_udp_tmp_host_en).setVisibility(View.GONE);
                currentView.findViewById(R.id.id_udp_tmp_host_en).setVisibility(View.GONE);

                break;
            }
            case UdmConstants.CONN_NET_PROTOCOL_UDP:{
                currentView.findViewById(R.id.label_accepting_income).setVisibility(View.VISIBLE);
                currentView.findViewById(R.id.udm_conn_udp_acception).setVisibility(View.VISIBLE);

                currentView.findViewById(R.id.label_connect_uni_multi).setVisibility(View.VISIBLE);
                currentView.findViewById(R.id.udm_conn_udp_data_mode).setVisibility(View.VISIBLE);

                currentView.findViewById(R.id.udm_conn_tcp_work_mode).setVisibility(View.GONE);
                currentView.findViewById(R.id.label_work_as).setVisibility(View.GONE);

                currentView.findViewById(R.id.id_tcp_conn_respons).setVisibility(View.GONE);
                currentView.findViewById(R.id.udm_conn_tcp_conn_respons).setVisibility(View.GONE);

                currentView.findViewById(R.id.id_udp_tmp_host_en).setVisibility(View.VISIBLE);
                currentView.findViewById(R.id.udm_conn_udp_tmp_host_en).setVisibility(View.VISIBLE);
                break;
            }
        }
    }
    private void initParamView(String seletedProtocol){
        switchTcpOrUdpView(seletedProtocol);
    }
    private void bindParamChangeEvent() {
        /**
         * When TCP or UDP switched , parameters must to reload.
         */
        toSetProtocol.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String[] paramIds = null;
                switchTcpOrUdpView(s.toString());
                if (s.toString().equals(UdmConstants.CONN_NET_PROTOCOL_TCP)) {
                    paramIds = ChannelParamsID.getTcpParamsId(toSetChannel.getValue());
                } else if (s.toString().equals(UdmConstants.CONN_NET_PROTOCOL_UDP)) {
                    paramIds = ChannelParamsID.getTcpParamsId(toSetChannel.getValue());
                }
                channelParameter = parameterReaderWriter.readChannelParam(toSetChannel.getValue(), mac,paramIds);
                if (s.toString().equals(UdmConstants.CONN_NET_PROTOCOL_TCP)) {
                    ParameterTransfer.transTcpParamToView(currentView, channelParameter);
                } else if (s.toString().equals(UdmConstants.CONN_NET_PROTOCOL_UDP)) {
                    ParameterTransfer.transUdpParamToView(currentView, channelParameter);
                }
            }
        });
        /**
         * When channel changed , parameters must to reload.
         */
        toSetChannel.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String channelId = s.toString();
                String[] paramIds = null;

                if (toSetProtocol.getValue().equals(UdmConstants.CONN_NET_PROTOCOL_TCP)) {
                    paramIds = ChannelParamsID.getTcpParamsId(channelId);
                } else if (toSetProtocol.getValue().equals(UdmConstants.CONN_NET_PROTOCOL_UDP)) {
                    paramIds = ChannelParamsID.getTcpParamsId(channelId);
                }
                channelParameter = parameterReaderWriter.readChannelParam(channelId,mac, paramIds);
                if (toSetProtocol.getValue().equals(UdmConstants.CONN_NET_PROTOCOL_TCP)) {
                    ParameterTransfer.transTcpParamToView(currentView, channelParameter);
                } else if (toSetProtocol.getValue().equals(UdmConstants.CONN_NET_PROTOCOL_UDP)) {
                    ParameterTransfer.transUdpParamToView(currentView, channelParameter);
                }
            }
        });
        /**
         * When UDP data mode changed , parameters must to change display.
         */
        toSetUdpDataMode.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String channelId = toSetChannel.getValue();
                UdmSpinner udpDataMode = (UdmSpinner) currentView.findViewById(R.id.udm_conn_udp_data_mode);
                if (UdmConstants.UDP_DATA_MODE_UNI.equals(udpDataMode.getValue())) {
                    ParameterTransfer.updateChannelUdpMutilParams(currentView,channelParameter);

                    String value = channelParameter.getParamValueById("CONN" + channelId + "_UDP_UNI_LOCAL_PORT");
                    ((UdmButtonTextEdit) currentView.findViewById(R.id.udm_conn_local_port)).setValue(value);//

                    value = channelParameter.getParamValueById("CONN" + channelId + "_UDP_UNI_HOST_IP0");
                    ((EditText) currentView.findViewById(R.id.udm_conn_host_ip0)).setText(value);//

                    value = channelParameter.getParamValueById("CONN" + channelId + "_UDP_UNI_HOST_PORT0");
                    ((EditText) currentView.findViewById(R.id.udm_conn_host_port0)).setText(value);//


                } else if (UdmConstants.UDP_DATA_MODE_MUL.equals(udpDataMode.getValue())) {
                    ParameterTransfer.updateChannelUdpUniParams(currentView,channelParameter);

                    String value = channelParameter.getParamValueById("CONN" + channelId + "_UDP_MUL_LOCAL_PORT");
                    ((UdmButtonTextEdit) currentView.findViewById(R.id.udm_conn_local_port)).setValue(value);

                    value = channelParameter.getParamValueById("CONN" + channelId + "_UDP_MUL_REMOTE_PORT");
                    ((UdmButtonTextEdit) currentView.findViewById(R.id.udm_conn_host_port0)).setValue(value);

                    value = channelParameter.getParamValueById("CONN" + channelId + "_UDP_MUL_REMOTE_IP");
                    ((UdmButtonTextEdit) currentView.findViewById(R.id.udm_conn_host_ip0)).setValue(value);
                }
            }
        });
    }



}

package com.ibamb.udm.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.ibamb.udm.R;
import com.ibamb.udm.beans.ChannelParameter;
import com.ibamb.udm.beans.DeviceParameter;
import com.ibamb.udm.beans.TCPChannelParameter;
import com.ibamb.udm.beans.UDPChannelParameter;
import com.ibamb.udm.dto.TCPChannelParameterDTO;
import com.ibamb.udm.instruct.IParameterReaderWriter;
import com.ibamb.udm.tag.UdmSpinner;

import java.util.ArrayList;
import java.util.List;


public class ConnectSettingFragment extends Fragment {

    private static final String HOST_IP = "IP";
    private static final String HOST_MAC = "MAC";

    private String ip;
    private String mac;
    private View currentView;
    private UdmSpinner protocolSpinner;//tup/udp
    private Button commitButton;

    private DeviceParameter deviceParameter;
    private IParameterReaderWriter parameterReaderWriter;

    private class ProtoclChangeListener implements Spinner.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String protocol = "TCP";//(String) protocolSpinner.getItemAtPosition(position);
            AdapterView v = null;
            if ("TCP".equals(protocol)) {
                currentView.findViewById(R.id.label_work_as).setVisibility(View.VISIBLE);
                currentView.findViewById(R.id.id_work_role).setVisibility(View.VISIBLE);

                currentView.findViewById(R.id.label_accepting_income).setVisibility(View.GONE);
                currentView.findViewById(R.id.id_accepting_income).setVisibility(View.GONE);

                currentView.findViewById(R.id.label_connect_uni_multi).setVisibility(View.GONE);
                currentView.findViewById(R.id.id_connect_uni_multi).setVisibility(View.GONE);
            } else if ("UDP".equals(protocol)) {
                currentView.findViewById(R.id.label_accepting_income).setVisibility(View.VISIBLE);
                currentView.findViewById(R.id.id_accepting_income).setVisibility(View.VISIBLE);

                currentView.findViewById(R.id.label_connect_uni_multi).setVisibility(View.VISIBLE);
                currentView.findViewById(R.id.id_connect_uni_multi).setVisibility(View.VISIBLE);
                currentView.findViewById(R.id.id_work_role).setVisibility(View.GONE);
                currentView.findViewById(R.id.label_work_as).setVisibility(View.GONE);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

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
     * @param ip Parameter 1.
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
        protocolSpinner =(UdmSpinner) currentView.findViewById(R.id.id_connect_protocol);

        commitButton = currentView.findViewById(R.id.id_conect_setting_commit);
//        String channelId ="1";//界面默认的通道
//        List<String> parmaIds = null;//默认通道的所有参数ID
//        ChannelParameter initChannelParam = parameterReaderWriter.readChannelParam(channelId,parmaIds);
        /**
         * 初始化界面参数值.
         */
        commitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(deviceParameter==null){
                    deviceParameter = new DeviceParameter(ip,mac);
                }
                String connetProtocol = ((UdmSpinner) currentView.findViewById(R.id.id_connect_protocol)).getValue();
                String channelId =((UdmSpinner) (currentView.findViewById(R.id.id_connect_channel))).getValue();
                boolean isTCPPermit = ((CheckBox) currentView.findViewById(R.id.id_tcp_check_box)).isChecked();
                boolean isUDPPermit = ((CheckBox) currentView.findViewById(R.id.id_udp_check_box)).isChecked();
                System.out.println(connetProtocol);
                ChannelParameter parameter = null;//保存从界面读取到的参数

                if ("TCP".equals(connetProtocol)) {
                    /**
                     * 从界面获取TCP某个通道的参数设置
                     */
                    TCPChannelParameterDTO dto = new TCPChannelParameterDTO(currentView, channelId);
                    TCPChannelParameter tcpChannelParameter = dto.getParamFromView();
                    if(deviceParameter.getTcpChannelParamList()==null){
                        deviceParameter.setTcpChannelParamList(new ArrayList<TCPChannelParameter>());
                    }
                    parameter = null;

                } else if ("UDP".equals(connetProtocol)) {
                    /**
                     * 从界面获取UDP某个通道的参数设置
                     */
                    UDPChannelParameter udpChannelParameter = null;
                    parameter = null;
                }

//                parameter = parameterReaderWriter.writeChannelParam(parameter);
                /**
                 * 将返回的参数更新到界面
                 */

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

}

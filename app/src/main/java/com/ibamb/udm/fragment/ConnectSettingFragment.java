package com.ibamb.udm.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.ibamb.udm.R;
import com.ibamb.udm.beans.DeviceParameter;
import com.ibamb.udm.beans.TCPChannelParameter;
import com.ibamb.udm.beans.UDPChannelParameter;
import com.ibamb.udm.dto.TCPChannelParameterDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ConnectSettingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ConnectSettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConnectSettingFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String HOST_IP = "IP";
    private static final String HOST_MAC = "MAC";

    // TODO: Rename and change types of parameters
    private String ip;
    private String mac;
    private View currentView;
    private Spinner protocolSpinner;//tup/udp
    private Button commitButton;

    private DeviceParameter deviceParameter;

    private class ProtoclChangeListener implements Spinner.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String protocol = (String) protocolSpinner.getItemAtPosition(position);
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

    private OnFragmentInteractionListener mListener;

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
    // TODO: Rename and change types and number of parameters
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
        protocolSpinner = currentView.findViewById(R.id.id_connect_protocol);
        protocolSpinner.setOnItemSelectedListener(new ProtoclChangeListener());
        commitButton = currentView.findViewById(R.id.id_conect_setting_commit);
        commitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(deviceParameter==null){
                    deviceParameter = new DeviceParameter(ip,mac);
                }
                String connetProtocol = ((Spinner) v.findViewById(R.id.id_connect_protocol)).getSelectedItem().toString();
                String channelId = ((Spinner) (v.findViewById(R.id.id_connect_channel))).getSelectedItem().toString();
                boolean isTCPPermit = ((CheckBox) v.findViewById(R.id.id_tcp_check_box)).isChecked();
                boolean isUDPPermit = ((CheckBox) v.findViewById(R.id.id_udp_check_box)).isChecked();
                if ("TCP".equals(connetProtocol)) {
                    /**
                     * 从界面获取TCP某个通道的参数设置
                     */
                    TCPChannelParameterDTO dto = new TCPChannelParameterDTO(currentView, channelId);
                    TCPChannelParameter tcpChannelParameter = dto.getParamFromView();
                    if(deviceParameter.getTcpChannelParamList()==null){
                        deviceParameter.setTcpChannelParamList(new ArrayList<TCPChannelParameter>());
                    }
                    /**
                     * 将界面上的TCP参数写入到设备,然后将设备返回的最新UDP参数回显到界面.
                     */
                    tcpChannelParameter = mListener.writeTCPParameterToDevice(tcpChannelParameter);
                } else if ("UDP".equals(connetProtocol)) {
                    /**
                     * 从界面获取UDP某个通道的参数设置
                     */
                    UDPChannelParameter udpChannelParameter = null;
                    /**
                     * 将界面上的UDP参数写入到设备,然后将设备返回的最新UDP参数回显到界面.
                     */
                    udpChannelParameter = mListener.writeUDPParameterToDevice(udpChannelParameter);
                }
            }
        });
        return currentView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {

        TCPChannelParameter readTCPParameterToDevice(String channelId,List<String> paramIds);
        UDPChannelParameter readUDPParameterToDevice(String channelId,List<String> paramIds);
        TCPChannelParameter writeTCPParameterToDevice(TCPChannelParameter tcpParam);
        UDPChannelParameter writeUDPParameterToDevice(UDPChannelParameter tcpParam);
    }
}

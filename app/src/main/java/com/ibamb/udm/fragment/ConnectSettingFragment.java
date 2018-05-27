package com.ibamb.udm.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ibamb.udm.R;
import com.ibamb.udm.module.beans.ChannelParameter;
import com.ibamb.udm.module.beans.ParameterItem;
import com.ibamb.udm.module.constants.UdmConstants;
import com.ibamb.udm.module.core.ParameterMapping;
import com.ibamb.udm.module.instruct.beans.Parameter;
import com.ibamb.udm.tag.UdmSpinner;
import com.ibamb.udm.task.ChannelParamReadAsyncTask;
import com.ibamb.udm.task.ChannelParamWriteAsynTask;
import com.ibamb.udm.util.ViewElementDataUtil;

import java.util.ArrayList;
import java.util.List;


public class ConnectSettingFragment extends Fragment {

    private static final String HOST_IP = "IP";
    private static final String HOST_MAC = "MAC";

    private View currentView;

    private String ip;
    private String mac;

    private UdmSpinner toSetProtocol;//tup/udp
    private UdmSpinner toSetChannel;
    private UdmSpinner toSetUdpDataMode;
    private Button commitButton;

    // UDP 相关的参数
    private String[] udpParams = {"conn_udp_data_mode", "conn_udp_tmp_host_en",
            "conn_udp_uni_host_ip0", "conn_udp_mul_remote_ip", "conn_udp_acception",
            "conn_udp_uni_local_port", "conn_udp_uni_host_port0", "conn_udp_mul_remote_port", "conn_udp_mul_local_port",
            "lab_conn_udp_acception", "lab_conn_udp_data_mode", "lab_conn_udp_tmp_host_en", "lab_conn_udp_uni_local_port",
            "lab_conn_udp_uni_host_port0", "lab_conn_udp_uni_host_ip0", "lab_conn_udp_mul_local_port",
            "lab_conn_udp_mul_remote_port", "lab_conn_udp_mul_remote_ip"};

    // TCP 相关的参数
    private String[] tcpParams = {"conn_tcp_conn_respons", "conn_tcp_host_ip0", "conn_tcp_work_mode",
            "conn_tcp_host_port0", "conn_tcp_local_port",
            "lab_conn_tcp_conn_respons", "lab_conn_tcp_host_ip0", "lab_conn_tcp_work_mode",
            "lab_conn_tcp_host_port0", "lab_conn_tcp_local_port"};

    //UDP 单播参数
    private String[] udpUniParams = {"conn_udp_uni_host_ip0", "conn_udp_uni_local_port", "conn_udp_uni_host_port0",
            "lab_conn_udp_uni_local_port", "lab_conn_udp_uni_host_port0", "lab_conn_udp_uni_host_ip0"};

    //UDP 多播参数
    private String[] udpMulParams = {"conn_udp_mul_remote_ip","conn_udp_mul_remote_port","conn_udp_mul_local_port",
            "lab_conn_udp_mul_local_port","lab_conn_udp_mul_remote_port", "lab_conn_udp_mul_remote_ip"};

    //用于保存当前通道的参数值，随着界面通道改变，该值也会发生改变。
    private ChannelParameter channelParameter;




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
    public void onStart() {
        super.onStart();
        //在界面初始化完毕后读取默认通道的参数，并且刷新界面数据。
        channelParameter = new ChannelParameter(mac, UdmConstants.UDM_DEFAULT_CHNL);
        List<Parameter> parameters = ParameterMapping.getChannelParamDef(Integer.parseInt(UdmConstants.UDM_DEFAULT_CHNL));
        List<ParameterItem> items = new ArrayList<>();
        for (Parameter parameter : parameters) {
            items.add(new ParameterItem(parameter.getId(), null));
        }
        channelParameter.setParamItems(items);
        ChannelParamReadAsyncTask task = new ChannelParamReadAsyncTask(currentView,channelParameter);
        task.execute(mac, UdmConstants.UDM_DEFAULT_CHNL);
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
        currentView = inflater.inflate(R.layout.fragment_connect_setting, container, false);

        toSetProtocol = (UdmSpinner) currentView.findViewById(R.id.udm_conn_net_protocol_set);
        toSetChannel = (UdmSpinner) currentView.findViewById(R.id.udm_connect_channel_set);
        toSetUdpDataMode = (UdmSpinner) currentView.findViewById(R.id.udm_conn_udp_data_mode);
        commitButton = currentView.findViewById(R.id.id_conect_setting_commit);
        /**
         * 初始化时默认显示TCP连接相关的参数。
         */
        for (String tagId : udpParams) {
            currentView.findViewWithTag(tagId).setVisibility(View.GONE);// UDP 相关的参数隐藏
        }
        toSetProtocol.setValue("TCP");
        toSetChannel.setValue("1");
        //界面参数事件绑定
        bindParamChangeEvent();
        commitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String channelId = toSetChannel.getValue();
                ChannelParameter changedParam = ViewElementDataUtil.getChangedData(currentView,channelParameter,channelId);
                ChannelParamWriteAsynTask task = new ChannelParamWriteAsynTask(currentView);
                task.execute(channelParameter,changedParam);
            }
        });
        return currentView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof IParameterReaderWriter) {
//
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }




    private void bindParamChangeEvent() {
//        /**
//         * 界面切换TCP或者UDP的时候，需要重新读取参数值。
//         */
//        toSetProtocol.getEditText().addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                String protocol = s.toString();//当前选择的协议类型
//                toSetChannel.setValue(UdmConstants.UDM_DEFAULT_CHNL);
//                /**
//                 * 如果选择的是TDP协议，则读取TCP连接参数，并隐藏UDP连接参数控件。
//                 */
//                if (UdmConstants.CONN_NET_PROTOCOL_TCP.equals(protocol)) {
//                    for (String tagId : udpParams) {
//                        currentView.findViewWithTag(tagId).setVisibility(View.GONE);// UDP 相关的参数隐藏
//                    }
//                    for (String tagId : tcpParams) {
//                        currentView.findViewWithTag(tagId).setVisibility(View.VISIBLE);
//                    }
//                } else if (UdmConstants.CONN_NET_PROTOCOL_UDP.equals(protocol)) {
//                    for (String tagId : tcpParams) {
//                        currentView.findViewWithTag(tagId).setVisibility(View.GONE);//TCP 相关的参数隐藏
//                    }
//                    for (String tagId : udpParams) {
//                        currentView.findViewWithTag(tagId).setVisibility(View.VISIBLE);
//                    }
//                }
//            }
//        });
//        /**
//         * 如果通道发生改变，则读取通道的参数值。
//         */
//        toSetChannel.getEditText().addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//            @Override
//            public void afterTextChanged(Editable s) {
//                String channelId = s.toString();
//                ChannelParamReadAsyncTask task = new ChannelParamReadAsyncTask(currentView,channelParameter);
//                task.execute(mac, channelId);
//            }
//        });
    }


}

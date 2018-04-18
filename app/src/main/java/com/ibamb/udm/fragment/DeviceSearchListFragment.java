package com.ibamb.udm.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ibamb.udm.R;
import com.ibamb.udm.activity.LoginActivity;
import com.ibamb.udm.activity.MainActivity;
import com.ibamb.udm.beans.DeviceInfo;
import com.ibamb.udm.listener.UdmSearchButtonClickListener;

import java.util.ArrayList;

public class DeviceSearchListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private String context;

    private ListView mListView;
    private FloatingActionButton searchButton;

    /**
     * 设备列表中点击事件，触发登录远程设备。
     */
    private AdapterView.OnItemClickListener itemOnclickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent((MainActivity) getActivity(), LoginActivity.class);
            String ip = ((TextView) view.findViewById(R.id.device_mac)).getText().toString();
            String mac = ((TextView) view.findViewById(R.id.device_mac)).getText().toString();
            Bundle params = new Bundle();
            params.putString("HOST_ADDRESS", ip);
            params.putString("HOST_MAC", mac);
            intent.putExtras(params);
            startActivityForResult(intent, 1);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle = data.getExtras();
        String namString = bundle.getString("name");
//        mtTextView.setText(namString);
    }

    private OnFragmentInteractionListener mListener;

    public DeviceSearchListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DeviceSearchListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DeviceSearchListFragment newInstance(String param1, String param2) {
        DeviceSearchListFragment fragment = new DeviceSearchListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_device_search_list, container, false);
        //取得界面浮动搜索按钮和列表控件
        searchButton = (FloatingActionButton) view.findViewById(R.id.udm_search_button);
        mListView = (ListView) view.findViewById(R.id.search_device_list);
        //浮动按钮添加搜索事件，通过搜索事件触发搜索设备，并异步更新列表控件。
        UdmSearchButtonClickListener searchClickListener = new UdmSearchButtonClickListener(searchButton,mListView,inflater);
        searchButton.setOnClickListener(searchClickListener);
        //给列表项添加点击事件，触发登录设备。
        mListView.setOnItemClickListener(itemOnclickListener);
        return view;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        inetAddresses = ((MainActivity)getActivity()).getInetAddresses();
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}

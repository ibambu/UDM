package com.ibamb.udm.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.ibamb.udm.R;
import com.ibamb.udm.activity.DeviceProfileActivity;
import com.ibamb.udm.activity.MainActivity;
import com.ibamb.udm.log.UdmLog;
import com.ibamb.udm.module.constants.Constants;
import com.ibamb.udm.module.core.TryUser;
import com.ibamb.udm.task.UserLoginAsyncTask;

import java.util.concurrent.ExecutionException;

public class DeviceSearchListFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;


    private String context;

    private ListView mListView;
//    private FloatingActionButton searchButton;
    private View loginView;
    private TextView noticeView;
    private String selectedMac;
    private String ip;
    private TextView vSearchNotice;

    /**
     * 设备列表中点击事件，触发登录远程设备。
     */
    private AdapterView.OnItemClickListener itemOnclickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            TextView macView =  view.findViewById(R.id.device_mac);
            //绑定登录设备事件。
            selectedMac = macView.getText().toString();
            ip= ((TextView) view.findViewById(R.id.device_ip)).getText().toString();
            /**
             * 尝试登陆
             */
            try{
                boolean trySuccess = false;
                for(int i=0;i<TryUser.getUserCount();i++){
                    UserLoginAsyncTask loginAsyncTask = new UserLoginAsyncTask();
                    String [] tryUser = TryUser.getUser(i+1);
                    if(tryUser==null){
                        continue;
                    }
                    String[] loginInfo = {tryUser[0], tryUser[1], selectedMac,ip};
                    loginAsyncTask.execute(loginInfo);
                    trySuccess = loginAsyncTask.get();
                    if(trySuccess){
                        break;
                    }
                }
                if(trySuccess){
                    Intent intent = new Intent((MainActivity) getActivity(), DeviceProfileActivity.class);
                    Bundle params = new Bundle();
                    params.putString("HOST_ADDRESS", ip);
                    params.putString("HOST_MAC", selectedMac);
                    intent.putExtras(params);
                    startActivityForResult(intent, 1);
                }else{
                    final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    loginView = LayoutInflater.from(view.getContext()).inflate(R.layout.alter_login_layout, null);
                    builder.setView(loginView);
                    final AlertDialog dialog = builder.show();
                    Button sigInInButton = loginView.findViewById(R.id.alter_sign_in_button);
                    noticeView = loginView.findViewById(R.id.notice_info);//显示登录结果
                    sigInInButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AutoCompleteTextView userNameView = loginView.findViewById(R.id.dev_user_name);
                            EditText passwordView = loginView.findViewById(R.id.dev_password);

                            String userName = userNameView.getText().toString();
                            String password = passwordView.getText().toString();

                            UserLoginAsyncTask loginAsyncTask = new UserLoginAsyncTask();
                            String[] loginInfo = {userName, password, selectedMac,ip};
                            loginAsyncTask.execute(loginInfo);

                            try {
                                Thread.sleep(800);
                                boolean isSuccess = loginAsyncTask.get();
                                if (true) {
                                    String[] tryUser = {userName,password};
                                    TryUser.setTryUser(tryUser);
                                    dialog.dismiss();
                                    Intent intent = new Intent((MainActivity) getActivity(), DeviceProfileActivity.class);
                                    Bundle params = new Bundle();
                                    params.putString("HOST_ADDRESS", ip);
                                    params.putString("HOST_MAC", selectedMac);
                                    intent.putExtras(params);
                                    startActivityForResult(intent, 1);
                                } else {
                                    loginAsyncTask.cancel(true);
                                    noticeView.setVisibility(View.VISIBLE);
                                    noticeView.setText(Constants.INFO_LOGIN_FAIL);
                                }
                            } catch (InterruptedException e) {
                                UdmLog.e(this.getClass().getName(),e.getMessage());
                            } catch (ExecutionException e) {
                                UdmLog.e(this.getClass().getName(),e.getMessage());
                            }
                        }
                    });
                }

            }catch (Exception e){
                e.printStackTrace();
                UdmLog.e(this.getClass().getName(),e.getMessage());
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    public DeviceSearchListFragment() {

    }

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
//        searchButton = (FloatingActionButton) view.findViewById(R.id.udm_search_button);
        mListView = (ListView) view.findViewById(R.id.search_device_list);
        vSearchNotice = view.findViewById(R.id.search_notice_info);

        //浮动按钮添加搜索事件，通过搜索事件触发搜索设备，并异步更新列表控件。
//        UdmSearchButtonClickListener searchClickListener = new UdmSearchButtonClickListener(mListView, vSearchNotice,inflater);
//        searchButton.setOnClickListener(searchClickListener);
        //给列表项添加点击事件，触发登录设备。
        mListView.setOnItemClickListener(itemOnclickListener);
        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}

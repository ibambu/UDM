package com.ibamb.udm.module.task;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.ibamb.udm.component.ServiceConst;
import com.ibamb.udm.module.beans.ChannelParameter;
import com.ibamb.udm.module.beans.ParameterItem;
import com.ibamb.udm.module.constants.Constants;
import com.ibamb.udm.module.constants.Control;
import com.ibamb.udm.module.core.TryUser;
import com.ibamb.udm.module.instruct.IParamWriter;
import com.ibamb.udm.module.instruct.ParamWriter;
import com.ibamb.udm.module.security.UserAuth;
import com.ibamb.udm.module.sys.SysManager;

import java.util.List;
import java.util.concurrent.Callable;

public class SyncDeviceParamTask implements Callable {
    private List<String> failList;
    private List<String> successList;
    private ChannelParameter srcChannelParameters;//源设备参数,包含各个通道参数在内。
    private ChannelParameter distChannelParameters;//目标设备参数，包含各个通道参数在内。
    private LocalBroadcastManager broadcastManager;//广播者
    private Object broadcastLock;//广播锁，广播时加锁。
    private int totalDeviceCount;
    private String syncTime;

    public void setFailList(List<String> failList) {
        this.failList = failList;
    }

    public void setSuccessList(List<String> successList) {
        this.successList = successList;
    }

    public void setSyncTime(String syncTime) {
        this.syncTime = syncTime;
    }


    public void setSrcChannelParameters(ChannelParameter srcChannelParameters) {
        this.srcChannelParameters = srcChannelParameters;
    }

    public void setDistChannelParameters(ChannelParameter distChannelParameters) {
        this.distChannelParameters = distChannelParameters;
    }

    public void setBroadcastManager(LocalBroadcastManager broadcastManager) {
        this.broadcastManager = broadcastManager;
    }

    public void setBroadcastLock(Object broadcastLock) {
        this.broadcastLock = broadcastLock;
    }

    public void setTotalDeviceCount(int totalDeviceCount) {
        this.totalDeviceCount = totalDeviceCount;
    }

    @Override
    public Object call() throws Exception {
        String syncReport = "";
        String mac = distChannelParameters.getMac();
        String ip = distChannelParameters.getIp();
        try {
            boolean isAuthSuccess = false;
            for (int i = 0; i < TryUser.getUserCount(); i++) {
                String[] userInfo = TryUser.getUser(i + 1);
                isAuthSuccess = UserAuth.login(userInfo[0], userInfo[1], mac, ip);
                if (isAuthSuccess) {
                    break;
                }
            }
            if (isAuthSuccess) {
                IParamWriter writer = new ParamWriter();
                copyParamValue(srcChannelParameters.getParamItems(), distChannelParameters.getParamItems());
                writer.writeChannelParam(distChannelParameters);
                int resultCode = distChannelParameters.getResultCode();
                String resultInfo ="";
                switch (resultCode){
                    case Control.ACKNOWLEDGE:
                        resultInfo = Constants.SYNC_SUCCESS;
                        break;
                    case Control.NO_PERMISSION:
                        resultInfo = Constants.SYNC_NO_PERMISSION;
                        break;
                    case Control.OPTION_NOT_SUPPORT:
                        resultInfo =  Constants.SYNC_NO_PERMISSION;
                        break;
                    case Control.NO_DATA_REPLY:
                        resultInfo = Constants.SYNC_NO_DATA_REPLY;
                        break;
                    case Control.VALUE_INVALID:
                        resultInfo =  Constants.SYNC_VALUE_INVALID;
                        break;
                        default:
                            resultInfo= Constants.SYNC_UNKNOWN_ERROR;
                }
                if (resultCode== Control.ACKNOWLEDGE) {
                    boolean isSuccess = SysManager.saveAndReboot(mac);//同步成功后重启设备。
                    if(!isSuccess){
                        //如果重启失败尝试3次
                        for(int i=0;i<3;i++){
                            isSuccess = SysManager.saveAndReboot(mac);
                            if(isSuccess){
                                break;
                            }
                        }
                    }
                    if(!isSuccess){
                        resultInfo= Constants.SYNC_SAVE_REBOOT_FAIL;
                    }
                }
                syncReport = ip + "#" + mac + "#" + resultInfo;
            } else {
                distChannelParameters.setResultCode(Control.AUTH_FAIL);
                syncReport = ip + "#" + mac + "#" + Constants.SYNC_AUTH_FAIL;
            }
        } catch (Exception e) {
            syncReport = ip + "#" + mac + "#" + Constants.SYNC_UNKNOWN_ERROR;
            throw e;
        } finally {
            synchronized (broadcastLock) {
                if(distChannelParameters.isSuccessful()){
                    successList.add(mac);
                }else{
                    failList.add(mac);
                }
                sendBroadcast(syncReport);
            }
        }
        return syncReport;
    }

    /**
     * 发送广播
     *
     * @param report
     */
    private void sendBroadcast(String report) {
        Intent intent = new Intent(ServiceConst.DEVICE_SYNCH_SERVICE);
        intent.putExtra("SYNCH_SUCCESS_COUNT", successList.size());
        intent.putExtra("SYNCH_FAIL_COUNT", failList.size());
        intent.putExtra("TARGET_DEVICE_NUMBER", totalDeviceCount);
        intent.putExtra("SYNCH_REPORT", report);
        intent.putExtra("SYNCH_TIME", syncTime);
        broadcastManager.sendBroadcast(intent);
    }

    /**
     * 复制参数值
     *
     * @param srcParamList
     * @param distParamList
     */
    private void copyParamValue(List<ParameterItem> srcParamList, List<ParameterItem> distParamList) {
        for (ParameterItem srcParamItem : srcParamList) {
            for (ParameterItem distParamItem : distParamList) {
                if (srcParamItem.getParamId().equals(distParamItem.getParamId())) {
                    distParamItem.setParamValue(srcParamItem.getParamValue());
                    break;
                }
            }
        }
    }
}

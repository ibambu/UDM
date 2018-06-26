package com.ibamb.udm.service;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import com.ibamb.udm.component.ServiceConst;
import com.ibamb.udm.log.UdmLog;
import com.ibamb.udm.module.beans.ChannelParameter;
import com.ibamb.udm.module.beans.DeviceInfo;
import com.ibamb.udm.module.beans.ParameterItem;
import com.ibamb.udm.module.constants.Constants;
import com.ibamb.udm.module.core.ParameterMapping;
import com.ibamb.udm.module.core.TryUser;
import com.ibamb.udm.module.instruct.IParamReader;
import com.ibamb.udm.module.instruct.IParamWriter;
import com.ibamb.udm.module.instruct.ParamReader;
import com.ibamb.udm.module.instruct.ParamWriter;
import com.ibamb.udm.module.instruct.beans.Parameter;
import com.ibamb.udm.module.security.UserAuth;
import com.ibamb.udm.module.sync.DeviceSyncResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DeviceSynchronizeService extends Service {

    private static ArrayList<DeviceInfo> targetDeviceList;

    private static final Object readLock = new Object();//读参数锁
    private static final Object writeLock = new Object();//写参数锁
    private static final Object authLock = new Object();//登录锁

    private boolean isLoginSuccess;

    private int readChannelCount;
    private int syncSuccessCount;
    private int syncFailCount;

    private List<ChannelParameter> srcChannelParameters;
    private Map<String, List<ChannelParameter>> distDevicePamaMap;

    private Map<String, DeviceSyncResult> syncResultMap;

    @Override
    public IBinder onBind(Intent intent) {
        return new DeviceSynchBinder();
    }

    /**
     * Binder 返回一个Service实例
     */
    public class DeviceSynchBinder extends Binder {
        public DeviceSynchronizeService getService() {
            return DeviceSynchronizeService.this;
        }
    }

    /**
     * 获取设备所有参数
     *
     * @param deviceInfo
     * @return
     */
    public List<ChannelParameter> getDeviceParams(DeviceInfo deviceInfo) {
        List<ChannelParameter> channelParameters = new ArrayList<>();
        for (int i = 0; i < Constants.MAX_CHANNEL; i++) {
            List<Parameter> parameterList = ParameterMapping.getChannelPublicParam(i);
            if (parameterList != null && !parameterList.isEmpty()) {
                ChannelParameter channelParameter = new ChannelParameter(deviceInfo.getMac(), deviceInfo.getIp(), String.valueOf(i));
                List<ParameterItem> parameterItems = new ArrayList<>();
                for (Parameter parameter : parameterList) {
                    parameterItems.add(new ParameterItem(parameter.getId(), null));
                }
                channelParameter.setParamItems(parameterItems);
                //读取参数值
                channelParameters.add(channelParameter);
            }
        }
        return channelParameters;
    }

    public void syncDeviceParam(final DeviceInfo templateDevice, String[] toSynchDeviceInfo) {
        if (toSynchDeviceInfo != null) {
            targetDeviceList = new ArrayList<>();
            distDevicePamaMap = new HashMap<>();
            for (String deviceInfo : toSynchDeviceInfo) {
                String[] deviceInfoArray = deviceInfo.split("#");
                if (deviceInfoArray.length > 1) {
                    DeviceInfo device = new DeviceInfo(deviceInfoArray[0], deviceInfoArray[1]);
                    targetDeviceList.add(device);
                    List<ChannelParameter> channelParameters = getDeviceParams(device);
                    distDevicePamaMap.put(device.getMac(), channelParameters);
                }
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        /**
                         * 读取参数定义，获得需要同步的参数。
                         */
                        srcChannelParameters = getDeviceParams(templateDevice);
                        for (ChannelParameter channelParameter : srcChannelParameters) {
                            ChannelParamReadTask task = new ChannelParamReadTask();
                            task.execute(channelParameter);
                        }
                        /**
                         * 模板设备所有共性参数读取完后才开始同步
                         */
                        Long startTime = System.currentTimeMillis();
                        boolean isReadCompleted;//模板设备参数是否已经全部读取完毕
                        while (true) {
                            long nowTime = System.currentTimeMillis();
                            synchronized (readLock) {
                                isReadCompleted = readChannelCount == srcChannelParameters.size();
                            }
                            if (isReadCompleted || nowTime - startTime > 5000) {
                                break;
                            }
                        }
                        System.out.println("is read completed.>>>>>>"+isReadCompleted);
                        if (isReadCompleted) {
                            /**
                             * 先登录目标设备,登录成功后再更新参数值
                             */
                            syncResultMap = new HashMap<>();
                            for (Iterator<String> it = distDevicePamaMap.keySet().iterator(); it.hasNext(); ) {
                                String mac = it.next();
                                List<ChannelParameter> distParamItems = distDevicePamaMap.get(mac);
                                boolean isAuthSuccess = false;
                                if (distParamItems != null) {
                                    for (int i = 0; i < TryUser.getUserCount(); i++) {
                                        String[] userInfo = TryUser.getUser(i + 1);
                                        LoginDeviceTask loginAsyncTask = new LoginDeviceTask();
                                        String[] loginInfo = {userInfo[0], userInfo[1], distParamItems.get(0).getMac(), distParamItems.get(0).getIp()};
                                        long sTime = System.currentTimeMillis();
                                        loginAsyncTask.execute(loginInfo);
                                        while (true) {
                                            long nowTime = System.currentTimeMillis();
                                            synchronized (authLock) {
                                                if (isLoginSuccess) {
                                                    isAuthSuccess = true;
                                                    isLoginSuccess = false;//设置下个设备登录状态为false。
                                                }
                                            }
                                            if (isAuthSuccess) {
                                                break;
                                            }
                                            if(nowTime - sTime > 5000){
                                                loginAsyncTask.cancel(true);
                                                System.out.println("login timeout, cancel it..");
                                                break;
                                            }
                                        }
                                        if (isAuthSuccess) {
                                            break;
                                        }
                                    }
                                }
                                System.out.println("is AuthSuccess.>>>>>>"+isAuthSuccess);
                                if (isAuthSuccess) {
                                    for (ChannelParameter channelParameter : distParamItems) {
                                        ChannelParamWriteTask task = new ChannelParamWriteTask();
                                        task.execute(channelParameter);
                                        task.getStatus();
                                    }
                                    break;
                                }else {

                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {

                    }
                }
            }).start();
        }
    }

    class LoginDeviceTask extends AsyncTask<String, Boolean, Boolean> {
        @Override
        protected Boolean doInBackground(String... strings) {
            boolean isSuccess = false;
            try {
                String userName = strings[0];
                String password = strings[1];
                String mac = strings[2];
                String ip = strings[3];
                isSuccess = UserAuth.login(userName, password, mac, ip);
            } catch (Exception e) {
                UdmLog.e(this.getClass().getName(), e.getMessage());
            }
            synchronized (authLock) {
                isLoginSuccess = isSuccess;
            }
            return isSuccess;
        }
    }

    /**
     * 读取设备参数
     */
    class ChannelParamReadTask extends AsyncTask<ChannelParameter, String, Void> {
        @Override
        protected Void doInBackground(ChannelParameter... channelParameters) {
            IParamReader reader = new ParamReader();
            if (channelParameters != null && channelParameters.length > 0) {
                reader.readChannelParam(channelParameters[0]);
                synchronized (readLock) {
                    if (channelParameters[0].isSuccessful()) {
                        readChannelCount++;
                    }
                }
            }
            return null;
        }
    }

    /**
     * 更新设备参数
     */
    class ChannelParamWriteTask extends AsyncTask<ChannelParameter, String, Boolean> {

        @Override
        protected Boolean doInBackground(ChannelParameter... channelParameters) {
            boolean isok = true;
            if (channelParameters != null && channelParameters.length > 0) {
                String mac = channelParameters[0].getMac();
                String channelId = channelParameters[0].getChannelId();
                for (ChannelParameter srcChannelParameter : srcChannelParameters) {
                    if (srcChannelParameter.getChannelId().equals(channelId)) {
                        List<ParameterItem> srcParamItems = srcChannelParameter.getParamItems();
                        List<ParameterItem> distParamItems = channelParameters[0].getParamItems();
                        copyParamValue(srcParamItems, distParamItems);
                        IParamWriter writer = new ParamWriter();
                        writer.writeChannelParam(channelParameters[0]);
                        isok &= channelParameters[0].isSuccessful();
                        synchronized (writeLock) {
                            DeviceSyncResult result = syncResultMap.get(mac);
                            if (result == null) {
                                List<ChannelParameter> chnlList = distDevicePamaMap.get(mac);
                                result = new DeviceSyncResult();
                                if (chnlList != null) {
                                    result.setChnlTotal(chnlList.size());
                                }
                                syncResultMap.put(mac, result);
                            }
                            if (channelParameters[0].isSuccessful()) {
                                result.setChnlSuccessCount((result.getChnlSuccessCount() + 1));
                            } else {
                                result.setChnlFailCount((result.getChnlFailCount() + 1));
                            }
                            //升级完毕，发送广播。
                            if (result.isCompleted()) {
                                if (result.isSyncSuccessful()) {
                                    syncSuccessCount++;
                                } else {
                                    syncFailCount++;
                                }
                                LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
                                Intent intent = new Intent(ServiceConst.DEVICE_SYNCH_SERVICE);
                                intent.putExtra("SYNCH_COUNT", syncSuccessCount);
                                System.out.println("SYNCH_COUNT======="+syncSuccessCount+" "+mac+" "+channelId);
                                intent.putExtra("TARGET_DEVICE_NUMBER", targetDeviceList.size());
                                intent.putExtra("SYNCH_FAIL_COUNT", syncFailCount);
                                broadcastManager.sendBroadcast(intent);
                            }
                        }
                        break;
                    }
                }
            }
            return isok;
        }
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

package com.ibamb.udm.service;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import com.ibamb.dnet.module.api.UdmClient;
import com.ibamb.dnet.module.beans.DeviceParameter;
import com.ibamb.dnet.module.beans.DeviceSyncMessage;
import com.ibamb.dnet.module.beans.ParameterItem;
import com.ibamb.dnet.module.core.ParameterMapping;
import com.ibamb.dnet.module.instruct.beans.Parameter;
import com.ibamb.udm.component.constants.UdmConstant;
import com.ibamb.udm.task.DetectSupportChannelsAsyncTask;
import com.ibamb.udm.task.SyncDeviceParamTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 将一个设备的参数值复制到其他设备。
 */
public class DeviceSynchronizeService extends Service {

    private static final Object broadcastLock = new Object();//同步线程发送广播时用到的锁
    private static final List<String> successList = new ArrayList<>();
    private static final List<String> failList = new ArrayList<>();
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
     * 设备参数同步入口
     *
     * @param templateDevice
     * @param toSynchDeviceInfo
     */
    public void syncDeviceParam(DeviceSyncMessage templateDevice, String[] toSynchDeviceInfo) throws Exception {
        String syncTime = timeFormat.format(new Date());
        if (toSynchDeviceInfo != null) {
            successList.clear();
            failList.clear();
            List<String> confChannels = ParameterMapping.getInstance().getSupportedChannels();//配置文件设置的通道，但是设备不一定全部支持。
            List<String> supportChannels = new ArrayList<>();//设备支持的通道
            /**
             * 通过读取设备通道的一个参数（例如通道1的参数：CONN1_NET_PROTOCOL），如果该参数无参数值，则认为设备是不支持的通道。
             */
            DeviceParameter testDeviceParameter = new DeviceParameter(templateDevice.getMac(), templateDevice.getIp(), "-1");
            testDeviceParameter.setParamItems(new ArrayList<ParameterItem>());
            for (String channelId : confChannels) {
                if (channelId.equals("0")) {
                    supportChannels.add(channelId);
                    continue;//0 通道是虚拟通道,此处为了方便读取参数，直接纳为支持通道。
                }
                ParameterItem item = new ParameterItem("CONN" + channelId + "_NET_PROTOCOL", null);
                testDeviceParameter.getParamItems().add(item);
            }
            DetectSupportChannelsAsyncTask detectSupportTask = new DetectSupportChannelsAsyncTask(testDeviceParameter);
            detectSupportTask.execute().get();
            int channelCount = 1;
            for(ParameterItem parameterItem: testDeviceParameter.getParamItems()){
                if(parameterItem.getParamId().equals("CONN" + channelCount + "_NET_PROTOCOL")
                        &&  parameterItem.getParamValue()!=null &&  parameterItem.getParamValue().trim().length()>0){
                    supportChannels.add(String.valueOf(channelCount));
                }
                channelCount ++ ;
            }

            /**
             * 读取设备支持的所有通道定义的参数
             */
            List<DeviceParameter> srcDeviceParameters = getDeviceParams(templateDevice, supportChannels);
            /**
             * 读取源设备的参数值，用于目标设备复制参数值。将所有参数存放在一个虚拟通道里面，便于一次读取。
             */
            DeviceParameter srcAllChannelParams = new DeviceParameter(templateDevice.getMac(),templateDevice.getIp(),"-1");
            srcAllChannelParams.setParamItems(new ArrayList<ParameterItem>());
            for (DeviceParameter deviceParameter : srcDeviceParameters) {
                srcAllChannelParams.getParamItems().addAll(deviceParameter.getParamItems());
            }
            ChannelParamReadTask srcParamReadTask = new ChannelParamReadTask();
            srcParamReadTask.execute(srcAllChannelParams).get();
            /**
             * 根据传入的目标设备信息，生成目标设备对象以及目标设备定义的通道参数。
             */
            ArrayList<DeviceSyncMessage> targetDeviceList = new ArrayList<>();//目标设备
            Map<String, DeviceParameter> distDevicePamaMap = new HashMap<>();//目标设备参数
            for (String deviceInfo : toSynchDeviceInfo) {
                String[] deviceInfoArray = deviceInfo.split("#");
                if (deviceInfoArray.length > 1) {
                    DeviceSyncMessage device = new DeviceSyncMessage(deviceInfoArray[0], deviceInfoArray[1]);
                    targetDeviceList.add(device);
                    List<DeviceParameter> deviceParameters = getDeviceParams(device, supportChannels);
                    DeviceParameter distAllChannelParams = new DeviceParameter(device.getMac(),device.getIp(),"-1");
                    distAllChannelParams.setParamItems(new ArrayList<ParameterItem>());
                    for(DeviceParameter deviceParameter : deviceParameters){
                        distAllChannelParams.getParamItems().addAll(deviceParameter.getParamItems());
                    }
                    distDevicePamaMap.put(device.getMac(), distAllChannelParams);
                }
            }
            /**
             * 创建线程池，并发同步目标设备参数。最大并发数为 5.
             */
            ExecutorService threadPool = Executors.newFixedThreadPool(5);
            for (Iterator<String> it = distDevicePamaMap.keySet().iterator(); it.hasNext(); ) {
                String mac = it.next();
                DeviceParameter distChannelParamItems = distDevicePamaMap.get(mac);

                SyncDeviceParamTask task = new SyncDeviceParamTask();
                task.setSrcDeviceParameters(srcAllChannelParams);
                task.setDistDeviceParameters(distChannelParamItems);
                task.setTotalDeviceCount(targetDeviceList.size());
                LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
                task.setBroadcastManager(broadcastManager);
                task.setBroadcastLock(broadcastLock);
                task.setSuccessList(successList);
                task.setFailList(failList);
                task.setSyncTime(syncTime);
                threadPool.submit(task);//提交任务在单独的线程执行参数同步。
            }
            threadPool.shutdown();//线程池停止接受任务。
        }
    }

    /**
     * 获取设备所有参数
     *
     * @param deviceInfo
     * @return
     */
    public List<DeviceParameter> getDeviceParams(DeviceSyncMessage deviceInfo, List<String> supportChannels) {
        List<DeviceParameter> deviceParameters = new ArrayList<>();
        for (int i = 0; i < UdmConstant.MAX_CHANNEL; i++) {
            List<Parameter> parameterList = ParameterMapping.getInstance().getChannelPublicParam(i);
            if (!supportChannels.contains(String.valueOf(i))) {
                continue;
            }
            if (parameterList != null && !parameterList.isEmpty()) {
                DeviceParameter deviceParameter = new DeviceParameter(deviceInfo.getMac(), deviceInfo.getIp(), String.valueOf(i));
                List<ParameterItem> parameterItems = new ArrayList<>();
                for (Parameter parameter : parameterList) {
                    parameterItems.add(new ParameterItem(parameter.getId(), null));
                }
                deviceParameter.setParamItems(parameterItems);
                //读取参数值
                deviceParameters.add(deviceParameter);
            }
        }
        return deviceParameters;
    }

    /**
     * 读取设备参数
     */
    class ChannelParamReadTask extends AsyncTask<DeviceParameter, String, DeviceParameter> {
        @Override
        protected DeviceParameter doInBackground(DeviceParameter... deviceParameters) {
            if (deviceParameters != null && deviceParameters.length > 0) {
                UdmClient.getInstance().readDeviceParameter(deviceParameters[0]);
            }
            return deviceParameters[0];
        }
    }
}

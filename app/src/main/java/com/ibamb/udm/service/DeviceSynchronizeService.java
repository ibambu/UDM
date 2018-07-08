package com.ibamb.udm.service;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import com.ibamb.udm.module.beans.ChannelParameter;
import com.ibamb.udm.module.beans.DeviceSyncMessage;
import com.ibamb.udm.module.beans.ParameterItem;
import com.ibamb.udm.module.constants.Constants;
import com.ibamb.udm.module.core.ParameterMapping;
import com.ibamb.udm.module.instruct.IParamReader;
import com.ibamb.udm.module.instruct.ParamReader;
import com.ibamb.udm.module.instruct.beans.Parameter;
import com.ibamb.udm.module.task.SyncDeviceParamTask;
import com.ibamb.udm.task.DetectSupportChannelsAsyncTask;

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
    private static final List<String> completedList = new ArrayList<>();
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
            completedList.clear();
            List<String> confChannels = ParameterMapping.getSupportedChannels();//配置文件设置的通道，但是设备不一定全部支持。
            List<String> supportChannels = new ArrayList<>();//设备支持的通道
            /**
             * 通过读取设备通道的一个参数（例如通道1的参数：CONN1_NET_PROTOCOL），如果该参数无参数值，则认为设备是不支持的通道。
             */
            for (String channelId : confChannels) {
                if (channelId.equals("0")) {
                    supportChannels.add(channelId);
                    continue;//0 通道是虚拟通道,此处为了方便读取参数，直接纳为支持通道。
                }
                ParameterItem item = new ParameterItem("CONN" + channelId + "_NET_PROTOCOL", null);
                ChannelParameter channelParameter = new ChannelParameter(templateDevice.getMac(), templateDevice.getIp(), channelId);
                channelParameter.setParamItems(new ArrayList<ParameterItem>());
                channelParameter.getParamItems().add(item);
                DetectSupportChannelsAsyncTask task = new DetectSupportChannelsAsyncTask(channelParameter);
                task.execute().get();
                if (item.getParamValue() == null) {
                    continue;
                }
                if (item.getParamValue().equals("0") || item.getParamValue().equals("1") || item.getParamValue().equals("2")) {
                    supportChannels.add(channelId);
                }
            }
            /**
             * 读取设备支持的所有通道定义的参数
             */
            List<ChannelParameter> srcChannelParameters = getDeviceParams(templateDevice, supportChannels);
            /**
             * 读取源设备的参数值，用于目标设备复制参数值。
             */
            for (ChannelParameter channelParameter : srcChannelParameters) {
                ChannelParamReadTask task = new ChannelParamReadTask();
                task.execute(channelParameter).get();
            }
            /**
             * 根据传入的目标设备信息，生成目标设备对象以及目标设备定义的通道参数。
             */
            ArrayList<DeviceSyncMessage> targetDeviceList = new ArrayList<>();//目标设备
            Map<String, List<ChannelParameter>> distDevicePamaMap = new HashMap<>();//目标设备参数
            for (String deviceInfo : toSynchDeviceInfo) {
                String[] deviceInfoArray = deviceInfo.split("#");
                if (deviceInfoArray.length > 1) {
                    DeviceSyncMessage device = new DeviceSyncMessage(deviceInfoArray[0], deviceInfoArray[1]);
                    targetDeviceList.add(device);
                    List<ChannelParameter> channelParameters = getDeviceParams(device, supportChannels);
                    distDevicePamaMap.put(device.getMac(), channelParameters);
                }
            }
            /**
             * 创建线程池，并发同步目标设备参数。最大并发数为 5.
             */
            ExecutorService threadPool = Executors.newFixedThreadPool(5);
            for (Iterator<String> it = distDevicePamaMap.keySet().iterator(); it.hasNext(); ) {
                String mac = it.next();
                List<ChannelParameter> distChannelParamItems = distDevicePamaMap.get(mac);
                SyncDeviceParamTask task = new SyncDeviceParamTask();
                task.setSrcChannelParameters(srcChannelParameters);
                task.setDistChannelParameters(distChannelParamItems);
                task.setTotalDeviceCount(targetDeviceList.size());
                LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
                task.setBroadcastManager(broadcastManager);
                task.setBroadcastLock(broadcastLock);
                task.setCompletedList(completedList);
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
    public List<ChannelParameter> getDeviceParams(DeviceSyncMessage deviceInfo, List<String> supportChannels) {
        List<ChannelParameter> channelParameters = new ArrayList<>();
        for (int i = 0; i < Constants.MAX_CHANNEL; i++) {
            List<Parameter> parameterList = ParameterMapping.getChannelPublicParam(i);
            if (!supportChannels.contains(String.valueOf(i))) {
                continue;
            }
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

    /**
     * 读取设备参数
     */
    class ChannelParamReadTask extends AsyncTask<ChannelParameter, String, ChannelParameter> {
        @Override
        protected ChannelParameter doInBackground(ChannelParameter... channelParameters) {
            IParamReader reader = new ParamReader();
            if (channelParameters != null && channelParameters.length > 0) {
                reader.readChannelParam(channelParameters[0]);
            }
            return channelParameters[0];
        }
    }
}

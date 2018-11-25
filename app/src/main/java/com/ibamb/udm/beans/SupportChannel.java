package com.ibamb.udm.beans;

public class SupportChannel {
    private String channelId;
    private String workMode;
    private String baudRate;

    public SupportChannel(String channelId, String workMode, String baudRate) {
        this.channelId = channelId;
        this.workMode = workMode;
        this.baudRate = baudRate;
    }

    public SupportChannel() {
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getWorkMode() {
        return workMode;
    }

    public void setWorkMode(String workMode) {
        this.workMode = workMode;
    }

    public String getBaudRate() {
        return baudRate;
    }

    public void setBaudRate(String baudRate) {
        this.baudRate = baudRate;
    }
}

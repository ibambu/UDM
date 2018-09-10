package com.ibamb.udm.beans;

public class UdmVersion {
    private int versionId;
    private int currentVersionId;
    private String versionName;
    private String versionDesc;
    private String updatePackage;
    private String apkFile;
    private String deviceUpdateVersion;

    public int getVersionId() {
        return versionId;
    }

    public void setVersionId(int versionId) {
        this.versionId = versionId;
    }

    public int getCurrentVersionId() {
        return currentVersionId;
    }

    public void setCurrentVersionId(int currentVersionId) {
        this.currentVersionId = currentVersionId;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionDesc() {
        return versionDesc;
    }

    public void setVersionDesc(String versionDesc) {
        this.versionDesc = versionDesc;
    }

    public String getUpdatePackage() {
        return updatePackage;
    }

    public void setUpdatePackage(String updatePackage) {
        this.updatePackage = updatePackage;
    }

    public String getApkFile() {
        return apkFile;
    }

    public void setApkFile(String apkFile) {
        this.apkFile = apkFile;
    }

    public String getDeviceUpdateVersion() {
        return deviceUpdateVersion;
    }

    public void setDeviceUpdateVersion(String deviceUpdateVersion) {
        this.deviceUpdateVersion = deviceUpdateVersion;
    }
}

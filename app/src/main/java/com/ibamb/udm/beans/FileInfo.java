package com.ibamb.udm.beans;

public class FileInfo {
    private String fileName;
    private String createTime;

    public FileInfo(String fileName, String createTime) {
        this.fileName = fileName;
        this.createTime = createTime;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}

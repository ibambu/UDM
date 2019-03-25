package com.ibamb.udm.beans;

import java.io.File;

public class FileInfo {
    private String fileName;
    private String createTime;
    private File file;
    private boolean isDirectory;
    private boolean isLable;

    public boolean isDirectory() {
        return isDirectory;
    }

    public boolean isLable() {
        return isLable;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setLable(boolean lable) {
        isLable = lable;
    }

    public void setDirectory(boolean directory) {
        isDirectory = directory;
    }

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

package com.ibamb.udm.beans;

public class CacheFileInfo {
    private String productName;
    private String productVersion;
    private String serailNO;
    private String cacheFileName;

    public CacheFileInfo() {
    }

    public CacheFileInfo(String productName, String productVersion, String serailNO, String cacheFileName) {
        this.productName = productName;
        this.productVersion = productVersion;
        this.serailNO = serailNO;
        this.cacheFileName = cacheFileName;
    }

    public String getSerailNO() {
        return serailNO;
    }

    public void setSerailNO(String serailNO) {
        this.serailNO = serailNO;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductVersion() {
        return productVersion;
    }

    public void setProductVersion(String productVersion) {
        this.productVersion = productVersion;
    }

    public String getCacheFileName() {
        return cacheFileName;
    }

    public void setCacheFileName(String cacheFileName) {
        this.cacheFileName = cacheFileName;
    }
}

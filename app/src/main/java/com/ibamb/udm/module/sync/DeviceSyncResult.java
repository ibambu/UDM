package com.ibamb.udm.module.sync;

public class DeviceSyncResult {
    private int chnlSuccessCount;
    private int chnlFailCount;
    private int chnlTotal;

    public boolean isSyncSuccessful(){
        return chnlSuccessCount==chnlTotal;
    }

    public boolean isCompleted() {
        return chnlSuccessCount + chnlFailCount ==chnlTotal ;
    }

    public int getChnlSuccessCount() {
        return chnlSuccessCount;
    }

    public void setChnlSuccessCount(int chnlSuccessCount) {
        this.chnlSuccessCount = chnlSuccessCount;
    }

    public int getChnlFailCount() {
        return chnlFailCount;
    }

    public void setChnlFailCount(int chnlFailCount) {
        this.chnlFailCount = chnlFailCount;
    }

    public int getChnlTotal() {
        return chnlTotal;
    }

    public void setChnlTotal(int chnlTotal) {
        this.chnlTotal = chnlTotal;
    }
}

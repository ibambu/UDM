package com.ibamb.udm.module.beans;

public class RetMessage {
    private int code;

    private boolean debugEabled;
    private StringBuilder logBuffer = new StringBuilder();//记录日志信息
    private StringBuilder noteBuffer = new StringBuilder();//记录提示信息

    public RetMessage(boolean debugEabled) {
        this.debugEabled = debugEabled;
    }

    public RetMessage() {

    }


    public void addLog(String message) {
        String msg = "[Info] " + message;
        if (debugEabled) {
            System.out.println(msg);
        }
        getLogBuffer().append(msg).append("\n");
    }

    public void addInfo(String message) {
        getNoteBuffer().append("[Info] ").append(message).append("\n");
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public StringBuilder getLogBuffer() {
        return logBuffer;
    }

    public void setLogBuffer(StringBuilder logBuffer) {
        this.logBuffer = logBuffer;
    }

    public StringBuilder getNoteBuffer() {
        return noteBuffer;
    }
}

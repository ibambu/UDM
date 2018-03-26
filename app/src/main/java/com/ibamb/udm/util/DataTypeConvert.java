package com.ibamb.udm.util;

/**
 * Created by luotao on 18-3-26.
 */

public class DataTypeConvert {
    /**
     * short 类型转成 byte 数组
     *
     * @param s
     * @return
     */
    public static byte[] shortToBytes(short s) {
        byte[] targets = new byte[2];
        for (int i = 0; i < 2; i++) {
            int offset = (targets.length - 1 - i) * 8;
            targets[i] = (byte) ((s >>> offset) & 0xff);
        }
        return targets;
    }

    /**
     * Long 类型转成 byte 数组
     *
     * @param s
     * @return
     */
    public static byte[] longToBytes(long s) {
        byte[] targets = new byte[2];
        for (int i = 0; i < 8; i++) {
            int offset = (targets.length - 1 - i) * 8;
            targets[i] = (byte) ((s >>> offset) & 0xff);
        }
        return targets;
    }

    /**
     * Int 类型转 Byte 数组
     *
     * @param num
     * @return
     */
    public static byte[] int2bytes(int num) {
        byte[] result = new byte[4];
        result[0] = (byte) ((num >>> 24) & 0xff);
        result[1] = (byte) ((num >>> 16) & 0xff);
        result[2] = (byte) ((num >>> 8) & 0xff);
        result[3] = (byte) ((num >>> 0) & 0xff);
        return result;
    }

    /**
     * Byte 数组转 Int
     *
     * @param bytes
     * @return
     */
    public static int bytes2int(byte[] bytes) {
        int result = 0;
        if (bytes.length == 4) {
            int a = (bytes[0] & 0xff) << 24;
            int b = (bytes[1] & 0xff) << 16;
            int c = (bytes[2] & 0xff) << 8;
            int d = (bytes[3] & 0xff);
            result = a | b | c | d;
        }
        return result;
    }
}

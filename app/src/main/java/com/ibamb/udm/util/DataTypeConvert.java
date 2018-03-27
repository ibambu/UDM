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

    public static short bytesToShort(byte[] bytes) {
        short s = 0;
        short s0 = (short) (bytes[0] & 0xff);//最低位
        short s1 = (short) (bytes[1] & 0xff);
        s1 <<= 8;
        s = (short) (s0 | s1);
        return s;
    }

    public static byte intToUnsignedByte(int value) {
        byte retValue = (byte) value;
        if (value > Byte.MAX_VALUE) {
            retValue = (byte) (value - Byte.MAX_VALUE + Byte.MIN_VALUE - 1);
        }
        return retValue;
    }

    public static short intToUnsignedShort(int value) {
        short retValue = (byte) value;
        if (value > Short.MAX_VALUE) {
            retValue = (byte) (value - Short.MAX_VALUE + Short.MIN_VALUE - 1);
        }
        return retValue;
    }

    /**
     * 将16进制字符串转换为byte[]
     *
     * @param str
     * @return
     */
    public static byte[] hexStringtoBytes(String str) {
        if (str == null || str.trim().equals("")) {
            return new byte[0];
        }
        byte[] bytes = new byte[str.length() / 2];
        for (int i = 0; i < str.length() / 2; i++) {
            String subStr = str.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) Integer.parseInt(subStr, 16);
        }
        return bytes;
    }

    public static void main(String[] args) {
        System.out.println(intToUnsignedByte(127));
    }
}

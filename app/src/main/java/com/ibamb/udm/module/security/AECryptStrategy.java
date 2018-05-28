package com.ibamb.udm.module.security;

import android.util.Log;

import com.ibamb.udm.log.UdmLog;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author luotao
 */
public class AECryptStrategy implements ICryptStrategy {

    private static final String KEY_AES = "AES";


    /**
     * 对文本加密
     * @param plaintext
     * @param key
     * @return
     */
    @Override
    public String encode(String plaintext, String key) {
        String resultStr = null;
        try {
            byte[] content = plaintext.getBytes(DefualtECryptValue.CHARSET);

            /**
             * 1.构造密钥生成器，指定为AES算法,不区分大小写
             */

            KeyGenerator kgen = KeyGenerator.getInstance(KEY_AES);
            /**
             * 2.按128位随机源初始化密钥
             */
            SecureRandom random = SecureRandom.getInstance("AES");
            random.setSeed(key.getBytes());
            kgen.init(DefualtECryptValue.ERYPT_LENGTH, random);
            /**
             * 3.生成始对称密钥，产生密钥字节数组。
             */
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            /**
             * 5.根据字节数组生成 AES 密钥
             */
            SecretKeySpec keySpec = new SecretKeySpec(enCodeFormat, KEY_AES);
            /**
             * 6.根据指定算法AES自成密码器
             */
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");// 创建密码器
            /**
             * 7.初始化密码器，对明文加密。
             */
            cipher.init(Cipher.ENCRYPT_MODE, keySpec,new IvParameterSpec(new byte[cipher.getBlockSize()]));
            byte[] result = cipher.doFinal(content);
            resultStr = transByte2HexStr(result);
        } catch (Exception e) {
            e.printStackTrace();
            UdmLog.e(this.getClass().getName(), e.getMessage());
        }
        return resultStr;
    }

    /**
     * 对密文解密
     *
     * @param ciphertext
     * @param key
     * @return
     */
    @Override
    public String decode(String ciphertext, String key) {
        String resultStr = null;
        try {
            byte[] content =  transHexStr2Byte(ciphertext);

            /**
             * 1.构造密钥生成器，指定为AES算法,不区分大小写
             */
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            /**
             * 2.按128位随机源初始化密钥
             */
            SecureRandom random = SecureRandom.getInstance("AES");
            random.setSeed(key.getBytes());
            kgen.init(128, random);
            /**g
             * 3.生成始对称密钥，产生密钥字节数组。
             */
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            /**
             * 5.根据字节数组生成 AES 密钥
             */
            SecretKeySpec keySpec = new SecretKeySpec(enCodeFormat, KEY_AES);
            /**
             * 6.根据指定算法AES自成密码器
             */
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");// 创建密码器
            /**
             * 7.初始化密码器，对密文解密。
             */
            cipher.init(Cipher.DECRYPT_MODE, keySpec,new IvParameterSpec(new byte[cipher.getBlockSize()]));
            byte[] result = cipher.doFinal(content);
            resultStr = new String(result, DefualtECryptValue.CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
            UdmLog.e(this.getClass().getName(), e.getMessage());
        }
        return resultStr;
    }

    /**
     * 将二进制转换成16进制
     *
     * @param buf
     * @return
     */
    public static String transByte2HexStr(byte buf[]) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 将16进制转换为二进制
     *
     * @param hexStr
     * @return
     */
    public static byte[] transHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) {
            return null;
        }
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    public static void main(String[] args) throws Exception {

        AECryptStrategy aaa = new AECryptStrategy();
        String content = "{'repairPhone':'18547854787','customPhone':'12365478965','captchav':'58m7'}";
        System.out.println("加密前：" + content);
        System.out.println("加密密钥和解密密钥：" + DefualtECryptValue.KEY);
        String encrypt = aaa.encode(content, DefualtECryptValue.KEY);
        System.out.println("加密后：" + encrypt);
        String decrypt = aaa.decode(encrypt, DefualtECryptValue.KEY);
        System.out.println("解密后：" + decrypt);
    }
}

package com.ibamb.udm.component.security;

import android.text.TextUtils;
import android.util.Base64;

import com.ibamb.udm.module.security.ICryptStrategy;

import java.security.Provider;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESCrypt implements ICryptStrategy {

    private final String HEX = "0123456789ABCDEF";
    //AES是加密方式 CBC是工作模式 PKCS5Padding是填充模式
    private final String CBC_PKCS5_PADDING = "AES/CBC/PKCS5Padding";
    //AES 加密
    private final String AES = "AES";
    // SHA1PRNG 强随机种子算法, 要区别4.2以上版本的调用方法
    private final String SHA1PRNG = "SHA1PRNG";

    /**
     * 加密
     * @param plaintext
     * @param key
     * @return
     */
    @Override
    public String encode(String plaintext, String key) {
        return encrypt(key, plaintext);
    }

    /**
     * 解密
     * @param ciphertext
     * @param key
     * @return
     */
    @Override
    public String decode(String ciphertext, String key) {
        return decrypt(key, ciphertext);
    }


    /**
     * 生成随机数，可以当做动态的密钥 加密和解密的密钥必须一致，不然将不能解密
     */
    public String generateKey() {
        try {
            SecureRandom localSecureRandom = SecureRandom.getInstance(SHA1PRNG);
            byte[] bytes_key = new byte[20];
            localSecureRandom.nextBytes(bytes_key);
            String str_key = toHex(bytes_key);
            return str_key;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 对密钥进行处理
    private byte[] getRawKey(byte[] seed) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance(AES);
        //for android
        SecureRandom sr = null;
        // 在4.2以上版本中，SecureRandom获取方式发生了改变
        int sdk_version = android.os.Build.VERSION.SDK_INT;
        if (sdk_version > 23) {  // Android  6.0 以上
            sr = SecureRandom.getInstance(SHA1PRNG, new CryptoProvider());
        } else if (android.os.Build.VERSION.SDK_INT >= 17) {   //4.2及以上
            sr = SecureRandom.getInstance(SHA1PRNG, "Crypto");
        } else {
            sr = SecureRandom.getInstance(SHA1PRNG);
        }


        // for Java
        // secureRandom = SecureRandom.getInstance(SHA1PRNG);
        sr.setSeed(seed);
        kgen.init(128, sr); //256 bits or 128 bits,192bits
        //AES中128位密钥版本有10个加密循环，192比特密钥版本有12个加密循环，256比特密钥版本则有14个加密循环。
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
        return raw;
    }

    /*
     * AES 加密
     */
    public String encrypt(String key, String cleartext) {
        if (TextUtils.isEmpty(cleartext)) {
            return cleartext;
        }
        try {
            byte[] result = encrypt(key, cleartext.getBytes());
//            return Base64Encoder.encode(result);
            return new String(Base64.encode(result, Base64.DEFAULT));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * AES 加密
     */
    private byte[] encrypt(String key, byte[] clear) throws Exception {
        byte[] raw = getRawKey(key.getBytes());
        SecretKeySpec skeySpec = new SecretKeySpec(raw, AES);
        Cipher cipher = Cipher.getInstance(CBC_PKCS5_PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(new byte[cipher.getBlockSize()]));
        byte[] encrypted = cipher.doFinal(clear);
        return encrypted;
    }

    /**
     * AES 解密
     */
    public String decrypt(String key, String encrypted) {
        if (TextUtils.isEmpty(encrypted)) {
            return encrypted;
        }
        try {
//            byte[] enc = Base64Decoder.decodeToBytes(encrypted);
            byte[] enc = Base64.decode(encrypted, Base64.DEFAULT);
            byte[] result = decrypt(key, enc);
            return new String(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * AES 解密
     */
    private byte[] decrypt(String key, byte[] encrypted) throws Exception {
        byte[] raw = getRawKey(key.getBytes());
        SecretKeySpec skeySpec = new SecretKeySpec(raw, AES);
        Cipher cipher = Cipher.getInstance(CBC_PKCS5_PADDING);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(new byte[cipher.getBlockSize()]));
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }

    //二进制转字符
    public String toHex(byte[] buf) {
        if (buf == null)
            return "";
        StringBuffer result = new StringBuffer(2 * buf.length);
        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }
        return result.toString();
    }

    private void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
    }


    public class CryptoProvider extends Provider {
        /**
         * Creates a Provider and puts parameters
         */
        public CryptoProvider() {
            super("Crypto", 1.0, "HARMONY (SHA1 digest; SecureRandom; SHA1withDSA signature)");
            put("SecureRandom.SHA1PRNG",
                    "org.apache.harmony.security.provider.crypto.SHA1PRNG_SecureRandomImpl");
            put("SecureRandom.SHA1PRNG ImplementedIn", "Software");
        }
    }

    public static void main(String[] args){
        AESCrypt aa = new AESCrypt();
        String a="aaaaaaaaaaaaaaaaaaaaaaaaa";
        String b = aa.encode("123qwe",a);
        System.out.println("000000=="+a);
        System.out.println("000000=="+b);
        String c = aa.decode("123qwe","wN/ibGUezcOX2lWa+QMjXlId2Cdq76di2MYVZ+0CB/dVvcwJnA==");
        System.out.println("000000=="+c);
    }
}

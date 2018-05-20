package com.ibamb.udm.security;

public interface ICryptStrategy {
    /**
     * 对文本内容加密
     * @param plaintext
     * @param key
     * @return
     */
    public String encode(String plaintext,String key);

    /**
     * 对密文解密
     * @param ciphertext
     * @param key
     * @return
     */
    public String decode(String ciphertext,String key);
}

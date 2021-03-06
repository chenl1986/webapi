package com.tubugs.springboot.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * Created by xuzhang on 2017/8/27.
 */
public class Base64Util {
    /**
     * Base64加密
     */
    public static String encryptBASE64(byte[] key) throws Exception {
        String encode = (new BASE64Encoder()).encodeBuffer(key);
        return encode.replaceAll("\r\n", "");
    }

    /**
     * Base64解密
     */
    public static byte[] decryptBASE64(String key) throws Exception {
        return (new BASE64Decoder()).decodeBuffer(key);
    }

}

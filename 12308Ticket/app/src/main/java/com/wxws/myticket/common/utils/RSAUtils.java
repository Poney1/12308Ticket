package com.wxws.myticket.common.utils;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * desc: 12306账号密码加密算法
 * Date: 2016-12-08 13:39
 *
 * @author jiangyan
 */
public class RSAUtils{
    private static String RSA = "RSA";

    private static String RSA_ANDROID = "RSA/ECB/PKCS1Padding";// java和android解密不同，如果需要加入解密，try this
    private static String RSA_JAVA = "RSA/None/PKCS1Padding";

    public static String mPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCcZtxnTQeGk9bxDeuhvq+nsHcIvAJv94ZVz12QoRGFcpTYqXv6eS5/g3L3BMxSodImvgr1GMXNesP4x8TurXPRfuUngAxlozN1W3sdIw+0Y13ybFsdSJ3c4ByFvsBoieIFe3J6SvYo2oMdAlbGADB6CuA0hureY3sRZdJDLFz0DwIDAQAB";

    public static RSAPublicKey loadPublicKey(String publicKeyStr) throws Exception
    {
        byte[] keyBytes = Base64Utils.decode(publicKeyStr);//Streams.base64Decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            return (RSAPublicKey) keyFactory.generatePublic(x509KeySpec);
        } catch(Exception e) {
            throw new SecurityException(e);
        }
    }

    public static byte[] encrypt(byte[] data) {
        try {
            RSAPublicKey publicKey = RSAUtils.loadPublicKey(mPublicKey);
            Cipher cipher = Cipher.getInstance(RSA_JAVA);
            // 编码前设定编码方式及密钥
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            // 传入编码数据并返回编码结果
            return cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
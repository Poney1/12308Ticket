package com.wxws.myticket.common.utils;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES加密可逆
 * @author ljf
 * @version 2015年3月12日 下午6:30:04
 */
public class AESUtils {

	// key必须是128位
	private static final String key = "1234567890LJFHGY";

	/**
	 * 加密
	 * @param data 加密的内容
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String data) {
		if(data==null){return null;}
		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
			cipher.init(Cipher.ENCRYPT_MODE, keyspec);
			byte[] encrypted = cipher.doFinal(data.getBytes());
			return Base64.encodeToString(encrypted, Base64.DEFAULT);
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 解密
	 * @param data 解密的内容
	 * @return
	 * @throws Exception
	 */
	public static String desEncrypt(String data) {
		if(data==null){return null;}
		try {
			byte[] encrypted1 = Base64.decode(data.getBytes(), Base64.DEFAULT);
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
			cipher.init(Cipher.DECRYPT_MODE, keyspec);
			byte[] original = cipher.doFinal(encrypted1);
			return new String(original, "UTF-8");
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}

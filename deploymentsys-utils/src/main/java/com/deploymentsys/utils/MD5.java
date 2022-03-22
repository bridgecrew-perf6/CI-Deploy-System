package com.deploymentsys.utils;

import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

@Component
public class MD5 {

	private static final String MD5KEY = "fdfd@!5";

	/**
	 * MD5方法
	 * 
	 * @param text
	 *            明文
	 * @param key
	 *            密钥
	 * @return 密文
	 * @throws Exception
	 */
	public String md5(String text) {
		// 加密后的字符串
		String encodeStr = DigestUtils.md5DigestAsHex((text + MD5KEY).getBytes());
		return encodeStr;
	}

	/**
	 * MD5验证方法
	 * 
	 * @param text
	 *            明文
	 * @param key
	 *            密钥
	 * @param md5
	 *            密文
	 * @return true/false
	 * @throws Exception
	 */
	public boolean verify(String text, String md5) {
		// 根据传入的密钥进行验证
		String md5Text = md5(text);
		if (md5Text.equalsIgnoreCase(md5)) {
			return true;
		}

		return false;
	}
}

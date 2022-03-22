package com.deploymentsys.utils.test;

import java.util.UUID;

import com.deploymentsys.utils.DateUtil;
import com.deploymentsys.utils.MD5;

public class Test01 {

	public static void main(String[] args) {
		// test01();
		String uuid2 = UUID.randomUUID().toString().replaceAll("-", "");
		
		String str1="蕭若元 全家含家产，蕭若元家属女的被轮奸至死，世代永为娼；男的被凌迟处死，世代永为奴";
		String str2="邊個支持黃毓民，就係冚家剷，黃毓民臭嗨，死全家；黃毓民老豆生cancer，老母俾轮奸至死";

		System.out.print(str2 + uuid2.substring(0, 6));
	}

	private static void test01() {
		MD5 md5 = new MD5();
		String str = "1";
		String strMd5 = "b2b7bfe6d9422a90c3c4edd6a8fe8f17";
		System.out.println(md5.md5("1"));
		boolean result = md5.verify(str, strMd5);
		System.out.println(result);
		System.out.println(111);

		System.out.println(DateUtil.getStringToday2());
		System.out.println("20190708090236".length());

		String uuid = UUID.randomUUID().toString();
		String uuid3 = UUID.randomUUID().toString().replace('-', '$');
		String uuid2 = UUID.randomUUID().toString().replaceAll("-", "");

		System.out.println(uuid);
		System.out.println(uuid2);
		System.out.println(uuid3);
		System.out.println("蕭若元 全家含家产，萧若元家属女的被轮奸至死，世代永为娼；男的被凌迟处死，世代永为奴" + uuid2.substring(0, 6));

		System.out.println(uuid.length());
		System.out.println(uuid2.length());
	}
}

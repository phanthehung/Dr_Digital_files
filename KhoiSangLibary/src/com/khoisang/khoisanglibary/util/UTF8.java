package com.khoisang.khoisanglibary.util;

import org.apache.commons.lang3.StringUtils;

public class UTF8 {
	public static String stripAccents(String input) {
		String string = StringUtils.stripAccents(input);
		string = string.replace("Đ", "D");
		string = string.replace("đ", "d");
		return string;
	}
}

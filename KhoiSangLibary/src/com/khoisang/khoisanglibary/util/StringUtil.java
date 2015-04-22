package com.khoisang.khoisanglibary.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import org.apache.commons.io.IOUtils;

public class StringUtil {
	public static String bytesToHexString(byte[] bytes, String seprator) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(0xFF & bytes[i]);
			if (hex.length() == 1) {
				sb.append('0');
			}
			sb.append(hex);
			sb.append(seprator);
		}
		return sb.toString();
	}

	public static String streamToString(InputStream inputStream)
			throws IOException {
		String string = IOUtils.toString(inputStream, "UTF-8");
		return string;
	}

	public static String streamToStringASCII(InputStream inputStream)
			throws IOException {
		String string = IOUtils.toString(inputStream, "US-ASCII");
		return string;
	}

	public static String random(int minLength, int maxLength) {
		Random random = new Random();
		while (maxLength <= 1) {
			maxLength = random.nextInt(Integer.MAX_VALUE);
		}

		while (minLength <= 0 || minLength >= maxLength) {
			minLength = random.nextInt(maxLength);
		}

		int randomLength = 0;
		while (randomLength < minLength) {
			if (minLength == (maxLength - 1)) {
				randomLength = minLength;
				break;
			}
			randomLength = random.nextInt(maxLength);
		}

		StringBuilder randomStringBuilder = new StringBuilder();
		char tempChar;
		for (int i = 0; i < randomLength; i++) {
			tempChar = (char) (random.nextInt(96) + 32);
			randomStringBuilder.append(tempChar);
		}
		return randomStringBuilder.toString();
	}
}

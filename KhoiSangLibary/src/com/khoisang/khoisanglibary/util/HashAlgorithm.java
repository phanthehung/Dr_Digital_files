package com.khoisang.khoisanglibary.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashAlgorithm {
	public enum Algorithm {
		MD5, SHA1
	}

	public static String hash(String key, Algorithm algorithm) {
		String result;
		try {
			MessageDigest mDigest = null;
			if (algorithm.compareTo(Algorithm.MD5) == 0) {
				mDigest = MessageDigest.getInstance("MD5");
			} else {
				mDigest = MessageDigest.getInstance("SHA-1");
			}
			mDigest.update(key.getBytes());
			result = StringUtil.bytesToHexString(mDigest.digest(), "");
		} catch (NoSuchAlgorithmException e) {
			result = String.valueOf(key.hashCode());
		}
		return result;
	}
}

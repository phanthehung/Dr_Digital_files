package com.khoisang.khoisanglibary.encryption;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.khoisang.khoisanglibary.util.Base64;

public class AES {
	private Cipher mCipherEncrypt;
	private Cipher mCipherDecrypt;

	@SuppressWarnings("unused")
	private AES() {
	}

	public AES(byte[] vector, byte[] key, String algrorithm, String mode,
			String padding) throws InvalidKeyException,
			NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidAlgorithmParameterException {
		init(vector, key, algrorithm, mode, padding);
	}

	private void init(byte[] vector, byte[] key, String algrorithm,
			String mode, String padding) throws NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException,
			InvalidAlgorithmParameterException {
		mCipherEncrypt = Cipher.getInstance(algrorithm + "/" + mode + "/"
				+ padding);
		mCipherDecrypt = Cipher.getInstance(algrorithm + "/" + mode + "/"
				+ padding);

		IvParameterSpec ivSpec = new IvParameterSpec(vector);
		SecretKeySpec keySpec = new SecretKeySpec(key, algrorithm);

		mCipherEncrypt.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
		mCipherDecrypt.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
	}

	public String Decrypt(String text, boolean base64)
			throws UnsupportedEncodingException, IOException,
			IllegalBlockSizeException, BadPaddingException {
		String result = "";
		byte[] data = text.getBytes("UTF-8");
		if (base64 == true) {
			data = Base64.decode(data);
		}
		byte[] results = mCipherDecrypt.doFinal(data);
		result = new String(results, "UTF-8");
		return result.trim();
	}

	public String Encrypt(String text, boolean base64)
			throws UnsupportedEncodingException, IOException,
			IllegalBlockSizeException, BadPaddingException {
		byte[] encryptByte = mCipherEncrypt.doFinal(padString(text
				.getBytes("UTF-8")));
		if (base64 == true) {
			return Base64.encodeBytes(encryptByte);
		} else {
			return new String(encryptByte, "UTF-8");
		}
	}

	private byte[] padString(byte[] source) {
		byte paddingChar = ' ';
		int size = 16;
		int x = source.length % size;
		int padLength = size - x;

		byte[] result = new byte[source.length + padLength];
		System.arraycopy(source, 0, result, 0, source.length);

		for (int i = 0; i < padLength; i++) {
			result[source.length + i] = paddingChar;
		}
		return result;
	}
}

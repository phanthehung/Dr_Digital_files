package com.khoisang.khoisanglibary.util;

import java.util.UUID;

import android.os.Build;

public class DeviceUtil {
	public static String getPsuedoUniqueID() {
		String m_szDevIDShort = "35" + (Build.BOARD.length() % 10)
				+ (Build.BRAND.length() % 10) + (Build.CPU_ABI.length() % 10)
				+ (Build.DEVICE.length() % 10)
				+ (Build.MANUFACTURER.length() % 10)
				+ (Build.MODEL.length() % 10) + (Build.PRODUCT.length() % 10);

		String serial = null;
		try {
			serial = android.os.Build.class.getField("SERIAL").get(null)
					.toString();
		} catch (Exception e) {
			serial = StringUtil.random(5, 20);
		}

		return new UUID(m_szDevIDShort.hashCode(), serial.hashCode())
				.toString();
	}
}

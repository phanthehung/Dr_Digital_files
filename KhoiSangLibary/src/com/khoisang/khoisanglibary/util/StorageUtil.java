package com.khoisang.khoisanglibary.util;

import java.io.File;

import android.content.Context;
import android.os.Environment;

public class StorageUtil {
	public static File getDiskCacheDir(Context context, String dirName) {
		File directory = null;
		if (checkExternalCacheDir(context) == true) {
			directory = new File(context.getExternalCacheDir(), dirName);
		} else if (context.getCacheDir() != null)
			directory = new File(context.getCacheDir(), dirName);
		else if (getExternalDir() != null) {
			directory = new File(getExternalDir(), context.getPackageName()
					+ File.separatorChar + dirName);
		} else
			return null;

		directory.mkdirs();
		return directory;
	}

	public static File getExternalDir() {
		File storageDir = new File("/mnt/");
		if (storageDir.isDirectory()) {
			File file = new File("/mnt/external_sd/");
			if (file.exists() == true)
				return file;

			file = new File("/mnt/extSdCard/");
			if (file.exists() == true)
				return file;
		} // End if
		return null;
	}

	public static boolean checkExternalCacheDir(Context context) {
		return Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())
				&& context.getExternalCacheDir() != null;
	}
}

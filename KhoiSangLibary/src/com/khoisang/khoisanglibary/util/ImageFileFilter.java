package com.khoisang.khoisanglibary.util;

import java.io.File;
import java.util.Locale;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageFileFilter {

	private final static String[] mValidateExtensions = new String[] { "jpg",
			"png", "gif", "jpeg" };

	public static boolean validateExtension(File file) {
		for (String extension : mValidateExtensions) {
			if (file.getName().toLowerCase(Locale.getDefault())
					.endsWith(extension)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isImage(File file) {
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(),
					options);
			if (options.outWidth > -1 && options.outHeight > -1) {
				return true;
			} else {
				return false;
			}
		} catch (Exception ex) {
			return false;
		}
	}
}

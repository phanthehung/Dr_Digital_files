package com.khoisang.khoisanglibary.util;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;

public class BitmapUtil {
	public static Bitmap decodeSampledBitmapFromDescriptor(
			FileDescriptor fileDescriptor, int reqWidth, int reqHeight) {
		Bitmap bitmap = null;
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
			options.inSampleSize = calculateInSampleSize(options, reqWidth,
					reqHeight);
			options.inJustDecodeBounds = false;
			bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor, null,
					options);
		} catch (Exception ex) {
			bitmap = null;
		}
		return bitmap;
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		int height = options.outHeight;
		int width = options.outWidth;
		int inSampleSize = 2;

		if (reqWidth < 1 || reqHeight < 1) {
			return inSampleSize;
		}

		if (height > reqHeight || width > reqWidth) {
			int halfHeight = height / 2;
			int halfWidth = width / 2;

			while ((halfHeight / inSampleSize) > reqHeight
					|| (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}

			long totalPixels = width * height / inSampleSize;
			long totalReqPixels = reqWidth * reqHeight * 2;
			while (totalPixels > totalReqPixels) {
				inSampleSize *= 2;
				totalPixels /= 2;
			}
		}
		return inSampleSize;
	}

	public static ByteArrayOutputStream compress(Bitmap original,
			CompressFormat format, int quantity) {
		if (original != null && original.isRecycled() == false) {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			if (original.compress(format, quantity, outputStream) == true)
				return outputStream;
		} // End if
		return null;
	}

	public static Bitmap scaleBitmap(Bitmap original, int maxSize) {
		int inputWidth = original.getWidth();
		int inputHeight = original.getHeight();
		int outputWidth = inputWidth;
		int outputHeight = inputHeight;
		if (inputWidth > maxSize || inputHeight > maxSize) {
			if (inputWidth > inputHeight) {
				outputWidth = maxSize;
				outputHeight = (inputHeight * maxSize) / inputWidth;
			} else {
				outputHeight = maxSize;
				outputWidth = (inputWidth * maxSize) / inputHeight;
			}
		}

		Bitmap resizedBitmap = Bitmap.createScaledBitmap(original, outputWidth,
				outputHeight, false);
		return resizedBitmap;
	}
}

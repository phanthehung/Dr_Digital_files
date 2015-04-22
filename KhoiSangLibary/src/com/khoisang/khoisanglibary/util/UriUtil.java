package com.khoisang.khoisanglibary.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class UriUtil {
	public static String getRealPathFromURI(Context context, Uri contentUri)
			throws Exception {
		Cursor cursor = null;
		try {
			String[] proj = { MediaStore.Images.Media.DATA };
			cursor = context.getContentResolver().query(contentUri, proj, null,
					null, null);
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		} // End finally
	}
}

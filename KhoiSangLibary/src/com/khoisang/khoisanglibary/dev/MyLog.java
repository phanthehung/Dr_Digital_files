package com.khoisang.khoisanglibary.dev;

import android.util.Log;

public class MyLog {
	private static final boolean isDebug = true;

	public static void i(Class<?> _class, String message) {
		if (isDebug == true) {
			Log.i(_class.getName(), message);
		}
	}

	public static void e(Class<?> _class, Exception ex) {
		if (isDebug == true) {
			Log.e(_class.getName(), ex.getMessage());
		}
	}
} // End Class

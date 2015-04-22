package com.khoisang.khoisanglibary.dev;

import org.apache.commons.lang3.exception.ExceptionUtils;

import android.util.Log;

public class DebugLog {
	public interface DebugLogListerner {
		public void debugLogHandlerError(Exception ex);
	}

	public static boolean DEBUG = true;
	private static DebugLogListerner mListerner;

	public static DebugLogListerner getListerner() {
		return mListerner;
	}

	public static void setListerner(DebugLogListerner listerner) {
		mListerner = listerner;
	}

	public static void i(String tag, String message) {
		if (DEBUG == true) {
			// Bug
			if (message != null)
				Log.i(tag, message);
			else
				Log.i(tag, "No message");
		}
	}

	public static void e(String tag, Exception ex) {
		if (mListerner != null) {
			mListerner.debugLogHandlerError(ex);
		}

		if (DEBUG == true) {
			// Bug
			String message = ExceptionUtils.getStackTrace(ex);
			if (message != null)
				Log.e(tag, message);
			else
				Log.e(tag, "No message");
		}
	}
}

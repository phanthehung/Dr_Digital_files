package com.khoisang.khoisanglibary.util;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

public class ServiceUtil {
	public static boolean isRunning(Context context, String serviceName) {
		ActivityManager manager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo serviceInfo : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if (serviceName.equals(serviceInfo.service.getClassName())) {
				return true;
			} // End if
		} // End for
		return false;
	}
}

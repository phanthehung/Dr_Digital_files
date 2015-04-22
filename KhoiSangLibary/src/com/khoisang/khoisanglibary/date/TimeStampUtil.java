package com.khoisang.khoisanglibary.date;

import java.util.Date;

import android.os.SystemClock;

public class TimeStampUtil {
	public static Date unixTimeStampToDate(Long timeStamp) {
		Date date = new Date(timeStamp * Long.valueOf(1000));
		return date;
	}

	public static Long dateToUnixTimeStamp(Date date) {
		return date.getTime() / Long.valueOf(1000);
	}

	public static long getCurrentTimeStamp() {
		return (long) (getCurrentSystemTimeStamp() / 1000L);
	}

	public static long getCurrentSystemTimeStamp() {
		return java.lang.System.currentTimeMillis();
	}

	public static long getUpTime() {
		return SystemClock.uptimeMillis();
	}

	public static long getUpTimeBySystemTimeStamp(long systemTimeStamp) {
		long upTime = getUpTime();
		long currentSystemTimeStamp = getCurrentSystemTimeStamp();
		return systemTimeStamp - currentSystemTimeStamp + upTime;
	}
}

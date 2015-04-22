package com.khoisang.khoisanglibary.date;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTime {
	public static String toString(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"dd-MMMM-yyyy hh:mm:ss");
		return dateFormat.format(date);
	}
}

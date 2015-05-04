package com.khoisang.drdigital.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.khoisang.drdigital.data.Notification;

import android.app.Application;

public class History {
	private static final String FILE_NAME = "History";

	public static void save(Application application, String title,
			String message) throws IOException {
		com.khoisang.khoisanglibary.dev.FileWriter fileWriter = new com.khoisang.khoisanglibary.dev.FileWriter(
				application, FILE_NAME);
		fileWriter.write(title + ";" + message + ";"
				+ String.valueOf(new Date().getTime()) + "\n");
	}

	public static List<Notification> get(Application application)
			throws IOException {
		List<Notification> listNotification = new ArrayList<Notification>();

		com.khoisang.khoisanglibary.dev.FileWriter fileWriter = new com.khoisang.khoisanglibary.dev.FileWriter(
				application, FILE_NAME);
		String message = fileWriter.get(false);
		if (message != null && message.equalsIgnoreCase("") == false) {
			String[] arrayNotification = message.split("\n");
			for (int i = 0; arrayNotification != null
					&& i < arrayNotification.length; i++) {
				String stringNotification = arrayNotification[i];
				String[] stringNotificationSplit = stringNotification
						.split(";");
				if (stringNotificationSplit != null
						&& stringNotificationSplit.length == 3) {
					Notification notification = new Notification();
					notification.title = stringNotificationSplit[0];
					notification.message = stringNotificationSplit[1];
					try {
						notification.time = Integer
								.valueOf(stringNotificationSplit[2]);
					} catch (Exception ex) {
						// Ignore
						notification.time = new Date().getTime();
					}
					listNotification.add(notification);
				}
			} // End For
		} // End If
		return listNotification;
	}
}

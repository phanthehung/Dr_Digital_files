package com.khoisang.drdigital.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.khoisang.drdigital.data.Notification;
import com.khoisang.khoisanglibary.util.Base64;

import android.app.Application;

public class History {
	private static final String FILE_NAME = "History";

	public static void delete(Application application) {
		com.khoisang.khoisanglibary.dev.FileWriter fileWriter = new com.khoisang.khoisanglibary.dev.FileWriter(application, FILE_NAME);
		fileWriter.delete();
	}

	public static void save(Application application, String title, String message, long timeStamp) throws IOException {
		com.khoisang.khoisanglibary.dev.FileWriter fileWriter = new com.khoisang.khoisanglibary.dev.FileWriter(application, FILE_NAME);
		fileWriter.write(Base64.encodeBytes(title.getBytes()) + ";" + Base64.encodeBytes(message.getBytes()) + ";" + Base64.encodeBytes((String.valueOf(timeStamp * 1000)).getBytes()) + "&");
	}

	public static List<Notification> get(Application application) throws IOException {
		List<Notification> listNotification = new ArrayList<Notification>();
		com.khoisang.khoisanglibary.dev.FileWriter fileWriter = new com.khoisang.khoisanglibary.dev.FileWriter(application, FILE_NAME);
		if (fileWriter.isExist() == false)
			return null;

		String message = fileWriter.get(false);
		if (message != null && message.equalsIgnoreCase("") == false) {
			String[] arrayNotification = message.split("&");
			for (int i = 0; arrayNotification != null && i < arrayNotification.length; i++) {
				String stringNotification = arrayNotification[i];
				String[] stringNotificationSplit = stringNotification.split(";");
				if (stringNotificationSplit != null && stringNotificationSplit.length == 3) {
					Notification notification = new Notification();
					notification.title = new String(Base64.decode(stringNotificationSplit[0]));
					notification.message = new String(Base64.decode(stringNotificationSplit[1]));
					try {
						notification.time = Long.valueOf(new String(Base64.decode(stringNotificationSplit[2])));
					} catch (Exception ex) {
						// Ignore
						notification.time = new Date().getTime();
					}
					listNotification.add(notification);
				}
			} // End For
		} // End If
		Collections.reverse(listNotification);
		return listNotification;
	}
}

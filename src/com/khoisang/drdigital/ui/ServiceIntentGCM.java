package com.khoisang.drdigital.ui;

import java.io.IOException;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.khoisang.drdigital.R;
import com.khoisang.drdigital.util.History;

public class ServiceIntentGCM extends IntentService {

	public ServiceIntentGCM() {
		super(ActivityMain.PROJECT_NUMBER_ID);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		String messageType = gcm.getMessageType(intent);
		if (!extras.isEmpty()) {
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
					.equals(messageType)) {
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
					.equals(messageType)) {
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
					.equals(messageType)) {
				String title = intent.getExtras().getString("title");
				String message = intent.getExtras().getString("message");
				try {
					generateNotification(getApplicationContext(), title,
							message);
					History.save(getApplication(), title, message);
				} catch (IOException e) {
					// Ignore
				}
			}
		}
		BroadcastReceiverGcm.completeWakefulIntent(intent);
	}

	private void generateNotification(Context context, String title,
			String message) {
		Vibrator vibrator = (Vibrator) context
				.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(500);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				context).setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(title).setContentText(message);
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(0, builder.build());
	}
}

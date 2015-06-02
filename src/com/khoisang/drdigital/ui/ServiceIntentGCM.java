package com.khoisang.drdigital.ui;

import java.io.IOException;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.khoisang.drdigital.R;
import com.khoisang.drdigital.util.History;

public class ServiceIntentGCM extends IntentService {
	public static final String KEY = "ServiceIntentGCM";
	public static final String NOTIFICATION_COUNTER = "notification_counter";

	public ServiceIntentGCM() {
		super(ActivityMain.PROJECT_NUMBER_ID);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		String messageType = gcm.getMessageType(intent);
		if (!extras.isEmpty()) {
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
				String title = intent.getExtras().getString("title");
				String message = intent.getExtras().getString("message");
				String timeString = intent.getExtras().getString("time");
				try {
					generateNotification(getApplicationContext(), title, message);
					History.save(getApplication(), title, message, Long.valueOf(timeString));
				} catch (IOException e) {
					// Ignore
				}
			}
		}
		BroadcastReceiverGcm.completeWakefulIntent(intent);
	}

	private void generateNotification(Context context, String title, String message) {
		Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(500);

		DrDigitalApplication.counter++;

		Intent intent = new Intent(this, ActivityMain.class);
		intent.putExtra(KEY, "true");

		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		NotificationCompat.Builder builder = new NotificationCompat.Builder(context).setSmallIcon(R.drawable.ic_launcher).setContentTitle(title).setContentText(message).setDefaults(Notification.DEFAULT_ALL).setWhen(System.currentTimeMillis()).setContentIntent(contentIntent);
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(1, builder.build());
	}
}

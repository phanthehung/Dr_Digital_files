package com.khoisang.drdigital.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.text.format.DateFormat;
import android.widget.TextView;

import com.khoisang.drdigital.data.Notification;
import com.khoisang.khoisanglibary.ui.ListViewItem;

public class AdapterNotification extends ListViewItem {

	private TextView _title;
	private TextView _time;
	private TextView _message;

	@Override
	public void initData(Object own, Object obj, int position) {
	}

	@Override
	public void setData(Object own, Object obj, int position) {
		Notification notification = (Notification) obj;
		_title.setText(notification.title);
		_message.setText(notification.message);

		SimpleDateFormat sdf = new SimpleDateFormat("MMM dd HH:mm:ss yyyy", Locale.getDefault());
		_time.setText(sdf.format(new Date(notification.time)));
	}

}

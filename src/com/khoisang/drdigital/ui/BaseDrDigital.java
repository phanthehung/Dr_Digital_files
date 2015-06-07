package com.khoisang.drdigital.ui;

import android.view.View;
import android.widget.ImageView;

public interface BaseDrDigital {
	public enum ContentType {
		Support, Information, Enquiry, Location, Notification
	}

	public void initView(View view);

	public void checkNotification(ContentType contentType);

	public ImageView getOption1();

	public ImageView getOption2();

	public ImageView getOption3();

	public ImageView getOption4();

	public void onClickSupport();

	public void onClickInformation();

	public void onClickEnquiry();

	public void onClickLocation();

	public void onClickNotification();
}

package com.khoisang.drdigital.ui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.khoisang.drdigital.R;
import com.khoisang.drdigital.adapter.AdapterNotification;
import com.khoisang.drdigital.data.Notification;
import com.khoisang.drdigital.ui.BaseDrDigital.ContentType;
import com.khoisang.khoisanglibary.ui.ListViewItem;
import com.khoisang.khoisanglibary.ui.fragment.ListViewFragment;

public class FragmentNotification extends ListViewFragment {

	private ImageView mBottomOption1;
	private ImageView mBottomOption2;
	private ImageView mBottomOption3;
	private ImageView mBottomOption4;

	private BaseDrDigital mBaseDrDigital;

	private ArrayList<Object> listNotification;

	private ArrayList<Object> getListNotification() {
		if (listNotification == null)
			listNotification = new ArrayList<Object>();
		return listNotification;
	}

	public FragmentNotification(BaseDrDigital baseDrDigital) {
		mBaseDrDigital = baseDrDigital;
	}

	public void setListNotification(List<Notification> listNotification) {
		getListNotification().clear();
		if (listNotification != null && listNotification.size() > 0)
			getListNotification().addAll(listNotification);
	}

	@Override
	protected void afterSetLayoutID(Bundle savedInstanceState) {
		super.afterSetLayoutID(savedInstanceState);

		mBaseDrDigital.initView(getView());
		mBaseDrDigital.checkNotification(ContentType.Notification);

		mBaseDrDigital.getOption1().setImageDrawable(getResources().getDrawable(R.drawable.support_icon));
		mBaseDrDigital.getOption2().setImageDrawable(getResources().getDrawable(R.drawable.information_icon));
		mBaseDrDigital.getOption3().setImageDrawable(getResources().getDrawable(R.drawable.enquiry_icon));
		mBaseDrDigital.getOption4().setImageDrawable(getResources().getDrawable(R.drawable.location_icon));

		mBaseDrDigital.getOption1().setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mBaseDrDigital.onClickSupport();
			}
		});
		mBaseDrDigital.getOption2().setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mBaseDrDigital.onClickLocation();

			}
		});
		mBaseDrDigital.getOption3().setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mBaseDrDigital.onClickLocation();

			}
		});
		mBaseDrDigital.getOption4().setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mBaseDrDigital.onClickLocation();

			}
		});

		if (getListNotification().size() == 0)
			showToast("Not found", false);
	}

	@Override
	protected int getListViewID() {
		return R.id.fragment_notification_list;
	}

	@Override
	protected int getLayoutItemID() {
		return R.layout.item_notification;
	}

	@Override
	protected ArrayList<Object> getDataToListView() {
		return getListNotification();
	}

	@Override
	protected void onClick(AdapterView<?> listView, View itemView, int position, Object obj) {
	}

	@Override
	protected ListViewItem getItemObject() {
		return new AdapterNotification();
	}

	@Override
	protected int getLayoutID() {
		return R.layout.fragment_notification;
	}

	@Override
	protected void reCreateView() {
		mBaseDrDigital.initView(getView());
		mBaseDrDigital.checkNotification(ContentType.Notification);
		if (getListNotification().size() == 0)
			showToast("Not found", false);
	}

}

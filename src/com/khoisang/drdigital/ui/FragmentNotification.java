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
import com.khoisang.khoisanglibary.ui.ListViewItem;
import com.khoisang.khoisanglibary.ui.fragment.ListViewFragment;

public class FragmentNotification extends ListViewFragment {

	private ImageView mImageViewBell;
	private ArrayList<Object> listNotification;

	private ArrayList<Object> getListNotification() {
		if (listNotification == null)
			listNotification = new ArrayList<Object>();
		return listNotification;
	}

	public FragmentNotification() {
	}

	public void setListNotification(List<Notification> listNotification) {
		if (listNotification != null && listNotification.size() > 0)
			getListNotification().addAll(listNotification);
	}

	@Override
	protected void afterSetLayoutID(Bundle savedInstanceState) {
		super.afterSetLayoutID(savedInstanceState);
		mImageViewBell = (ImageView) findViewById(R.id.layout_header_bell);
		mImageViewBell.setVisibility(View.INVISIBLE);
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
	protected void onClick(AdapterView<?> listView, View itemView,
			int position, Object obj) {
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
	}

}

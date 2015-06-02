package com.khoisang.drdigital.ui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.khoisang.drdigital.R;
import com.khoisang.drdigital.adapter.AdapterNotification;
import com.khoisang.drdigital.constant.Event;
import com.khoisang.drdigital.data.Notification;
import com.khoisang.khoisanglibary.ui.ActionEvent;
import com.khoisang.khoisanglibary.ui.ListViewItem;
import com.khoisang.khoisanglibary.ui.fragment.ListViewFragment;

public class FragmentNotification extends ListViewFragment {

	private ImageView mBottomOption1;
	private ImageView mBottomOption2;
	private ImageView mBottomOption3;
	private ImageView mBottomOption4;

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
		getListNotification().clear();
		if (listNotification != null && listNotification.size() > 0)
			getListNotification().addAll(listNotification);
	}

	@Override
	protected void afterSetLayoutID(Bundle savedInstanceState) {
		super.afterSetLayoutID(savedInstanceState);

		mBottomOption1 = (ImageView) findViewById(R.id.layout_bottom_1);
		mBottomOption1.setImageDrawable(getResources().getDrawable(
				R.drawable.support_icon));
		mBottomOption1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				FragmentNotification.this.raiseEvent(new ActionEvent(
						Event.SUPPORT_NON_BACK, null));
			}
		});

		mBottomOption2 = (ImageView) findViewById(R.id.layout_bottom_2);
		mBottomOption2.setImageDrawable(getResources().getDrawable(
				R.drawable.information_icon));
		mBottomOption2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				FragmentNotification.this.raiseEvent(new ActionEvent(
						Event.INFORMATION_NON_BACK, null));
			}
		});

		mBottomOption3 = (ImageView) findViewById(R.id.layout_bottom_3);
		mBottomOption3.setImageDrawable(getResources().getDrawable(
				R.drawable.enquiry_icon));
		mBottomOption3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				FragmentNotification.this.raiseEvent(new ActionEvent(
						Event.ENQUIRY_NON_BACK, null));
			}
		});

		mBottomOption4 = (ImageView) findViewById(R.id.layout_bottom_4);
		mBottomOption4.setImageDrawable(getResources().getDrawable(
				R.drawable.location_icon));
		mBottomOption4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FragmentNotification.this.raiseEvent(new ActionEvent(
						Event.LOCATION_NON_BACK, null));
			}
		});

		mImageViewBell = (ImageView) findViewById(R.id.layout_header_bell);
		mImageViewBell.setVisibility(View.INVISIBLE);

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
		if (getListNotification().size() == 0)
			showToast("Not found", false);
	}

}

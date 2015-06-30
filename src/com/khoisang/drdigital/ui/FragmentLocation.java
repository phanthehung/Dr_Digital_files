package com.khoisang.drdigital.ui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;

import com.itech.drdigital.R;
import com.khoisang.drdigital.adapter.AdapterLocation;
import com.khoisang.drdigital.data.Location;
import com.khoisang.drdigital.ui.BaseDrDigital.ContentType;
import com.khoisang.khoisanglibary.ui.ListViewItem;
import com.khoisang.khoisanglibary.ui.fragment.ListViewFragment;

public class FragmentLocation extends ListViewFragment implements OnClickListener {
	// UI

	private BaseDrDigital mBaseDrDigital;
	private ArrayList<Object> mListLocation;

	private ArrayList<Object> getListLocation() {
		if (mListLocation == null)
			mListLocation = new ArrayList<>();
		return mListLocation;
	}

	public FragmentLocation() {
	}

	public FragmentLocation(List<Location> listLocation, BaseDrDigital baseDrDigital) {
		getListLocation().addAll(listLocation);
		mBaseDrDigital = baseDrDigital;
	}

	@Override
	protected void afterSetLayoutID(Bundle savedInstanceState) {
		super.afterSetLayoutID(savedInstanceState);

		mBaseDrDigital.initView(getView());
		mBaseDrDigital.checkNotification(ContentType.Location);

		mBaseDrDigital.getOption1().setImageDrawable(getResources().getDrawable(R.drawable.support_icon));
		mBaseDrDigital.getOption2().setImageDrawable(getResources().getDrawable(R.drawable.information_icon));
		mBaseDrDigital.getOption3().setImageDrawable(getResources().getDrawable(R.drawable.enquiry_icon));
		mBaseDrDigital.getOption4().setImageDrawable(getResources().getDrawable(R.drawable.notification_icon));

		mBaseDrDigital.getOption1().setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mBaseDrDigital.onClickSupport();
			}
		});
		mBaseDrDigital.getOption2().setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mBaseDrDigital.onClickInformation();

			}
		});
		mBaseDrDigital.getOption3().setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mBaseDrDigital.onClickEnquiry();

			}
		});
		mBaseDrDigital.getOption4().setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mBaseDrDigital.onClickNotification();

			}
		});

		if (getListLocation().size() == 0)
			showToast("Not found", false);
	}

	@Override
	protected int getLayoutID() {
		return R.layout.fragment_location;
	}

	@Override
	protected void reCreateView() {
		mBaseDrDigital.initView(getView());
		mBaseDrDigital.checkNotification(ContentType.Location);
		if (getListLocation().size() == 0)
			showToast("Not found", false);
	}

	public void checkNotification() {

		mBaseDrDigital.checkNotification(ContentType.Location);
	}

	@Override
	protected int getListViewID() {
		return R.id.fragment_location_address_list;
	}

	@Override
	protected int getLayoutItemID() {
		return R.layout.item_location;
	}

	@Override
	protected ArrayList<Object> getDataToListView() {
		return getListLocation();
	}

	@Override
	protected void onClick(AdapterView<?> listView, View itemView, int position, Object obj) {

	}

	@Override
	protected ListViewItem getItemObject() {
		return new AdapterLocation();
	}

	@Override
	public void onClick(View v) {
	}

}

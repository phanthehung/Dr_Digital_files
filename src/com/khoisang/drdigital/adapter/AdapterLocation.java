package com.khoisang.drdigital.adapter;

import java.lang.ref.WeakReference;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.khoisang.drdigital.constant.Event;
import com.khoisang.drdigital.data.Location;
import com.khoisang.drdigital.ui.ActivityMain;
import com.khoisang.drdigital.ui.FragmentLocation;
import com.khoisang.khoisanglibary.ui.ActionEvent;
import com.khoisang.khoisanglibary.ui.ListViewItem;

public class AdapterLocation extends ListViewItem implements OnClickListener {

	// UI
	private TextView _brand;
	private TextView _address;
	private TextView _phone_number;
	private TextView _map;

	private Location mLocation;
	private WeakReference<FragmentLocation> mFragmentLocation;

	@Override
	public void initData(Object own, Object obj, int position) {
		mFragmentLocation = new WeakReference<FragmentLocation>((FragmentLocation) own);
		_map.setOnClickListener(this);
	}

	@Override
	public void setData(Object own, Object obj, int position) {
		mLocation = (Location) obj;

		_brand.setText(mLocation.locationName);
		_address.setText(mLocation.address);
		_phone_number.setText(mLocation.tel);
	}

	@Override
	public void onClick(View v) {
		if (mFragmentLocation != null && mFragmentLocation.get() != null) {
			ActivityMain activityMain = (ActivityMain) mFragmentLocation.get().getActivity();
			activityMain.handleEvent(new ActionEvent(Event.SHOW_MAP, mLocation));
		}
	}

}

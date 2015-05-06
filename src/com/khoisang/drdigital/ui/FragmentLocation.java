package com.khoisang.drdigital.ui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.khoisang.drdigital.R;
import com.khoisang.drdigital.adapter.AdapterLocation;
import com.khoisang.drdigital.constant.Event;
import com.khoisang.drdigital.data.Location;
import com.khoisang.khoisanglibary.ui.ActionEvent;
import com.khoisang.khoisanglibary.ui.ListViewItem;
import com.khoisang.khoisanglibary.ui.fragment.ListViewFragment;

public class FragmentLocation extends ListViewFragment implements
		OnClickListener {
	// UI
	private ImageView mImageViewBell;
	private ImageView mBottomOption1;
	private ImageView mBottomOption2;
	private ImageView mBottomOption3;

	private ArrayList<Object> mListLocation;

	private ArrayList<Object> getListLocation() {
		if (mListLocation == null)
			mListLocation = new ArrayList<>();
		return mListLocation;
	}

	public FragmentLocation() {
	}

	public FragmentLocation(List<Location> listLocation) {
		getListLocation().addAll(listLocation);
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
				FragmentLocation.this.raiseEvent(new ActionEvent(
						Event.SUPPORT_NON_BACK, null));
			}
		});

		mBottomOption2 = (ImageView) findViewById(R.id.layout_bottom_2);
		mBottomOption2.setImageDrawable(getResources().getDrawable(
				R.drawable.information_icon));
		mBottomOption2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				FragmentLocation.this.raiseEvent(new ActionEvent(
						Event.INFORMATION_NON_BACK, null));
			}
		});

		mBottomOption3 = (ImageView) findViewById(R.id.layout_bottom_3);
		mBottomOption3.setImageDrawable(getResources().getDrawable(
				R.drawable.enquiry_icon));
		mBottomOption3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				FragmentLocation.this.raiseEvent(new ActionEvent(
						Event.ENQUIRY_NON_BACK, null));
			}
		});

		mImageViewBell = (ImageView) findViewById(R.id.layout_header_bell);
		mImageViewBell.setOnClickListener(this);

		if (getListLocation().size() == 0)
			showToast("Not found", false);
	}

	@Override
	protected int getLayoutID() {
		return R.layout.fragment_location;
	}

	@Override
	protected void reCreateView() {
		if (getListLocation().size() == 0)
			showToast("Not found", false);
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
	protected void onClick(AdapterView<?> listView, View itemView,
			int position, Object obj) {

	}

	@Override
	protected ListViewItem getItemObject() {
		return new AdapterLocation();
	}

	@Override
	public void onClick(View v) {
		if (v == mImageViewBell) {
			raiseEvent(new ActionEvent(6, null));
		}
	}

}

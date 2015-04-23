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
import com.khoisang.drdigital.data.Location;
import com.khoisang.khoisanglibary.ui.ActionEvent;
import com.khoisang.khoisanglibary.ui.ListViewItem;
import com.khoisang.khoisanglibary.ui.fragment.ListViewFragment;

public class FragmentLocation extends ListViewFragment implements
		OnClickListener {

	private ImageView mImageViewBell;

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
		mImageViewBell = (ImageView) findViewById(R.id.layout_header_bell);
		mImageViewBell.setOnClickListener(this);
	}

	@Override
	protected int getLayoutID() {
		return R.layout.fragment_location;
	}

	@Override
	protected void reCreateView() {
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

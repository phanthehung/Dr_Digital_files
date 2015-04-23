package com.khoisang.drdigital.ui;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.widget.AdapterView;

import com.khoisang.drdigital.R;
import com.khoisang.drdigital.adapter.AdapterLocation;
import com.khoisang.drdigital.data.Location;
import com.khoisang.khoisanglibary.ui.ListViewItem;
import com.khoisang.khoisanglibary.ui.fragment.ListViewFragment;

public class FragmentLocation extends ListViewFragment {

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

}

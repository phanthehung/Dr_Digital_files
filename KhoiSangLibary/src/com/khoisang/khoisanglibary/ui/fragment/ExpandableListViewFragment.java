package com.khoisang.khoisanglibary.ui.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.khoisang.khoisanglibary.ui.ListViewItem;

public abstract class ExpandableListViewFragment extends BaseFragment implements
		OnItemClickListener {

	protected BaseExpandableListAdapter mAdapter;
	protected ExpandableListView mListView;
	protected ArrayList<Object> mData;

	@Override
	protected void afterSetLayoutID(Bundle savedInstanceState) {
		mListView = (ExpandableListView) findViewById(getListViewID());
		mAdapter = getAdapter();
		mData = getDataToListView();
		if (mData == null) {
			mData = new ArrayList<Object>();
		}

		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
	}

	protected abstract int getListViewID();

	protected abstract int getLayoutItemID();

	protected abstract ArrayList<Object> getDataToListView();

	protected abstract void onClick(AdapterView<?> listView, View itemView,
			int position, Object obj);

	protected abstract ListViewItem getItemObject();

	protected abstract BaseExpandableListAdapter getAdapter();

	protected ListView getListview() {
		return mListView;
	}

	@Override
	public void onItemClick(AdapterView<?> listView, View itemView,
			int position, long id) {
		onClick(listView, itemView, position, mData.get(position));
	}

} // End class ListFragment

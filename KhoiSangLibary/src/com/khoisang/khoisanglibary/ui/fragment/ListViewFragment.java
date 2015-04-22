package com.khoisang.khoisanglibary.ui.fragment;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import com.khoisang.khoisanglibary.dev.MyLog;
import com.khoisang.khoisanglibary.ui.ListViewItem;
import com.khoisang.khoisanglibary.ui.UICore;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public abstract class ListViewFragment extends BaseFragment implements
		OnItemClickListener {

	protected ListViewAdapter mAdapter;
	protected ListView mListView;
	private Class<?> mHolderClass;
	protected ArrayList<Object> mData;

	@Override
	protected void afterSetLayoutID(Bundle savedInstanceState) {
		mListView = (ListView) findViewById(getListViewID());
		mAdapter = new ListViewAdapter();
		mData = getDataToListView();
		if (mData == null) {
			mData = new ArrayList<Object>();
		}
		mHolderClass = getItemObject().getClass();

		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
	}

	protected abstract int getListViewID();

	protected abstract int getLayoutItemID();

	protected abstract ArrayList<Object> getDataToListView();

	protected abstract void onClick(AdapterView<?> listView, View itemView,
			int position, Object obj);

	protected abstract ListViewItem getItemObject();

	protected ListView getListview() {
		return mListView;
	}

	@Override
	public void onItemClick(AdapterView<?> listView, View itemView,
			int position, long id) {
		onClick(listView, itemView, position, mData.get(position));
	}

	public class ListViewAdapter extends ArrayAdapter<Object> {
		public ListViewAdapter() {
			super(getActivity(), getLayoutItemID());
		}

		@Override
		public Object getItem(int position) {
			if (mData != null)
				return mData.get(position);
			else
				return null;
		}

		@Override
		public int getCount() {
			if (mData != null)
				return mData.size();
			else
				return 0;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				int itemLayoutId = getLayoutItemID();
				convertView = getActivity().getLayoutInflater().inflate(
						itemLayoutId, null);

				try {
					Constructor<?> constructor = mHolderClass.getConstructor();
					ListViewItem item = (ListViewItem) constructor
							.newInstance(new Object[] {});
					UICore.autoFindID(getActivity(), item, convertView,
							itemLayoutId);
					item.initData(ListViewFragment.this, mData.get(position),
							position);
					convertView.setTag(item);
				} catch (IllegalArgumentException e) {
					MyLog.e(getClass(), e);
				} catch (java.lang.InstantiationException e) {
					MyLog.e(getClass(), e);
				} catch (IllegalAccessException e) {
					MyLog.e(getClass(), e);
				} catch (InvocationTargetException e) {
					MyLog.e(getClass(), e);
				} catch (NoSuchMethodException e) {
					MyLog.e(getClass(), e);
				}
			}

			ListViewItem objectHolder = (ListViewItem) convertView.getTag();
			objectHolder.setData(ListViewFragment.this, mData.get(position),
					position);
			return convertView;
		}
	} // End class mListAdapter

} // End class ListFragment

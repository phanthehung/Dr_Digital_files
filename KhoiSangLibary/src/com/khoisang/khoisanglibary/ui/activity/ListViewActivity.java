package com.khoisang.khoisanglibary.ui.activity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.khoisang.khoisanglibary.dev.MyLog;
import com.khoisang.khoisanglibary.ui.ListViewItem;
import com.khoisang.khoisanglibary.ui.UICore;

public abstract class ListViewActivity extends BaseActivity implements
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
			super(ListViewActivity.this, getLayoutItemID());
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
				convertView = ListViewActivity.this.getLayoutInflater()
						.inflate(itemLayoutId, null);

				try {
					Constructor<?> constructor = mHolderClass.getConstructor();
					ListViewItem item = (ListViewItem) constructor
							.newInstance(new Object[] {});
					UICore.autoFindID(ListViewActivity.this, item, convertView,
							itemLayoutId);
					item.initData(ListViewActivity.this, mData.get(position),
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
			objectHolder.setData(ListViewActivity.this, mData.get(position),
					position);
			return convertView;
		}
	} // End Class
} // End Class

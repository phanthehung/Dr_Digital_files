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
import android.widget.ImageView;
import android.widget.ListView;

import com.khoisang.khoisanglibary.R;
import com.khoisang.khoisanglibary.dev.MyLog;
import com.khoisang.khoisanglibary.ui.ListViewItem;
import com.khoisang.khoisanglibary.ui.UICore;

public abstract class NavigationDrawerMenuListViewActivity extends
		NavigationDrawerMenuSingleActivity implements OnItemClickListener {
	public ListViewAdapter mAdapter;
	private ListView mListView;
	private Class<?> mHolderClass;
	private ArrayList<Object> mData;
	private ImageView mMenuLogo;

	@Override
	public void afterSetLayoutID(Bundle savedInstanceState) {
		super.afterSetLayoutID(savedInstanceState);
		mListView = (ListView) findViewById(getListViewID());
		mAdapter = new ListViewAdapter();
		mData = getDataToMenuListView();
		mHolderClass = getItemMenuObject().getClass();

		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);

		int menuLogoId = getLogoMenuID();
		if (menuLogoId != 0) {
			mMenuLogo = (ImageView) findViewById(R.id.activity_navigation_drawer_menu_listview_menu_logo);
			mMenuLogo.setImageResource(menuLogoId);
		}
	}

	@Override
	public int getLayoutLeftMenuID() {
		return R.id.activity_navigation_drawer_menu_listview_layout_menu;
	}

	public int getListViewID() {
		return R.id.activity_navigation_drawer_menu_listview_menu;
	}

	@Override
	public int getLayoutID() {
		return R.layout.activity_navigation_drawer_menu_listview;
	};

	@Override
	protected int getLayoutContentID() {
		return R.id.activity_navigation_drawer_menu_listview_content;
	}

	@Override
	protected int getLayoutRootID() {
		return R.id.activity_navigation_drawer_menu_listview_root;
	}

	@Override
	public void onItemClick(AdapterView<?> listView, View itemView,
			int position, long id) {
		onMenuClick(listView, itemView, position, mData.get(position));
	}

	private class ListViewAdapter extends ArrayAdapter<Object> {
		public ListViewAdapter() {
			super(NavigationDrawerMenuListViewActivity.this,
					getLayoutMenuItemID());
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
				int itemLayoutId = NavigationDrawerMenuListViewActivity.this
						.getLayoutMenuItemID();
				convertView = NavigationDrawerMenuListViewActivity.this
						.getLayoutInflater().inflate(itemLayoutId, null);

				try {
					Constructor<?> constructor = mHolderClass.getConstructor();
					ListViewItem item = (ListViewItem) constructor
							.newInstance(new Object[] {});
					UICore.autoFindID(
							NavigationDrawerMenuListViewActivity.this, item,
							convertView, itemLayoutId);
					item.initData(NavigationDrawerMenuListViewActivity.this,
							mData.get(position), position);
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
			objectHolder.setData(NavigationDrawerMenuListViewActivity.this,
					mData.get(position), position);
			return convertView;
		}
	} // End Class

	protected abstract int getLogoMenuID();

	protected abstract int getLayoutMenuItemID();

	protected abstract ListViewItem getItemMenuObject();

	protected abstract ArrayList<Object> getDataToMenuListView();

	protected abstract void onMenuClick(AdapterView<?> listView, View itemView,
			int position, Object obj);
}

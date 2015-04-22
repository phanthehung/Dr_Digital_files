package com.khoisang.khoisanglibary.ui.extend.widget;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class MyPagerAdapter extends PagerAdapter {

	public boolean checkSwipe() {
		float totalPageWidth = 0;
		for (int i = 0; i < getCount(); i++) {
			totalPageWidth += getPageWidth(i);
		}
		if (totalPageWidth <= 1) {
			return false;
		}

		return true;
	}

	@Override
	public int getCount() {
		return 0;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}
}

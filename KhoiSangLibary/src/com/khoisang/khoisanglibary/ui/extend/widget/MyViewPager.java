package com.khoisang.khoisanglibary.ui.extend.widget;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MyViewPager extends ViewPager {

	private boolean enabled;
	private WeakReference<MyPagerAdapter> mPagerAdapter;

	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.enabled = true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (this.enabled) {
			return super.onTouchEvent(event);
		}

		return false;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		if (this.enabled) {
			return super.onInterceptTouchEvent(event);
		}

		return false;
	}

	public void setPagingEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void setAdapter(MyPagerAdapter adapter) {
		super.setAdapter(adapter);

		if (adapter != null) {
			mPagerAdapter = new WeakReference<MyPagerAdapter>(adapter);
			setPagingEnabled(adapter.checkSwipe());
		}
	} // End if

	public void notifyDataSetChanged() {
		if (mPagerAdapter != null) {
			mPagerAdapter.get().notifyDataSetChanged();
			setPagingEnabled(mPagerAdapter.get().checkSwipe());
		}
	}
}

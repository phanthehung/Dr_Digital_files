package com.khoisang.khoisanglibary.ui.activity;

import android.view.ViewGroup;

import com.khoisang.khoisanglibary.R;

public abstract class NavigationDrawerMenuSingleActivity extends
		NavigationDrawerMenuActivity {

	private ViewGroup mViewLeftMenu;
	private ViewGroup mViewContent;

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public int getLayoutID() {
		return R.layout.activity_navigation_drawer_menu_single;
	}

	@Override
	protected int getLayoutContentID() {
		return R.id.activity_navigation_drawer_menu_single_content;
	}

	@Override
	protected int getLayoutRootID() {
		return R.id.activity_navigation_drawer_menu_single_root;
	}

	protected int getLayoutLeftMenuID() {
		return R.id.activity_navigation_drawer_menu_single_leftmenu;
	}

	protected ViewGroup getViewLeftMenu() {
		if (mViewLeftMenu == null) {
			mViewLeftMenu = (ViewGroup) findViewById(getLayoutLeftMenuID());
		}
		return mViewLeftMenu;
	}

	protected ViewGroup getViewContent() {
		if (mViewContent == null) {
			mViewContent = (ViewGroup) findViewById(getLayoutContentID());
		}
		return mViewContent;
	}
} // End Class

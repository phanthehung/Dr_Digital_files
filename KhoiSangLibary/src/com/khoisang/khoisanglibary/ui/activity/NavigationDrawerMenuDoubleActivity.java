package com.khoisang.khoisanglibary.ui.activity;

import com.khoisang.khoisanglibary.R;

public abstract class NavigationDrawerMenuDoubleActivity extends
		NavigationDrawerMenuActivity {

	@Override
	public int getLayoutID() {
		return R.layout.activity_navigation_drawer_menu_double;
	};

	@Override
	protected int getLayoutContentID() {
		return R.id.activity_navigation_drawer_menu_double_content;
	}

	@Override
	protected int getLayoutRootID() {
		return R.id.activity_navigation_drawer_menu_double_root;
	}
}

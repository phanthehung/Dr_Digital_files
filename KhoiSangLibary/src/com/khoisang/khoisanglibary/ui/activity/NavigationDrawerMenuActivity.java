package com.khoisang.khoisanglibary.ui.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.khoisang.khoisanglibary.R;

public abstract class NavigationDrawerMenuActivity extends BaseActivity {
	public DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mActionBar;

	@Override
	public void afterSetLayoutID(Bundle savedInstanceState) {
		mDrawerLayout = (DrawerLayout) findViewById(getLayoutRootID());

		mActionBar = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.string.khoisang_menu_opening, R.string.khoisang_menu_closing) {

			public void onDrawerClosed(View drawerView) {
				supportInvalidateOptionsMenu();
				NavigationDrawerMenuActivity.this.onDrawerClosed(drawerView);

			}

			public void onDrawerOpened(View drawerView) {
				supportInvalidateOptionsMenu();
				NavigationDrawerMenuActivity.this.onDrawerOpened(drawerView);
			}

			@Override
			public void onDrawerSlide(View drawerView, float slideOffset) {
				super.onDrawerSlide(drawerView, slideOffset);
				NavigationDrawerMenuActivity.this.onDrawerSlide(drawerView,
						slideOffset);
			}

			@Override
			public void onDrawerStateChanged(int newState) {
				super.onDrawerStateChanged(newState);
				NavigationDrawerMenuActivity.this
						.onDrawerStateChanged(newState);
			}

		};

		mDrawerLayout.setDrawerListener(mActionBar);
	}

	protected abstract int getDrawableMenuIcon();

	protected abstract int getLayoutContentID();

	protected abstract int getLayoutRootID();

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mActionBar.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mActionBar.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	abstract public void onDrawerClosed(View drawerView);

	abstract public void onDrawerOpened(View drawerView);

	abstract public void onDrawerSlide(View drawerView, float slideOffset);

	abstract public void onDrawerStateChanged(int newState);
} // End Class

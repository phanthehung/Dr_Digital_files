package com.khoisang.drdigital.ui;

import com.khoisang.khoisanglibary.ui.ActionEvent;
import com.khoisang.khoisanglibary.ui.activity.BaseActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class ActivityMain extends BaseActivity implements OnClickListener {

	private FragmentHome mFragmentHome;
	private FragmentSupport mFragmentsupport;

	@Override
	protected int getLayoutID() {
		// TODO Auto-generated method stub
		return R.layout.activity_main;
	}
	@Override
	public void handleEvent(ActionEvent actionEvent) {
		// TODO Auto-generated method stub

	}
	@Override
	protected void beforeSetLayoutID(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		initFragments();

	}
	private void initFragments() {
		// TODO Auto-generated method stub
		mFragmentHome = new FragmentHome();
		mFragmentsupport= new FragmentSupport();
	}
	@Override
	protected void afterSetLayoutID(Bundle savedInstanceState) {

	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	}

}

package com.khoisang.drdigital.ui;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;

import com.khoisang.drdigital.R;
import com.khoisang.khoisanglibary.ui.ActionEvent;
import com.khoisang.khoisanglibary.ui.activity.BaseActivity;

public class ActivityMain extends BaseActivity implements OnClickListener {

	private FragmentHome mFragmentHome;
	private FragmentSupport mFragmentsupport;

	@Override
	protected int getLayoutID() {
		return R.layout.activity_main;
	}

	@Override
	public void handleEvent(ActionEvent actionEvent) {

	}

	@Override
	protected void beforeSetLayoutID(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		initFragments();
	}

	private void initFragments() {
		mFragmentHome = new FragmentHome();
		mFragmentsupport = new FragmentSupport();
	}

	@Override
	protected void afterSetLayoutID(Bundle savedInstanceState) {
		addFragment(mFragmentHome, R.id.activity_main_content);
	}

	@Override
	public void onClick(View v) {
	}

}

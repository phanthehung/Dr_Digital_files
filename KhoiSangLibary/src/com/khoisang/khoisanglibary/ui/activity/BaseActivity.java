package com.khoisang.khoisanglibary.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.khoisang.khoisanglibary.ui.ActionEvent;
import com.khoisang.khoisanglibary.ui.UICore;
import com.khoisang.khoisanglibary.ui.extend.bitmap.ImageHttpLoader;

public abstract class BaseActivity extends ActionBarActivity {
	public enum ActivityState {
		onCreate, onStart, onRestart, onResume, onPause, onStop, onDestroy
	}

	private static ImageHttpLoader mImageLoader;
	private ActivityState mActivityState;

	public ActivityState getActivityState() {
		return mActivityState;
	}

	private void setActivityState(ActivityState activityState) {
		mActivityState = activityState;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setActivityState(ActivityState.onCreate);
		beforeSetLayoutID(savedInstanceState);
		//
		super.onCreate(savedInstanceState);
		//
		int layoutId = getLayoutID();
		setContentView(layoutId);
		autoFindID(this, null, layoutId);
		//
		afterSetLayoutID(savedInstanceState);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		setActivityState(ActivityState.onRestart);
	}

	@Override
	protected void onStart() {
		super.onStart();
		setActivityState(ActivityState.onStart);
	}

	@Override
	protected void onResume() {
		super.onResume();
		setActivityState(ActivityState.onResume);
	}

	@Override
	protected void onPause() {
		super.onPause();
		setActivityState(ActivityState.onPause);
	}

	@Override
	protected void onStop() {
		super.onStop();
		setActivityState(ActivityState.onStop);
	}

	@Override
	protected void onDestroy() {
		setActivityState(ActivityState.onDestroy);
		super.onDestroy();
	}

	protected abstract int getLayoutID();

	public abstract void handleEvent(ActionEvent actionEvent);

	protected abstract void beforeSetLayoutID(Bundle savedInstanceState);

	protected abstract void afterSetLayoutID(Bundle savedInstanceState);

	protected boolean hideKeyboard() {
		return UICore.hideKeyboard(this);
	}

	protected void showKeyboard(View view) {
		UICore.showKeyboard(this, view);
	}

	protected String getResourceNameByID(int id) {
		return UICore.getResourceNameByID(this, id);
	}

	// 0 : Not Found
	protected int getResourceIDByName(String name, String type) {
		return UICore.getResourceIDByName(this, name, type);
	}

	public void autoFindID(Object obj, View baseView, int layoutID) {
		UICore.autoFindID(this, obj, baseView, layoutID);
	}

	protected boolean addFragment(Fragment fragment, int id) {
		if (fragment != null) {
			if (fragment.isAdded() == false) {
				FragmentManager fragmentManager = getSupportFragmentManager();
				FragmentTransaction fragmentTransacton = fragmentManager
						.beginTransaction();
				fragmentTransacton.add(id, fragment);
				fragmentTransacton.commitAllowingStateLoss();
				fragmentManager.executePendingTransactions();
				return true;
			}
		}
		return false;
	}

	protected void replaceFragment(Fragment fragment, int id,
			boolean addToBackStack) {
		if (fragment != null) {
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction fragmentTransacton = fragmentManager
					.beginTransaction();
			fragmentTransacton.replace(id, fragment);
			if (addToBackStack == true) {
				fragmentTransacton.addToBackStack(null);
			}
			fragmentTransacton.commitAllowingStateLoss();
			fragmentManager.executePendingTransactions();
		}
	}

	protected void removeFragment(Fragment fragment) {
		if (fragment != null) {
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction fragmentTransacton = fragmentManager
					.beginTransaction();
			fragmentTransacton.remove(fragment);
			fragmentTransacton.commitAllowingStateLoss();
			fragmentManager.executePendingTransactions();
		}
	}

	protected void clearFragmentBackStack() {
		FragmentManager fragmentManager = getSupportFragmentManager();
		int counter = fragmentManager.getBackStackEntryCount();
		if (counter > 0) {

			FragmentTransaction fragmentTransacton = fragmentManager
					.beginTransaction();
			for (int i = 0; i < counter; i++) {
				fragmentManager.popBackStack();
			}
			fragmentTransacton.commitAllowingStateLoss();
		}

		fragmentManager.executePendingTransactions();
	}

	protected void popBackStackFragment() {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransacton = fragmentManager
				.beginTransaction();
		fragmentManager.popBackStack();
		fragmentTransacton.commitAllowingStateLoss();
		fragmentManager.executePendingTransactions();
	}

	protected void raiseEvent(ActionEvent actionEvent) {
		if (actionEvent.own == null)
			actionEvent.own = this;
		handleEvent(actionEvent);
	}

	public ImageHttpLoader getImageLoader(Bitmap bitmap) {
		if (mImageLoader == null) {
			mImageLoader = new ImageHttpLoader(this, bitmap);
		}
		return mImageLoader;
	}

	protected String getTag() {
		return this.getClass().getSimpleName();
	}

	public void showIndicator(String message, Boolean cancelable) {
		UICore uiCore = UICore.getInstance();
		uiCore.showIndicator(this, message, cancelable);
	}

	public void hideIndicator() {
		UICore uiCore = UICore.getInstance();
		uiCore.hideIndicator();
	}

	public void showToast(String message, Boolean longTime) {
		UICore uiCore = UICore.getInstance();
		uiCore.showToast(this, message, longTime);
	}

	public AlertDialog getDialog(Activity activity, String message,
			Boolean cancelable, DialogInterface.OnClickListener positiveButton,
			String positiveContent,
			DialogInterface.OnClickListener negativeButton,
			String negativeContent) {
		UICore uiCore = UICore.getInstance();
		return uiCore.getDialog(activity, message, cancelable, positiveButton,
				positiveContent);
	}

	public AlertDialog getDialog(Activity activity, String message,
			Boolean cancelable, DialogInterface.OnClickListener positiveButton,
			String positiveContent) {
		UICore uiCore = UICore.getInstance();
		return uiCore.getDialog(activity, message, cancelable, positiveButton,
				positiveContent);
	}

	public int getScreenWidth() {
		UICore uiCore = UICore.getInstance();
		return uiCore.getScreenWidth(this);
	}

	public int getScreenHeight() {
		UICore uiCore = UICore.getInstance();
		return uiCore.getScreenHeight(this);
	}
} // End class

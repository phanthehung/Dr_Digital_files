package com.khoisang.drdigital.ui;

import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.khoisang.drdigital.R;
import com.khoisang.drdigital.data.Location;
import com.khoisang.khoisanglibary.dev.DebugLog;
import com.khoisang.khoisanglibary.ui.ActionEvent;
import com.khoisang.khoisanglibary.ui.activity.BaseActivity;

public class ActivityMain extends BaseActivity implements OnClickListener {

	public static final String PROJECT_NUMBER_ID = "660565524128";
	private static final String PROPERTY_REG_ID = "registration_id";
	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

	private FragmentHome mFragmentHome;
	private FragmentSupport mFragmentSupport;
	private FragmentEnquery mFragmentEnquery;
	private FragmentLocation mFragmentLocation;
	private FragmentInformation mFragmentInformation;

	private GoogleCloudMessaging mGoogleCloudMessage;
	private String mRegId;

	@Override
	protected int getLayoutID() {
		return R.layout.activity_main;
	}

	@Override
	public void handleEvent(ActionEvent actionEvent) {
		switch (actionEvent.eventID) {
		case 1:
			replaceFragment(mFragmentSupport, R.id.activity_main_content, true);
			break;
		case 2:
			replaceFragment(mFragmentEnquery, R.id.activity_main_content, true);
			break;
		case 3:
			replaceFragment(mFragmentLocation, R.id.activity_main_content, true);
			break;
		case 4:
			replaceFragment(mFragmentInformation, R.id.activity_main_content,
					true);
			break;
		case 5:
			Intent intent = new Intent(this, ActivityGoogleMap.class);
			Location location = (Location) actionEvent.parameters;
			ActivityGoogleMap.Location = location;
			startActivity(intent);
		default:
			break;
		}
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
		mFragmentSupport = new FragmentSupport();
		mFragmentEnquery = new FragmentEnquery();
		mFragmentLocation = new FragmentLocation();
		mFragmentInformation = new FragmentInformation();
	}

	@Override
	protected void afterSetLayoutID(Bundle savedInstanceState) {
		addFragment(mFragmentHome, R.id.activity_main_content);
		if (checkPlayServices() == true) {
			mGoogleCloudMessage = GoogleCloudMessaging
					.getInstance(getApplicationContext());
			mRegId = getRegistrationId(getApplicationContext());
			if (mRegId.equalsIgnoreCase("") == true) {
				registerInBackground();
			}
		}
	}

	@Override
	public void onClick(View v) {
	}

	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, this,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			}
			return false;
		}
		return true;
	}

	private void storeRegistrationId(Context context, String regId) {
		SharedPreferences prefs = getPreferences(context);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(PROPERTY_REG_ID, regId);
		editor.commit();
	}

	private String getRegistrationId(Context context) {
		SharedPreferences prefs = getPreferences(context);
		String registrationId = prefs.getString(PROPERTY_REG_ID, "");
		return registrationId;
	}

	private SharedPreferences getPreferences(Context context) {
		return getSharedPreferences(this.getClass().getSimpleName(),
				Context.MODE_PRIVATE);
	}

	private void registerInBackground() {
		AsyncTask<Void, Object, Object> asyncTask = new AsyncTask<Void, Object, Object>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (mGoogleCloudMessage == null) {
						mGoogleCloudMessage = GoogleCloudMessaging
								.getInstance(ActivityMain.this);
					}
					mRegId = mGoogleCloudMessage.register(PROJECT_NUMBER_ID);
					storeRegistrationId(ActivityMain.this, mRegId);
				} catch (IOException ex) {
					DebugLog.e(getTag(), ex);
				}
				return msg;
			}
		};
		asyncTask.execute();
	}
}

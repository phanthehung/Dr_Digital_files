package com.khoisang.drdigital.ui;

import java.net.UnknownHostException;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.khoisang.drdigital.R;
import com.khoisang.drdigital.api.ApiManager;
import com.khoisang.drdigital.api.structure.InputGetData;
import com.khoisang.drdigital.api.structure.OutputGetData;
import com.khoisang.drdigital.constant.Event;
import com.khoisang.drdigital.data.Location;
import com.khoisang.drdigital.data.Notification;
import com.khoisang.drdigital.util.History;
import com.khoisang.khoisanglibary.dev.DebugLog;
import com.khoisang.khoisanglibary.dev.DebugLog.DebugLogListerner;
import com.khoisang.khoisanglibary.dev.ExceptionToMessage;
import com.khoisang.khoisanglibary.network.HttpHandler;
import com.khoisang.khoisanglibary.network.HttpResult;
import com.khoisang.khoisanglibary.ui.ActionEvent;
import com.khoisang.khoisanglibary.ui.activity.BaseActivity;
import com.khoisang.khoisanglibary.util.NetwordUtil;

public class ActivityMain extends BaseActivity implements OnClickListener,
HttpHandler, DebugLogListerner {

	public static final String PROJECT_NUMBER_ID = "660565524128";
	private static final String PROPERTY_REG_ID = "registration_id";
	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

	private FragmentHome mFragmentHome;
	private FragmentSupport mFragmentSupport;
	private FragmentEnquiry mFragmentEnquiry;
	private FragmentLocation mFragmentLocation;
	private FragmentInformation mFragmentInformation;
	private FragmentNotification mFragmentNotification;

	private int mNotificationCounter = 0;
	private GoogleCloudMessaging mGoogleCloudMessage;
	private String mRegId;

	public FragmentHome getmFragmentHome() {
		return mFragmentHome;
	}

	public void setmFragmentHome(FragmentHome mFragmentHome) {
		this.mFragmentHome = mFragmentHome;
	}

	public FragmentSupport getmFragmentSupport() {
		return mFragmentSupport;
	}

	public void setmFragmentSupport(FragmentSupport mFragmentSupport) {
		this.mFragmentSupport = mFragmentSupport;
	}

	public FragmentEnquiry getmFragmentEnquiry() {
		return mFragmentEnquiry;
	}

	public void setmFragmentEnquiry(FragmentEnquiry mFragmentEnquiry) {
		this.mFragmentEnquiry = mFragmentEnquiry;
	}

	public FragmentLocation getmFragmentLocation() {
		return mFragmentLocation;
	}

	public void setmFragmentLocation(FragmentLocation mFragmentLocation) {
		this.mFragmentLocation = mFragmentLocation;
	}

	public FragmentInformation getmFragmentInformation() {
		return mFragmentInformation;
	}

	public void setmFragmentInformation(FragmentInformation mFragmentInformation) {
		this.mFragmentInformation = mFragmentInformation;
	}

	public FragmentNotification getmFragmentNotification() {
		return mFragmentNotification;
	}

	public void setmFragmentNotification(
			FragmentNotification mFragmentNotification) {
		this.mFragmentNotification = mFragmentNotification;
	}

	@Override
	protected int getLayoutID() {
		return R.layout.activity_main;
	}

	public int getmNotificationCounter() {
		return mNotificationCounter;
	}

	public void setmNotificationCounter(int mNotificationCounter) {
		this.mNotificationCounter = mNotificationCounter;
	}

	@Override
	public void handleEvent(ActionEvent actionEvent) {
		switch (actionEvent.eventID) {
		case 1:
			replaceFragment(mFragmentSupport, R.id.activity_main_content, true);
			break;
		case 2:
			replaceFragment(mFragmentEnquiry, R.id.activity_main_content, true);
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
			break;
		case 6:
			DrDigitalApplication.counter = 0;
			List<Notification> listNotifications = null;
			try {
				listNotifications = History.get(getApplication());
			} catch (Exception ex) {
				handleError(ex);
			}
			mFragmentNotification.setListNotification(listNotifications);
			replaceFragment(mFragmentNotification, R.id.activity_main_content,
					true);

			break;
		case Event.SUPPORT_NON_BACK:
			clearFragmentBackStack();
			replaceFragment(mFragmentSupport, R.id.activity_main_content, false);
			break;
		case Event.INFORMATION_NON_BACK:
			clearFragmentBackStack();
			replaceFragment(mFragmentInformation, R.id.activity_main_content,
					false);
			break;
		case Event.ENQUIRY_NON_BACK:
			clearFragmentBackStack();
			replaceFragment(mFragmentEnquiry, R.id.activity_main_content, false);
			break;
		case Event.LOCATION_NON_BACK:
			clearFragmentBackStack();
			replaceFragment(mFragmentLocation, R.id.activity_main_content,
					false);
			break;
		case Event.NOTIFICATION_NON_BACK:
			DrDigitalApplication.counter = 0;
			List<Notification> listNotifications_non_back = null;
			try {
				listNotifications_non_back = History.get(getApplication());
			} catch (Exception ex) {
				handleError(ex);
			}
			mFragmentNotification.setListNotification(listNotifications_non_back);
			replaceFragment(mFragmentNotification, R.id.activity_main_content,
					true);

			break;
		default:
			break;
		}
	}

	@Override
	protected void beforeSetLayoutID(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	@Override
	protected void afterSetLayoutID(Bundle savedInstanceState) {
		DebugLog.setListerner(this);
		mFragmentNotification = new FragmentNotification();
		mFragmentHome = new FragmentHome();
		addFragment(mFragmentHome, R.id.activity_main_content);

		if (NetwordUtil.isNetworkAvailable(this) == false) {
			handleError(new UnknownHostException());
			return;
		}

		if (checkPlayServices() == true) {
			mGoogleCloudMessage = GoogleCloudMessaging
					.getInstance(getApplicationContext());
			mRegId = getRegistrationId(getApplicationContext());
			if (mRegId.equalsIgnoreCase("") == true) {
				registerInBackground();
			} else {
				try {
					callFirstApi(mRegId);
				} catch (Exception ex) {
					handleError(ex);
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
	}

	@Override
	protected void onNewIntent(Intent intent) {
		processIntent(intent);
	};

	private void processIntent(Intent intent) {
		if (intent.hasExtra(ServiceIntentGCM.KEY) == true) {
			handleEvent(new ActionEvent(6, null));
		}
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

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							try {
								callFirstApi(mRegId);
							} catch (Exception ex) {
								handleError(ex);
							}
						}
					});
				} catch (Exception ex) {
					handleError(ex);
				}
				return msg;
			}
		};
		asyncTask.execute();
	}

	private void callFirstApi(final String deviceId) throws Exception {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				showIndicator("loading", false);
				InputGetData inputGetData = new InputGetData();
				inputGetData.deviceID = deviceId;
				ApiManager.getData(inputGetData, ActivityMain.this);
			}
		}, 100);
	}

	private void handleError(final Exception ex) {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				DebugLog.e(ActivityMain.this.getTag(), ex);
				String message = ExceptionToMessage.getMessage(
						ActivityMain.this.getResources(), ex);
				new AlertDialog.Builder(ActivityMain.this)
				.setTitle("Error")
				.setMessage(message)
				.setPositiveButton(android.R.string.yes,
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int which) {
						dialog.dismiss();
						ActivityMain.this.finish();
					}
				}).show();

			}
		}, 100);
	}

	@Override
	public void prePrequest(int ID, int Name, Object holder) {
	}

	@Override
	public void handleException(int ID, int Name, final Exception ex,
			Object holder) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				hideIndicator();
				handleError(ex);
			}
		});
	}

	@Override
	public void handleCancel(int ID, int Name, Object holder) {
		hideIndicator();
	}

	@Override
	public void handleResult(int ID, int Name, HttpResult httpResult,
			String bodyString, Object holder) {
		DebugLog.i(getTag(), bodyString);
		try {
			hideIndicator();
			OutputGetData outputGetData = new Gson().fromJson(bodyString,
					OutputGetData.class);
			mFragmentSupport = new FragmentSupport(outputGetData.support);
			mFragmentEnquiry = new FragmentEnquiry(outputGetData.enquiry);
			mFragmentLocation = new FragmentLocation(outputGetData.location);
			mFragmentInformation = new FragmentInformation(outputGetData.info);
			//
			processIntent(getIntent());
		} catch (Exception ex) {
			handleError(ex);
		}
	}

	@Override
	public void debugLogHandlerError(Exception ex) {
	}
}

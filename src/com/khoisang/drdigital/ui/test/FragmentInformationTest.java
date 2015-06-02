package com.khoisang.drdigital.ui.test;

import android.os.AsyncTask;
import android.test.ActivityInstrumentationTestCase2;
import android.webkit.WebView;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.khoisang.drdigital.R;
import com.khoisang.drdigital.api.ApiManager;
import com.khoisang.drdigital.api.structure.InputGetData;
import com.khoisang.drdigital.ui.ActivityMain;
import com.khoisang.drdigital.ui.FragmentInformation;

public class FragmentInformationTest extends ActivityInstrumentationTestCase2 {
	private FragmentInformation fragmentInformation;
	private ActivityMain activity;
	private GoogleCloudMessaging mGoogleCloudMessage;
	String mRegId;

	public static final String PROJECT_NUMBER_ID = "660565524128";
	private static final String PROPERTY_REG_ID = "registration_id";
	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

	public FragmentInformationTest() {
		super(ActivityMain.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		activity = (ActivityMain) getActivity();
		mGoogleCloudMessage = GoogleCloudMessaging.getInstance(activity.getApplicationContext());
		registerInBackground();
		Thread.sleep(3000);
		fragmentInformation = activity.getmFragmentInformation();
	}

	public void testPrecondition() {
		assertNotNull("activiy null", activity);
		assertNotNull("fragment null", fragmentInformation);
	}

	private void registerInBackground() {
		AsyncTask<Void, Object, Object> asyncTask = new AsyncTask<Void, Object, Object>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (mGoogleCloudMessage == null) {
						mGoogleCloudMessage = GoogleCloudMessaging.getInstance(activity);
					}
					mRegId = mGoogleCloudMessage.register(PROJECT_NUMBER_ID);
					callFirstApi(mRegId);
				} catch (Exception ex) {
				}
				return msg;
			}
		};
		asyncTask.execute();
	}

	public void callFirstApi(final String deviceId) throws Exception {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				InputGetData inputGetData = new InputGetData();
				inputGetData.deviceID = deviceId;
				ApiManager.getData(inputGetData, activity);
			}
		});
	}

	public void testWebviewNotNull() {
		try {
			Thread.sleep(1000);
			activity.replaceFragment(fragmentInformation, R.id.activity_main_content, false);
			Thread.sleep(1000);

			WebView mWebview = (WebView) fragmentInformation.getWebView();
			assertNotNull(mWebview);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

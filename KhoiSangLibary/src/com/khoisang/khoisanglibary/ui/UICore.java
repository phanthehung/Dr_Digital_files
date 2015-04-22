package com.khoisang.khoisanglibary.ui;

import java.lang.reflect.Field;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.khoisang.khoisanglibary.dev.MyLog;

public class UICore {
	private ProgressDialog mProgressDialog;
	public static UICore sIntance;

	private UICore() {
	}

	public static UICore getInstance() {
		if (sIntance == null) {
			sIntance = new UICore();
		}
		return sIntance;
	}

	// Call requires API level 3
	public static boolean hideKeyboard(Activity activity) {
		// Check if no view has focus
		View view = activity.getCurrentFocus();
		if (view != null) {
			InputMethodManager inputManager = (InputMethodManager) activity
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
			return true;
		}
		return false;
	}

	public static void showKeyboard(Activity activity, View view) {
		InputMethodManager inputManager = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.showSoftInput(view, 0);
	}

	public static String getResourceNameByID(Context context, int id) {
		String resourceName = context.getResources().getResourceEntryName(id);
		return resourceName;
	}

	public void showIndicator(Context context, String message,
			Boolean cancelable) {
		hideIndicator();
		mProgressDialog = new ProgressDialog(context);
		mProgressDialog.setMessage(message);
		mProgressDialog.setCanceledOnTouchOutside(cancelable);
		mProgressDialog.setCancelable(cancelable);
		mProgressDialog.show();
	}

	public void hideIndicator() {
		if (mProgressDialog != null) {
			mProgressDialog.cancel();
			mProgressDialog.dismiss();
			mProgressDialog = null;
		}
	}

	public int getScreenWidth(Activity activity) {
		DisplayMetrics displaymetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay()
				.getMetrics(displaymetrics);
		return displaymetrics.widthPixels;
	}

	public int getScreenHeight(Activity activity) {
		DisplayMetrics displaymetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay()
				.getMetrics(displaymetrics);
		return displaymetrics.heightPixels;
	}

	public void showToast(Activity application, String message, Boolean longTime) {
		int duration = 0;
		if (longTime == true)
			duration = Toast.LENGTH_LONG;
		else
			duration = Toast.LENGTH_SHORT;
		Toast.makeText(application, message, duration).show();
	}

	public AlertDialog getDialog(Context context, String message,
			Boolean cancelable, DialogInterface.OnClickListener positiveButton,
			String positiveContent,
			DialogInterface.OnClickListener negativeButton,
			String negativeContent) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		builder.setMessage(message);
		builder.setPositiveButton(positiveContent, positiveButton);
		builder.setNegativeButton(negativeContent, negativeButton);
		builder.setCancelable(cancelable);

		AlertDialog dialog = builder.create();
		return dialog;
	}

	public AlertDialog getDialog(Context context, String message,
			Boolean cancelable, DialogInterface.OnClickListener positiveButton,
			String positiveContent) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		builder.setMessage(message);
		builder.setPositiveButton(positiveContent, positiveButton);
		builder.setCancelable(cancelable);

		AlertDialog dialog = builder.create();
		return dialog;
	}

	// 0 : Not Found
	public static int getResourceIDByName(Context context, String name,
			String type) {
		int resourceId = context.getResources().getIdentifier(name, type,
				context.getPackageName());
		return resourceId;
	}

	public static void autoFindID(Activity activity, Object obj, View baseView,
			int layoutID) {
		Field[] fields = obj.getClass().getDeclaredFields();
		for (Field field : fields) {
			try {
				if (field.isAccessible() == false) {
					field.setAccessible(true);
				}
				if (field.get(obj) == null) {
					Class<?> fieldType = field.getType();
					if (android.view.View.class.isAssignableFrom(fieldType)) {
						String fieldName = field.getName().toLowerCase(
								Locale.ENGLISH);
						String resourceLayoutName = getResourceNameByID(
								activity, layoutID);
						String resourceIdName = resourceLayoutName + fieldName;
						int resourceId = getResourceIDByName(activity,
								resourceIdName, "id");
						if (resourceId > 0) {
							View view = null;
							if (baseView != null) { // Fragment
								view = baseView.findViewById(resourceId);
							} else { // Activity
								view = activity.findViewById(resourceId);
							}
							field.set(obj, view);
						} // End if
					} // End if
				} // End if
			} catch (IllegalArgumentException e) {
				MyLog.e(activity.getClass(), e);
			} catch (IllegalAccessException e) {
				MyLog.e(activity.getClass(), e);
			} // End try
		} // End for
	}

	public int getStatusBarHeight(Context context) {
		int result = 0;
		int resourceId = context.getResources().getIdentifier(
				"status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = context.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}
} // End Class

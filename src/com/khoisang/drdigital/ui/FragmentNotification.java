package com.khoisang.drdigital.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.itech.drdigital.R;
import com.khoisang.drdigital.adapter.AdapterNotification;
import com.khoisang.drdigital.data.Notification;
import com.khoisang.drdigital.ui.BaseDrDigital.ContentType;
import com.khoisang.drdigital.util.History;
import com.khoisang.khoisanglibary.ui.ListViewItem;
import com.khoisang.khoisanglibary.ui.fragment.ListViewFragment;

public class FragmentNotification extends ListViewFragment {
	private BaseDrDigital mBaseDrDigital;
	private AsyncTaskDeleteNotification mAysncTaskDeleteNotification;
	private ArrayList<Object> listNotification;

	private ArrayList<Object> getListNotification() {
		if (listNotification == null)
			listNotification = new ArrayList<Object>();
		return listNotification;
	}

	public FragmentNotification(BaseDrDigital baseDrDigital) {
		mBaseDrDigital = baseDrDigital;
	}

	public void setListNotification(List<Notification> listNotification) {
		getListNotification().clear();
		if (listNotification != null && listNotification.size() > 0)
			getListNotification().addAll(listNotification);
	}

	@Override
	protected void afterSetLayoutID(Bundle savedInstanceState) {
		super.afterSetLayoutID(savedInstanceState);

		mBaseDrDigital.initView(getView());
		mBaseDrDigital.checkNotification(ContentType.Notification);

		mBaseDrDigital.getOption1().setImageDrawable(getResources().getDrawable(R.drawable.support_icon));
		mBaseDrDigital.getOption2().setImageDrawable(getResources().getDrawable(R.drawable.information_icon));
		mBaseDrDigital.getOption3().setImageDrawable(getResources().getDrawable(R.drawable.enquiry_icon));
		mBaseDrDigital.getOption4().setImageDrawable(getResources().getDrawable(R.drawable.location_icon));

		mBaseDrDigital.getOption1().setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mBaseDrDigital.onClickSupport();
			}
		});
		mBaseDrDigital.getOption2().setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mBaseDrDigital.onClickLocation();

			}
		});
		mBaseDrDigital.getOption3().setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mBaseDrDigital.onClickLocation();

			}
		});
		mBaseDrDigital.getOption4().setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mBaseDrDigital.onClickLocation();

			}
		});

		if (getListNotification().size() == 0)
			showToast("Not found", false);
	}

	@Override
	protected int getListViewID() {
		return R.id.fragment_notification_list;
	}

	@Override
	protected int getLayoutItemID() {
		return R.layout.item_notification;
	}

	@Override
	protected ArrayList<Object> getDataToListView() {
		return getListNotification();
	}

	@Override
	protected void onClick(AdapterView<?> listView, View itemView, final int position, Object obj) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
		alertDialogBuilder.setTitle(getActivity().getResources().getString(R.string.dialog_delete_title));
		alertDialogBuilder.setMessage(getActivity().getResources().getString(R.string.dialog_delete_message)).setCancelable(false).setPositiveButton(getActivity().getString(R.string.dialog_yes), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				try {
					DeleteNotification(position);
					dialog.dismiss();
				} catch (IOException e) {
					// Ignore
				}
			}
		}).setNegativeButton(getActivity().getResources().getString(R.string.dialog_no), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
				dialog.dismiss();
			}
		});

		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	@Override
	protected ListViewItem getItemObject() {
		AdapterNotification itemObject = new AdapterNotification();
		return itemObject;
	}

	@Override
	protected int getLayoutID() {
		return R.layout.fragment_notification;
	}

	@Override
	protected void reCreateView() {
		mBaseDrDigital.initView(getView());
		mBaseDrDigital.checkNotification(ContentType.Notification);

		if (getListNotification().size() == 0)
			showToast("Not found", false);
	}

	public void checkNotification() {
		mBaseDrDigital.checkNotification(ContentType.Notification);
	}

	public void DeleteNotification(int position) throws IOException {
		if (getActivity() == null || getActivity().getApplication() == null)
			return;

		getListNotification().remove(position);
		mAdapter.notifyDataSetChanged();

		if (mAysncTaskDeleteNotification != null) {
			mAysncTaskDeleteNotification.cancel(false);
			mAysncTaskDeleteNotification = null;
		}
		mAysncTaskDeleteNotification = new AsyncTaskDeleteNotification();
		mAysncTaskDeleteNotification.execute(getListNotification());
	}

	class AsyncTaskDeleteNotification extends AsyncTask<List<Object>, Void, Void> {

		@Override
		protected Void doInBackground(List<Object>... params) {
			List<Object> listNotification = params[0];

			try {
				History.delete(getActivity().getApplication());
			} catch (Exception ex) {
				// Nothing
			}
			for (Object tempObject : listNotification) {
				try {
					if (this.isCancelled() == true)
						break;
					Notification notification = (Notification) tempObject;
					History.save(getActivity().getApplication(), notification.title, notification.message, notification.time);
				} catch (IOException e) {
					// Nothing
				}
			}
			return null;
		}
	}
}

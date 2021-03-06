package com.khoisang.drdigital.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itech.drdigital.R;
import com.khoisang.drdigital.constant.Event;
import com.khoisang.drdigital.ui.BaseDrDigital.ContentType;
import com.khoisang.khoisanglibary.ui.ActionEvent;
import com.khoisang.khoisanglibary.ui.fragment.BaseFragment;

public class FragmentHome extends BaseFragment implements OnClickListener {

	private LinearLayout _location;
	private LinearLayout _support;
	private LinearLayout _enquiry;
	private LinearLayout _information;
	private LinearLayout _notification;
	private TextView _counter;

	@Override
	protected int getLayoutID() {
		return R.layout.fragment_home;
	}

	@Override
	protected void afterSetLayoutID(Bundle savedInstanceState) {
		_location.setOnClickListener(this);
		_support.setOnClickListener(this);
		_enquiry.setOnClickListener(this);
		_information.setOnClickListener(this);
		_notification.setOnClickListener(this);

		checkNotification();
	}

	@Override
	protected void reCreateView() {
		checkNotification();
	}

	@Override
	public void onClick(View v) {
		if (v == _support) {
			raiseEvent(new ActionEvent(Event.SUPPORT_NON_BACK, null));
		} else if (v == _enquiry) {
			raiseEvent(new ActionEvent(Event.ENQUIRY_NON_BACK, null));
		} else if (v == _location) {
			raiseEvent(new ActionEvent(Event.LOCATION_NON_BACK, null));
		} else if (v == _information) {
			raiseEvent(new ActionEvent(Event.INFORMATION_NON_BACK, null));
		} else if (v == _notification) {
			raiseEvent(new ActionEvent(Event.NOTIFICATION_NON_BACK, null));
		}
	}

	public void checkNotification() {
		ApplicationDrDigital applicationDrDigital = (ApplicationDrDigital) getActivity().getApplicationContext();
		if (applicationDrDigital.getCounter() == 0) {
			_counter.setVisibility(View.INVISIBLE);
		} else {
			_counter.setVisibility(View.VISIBLE);
			_counter.setText(String.valueOf(applicationDrDigital.getCounter()));
		}
	}

}

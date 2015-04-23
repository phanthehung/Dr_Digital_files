package com.khoisang.drdigital.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.khoisang.drdigital.R;
import com.khoisang.khoisanglibary.ui.ActionEvent;

public class FragmentHome extends DrDigitalBasegFragment {

	private LinearLayout _location;
	private LinearLayout _support;
	private LinearLayout _enquiry;
	private LinearLayout _information;

	@Override
	protected int getLayoutID() {
		return R.layout.fragment_home;
	}

	@Override
	protected void afterSetLayoutID(Bundle savedInstanceState) {
		super.afterSetLayoutID(savedInstanceState);

		_location.setOnClickListener(this);
		_support.setOnClickListener(this);
		_enquiry.setOnClickListener(this);
		_information.setOnClickListener(this);
	}

	@Override
	protected void reCreateView() {
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		if (v == _support) {
			raiseEvent(new ActionEvent(1, null));
		} else if (v == _enquiry) {
			raiseEvent(new ActionEvent(2, null));
		} else if (v == _location) {
			raiseEvent(new ActionEvent(3, null));
		} else if (v == _information) {
			raiseEvent(new ActionEvent(4, null));
		}
	}

}

package com.khoisang.drdigital.ui;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import com.khoisang.drdigital.R;
import com.khoisang.khoisanglibary.ui.fragment.BaseFragment;

public class FragmentInformation extends BaseFragment implements
		android.view.View.OnClickListener {

	private ImageView _support;
	private ImageView _enquiry;
	private ImageView _location;
	private WebView _webview;

	@Override
	protected int getLayoutID() {
		return R.layout.fragment_information;
	}

	@Override
	protected void afterSetLayoutID(Bundle savedInstanceState) {
		_enquiry.setOnClickListener(this);
		_location.setOnClickListener(this);
		_support.setOnClickListener(this);
	}

	@Override
	protected void reCreateView() {

	}

	@Override
	public void onClick(View v) {
		if (v == _enquiry) {

		} else if (v == _location) {

		} else if (v == _support) {

		}
	}

}

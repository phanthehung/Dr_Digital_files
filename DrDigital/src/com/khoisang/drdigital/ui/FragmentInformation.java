package com.khoisang.drdigital.ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import com.khoisang.khoisanglibary.ui.fragment.BaseFragment;

public class FragmentInformation extends BaseFragment implements android.view.View.OnClickListener {

	private ImageView _support;
	private ImageView _enquiry;
	private ImageView _location;
	private	WebView _webview;
	
	
	@Override
	protected int getLayoutID() {
		// TODO Auto-generated method stub
		return R.layout.fragment_information;
	}

	@Override
	protected void afterSetLayoutID(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		_enquiry.setOnClickListener(this);
		_location.setOnClickListener(this);
		_support.setOnClickListener(this);
	}

	@Override
	protected void reCreateView() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == _enquiry) {
			
		} else if(v == _location) {

		}
		else if(v == _support){
			
		}
	}

}

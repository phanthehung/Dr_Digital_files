package com.khoisang.drdigital.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.khoisang.khoisanglibary.ui.fragment.BaseFragment;

public class FragmentHome extends BaseFragment implements OnClickListener{

	private LinearLayout _location;
	private LinearLayout _support;
	private LinearLayout _enquiry;


	@Override
	protected int getLayoutID() {
		// TODO Auto-generated method stub
		return R.layout.fragment_home;
	}

	@Override
	protected void afterSetLayoutID(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		_location.setOnClickListener(this);
		_support.setOnClickListener(this);
		_enquiry.setOnClickListener(this);
	}

	@Override
	protected void reCreateView() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == _support) {
		}
		else if(v == _enquiry)
		{
			
		}
		else if(v == _location){
		}
	}

}

package com.khoisang.drdigital.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.khoisang.khoisanglibary.ui.fragment.BaseFragment;

public class FragmentSupport extends BaseFragment implements OnClickListener {

	private TextView _phone_number;
	private ImageView _call_button;
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == _phone_number || v == _call_button) {
//			String phoneNumb = "tel:+84866605741";
			String phoneNumb = _phone_number.getText().toString();
			Intent phone = new Intent(Intent.ACTION_CALL);
			phone.setData(Uri.parse(phoneNumb));
			startActivity(phone);
		}
	}

	@Override
	protected int getLayoutID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void afterSetLayoutID(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void reCreateView() {
		// TODO Auto-generated method stub

	}

}

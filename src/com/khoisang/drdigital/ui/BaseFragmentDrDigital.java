package com.khoisang.drdigital.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.khoisang.drdigital.R;
import com.khoisang.khoisanglibary.ui.ActionEvent;
import com.khoisang.khoisanglibary.ui.fragment.BaseFragment;

public abstract class BaseFragmentDrDigital extends BaseFragment implements
		OnClickListener {
	private ImageView mImageViewBell;

	@Override
	protected void afterSetLayoutID(Bundle savedInstanceState) {
		mImageViewBell = (ImageView) findViewById(R.id.layout_header_bell);
		mImageViewBell.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == mImageViewBell) {
			raiseEvent(new ActionEvent(6, null));
		}
	}

}

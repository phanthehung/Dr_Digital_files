package com.khoisang.drdigital.ui;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import com.khoisang.drdigital.R;
import com.khoisang.drdigital.constant.Event;
import com.khoisang.khoisanglibary.ui.ActionEvent;

public class FragmentInformation extends BaseFragmentDrDigital {
	// UI
	WebView _webview;
	private ImageView mBottomOption1;
	private ImageView mBottomOption2;
	private ImageView mBottomOption3;

	//
	private String mContent;

	public FragmentInformation() {
	}

	public FragmentInformation(String content) {
		mContent = content;
	}

	@Override
	protected int getLayoutID() {
		return R.layout.fragment_information;
	}

	@Override
	protected void afterSetLayoutID(Bundle savedInstanceState) {
		super.afterSetLayoutID(savedInstanceState);

		mBottomOption1 = (ImageView) findViewById(R.id.layout_bottom_1);
		mBottomOption1.setImageDrawable(getResources().getDrawable(
				R.drawable.support_icon));
		mBottomOption1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				FragmentInformation.this.raiseEvent(new ActionEvent(
						Event.SUPPORT_NON_BACK, null));

			}
		});

		mBottomOption2 = (ImageView) findViewById(R.id.layout_bottom_2);
		mBottomOption2.setImageDrawable(getResources().getDrawable(
				R.drawable.enquiry_icon));
		mBottomOption2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				FragmentInformation.this.raiseEvent(new ActionEvent(
						Event.ENQUIRY_NON_BACK, null));

			}
		});

		mBottomOption3 = (ImageView) findViewById(R.id.layout_bottom_3);
		mBottomOption3.setImageDrawable(getResources().getDrawable(
				R.drawable.location_icon));
		mBottomOption3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				FragmentInformation.this.raiseEvent(new ActionEvent(
						Event.LOCATION_NON_BACK, null));

			}
		});

		if (mContent != null) {
			StringBuilder headString = new StringBuilder();
			headString
					.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?><head><meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" /><style>body{font-family:'Arial';}</style></head>");
			StringBuilder htmlString = new StringBuilder();
			htmlString.append("<html>" + headString
					+ "<body style='padding: 10%;'>" + mContent
					+ "</body></html>");

			String html = htmlString.toString();
			_webview.loadData(html, "text/html; charset=utf-8", "UTF-8");
		}
		_webview.setBackgroundColor(android.graphics.Color.TRANSPARENT);
	}

	@Override
	protected void reCreateView() {

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
	}

}

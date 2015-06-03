package com.khoisang.drdigital.ui;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import com.khoisang.drdigital.R;
import com.khoisang.drdigital.ui.BaseDrDigital.ContentType;
import com.khoisang.khoisanglibary.ui.fragment.BaseFragment;

public class FragmentSupport extends BaseFragment {
	// UI
	private WebView _webview;
	//
	private String mContent;
	private BaseDrDigital mBaseDrDigital;

	public FragmentSupport() {
	}

	public FragmentSupport(String content, BaseDrDigital baseDrDigital) {
		mContent = content;
		mBaseDrDigital = baseDrDigital;
	}

	@Override
	protected int getLayoutID() {
		return R.layout.fragment_support;
	}

	@Override
	protected void afterSetLayoutID(Bundle savedInstanceState) {
		mBaseDrDigital.initView(getView());
		mBaseDrDigital.checkNotification(ContentType.Support);

		mBaseDrDigital.getOption1().setImageDrawable(getResources().getDrawable(R.drawable.information_icon));
		mBaseDrDigital.getOption2().setImageDrawable(getResources().getDrawable(R.drawable.enquiry_icon));
		mBaseDrDigital.getOption3().setImageDrawable(getResources().getDrawable(R.drawable.location_icon));
		mBaseDrDigital.getOption4().setImageDrawable(getResources().getDrawable(R.drawable.notification_icon));

		mBaseDrDigital.getOption1().setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mBaseDrDigital.onClickInformation();
			}
		});
		mBaseDrDigital.getOption2().setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mBaseDrDigital.onClickEnquiry();

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
				mBaseDrDigital.onClickNotification();

			}
		});

		if (mContent != null) {
			StringBuilder headString = new StringBuilder();
			headString.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?><head><meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" /><style>body{font-family:'Arial';}</style></head>");
			StringBuilder htmlString = new StringBuilder();
			htmlString.append("<html>" + headString + "<body style='padding: 10%;'>" + mContent + "</body></html>");

			String html = htmlString.toString();
			_webview.loadData(html, "text/html; charset=utf-8", "UTF-8");
		}
		_webview.setBackgroundColor(android.graphics.Color.TRANSPARENT);
	}

	@Override
	protected void reCreateView() {
		mBaseDrDigital.initView(getView());
		mBaseDrDigital.checkNotification(ContentType.Support);
	}

}

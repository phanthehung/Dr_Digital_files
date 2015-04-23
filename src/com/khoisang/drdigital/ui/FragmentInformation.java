package com.khoisang.drdigital.ui;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import com.khoisang.drdigital.R;

public class FragmentInformation extends DrDigitalBasegFragment {

	// UI
	WebView _webview;

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
		if (mContent != null) {
			StringBuilder headString = new StringBuilder();
			headString
					.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?><head><meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" /><style>body{font-family:'Arial';}</style></head>");
			StringBuilder htmlString = new StringBuilder();
			htmlString.append("<html>" + headString + "<body>" + mContent
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

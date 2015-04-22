package com.khoisang.drdigital.ui;

import android.os.Bundle;
import android.webkit.WebView;

import com.khoisang.drdigital.R;
import com.khoisang.khoisanglibary.ui.fragment.BaseFragment;

public class FragmentSupport extends BaseFragment {
	// UI
	private WebView _webview;

	private String mContent;

	public FragmentSupport() {

	}

	public FragmentSupport(String content) {
		mContent = content;
	}

	@Override
	protected int getLayoutID() {
		return R.layout.fragment_support;
	}

	@Override
	protected void afterSetLayoutID(Bundle savedInstanceState) {
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

}

package com.khoisang.khoisanglibary.network;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import android.net.Uri;
import android.util.Log;

import com.khoisang.khoisanglibary.network.HttpResult.MethodType;
import com.khoisang.khoisanglibary.network.ProgressHttpEntityWrapper.ProgressCallback;
import com.khoisang.khoisanglibary.os.AsyncTask;
import com.khoisang.khoisanglibary.util.NetwordUtil;
import com.khoisang.khoisanglibary.util.StringUtil;

@SuppressWarnings("deprecation")
public class HttpManager {
	public interface HttpPostDelegate {
		public void httpPostProgress(AsyntaskHttp asyntaskHttp, Float progress);
	}

	private static int CONNECT_TIMEOUT = 60000;
	private WeakHashMap<Integer, AsyntaskHttp> mRequests;

	public HttpManager() {
		mRequests = new WeakHashMap<Integer, AsyntaskHttp>();
	}

	public int get(int name, Object holder, Uri uri) {
		int id = getAsyntaskId();
		AsyntaskHttp asynctask = new AsyntaskHttp(id, name, uri, null,
				HttpGet.METHOD_NAME, null, 1, null, holder);
		asynctask.execute();
		addRequest(asynctask);
		return id;
	}

	public int get(int name, Object holder, Uri uri, HttpHandler handler) {
		int id = getAsyntaskId();
		AsyntaskHttp asynctask = new AsyntaskHttp(id, name, uri, handler,
				HttpGet.METHOD_NAME, null, 1, null, holder);
		asynctask.execute();
		addRequest(asynctask);
		return id;
	}

	public int get(int name, Object holder, Uri uri, HttpHandler handler,
			WeakHashMap<String, String> headers) {
		int id = getAsyntaskId();
		AsyntaskHttp asynctask = new AsyntaskHttp(id, name, uri, handler,
				HttpGet.METHOD_NAME, headers, 1, null, holder);
		asynctask.execute();
		addRequest(asynctask);
		return id;
	}

	public int get(int name, Object holder, Uri uri, HttpHandler handler,
			WeakHashMap<String, String> headers, int numTry) {
		int id = getAsyntaskId();
		AsyntaskHttp asynctask = new AsyntaskHttp(id, name, uri, handler,
				HttpGet.METHOD_NAME, headers, numTry, null, holder);
		asynctask.execute();
		addRequest(asynctask);
		return id;
	}

	public int post(int name, Object holder, Uri uri) {
		int id = getAsyntaskId();
		AsyntaskHttp asynctask = new AsyntaskHttp(id, name, uri, null,
				HttpPost.METHOD_NAME, null, 1, null, holder);
		asynctask.execute();
		addRequest(asynctask);
		return id;
	}

	public int post(int name, Object holder, Uri uri, HttpHandler handler) {
		int id = getAsyntaskId();
		AsyntaskHttp asynctask = new AsyntaskHttp(id, name, uri, handler,
				HttpPost.METHOD_NAME, null, 1, null, holder);
		asynctask.execute();
		addRequest(asynctask);
		return id;
	}

	public int post(int name, Object holder, Uri uri, HttpHandler handler,
			WeakHashMap<String, String> headers) {
		int id = getAsyntaskId();
		AsyntaskHttp asynctask = new AsyntaskHttp(id, name, uri, handler,
				HttpPost.METHOD_NAME, headers, 1, null, holder);
		asynctask.execute();
		addRequest(asynctask);
		return id;
	}

	public int post(int name, Object holder, Uri uri, HttpHandler handler,
			WeakHashMap<String, String> headers,
			MultipartEntityBuilder multipartEntityBuilder) {
		int id = getAsyntaskId();
		AsyntaskHttp asynctask = new AsyntaskHttp(id, name, uri, handler,
				HttpPost.METHOD_NAME, headers, 1, multipartEntityBuilder,
				holder);
		asynctask.execute();
		addRequest(asynctask);
		return id;
	}

	public int post(int name, Object holder, Uri uri, HttpHandler handler,
			WeakHashMap<String, String> headers,
			MultipartEntityBuilder multipartEntityBuilder, int numTry) {
		int id = getAsyntaskId();
		AsyntaskHttp asynctask = new AsyntaskHttp(id, name, uri, handler,
				HttpPost.METHOD_NAME, headers, numTry, multipartEntityBuilder,
				holder);
		asynctask.execute();
		addRequest(asynctask);
		return id;
	}

	private void addRequest(AsyntaskHttp asyntaskHttp) {
		if (mRequests != null) {
			mRequests.put(asyntaskHttp.getId(), asyntaskHttp);
		}
	}

	public AsyntaskHttp getAsyntaskById(int Id) {
		if (mRequests != null)
			return mRequests.get(Id);
		return null;
	}

	private int getAsyntaskId() {
		if (mRequests != null)
			return mRequests.entrySet().size() + 1;
		return 0;
	}

	private HttpRequestBase initHttpRequest(Uri uri,
			Map<String, String> headers, String method) {
		String url = uri.toString();
		HttpRequestBase httpRequest = null;
		if (method
				.equalsIgnoreCase(org.apache.http.client.methods.HttpGet.METHOD_NAME) == true) {
			httpRequest = new HttpGet(url);
		} else {
			httpRequest = new HttpPost(url);
		}

		if (headers != null) {
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				httpRequest.addHeader(entry.getKey(), entry.getValue());
			}
		}
		return httpRequest;
	}

	private CloseableHttpClient initDefaultHttpClient() {
		RequestConfig.Builder requestBuilder = RequestConfig.custom();
		requestBuilder = requestBuilder.setConnectTimeout(CONNECT_TIMEOUT);
		requestBuilder = requestBuilder
				.setConnectionRequestTimeout(CONNECT_TIMEOUT);

		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setDefaultRequestConfig(requestBuilder.build());
		CloseableHttpClient client = builder.build();
		return client;
	}

	public class AsyntaskHttp extends AsyncTask<Void, Void, HttpResult>
			implements ProgressCallback {

		private WeakReference<HttpPostDelegate> mDelegate;
		private WeakHashMap<String, String> mHeaders;
		private WeakReference<HttpHandler> mHandler;
		private WeakReference<Object> mHolder;
		private MultipartEntityBuilder mMultipartEntityBuilder;
		private Uri mUri;
		private String mMethodName;
		private int mTryToConnectWhenFail = 1; // Default
		private int mID;
		private int mName;
		private String mBodyString;
		private Float mDurationProcess = 0F;

		public int getId() {
			return mID;
		}

		public AsyntaskHttp(int id, int name, Uri uri, HttpHandler handler,
				String methodName, WeakHashMap<String, String> header,
				int tryNumber, MultipartEntityBuilder entity, Object holder) {
			mID = id;
			mName = name;
			mHandler = new WeakReference<HttpHandler>(handler);
			mHolder = new WeakReference<Object>(holder);
			mMultipartEntityBuilder = entity;
			mUri = uri;
			mMethodName = methodName;
			mHeaders = header;
			mTryToConnectWhenFail = tryNumber;
		}

		@Override
		protected HttpResult doInBackground(Void... params) {
			CloseableHttpClient httpClient = null;
			int tryToConnect = 0;
			while (tryToConnect < mTryToConnectWhenFail) {
				try {
					HttpResult httpResult = new HttpResult();
					httpClient = initDefaultHttpClient();
					HttpRequestBase httpRequest = initHttpRequest(mUri,
							mHeaders, mMethodName);
					List<NameValuePair> listParam = URLEncodedUtils.parse(
							httpRequest.getURI(), "UTF-8");

					if (httpRequest.getParams() == null) {
						HttpParams httpParams = new BasicHttpParams();
						httpRequest.setParams(httpParams);
					}
					for (int i = 0; i < listParam.size(); i++) {
						NameValuePair nameValuePair = listParam.get(i);
						httpRequest.getParams().setParameter(
								nameValuePair.getName(),
								nameValuePair.getValue());
					}

					httpResult.HttpRequest = httpRequest;
					httpResult.RequestPath = httpRequest.getURI();
					httpResult.HttpParamsRequest = httpRequest.getParams();

					if (mMethodName.equalsIgnoreCase(HttpPost.METHOD_NAME) == true) {
						httpResult.Method = MethodType.POST;
						if (mMultipartEntityBuilder != null) {
							((HttpPost) httpRequest)
									.setEntity(new ProgressHttpEntityWrapper(
											mMultipartEntityBuilder.build(),
											this));
						}
					}
					NetwordUtil.disableConnectionReuseIfNecessary();

					HttpResponse httpResponse = null;
					httpResponse = httpClient.execute(httpRequest);
					HttpEntity httpEntity = httpResponse.getEntity();
					httpResult.InputStream = httpEntity.getContent();
					httpResult.StatusCode = httpResponse.getStatusLine()
							.getStatusCode();
					httpResult.Headers = httpResponse.getAllHeaders();
					httpResult.HttpParamsResponse = httpResponse.getParams();
					mBodyString = StringUtil
							.streamToString(httpResult.InputStream);
					return httpResult;
				} catch (Exception ex) {
					if (mHandler != null && mHandler.get() != null)
						mHandler.get().handleException(mID, mName, ex,
								mHolder.get());
				} finally {
					try {
						if (httpClient != null) {
							httpClient.close();
							httpClient = null;
						}
					} catch (IOException e) {
						// Ignore
					}
					tryToConnect++;
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(HttpResult httpResult) {
			super.onPostExecute(httpResult);
			if (httpResult != null) {
				if (mHandler != null && mHandler.get() != null)
					mHandler.get().handleResult(mID, mName, httpResult,
							mBodyString, mHolder.get());
				httpResult.dispose();
				httpResult = null;
			}
			// Release
			dispose();
		}

		@Override
		protected void onCancelled() {
			if (mHandler != null && mHandler.get() != null)
				mHandler.get().handleCancel(mID, mName, mHolder.get());
			// Release
			dispose();
		}

		@Override
		public void httpPostProgress(float progress) {
			mDurationProcess = progress;
			if (mDelegate != null && mDelegate.get() != null) {
				mDelegate.get().httpPostProgress(this, progress);
			}
		}

		public void setDelegate(HttpPostDelegate mDelegate) {
			this.mDelegate = new WeakReference<HttpManager.HttpPostDelegate>(
					mDelegate);
		}

		public Float getDurationProcess() {
			return mDurationProcess;
		}

		private void dispose() {
			mRequests.put(mID, null);
		}
	} // End Class

} // End Class

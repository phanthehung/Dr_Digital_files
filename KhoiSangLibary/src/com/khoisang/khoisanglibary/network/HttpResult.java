package com.khoisang.khoisanglibary.network;

import java.io.InputStream;
import java.net.URI;

import org.apache.http.Header;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.params.HttpParams;

public class HttpResult {
	public enum MethodType {
		GET, POST
	}

	public InputStream InputStream;
	public int StatusCode;
	public Header[] Headers;
	public HttpParams HttpParamsRequest;
	public HttpParams HttpParamsResponse;
	public MethodType Method;
	public HttpRequestBase HttpRequest;
	public URI RequestPath;

	public HttpResult() {
		Method = MethodType.GET;
	}

	public void dispose() {
		InputStream = null;
		Headers = null;
		HttpParamsRequest = null;
		HttpParamsResponse = null;
		HttpRequest = null;
		RequestPath = null;
	}
}

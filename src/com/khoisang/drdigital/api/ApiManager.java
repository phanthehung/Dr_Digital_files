package com.khoisang.drdigital.api;

import org.apache.http.entity.mime.MultipartEntityBuilder;

import android.net.Uri;
import android.net.Uri.Builder;

import com.google.gson.Gson;
import com.khoisang.drdigital.api.structure.InputGetData;
import com.khoisang.khoisanglibary.network.HttpHandler;
import com.khoisang.khoisanglibary.network.HttpManager;

public class ApiManager {
	public static void getData(InputGetData inputGetData, HttpHandler httpHandler) {
		String json = new Gson().toJson(inputGetData);
		HttpManager httpManager = new HttpManager();
		MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
		multipartEntityBuilder.addTextBody("data", json);

		Uri.Builder builder = new Builder();
		builder.scheme("http");
		builder.encodedAuthority("itech.com.sg");
		builder.encodedPath("drdigital/api/getData");

		httpManager.post(1, null, builder.build(), httpHandler, null, multipartEntityBuilder, 1);
	}
}

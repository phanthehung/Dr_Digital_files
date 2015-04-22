package com.khoisang.khoisanglibary.network;

public interface HttpHandler {
	abstract void prePrequest(int ID, int Name, Object holder);

	abstract void handleException(int ID, int Name, Exception ex, Object holder);

	abstract void handleCancel(int ID, int Name, Object holder);

	abstract void handleResult(int ID, int Name, HttpResult httpResult,
			String bodyString, Object holder);
}

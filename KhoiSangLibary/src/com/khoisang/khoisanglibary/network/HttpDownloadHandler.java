package com.khoisang.khoisanglibary.network;

import java.io.InputStream;

public interface HttpDownloadHandler {
	abstract void downloadPrePrequest(int ID, int Name, Object holder);

	abstract void downloadHandleException(int ID, int Name, Exception ex,
			Object holder);

	abstract void downloadHandleCancel(int ID, int Name, Object holder);

	abstract void downloadHandleInputStream(int ID, int Name,
			HttpResult httpResult, InputStream inputStream, Object holder);
}

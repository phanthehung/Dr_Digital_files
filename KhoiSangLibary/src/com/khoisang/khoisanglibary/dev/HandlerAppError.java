package com.khoisang.khoisanglibary.dev;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

import android.app.Application;

import com.khoisang.khoisanglibary.util.FileUtil;

public class HandlerAppError {
	private WeakReference<Application> mApplication;
	private String mErrorFileName;

	public HandlerAppError() {
	}

	public HandlerAppError(Application application, String fileName) {
		mApplication = new WeakReference<Application>(application);
		mErrorFileName = fileName;
	}

	public void writeError(String content) throws IOException {
		if (content == null)
			return;

		synchronized (this) {
			File folder = mApplication.get().getFilesDir();
			File fileError = new File(folder, mErrorFileName);
			FileOutputStream fileOutputStream = new FileOutputStream(fileError,
					true);
			try {
				fileOutputStream.write(content.getBytes());
				fileOutputStream.flush();
			} finally {
				fileOutputStream.close();
			}
		}
	}

	public String getError() throws IOException {
		synchronized (this) {
			File folder = mApplication.get().getFilesDir();
			File fileError = new File(folder, mErrorFileName);
			String error = FileUtil.readFile(fileError);
			// Delete
			mApplication.get().deleteFile(mErrorFileName);
			return error;
		}
	}
}

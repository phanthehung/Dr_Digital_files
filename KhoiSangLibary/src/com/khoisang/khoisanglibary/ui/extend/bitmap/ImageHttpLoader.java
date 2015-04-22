package com.khoisang.khoisanglibary.ui.extend.bitmap;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;

import com.khoisang.khoisanglibary.dev.MyLog;
import com.khoisang.khoisanglibary.util.BitmapUtil;
import com.khoisang.khoisanglibary.util.FileUtil;
import com.khoisang.khoisanglibary.util.NetwordUtil;
import com.khoisang.khoisanglibary.util.StorageUtil;
import com.khoisang.khoisanglibary.util.SystemInfo;

public class ImageHttpLoader extends BaseImageHttpLoader {
	private static final int HTTP_CACHE_SIZE = 10 * 1024 * 1024;
	private static final int BUFFER_SIZE = 10 * 1024;
	private static final String HTTP_CACHE_DIR = "http";
	private static final String THUMB_CACHE_DIR = "thumb";

	private LruDiskCache mHttpLruCache;
	private boolean mCacheStarting = true;
	private static final int DISK_CACHE_INDEX = 0;
	private ImageCache.ImageCacheParams mCacheParams;
	private final Object mHttpCacheLock = new Object();

	public ImageHttpLoader(ActionBarActivity activity, Bitmap bitmap) {
		super(activity, bitmap);
		init(activity);
	}

	private void init(ActionBarActivity activity) {
		File httpCacheDir = StorageUtil.getDiskCacheDir(
				activity.getApplicationContext(), HTTP_CACHE_DIR);
		File thumbCacheDir = StorageUtil.getDiskCacheDir(
				activity.getApplicationContext(), THUMB_CACHE_DIR);

		mCacheParams = new ImageCache.ImageCacheParams(thumbCacheDir,
				httpCacheDir);
		initImageCache(activity.getSupportFragmentManager(), mCacheParams);
	}

	@Override
	protected void initThumbCacheInternal() {
		super.initThumbCacheInternal();
		initHttpCacheInternal();
	}

	private void initHttpCacheInternal() {
		File thumbHttpDir = mCacheParams.mHttpCacheDir;
		if (thumbHttpDir == null) {
			mHttpLruCache = null;
			return;
		}

		if (thumbHttpDir.exists() == true) {
			try {
				FileUtil.deleteContents(thumbHttpDir);
			} catch (IOException ex) {
				// ignore
			}
		}
		thumbHttpDir.mkdirs();

		synchronized (mHttpCacheLock) {
			if (SystemInfo.getUsableSpace(thumbHttpDir) > HTTP_CACHE_SIZE) {
				try {
					mHttpLruCache = LruDiskCache.open(thumbHttpDir, 1, 1,
							HTTP_CACHE_SIZE);
				} catch (IOException e) {
					mHttpLruCache = null;
				}
			}
			mCacheStarting = false;
			mHttpCacheLock.notifyAll();
		}
	}

	@Override
	protected void clearThumbCacheInternal() {
		super.clearThumbCacheInternal();
		clearHttpCacheInternal();
	}

	private void clearHttpCacheInternal() {
		synchronized (mHttpCacheLock) {
			if (mHttpLruCache != null && !mHttpLruCache.isClosed()) {
				try {
					mHttpLruCache.delete();
				} catch (IOException e) {
					// Ignore
				}
				mHttpLruCache = null;
				mCacheStarting = true;
			}
		}
	}

	@Override
	protected void flushThumbCacheInternal() {
		super.flushThumbCacheInternal();
		flushHttpCacheInternal();
	}

	private void flushHttpCacheInternal() {
		synchronized (mHttpCacheLock) {
			if (mHttpLruCache != null) {
				try {
					mHttpLruCache.flush();
				} catch (IOException e) {
					MyLog.e(getClass(), e);
				}
			}
		}
	}

	@Override
	protected void closeThumbCacheInternal() {
		super.closeThumbCacheInternal();
		closeHttpCacheInternal();
	}

	private void closeHttpCacheInternal() {
		synchronized (mHttpCacheLock) {
			if (mHttpLruCache != null) {
				try {
					if (!mHttpLruCache.isClosed()) {
						mHttpLruCache.close();
						mHttpLruCache = null;
					}
				} catch (IOException e) {
					MyLog.e(getClass(), e);
				}
			} // End if
		} // End sync
	}

	private Bitmap processBitmap(String key, String data, int width, int height) {
		FileDescriptor fileDescriptor = null;
		FileInputStream fileInputStream = null;
		LruDiskCache.Snapshot snapshot;
		synchronized (mHttpCacheLock) {
			while (mCacheStarting) {
				try {
					mHttpCacheLock.wait();
				} catch (InterruptedException e) {
					MyLog.e(getClass(), e);
				}
			}

			if (mHttpLruCache != null) {
				try {
					snapshot = mHttpLruCache.get(key);
					if (snapshot == null) {
						LruDiskCache.Editor editor = mHttpLruCache.edit(key);
						if (editor != null) {
							if (downloadUrlToStream(data,
									editor.newOutputStream(DISK_CACHE_INDEX))) {
								editor.commit();
							} else {
								editor.abort();
							}
						}
						snapshot = mHttpLruCache.get(key);
					}
					if (snapshot != null) {
						fileInputStream = (FileInputStream) snapshot
								.getInputStream(DISK_CACHE_INDEX);
						fileDescriptor = fileInputStream.getFD();
					}
				} catch (IOException e) {
					MyLog.e(getClass(), e);
				} catch (IllegalStateException e) {
					MyLog.e(getClass(), e);
				} finally {
					if (fileDescriptor == null && fileInputStream != null) {
						try {
							fileInputStream.close();
						} catch (IOException e) {
							// Ignore
						}
					} // End if
				} // End finally
			}
		}

		Bitmap bitmap = null;
		if (fileDescriptor != null) {
			bitmap = decodeSampledBitmapFromDescriptor(fileDescriptor, width,
					height);
		}
		if (fileInputStream != null) {
			try {
				fileInputStream.close();
			} catch (IOException e) {
				// Ignore
			}
		}
		return bitmap;
	}

	@Override
	protected Bitmap loadAndResizeBitmap(String key, String data, int width,
			int height) {
		return processBitmap(key, data, width, height);
	}

	private boolean downloadUrlToStream(String urlString,
			OutputStream outputStream) {
		NetwordUtil.disableConnectionReuseIfNecessary();

		HttpURLConnection urlConnection = null;
		BufferedOutputStream bufferOut = null;
		BufferedInputStream bufferIn = null;

		try {
			final URL url = new URL(urlString);
			urlConnection = (HttpURLConnection) url.openConnection();
			bufferIn = new BufferedInputStream(urlConnection.getInputStream(),
					BUFFER_SIZE);
			bufferOut = new BufferedOutputStream(outputStream, BUFFER_SIZE);

			int byteRead;
			while ((byteRead = bufferIn.read()) != -1) {
				bufferOut.write(byteRead);
			}
			return true;
		} catch (final IOException e) {
			MyLog.e(getClass(), e);
		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
			try {
				if (bufferOut != null) {
					bufferOut.close();
				}
			} catch (IOException e) {
				// Ignore
			}
			try {
				if (bufferIn != null) {
					bufferIn.close();
				}
			} catch (IOException e) {
				// Ignore
			}
		}
		return false;
	}

	@Override
	public Bitmap decodeSampledBitmapFromDescriptor(
			FileDescriptor fileDescriptor, int reqWidth, int reqHeight) {
		return BitmapUtil.decodeSampledBitmapFromDescriptor(fileDescriptor,
				reqWidth, reqHeight);
	}
}

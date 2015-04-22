package com.khoisang.khoisanglibary.ui.extend.bitmap;

import java.io.OutputStream;
import java.lang.ref.WeakReference;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Looper;

import com.khoisang.khoisanglibary.os.AsyncTask.Status;
import com.khoisang.khoisanglibary.ui.extend.bitmap.BaseImageHttpLoader.ImageViewAsyncTask;

public class BitmapDrawableRecycling extends BitmapDrawable {
	private int mCacheRefCount = 0;
	private int mDisplayRefCount = 0;

	public Object LockRecycle = new Object();

	public boolean isRecycle() {
		synchronized (LockRecycle) {
			Bitmap bitmap = getBitmap();
			return bitmap == null || bitmap.isRecycled() == true;
		}
	}

	public void recycle() {
		synchronized (LockRecycle) {
			Bitmap bitmap = getBitmap();
			if (bitmap != null && bitmap.isRecycled() == false) {
				bitmap.recycle();
				bitmap = null;
				System.gc();
			}
		}
	}

	private final WeakReference<ImageViewAsyncTask> bitmapWorkerTaskReference;

	public BitmapDrawableRecycling(Resources resource, Bitmap bitmap,
			ImageViewAsyncTask imageViewLoader) {
		super(resource, bitmap);

		setAntiAlias(true);
		bitmapWorkerTaskReference = new WeakReference<ImageViewAsyncTask>(
				imageViewLoader);
	}

	public ImageViewAsyncTask getAsysnTask() {
		return bitmapWorkerTaskReference.get();
	}

	public void setIsDisplayed(boolean isDisplayed) {
		synchronized (this) {
			if (isDisplayed) {
				mDisplayRefCount++;
			} else {
				mDisplayRefCount--;
			}
			checkRecycle();
		}
	}

	public void setIsCached(boolean isCached) {
		synchronized (this) {
			if (isCached) {
				mCacheRefCount++;
			} else {
				mCacheRefCount--;
			}
			checkRecycle();
		}
	}

	private void checkRecycle() {
		if (mCacheRefCount <= 0 && mDisplayRefCount <= 0) {
			ImageViewAsyncTask imageViewLoader = getAsysnTask();
			if (imageViewLoader != null) {
				if (imageViewLoader.getStatus() == Status.RUNNING) {
					try {
						imageViewLoader.cancel(false);
					} catch (InterruptedException ex) {
						// Ignore
					}
				}
				final ImageViewRecycling imageViewRecycling = imageViewLoader
						.getImageView();
				if (imageViewRecycling != null) {
					Handler handler = new Handler(Looper.getMainLooper());
					handler.post(new Runnable() {
						@Override
						public void run() {
							imageViewRecycling.setCheckDrawable(false);
							imageViewRecycling.setImageDrawable(null);
						}
					});
				}
			}
			recycle();
		} // end if
	}

	public boolean compress(OutputStream outputStream,
			CompressFormat compressFormat, int quality) {
		synchronized (this) {
			if (isRecycle() == false) {
				Bitmap bitmap = getBitmap();
				if (bitmap != null) {
					bitmap.compress(compressFormat, quality, outputStream);
					return true;
				}
			}
			return false;
		} // End sync
	}
}

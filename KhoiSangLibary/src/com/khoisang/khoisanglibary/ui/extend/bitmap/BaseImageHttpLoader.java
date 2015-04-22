package com.khoisang.khoisanglibary.ui.extend.bitmap;

import java.io.FileDescriptor;
import java.lang.ref.WeakReference;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

import com.khoisang.khoisanglibary.os.AsyncTask;
import com.khoisang.khoisanglibary.util.HashAlgorithm;
import com.khoisang.khoisanglibary.util.HashAlgorithm.Algorithm;

public abstract class BaseImageHttpLoader {
	public interface DelegateImageLoader {
		public void finishLoadImage(ImageViewRecycling imageView,
				BitmapDrawableRecycling drawable);

		public void failedLoadImage();

		public void cancleLoadImage();
	}

	private static final int MESSAGE_CLEAR = 0;
	private static final int MESSAGE_INIT_DISK_CACHE = 1;
	private static final int MESSAGE_FLUSH = 2;
	private static final int MESSAGE_CLOSE = 3;
	private static final int MESSAGE_CLEAR_MEMORY = 4;

	private ImageCache mImageCache;
	private ImageCache.ImageCacheParams mImageCacheParams;
	private boolean mExitTasks = false;
	private final Object mPauseWorkLock = new Object();
	private Bitmap mDefaultLoading;

	protected Resources mResources;
	protected boolean mPauseWork = false;

	protected int mScreenWidth;
	protected int mScreenHeight;

	protected BaseImageHttpLoader(Context context, final Bitmap defaultLoading) {
		mResources = context.getResources();

		DisplayMetrics displaymetrics = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(displaymetrics);
		mScreenWidth = displaymetrics.widthPixels;
		mScreenHeight = displaymetrics.heightPixels;
		mDefaultLoading = defaultLoading;
	}

	public void loadImage(String data, ImageViewRecycling imageView,
			DelegateImageLoader delegate) {

		setDefaultImage(imageView, mDefaultLoading);

		int width = imageView.getLayoutParams().width;
		int height = imageView.getLayoutParams().height;

		if (width < 1)
			width = mScreenWidth;

		if (height < 1)
			height = mScreenHeight;

		data = data.trim();

		if (data == null || data.equalsIgnoreCase("") == true) {
			return;
		}

		String key = HashAlgorithm.hash(data, Algorithm.MD5);

		BitmapDrawableRecycling bitmapDrawable = null;
		if (mImageCache != null) {
			bitmapDrawable = mImageCache.getBitmapFromMemCache(key);
		}

		if (bitmapDrawable != null) {
			setImageDrawable(delegate, imageView, bitmapDrawable);
		} else if (cancelPotentialWork(key, imageView)) {
			ImageViewAsyncTask asyncTask = new ImageViewAsyncTask(key, data,
					width, height, imageView, delegate);
			imageView.setImageViewAsyncTask(asyncTask);
			asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		} else {
			if (delegate != null)
				delegate.failedLoadImage();
		}
	}

	private void setDefaultImage(ImageViewRecycling imageViewRecycling,
			Bitmap bitmap) {
		if (imageViewRecycling != null) {
			imageViewRecycling.setImageBitmap(bitmap);
		}
	}

	protected void initImageCache(FragmentManager fragmentManager,
			ImageCache.ImageCacheParams cacheParams) {
		mImageCacheParams = cacheParams;
		mImageCache = ImageCache
				.getInstance(fragmentManager, mImageCacheParams);
		new CacheAsyncTask().execute(MESSAGE_INIT_DISK_CACHE);
	}

	public void setExitTasksEarly(boolean exitTasksEarly) {
		mExitTasks = exitTasksEarly;
		setPauseWork(false);
	}

	protected abstract Bitmap loadAndResizeBitmap(String key, String data,
			int width, int height);

	protected ImageCache getImageCache() {
		return mImageCache;
	}

	public void cancelWork(ImageViewRecycling imageView) {
		ImageViewAsyncTask bitmapWorkerTask = getAsyncTask(imageView);
		if (bitmapWorkerTask != null) {
			try {
				bitmapWorkerTask.cancel(false);
			} catch (InterruptedException e) {
				// Ignore
			}
		}
	}

	public boolean cancelPotentialWork(String key, ImageViewRecycling imageView) {
		ImageViewAsyncTask imageViewLoad = getAsyncTask(imageView);
		if (imageViewLoad != null) {
			if (imageViewLoad.mKey == null
					|| imageViewLoad.mKey.equals(key) == false) {
				try {
					imageViewLoad.cancel(false);
				} catch (InterruptedException e) {
					// Ignore
				}
			} else {
				return false;
			}
		}
		return true;
	}

	private ImageViewAsyncTask getAsyncTask(ImageViewRecycling imageView) {
		if (imageView != null) {
			return imageView.getImageViewAsyncTask();
		}
		return null;
	}

	public class ImageViewAsyncTask extends
			AsyncTask<Void, Void, BitmapDrawableRecycling> {
		private String mData;
		private String mKey;
		private int mWidth;
		private int mHeight;
		private WeakReference<ImageViewRecycling> imageViewReference;
		private WeakReference<DelegateImageLoader> delegate;

		public ImageViewAsyncTask(String key, String data, int width,
				int height, ImageViewRecycling imageView,
				DelegateImageLoader delegateImageLoader) {
			mKey = key;
			mData = data;
			mWidth = width;
			mHeight = height;
			imageViewReference = new WeakReference<ImageViewRecycling>(
					imageView);
			if (delegateImageLoader != null) {
				delegate = new WeakReference<BaseImageHttpLoader.DelegateImageLoader>(
						delegateImageLoader);
			}
		}

		@Override
		protected BitmapDrawableRecycling doInBackground(Void... params) {
			Bitmap bitmap = null;
			BitmapDrawableRecycling bitmapDrawableRecycling = null;

			synchronized (mPauseWorkLock) {
				while (mPauseWork == true && isCancelled() == false) {
					try {
						mPauseWorkLock.wait();
					} catch (InterruptedException e) {
						// nothing
					}
				}
			}

			if (mImageCache != null && isCancelled() == false
					&& getImageView() != null && mExitTasks == false) {
				bitmap = mImageCache.getBitmapFromDiskCache(
						BaseImageHttpLoader.this, mKey, mWidth, mHeight);
			}

			if (bitmap == null && isCancelled() == false
					&& getImageView() != null && mExitTasks == false) {
				bitmap = loadAndResizeBitmap(mKey, mData, mWidth, mHeight);
			}

			if (bitmap != null) {
				bitmapDrawableRecycling = new BitmapDrawableRecycling(
						mResources, bitmap, this);
				if (mImageCache != null) {
					if (bitmapDrawableRecycling != null
							&& bitmapDrawableRecycling.isRecycle() == false) {
						mImageCache.addBitmapToMemCacheAndDiskCache(mKey,
								bitmapDrawableRecycling);
					} else {
						bitmapDrawableRecycling = null;
					} // End if
				} // End if
			} // End if

			return bitmapDrawableRecycling;
		}

		@Override
		protected void onPostExecute(BitmapDrawableRecycling value) {
			DelegateImageLoader delegateImageLoader = null;
			if (delegate != null) {
				delegateImageLoader = delegate.get();
			}

			if (isCancelled() || mExitTasks) {
				if (delegateImageLoader != null)
					delegateImageLoader.failedLoadImage();
				return;
			}

			ImageViewRecycling imageView = getImageView();
			if (value != null && imageView != null) {
				setImageDrawable(delegateImageLoader, imageView, value);
			}
		}

		@Override
		protected void onCancelled(BitmapDrawableRecycling value) {
			super.onCancelled(value);
			synchronized (mPauseWorkLock) {
				mPauseWorkLock.notifyAll();
			}

			DelegateImageLoader delegateImageLoader = null;
			if (delegate != null) {
				delegateImageLoader = delegate.get();
			}

			if (delegateImageLoader != null) {
				delegateImageLoader.cancleLoadImage();
			}
		}

		public ImageViewRecycling getImageView() {
			ImageViewRecycling imageView = imageViewReference.get();
			if (imageView != null) {
				ImageViewAsyncTask asyncTask = getAsyncTask(imageView);

				if (this == asyncTask) {
					return imageView;
				}
			}

			return null;
		}
	}

	private void setImageDrawable(DelegateImageLoader delegateImageLoader,
			ImageViewRecycling imageView, BitmapDrawableRecycling drawable) {
		if (drawable instanceof BitmapDrawableRecycling
				&& ((BitmapDrawableRecycling) drawable).isRecycle() == false) {
			imageView.setImageDrawable(drawable);
			imageViewFadeIn(imageView);
		}

		if (delegateImageLoader != null)
			delegateImageLoader.finishLoadImage(imageView, drawable);
	}

	private void imageViewFadeIn(final ImageView imageView) {
		Animation animation = new AlphaAnimation(0, 1);
		animation.setDuration(1000);
		animation.setRepeatCount(0);
		animation.setAnimationListener(new AnimationListener() {
			public void onAnimationEnd(Animation animation) {
				Drawable drawable = imageView.getDrawable();
				if (drawable instanceof BitmapDrawableRecycling
						&& ((BitmapDrawableRecycling) drawable).isRecycle() == false) {
					imageView.setVisibility(View.VISIBLE);
				}
			}

			public void onAnimationRepeat(Animation animation) {
			}

			public void onAnimationStart(Animation animation) {
				imageView.setVisibility(View.INVISIBLE);
			}
		});
		imageView.startAnimation(animation);
	}

	public void setPauseWork(boolean pauseWork) {
		synchronized (mPauseWorkLock) {
			mPauseWork = pauseWork;
			if (mPauseWork == false) {
				mPauseWorkLock.notifyAll();
			}
		}
	}

	protected class CacheAsyncTask extends AsyncTask<Object, Void, Void> {

		@Override
		protected Void doInBackground(Object... params) {
			switch ((Integer) params[0]) {
			case MESSAGE_CLEAR:
				clearThumbCacheInternal();
				break;
			case MESSAGE_INIT_DISK_CACHE:
				initThumbCacheInternal();
				break;
			case MESSAGE_FLUSH:
				flushThumbCacheInternal();
				break;
			case MESSAGE_CLOSE:
				closeThumbCacheInternal();
				break;
			case MESSAGE_CLEAR_MEMORY:
				clearMemoryCacheInternal();
				break;
			}
			return null;
		}
	}

	protected void initThumbCacheInternal() {
		if (mImageCache != null) {
			mImageCache.initThumbCache();
		}
	}

	protected void clearThumbCacheInternal() {
		if (mImageCache != null) {
			mImageCache.clearThumbCache();
		}
	}

	protected void flushThumbCacheInternal() {
		if (mImageCache != null) {
			mImageCache.flushThumbCache();
		}
	}

	protected void closeThumbCacheInternal() {
		if (mImageCache != null) {
			mImageCache.closeThumbCache();
			mImageCache = null;
		}
	}

	protected void clearMemoryCacheInternal() {
		if (mImageCache != null) {
			mImageCache.clearMemoryCache();
		}
	}

	public void clearMemoryCache() {
		new CacheAsyncTask().execute(MESSAGE_CLEAR_MEMORY);
	}

	public void clearCache() {
		new CacheAsyncTask().execute(MESSAGE_CLEAR);
	}

	public void flushCache() {
		new CacheAsyncTask().execute(MESSAGE_FLUSH);
	}

	public void closeCache() {
		new CacheAsyncTask().execute(MESSAGE_CLOSE);
	}

	public abstract Bitmap decodeSampledBitmapFromDescriptor(
			FileDescriptor fileDescriptor, int reqWidth, int reqHeight);
}

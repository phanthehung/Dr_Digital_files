package com.khoisang.khoisanglibary.ui.extend.bitmap;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.LruCache;

import com.khoisang.khoisanglibary.dev.MyLog;
import com.khoisang.khoisanglibary.util.FileUtil;
import com.khoisang.khoisanglibary.util.SystemInfo;

public class ImageCache {
	private static final int DISK_CACHE_INDEX = 0;

	private LruDiskCache mThumbLruCache;
	private LruCache<String, BitmapDrawableRecycling> mMemLruCache;
	private ImageCacheParams mCacheParams;
	private final Object mThumbCacheLockObject = new Object();
	private boolean mDiskCacheStarting = true;

	private ImageCache(ImageCacheParams cacheParams) {
		init(cacheParams);
	}

	public static ImageCache getInstance(FragmentManager fragmentManager,
			ImageCacheParams cacheParams) {
		RetainFragment retainFragment = findOrCreateRetainFragment(fragmentManager);
		ImageCache imageCache = (ImageCache) retainFragment.getObject();
		if (imageCache == null) {
			imageCache = new ImageCache(cacheParams);
			retainFragment.setObject(imageCache);
		}
		return imageCache;
	}

	private void init(ImageCacheParams cacheParams) {
		mCacheParams = cacheParams;
		if (mCacheParams.mMemoryCacheEnabled) {
			mMemLruCache = new LruCache<String, BitmapDrawableRecycling>(
					mCacheParams.mMemCacheSize) {

				@Override
				protected void entryRemoved(boolean evicted, String key,
						BitmapDrawableRecycling oldValue,
						BitmapDrawableRecycling newValue) {
					if (oldValue != null)
						oldValue.setIsCached(false);
				}

				@Override
				protected int sizeOf(String key, BitmapDrawableRecycling value) {
					final int bitmapSize = getBitmapSize(value) / 1024;
					return bitmapSize == 0 ? 1 : bitmapSize;
				}
			};
		}

		if (cacheParams.mInitDiskCacheOnCreate) {
			initThumbCache();
		}
	}

	public void initThumbCache() {
		File diskCacheDir = mCacheParams.mThumbCacheDir;
		if (diskCacheDir == null) {
			mThumbLruCache = null;
			return;
		}

		synchronized (mThumbCacheLockObject) {
			if (mThumbLruCache == null || mThumbLruCache.isClosed()) {
				if (mCacheParams.mDiskCacheEnabled && diskCacheDir != null) {
					if (diskCacheDir.exists()) {
						try {
							FileUtil.deleteContents(diskCacheDir);
						} catch (IOException ex) {
							// Ignore
						}
					}
					diskCacheDir.mkdirs();
					if (SystemInfo.getUsableSpace(diskCacheDir) > mCacheParams.mDiskCacheSize) {
						try {
							mThumbLruCache = LruDiskCache.open(diskCacheDir, 1,
									1, mCacheParams.mDiskCacheSize);
						} catch (final IOException e) {
							MyLog.e(getClass(), e);
							mCacheParams.mThumbCacheDir = null;
						}
					}
				}
			}
			mDiskCacheStarting = false;
			mThumbCacheLockObject.notifyAll();
		}
	}

	public void addBitmapToMemCacheAndDiskCache(String key,
			BitmapDrawableRecycling value) {
		if (key == null || value == null || value.isRecycle() == true) {
			return;
		}

		if (mMemLruCache != null) {
			value.setIsCached(true);
			mMemLruCache.put(key, value);
		}

		synchronized (mThumbCacheLockObject) {
			if (mThumbLruCache != null) {
				OutputStream outputStream = null;
				try {
					LruDiskCache.Snapshot snapshot = mThumbLruCache.get(key);
					if (snapshot == null) {
						final LruDiskCache.Editor editor = mThumbLruCache
								.edit(key);
						if (editor != null) {
							outputStream = editor
									.newOutputStream(DISK_CACHE_INDEX);
							if (value.compress(outputStream,
									mCacheParams.mCompressFormat,
									mCacheParams.mCompressQuality) == true)
								editor.commit();
							outputStream.close();
						}
					} else {
						snapshot.getInputStream(DISK_CACHE_INDEX).close();
					}
				} catch (final IOException e) {
					MyLog.e(getClass(), e);
				} catch (Exception e) {
					MyLog.e(getClass(), e);
				} finally {
					try {
						if (outputStream != null) {
							outputStream.close();
						}
					} catch (IOException e) {
						MyLog.e(getClass(), e);
					}
				}
			}
		}
	}

	public BitmapDrawableRecycling getBitmapFromMemCache(String key) {
		BitmapDrawableRecycling bitmapDrawable = null;
		if (mMemLruCache != null) {
			bitmapDrawable = mMemLruCache.get(key);
		}
		if (bitmapDrawable == null || bitmapDrawable.isRecycle() == true) {
			mMemLruCache.remove(key);
			return null;
		}
		return bitmapDrawable;
	}

	public Bitmap getBitmapFromDiskCache(BaseImageHttpLoader imageResizer,
			String key, int width, int height) {
		Bitmap bitmap = null;

		synchronized (mThumbCacheLockObject) {
			while (mDiskCacheStarting) {
				try {
					mThumbCacheLockObject.wait();
				} catch (InterruptedException e) {
					MyLog.e(getClass(), e);
				}
			}

			if (mThumbLruCache != null) {
				InputStream inputStream = null;
				try {
					LruDiskCache.Snapshot snapshot = mThumbLruCache.get(key);
					if (snapshot != null) {
						inputStream = snapshot.getInputStream(DISK_CACHE_INDEX);
						if (inputStream != null) {
							FileDescriptor fd = ((FileInputStream) inputStream)
									.getFD();
							bitmap = imageResizer
									.decodeSampledBitmapFromDescriptor(fd,
											width, height);
						}
					}
				} catch (final IOException e) {
					MyLog.e(getClass(), e);
				} finally {
					try {
						if (inputStream != null) {
							inputStream.close();
						}
					} catch (IOException e) {
						// Ignore
					}
				}
			}
			return bitmap;
		}
	}

	public void clearMemoryCache() {
		if (mMemLruCache != null) {
			mMemLruCache.evictAll();
		}
	}

	public void clearThumbCache() {
		clearMemoryCache();

		synchronized (mThumbCacheLockObject) {
			mDiskCacheStarting = true;
			if (mThumbLruCache != null && mThumbLruCache.isClosed() == false) {
				try {
					mThumbLruCache.delete();
				} catch (IOException e) {
					MyLog.e(getClass(), e);
				}
				mThumbLruCache = null;
				initThumbCache();
			}
		}
	}

	public void flushThumbCache() {
		synchronized (mThumbCacheLockObject) {
			if (mThumbLruCache != null) {
				try {
					mThumbLruCache.flush();
				} catch (IOException e) {
					MyLog.e(getClass(), e);
				}
			}
		}
	}

	public void closeThumbCache() {
		synchronized (mThumbCacheLockObject) {
			if (mThumbLruCache != null) {
				try {
					if (mThumbLruCache.isClosed() == false) {
						mThumbLruCache.close();
						mThumbLruCache = null;
					}
				} catch (IOException e) {
					MyLog.e(getClass(), e);
				}
			}
		}
	}

	public static class ImageCacheParams {
		private static final int DEFAULT_MEM_CACHE_SIZE = 1024 * 1024 * 5;
		private static final int DEFAULT_DISK_CACHE_SIZE = 1024 * 1024 * 5;
		private static final CompressFormat DEFAULT_COMPRESS_FORMAT = CompressFormat.JPEG;
		private static final int DEFAULT_COMPRESS_QUALITY = 75;

		private static final boolean DEFAULT_MEM_CACHE_ENABLED = true;
		private static final boolean DEFAULT_DISK_CACHE_ENABLED = true;
		private static final boolean DEFAULT_INIT_DISK_CACHE_ON_CREATE = true;

		public int mMemCacheSize = DEFAULT_MEM_CACHE_SIZE;
		public int mDiskCacheSize = DEFAULT_DISK_CACHE_SIZE;
		public CompressFormat mCompressFormat = DEFAULT_COMPRESS_FORMAT;
		public int mCompressQuality = DEFAULT_COMPRESS_QUALITY;
		public boolean mMemoryCacheEnabled = DEFAULT_MEM_CACHE_ENABLED;
		public boolean mDiskCacheEnabled = DEFAULT_DISK_CACHE_ENABLED;
		public boolean mInitDiskCacheOnCreate = DEFAULT_INIT_DISK_CACHE_ON_CREATE;
		public File mHttpCacheDir;
		public File mThumbCacheDir;

		public ImageCacheParams(File thumbCacheDir, File httpCacheDir) {
			mThumbCacheDir = thumbCacheDir;
			mHttpCacheDir = httpCacheDir;
			setMemCacheSizePercent(0.4f);
		}

		private void setMemCacheSizePercent(float percent) {
			mMemCacheSize = Math.round(percent
					* Runtime.getRuntime().maxMemory() / 1024);
		}
	}

	public static int getBitmapSize(BitmapDrawable value) {
		Bitmap bitmap = value.getBitmap();
		return bitmap.getRowBytes() * bitmap.getHeight();
	}

	private static RetainFragment findOrCreateRetainFragment(
			FragmentManager fragmentManager) {
		String tag = "RetainFragment";
		RetainFragment retainFragment = (RetainFragment) fragmentManager
				.findFragmentByTag(tag);

		if (retainFragment == null) {
			retainFragment = new RetainFragment();
			fragmentManager.beginTransaction().add(retainFragment, tag)
					.commitAllowingStateLoss();
		}

		return retainFragment;
	}

	public static class RetainFragment extends Fragment {
		private Object mObject;

		public RetainFragment() {
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setRetainInstance(true);
		}

		public void setObject(Object object) {
			mObject = object;
		}

		public Object getObject() {
			return mObject;
		}
	}
}

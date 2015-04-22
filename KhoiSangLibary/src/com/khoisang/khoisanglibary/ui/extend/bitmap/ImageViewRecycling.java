/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.khoisang.khoisanglibary.ui.extend.bitmap;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.ImageView;

import com.khoisang.khoisanglibary.ui.extend.bitmap.BaseImageHttpLoader.ImageViewAsyncTask;

public class ImageViewRecycling extends ImageView implements OnPreDrawListener {

	private boolean KeepImageOnDetachedFromWindow;
	//
	private boolean mCheckDrawable;
	private boolean RoundedCorner;
	private Path mClipPath;
	private RectF mRect;

	private WeakReference<ImageViewAsyncTask> mImageViewAsyncTask;

	public boolean getKeepImageOnDetachedFromWindow() {
		return KeepImageOnDetachedFromWindow;
	}

	public void setKeepImageOnDetachedFromWindow(
			Boolean keepImageOnDetachedFromWindow) {
		KeepImageOnDetachedFromWindow = keepImageOnDetachedFromWindow;
	}

	private boolean getRoundedCorner() {
		return RoundedCorner;
	}

	public boolean getCheckDrawable() {
		return mCheckDrawable;
	}

	public void setCheckDrawable(Boolean checkDrawable) {
		this.mCheckDrawable = checkDrawable;
	}

	public ImageViewRecycling(Context context) {
		super(context);
		init();
	}

	public ImageViewRecycling(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ImageViewRecycling(Context context, AttributeSet attrs, int def) {
		super(context, attrs, def);
		init();
	}

	private void init() {
		setCheckDrawable(true);
		setRoundedCorner(false);
		setKeepImageOnDetachedFromWindow(false);
		buildDrawingCache(true);
	}

	public void setImageViewAsyncTask(ImageViewAsyncTask imageViewAsyncTask) {
		if (imageViewAsyncTask != null) {
			mImageViewAsyncTask = new WeakReference<BaseImageHttpLoader.ImageViewAsyncTask>(
					imageViewAsyncTask);
		}
	}

	public ImageViewAsyncTask getImageViewAsyncTask() {
		if (mImageViewAsyncTask != null)
			return mImageViewAsyncTask.get();
		return null;
	}

	public void setRoundedCorner(Boolean value) {
		RoundedCorner = value;
		if (RoundedCorner == true) {
			ViewTreeObserver viewTreeObserver = getViewTreeObserver();
			viewTreeObserver.addOnPreDrawListener(this);
		}
	}

	@Override
	protected void onDetachedFromWindow() {
		destroyDrawingCache();
		if (getKeepImageOnDetachedFromWindow() == false) {
			setCheckDrawable(true);
			setImageDrawable(null);
		}
		super.onDetachedFromWindow();
	}

	@Override
	public void setImageDrawable(Drawable newDrawable) {
		Drawable previousDrawable = getDrawable();
		super.setImageDrawable(newDrawable);

		if (getCheckDrawable() == true) {
			checkDrawable(newDrawable, true);
			checkDrawable(previousDrawable, false);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (getRoundedCorner() == true && mClipPath != null) {
			canvas.clipPath(mClipPath);
		}
		super.onDraw(canvas);
	}

	private void checkDrawable(Drawable drawable, final boolean isDisplayed) {
		if (drawable != null) {
			if (drawable instanceof BitmapDrawableRecycling) {
				((BitmapDrawableRecycling) drawable)
						.setIsDisplayed(isDisplayed);
			}
		} // End if
	}

	@Override
	public boolean onPreDraw() {
		if (mRect == null) {
			mRect = new RectF(0, 0, getWidth(), getHeight());
		}

		if (mClipPath == null) {
			mClipPath = new Path();
			mClipPath.addRoundRect(mRect, getWidth(), getHeight(),
					Path.Direction.CW);
		}

		Drawable drawable = getDrawable();
		if (drawable instanceof BitmapDrawable) {
			Bitmap bitmap = ((BitmapDrawable) getDrawable()).getBitmap();
			if (bitmap != null && bitmap.isRecycled() == true)
				return false;
		}
		return true;
	}

}

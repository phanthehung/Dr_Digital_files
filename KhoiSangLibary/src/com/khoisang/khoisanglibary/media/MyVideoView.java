package com.khoisang.khoisanglibary.media;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class MyVideoView extends SurfaceView implements
		OnBufferingUpdateListener, OnCompletionListener, OnErrorListener,
		OnInfoListener, OnPreparedListener, OnSeekCompleteListener,
		OnVideoSizeChangedListener {
	public enum StatePlayer {
		PRE_SETUP_HOLDER, SETUP_HOLDER, PREPARE, PLAYING, STOPPED, NONE
	}

	public enum StateSurfaceView {
		DESTROYED, CREATED, CHANGED, NONE
	}

	public interface MediaPlayerListener {
		void playerOnError(Exception ex, int what, int extra);

		void playerOnStart();

		void playerOnStop();
	}

	public MyVideoView(Context context) {
		super(context);
		init();
	}

	public MyVideoView(Context context, AttributeSet attributes) {
		super(context, attributes);
		init();
	}

	private void init() {
		setStatePlayer(StatePlayer.NONE);
		setStateSurfaceView(StateSurfaceView.NONE);

		SurfaceHolder surfaceHolder = getHolder();
		surfaceHolder.addCallback(new Callback() {
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				setStateSurfaceView(StateSurfaceView.DESTROYED);
			}

			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				setStateSurfaceView(StateSurfaceView.CREATED);
				try {
					if (getStatePlayer() == StatePlayer.PRE_SETUP_HOLDER)
						prePlay(holder);
				} catch (IllegalArgumentException | SecurityException
						| IllegalStateException ex) {
					listenerError(ex, 0, 0);
				}
			}

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {
				setStateSurfaceView(StateSurfaceView.CHANGED);
			}
		});
	}

	private MediaPlayer mPlayer;
	private String mFilePath;
	private boolean mLooping = false;
	private int mPosition = 0;
	private StatePlayer mStatePlayer;
	private StateSurfaceView mStateSurfaceView;
	private MediaPlayerListener mListener;
	private boolean mErrorPlaying;

	public boolean isLooping() {
		return mLooping;
	}

	public void setLooping(boolean looping) {
		this.mLooping = looping;
	}

	public String getFilePath() {
		return mFilePath;
	}

	public void setFilePath(String filePath) {
		this.mFilePath = filePath;
	}

	public MediaPlayerListener getListener() {
		return mListener;
	}

	public void setListener(MediaPlayerListener listener) {
		this.mListener = listener;
	}

	public StatePlayer getStatePlayer() {
		return mStatePlayer;
	}

	private void setStatePlayer(StatePlayer mState) {
		this.mStatePlayer = mState;
	}

	public StateSurfaceView getStateSurfaceView() {
		return mStateSurfaceView;
	}

	private void setStateSurfaceView(StateSurfaceView stateSurfaceView) {
		this.mStateSurfaceView = stateSurfaceView;
	}

	public int getPosition() {
		return mPosition;
	}

	public void setPosition(int position) {
		this.mPosition = position;
	}

	public void play(String filePath) {
		play(filePath, false);
	}

	public void play(String filePath, boolean looping) {
		play(filePath, looping, 0);
	}

	private void prePlay(SurfaceHolder holder) {
		try {
			stop();

			if (mPlayer == null) {
				mPlayer = new MediaPlayer();
				mPlayer.setOnBufferingUpdateListener(this);
				mPlayer.setOnCompletionListener(this);
				mPlayer.setOnErrorListener(this);
				mPlayer.setOnInfoListener(this);
				mPlayer.setOnPreparedListener(this);
				mPlayer.setOnSeekCompleteListener(this);
				mPlayer.setOnVideoSizeChangedListener(this);
			}
			mPlayer.setDataSource(getFilePath());
			// Update state
			setStatePlayer(StatePlayer.SETUP_HOLDER);
			mPlayer.setDisplay(holder);

			mPlayer.prepareAsync();
			setStatePlayer(StatePlayer.PREPARE);
		} catch (Exception ex) {
			listenerError(ex, 0, 0);
		}
	}

	public void play(String filePath, boolean looping, int position) {
		try {
			if (filePath == null
					|| filePath.trim().equalsIgnoreCase("") == true)
				throw new Exception("File's path is empty");

			setFilePath(filePath);
			setLooping(looping);
			setPosition(position);

			if (getStateSurfaceView() == StateSurfaceView.CREATED
					|| getStateSurfaceView() == StateSurfaceView.CHANGED) {
				prePlay(getHolder());
			} else {
				setStatePlayer(StatePlayer.PRE_SETUP_HOLDER);
			}
		} catch (Exception ex) {
			listenerError(ex, 0, 0);
		}

	}

	public void stop() {
		try {
			if (mPlayer != null) {
				if (mPlayer.isPlaying() == true) {
					mPlayer.stop();
					listenerStop();
				}
				mPlayer.reset();
				mPlayer.release();
				mPlayer = null;
			}
		} catch (Exception ex) {
			listenerError(ex, 0, 0);
		}
	}

	public void pause() {
		try {
			if (mPlayer != null) {
				setPosition(mPlayer.getCurrentPosition());
			}
			stop();
		} catch (Exception ex) {
			listenerError(ex, 0, 0);
		}
	}

	public void resume() {
		try {
			if (getFilePath() != null)
				play(getFilePath(), isLooping(), getPosition());
		} catch (Exception ex) {
			listenerError(ex, 0, 0);
		}
	}

	public boolean isPlaying() {
		if (mPlayer != null)
			return mPlayer.isPlaying();
		return false;
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		Handler handler = new Handler();
		final String path = getFilePath();

		if (mLooping == true) {
			// Bug not looping
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					if (mPlayer != null && mPlayer.isPlaying() == false
							&& path == getFilePath() && mErrorPlaying == false) {
						play(path, mLooping);
					}
				}
			}, 1000);
		}
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
	}

	@Override
	public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
	}

	@Override
	public void onSeekComplete(MediaPlayer mp) {
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		mPlayer.setLooping(isLooping());
		if (getPosition() > 0) {
			mPlayer.seekTo(getPosition());
			setPosition(0);
		}
		mPlayer.start();
		listenerStart();
	}

	@Override
	public boolean onInfo(MediaPlayer mp, int what, int extra) {
		return false;
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {

		return false;
	}

	private void listenerError(Exception ex, int what, int extra) {
		mErrorPlaying = true;
		this.setStatePlayer(StatePlayer.NONE);
		if (getListener() != null)
			getListener().playerOnError(ex, what, extra);
	}

	private void listenerStart() {
		mErrorPlaying = false;
		this.setStatePlayer(StatePlayer.PLAYING);
		if (getListener() != null)
			getListener().playerOnStart();
	}

	private void listenerStop() {
		this.setStatePlayer(StatePlayer.STOPPED);
		if (getListener() != null)
			getListener().playerOnStop();
	}
}

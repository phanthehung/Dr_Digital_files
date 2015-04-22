package com.khoisang.khoisanglibary.network;

import org.apache.http.HttpEntity;
import org.apache.http.entity.HttpEntityWrapper;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

class ProgressHttpEntityWrapper extends HttpEntityWrapper {

	private final ProgressCallback progressCallback;

	public static interface ProgressCallback {
		public void httpPostProgress(float progress);
	}

	public ProgressHttpEntityWrapper(final HttpEntity entity,
			final ProgressCallback progressCallback) {
		super(entity);
		this.progressCallback = progressCallback;
	}

	@Override
	public void writeTo(final OutputStream out) throws IOException {
		this.wrappedEntity
				.writeTo(out instanceof ProgressFilterOutputStream ? out
						: new ProgressFilterOutputStream(out,
								this.progressCallback, getContentLength()));
	}

	static class ProgressFilterOutputStream extends FilterOutputStream {

		private final ProgressCallback progressCallback;
		private long transferred;
		private long totalBytes;

		ProgressFilterOutputStream(final OutputStream out,
				final ProgressCallback progressCallback, final long totalBytes) {
			super(out);
			this.progressCallback = progressCallback;
			this.transferred = 0;
			this.totalBytes = totalBytes;
		}

		@Override
		public void write(final byte[] data, final int off, final int len)
				throws IOException {
			this.transferred += len;
			this.progressCallback.httpPostProgress(getCurrentProgress());
			out.write(data, off, len);
		}

		@Override
		public void write(final int number) throws IOException {
			this.transferred++;
			this.progressCallback.httpPostProgress(getCurrentProgress());
			out.write(number);
		}

		private float getCurrentProgress() {
			return ((float) this.transferred / this.totalBytes) * 100;
		}
	}

}

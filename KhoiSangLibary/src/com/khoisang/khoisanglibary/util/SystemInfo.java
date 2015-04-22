package com.khoisang.khoisanglibary.util;

import java.io.File;

import android.os.StatFs;

public class SystemInfo {
	public static long getUsableSpace(File path) {
		final StatFs stats = new StatFs(path.getPath());
		return (long) stats.getBlockSize() * (long) stats.getAvailableBlocks();
	}
}

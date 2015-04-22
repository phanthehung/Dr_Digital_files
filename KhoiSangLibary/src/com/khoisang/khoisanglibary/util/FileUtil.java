package com.khoisang.khoisanglibary.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtil {
	public static void deleteContents(File dir) throws IOException {
		File[] files = dir.listFiles();
		if (files == null) {
			return;
		}
		for (File file : files) {
			if (file.isDirectory()) {
				deleteContents(file);
			}
			if (file.delete() == false) {
				throw new IOException();
			}
		} // End for
	}

	public static String readFile(File file) throws IOException {
		FileInputStream fileInputStream = new FileInputStream(file);
		return StringUtil.streamToString(fileInputStream);
	}

	public static boolean writeFile(ByteArrayOutputStream bytes, File file) {
		FileOutputStream fileOutput = null;
		try {
			fileOutput = new FileOutputStream(file);
			bytes.writeTo(fileOutput);
			return true;
		} catch (IOException ioe) {
			// Ignore
		} finally {
			if (fileOutput != null)
				try {
					fileOutput.close();
				} catch (IOException e) {
					// Ignore
				}
		}
		return false;
	}
}

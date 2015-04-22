package com.khoisang.khoisanglibary.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class WriterLog {
	public enum LogType {
		Infor, Error
	}

	private File mFile;
	private Object mObjectLocked = new Object();
	private static WriterLog sIntance;

	private WriterLog(File file) {
		mFile = file;
	}

	public static WriterLog getInstance(File file) {
		if (sIntance == null) {
			sIntance = new WriterLog(file);
		}
		return sIntance;
	}

	public synchronized void write(LogType logType, String content)
			throws IOException {
		synchronized (mObjectLocked) {
			if (mFile.exists() == false)
				mFile.createNewFile();

			FileWriter fileWriter = null;
			try {
				fileWriter = new FileWriter(mFile, true);
				fileWriter.write("\n");
				fileWriter.write("------");
				fileWriter.write("\n");
				fileWriter.write(new Date().toString());
				fileWriter.write(": ");
				fileWriter.write("LogType (" + logType.toString() + ")");
				fileWriter.write(" - ");
				fileWriter.write(content);
				fileWriter.write("\n");
				fileWriter.write("------");
				fileWriter.write("\n");
				fileWriter.flush();
			} catch (IOException ex) {
				ex.printStackTrace();
			} finally {
				if (fileWriter != null) {
					try {
						fileWriter.close();
						fileWriter = null;
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				} // End if
			}
		}
	}

	public String read() {
		synchronized (mObjectLocked) {
			try {
				return FileUtil.readFile(mFile);
			} catch (IOException e) {
				//
			}
			return "";
		}
	}

	public void delete() {
		synchronized (mObjectLocked) {
			mFile.delete();
		}
	}
}

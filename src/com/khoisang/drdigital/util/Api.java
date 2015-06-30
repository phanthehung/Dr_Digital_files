package com.khoisang.drdigital.util;

import java.io.IOException;

import com.khoisang.khoisanglibary.dev.FileWriter;

import android.app.Application;

public class Api {
	private static final String FILE_NAME = "Api";

	public static void delete(Application application) {
		FileWriter fileWriter = new FileWriter(application, FILE_NAME);
		fileWriter.delete();
	}

	public static void save(Application application, String content) throws IOException {
		FileWriter fileWriter = new FileWriter(application, FILE_NAME);
		fileWriter.write(content);
	}

	public static String get(Application application) throws IOException {
		FileWriter fileWriter = new FileWriter(application, FILE_NAME);
		return fileWriter.get(false);
	}

	public static boolean exist(Application application) throws IOException {
		FileWriter fileWriter = new FileWriter(application, FILE_NAME);
		return fileWriter.exist();
	}
}

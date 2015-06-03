package com.khoisang.drdigital.ui;

import android.app.Application;

public class ApplicationDrDigital extends Application {
	private int counter = 0;

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}
}

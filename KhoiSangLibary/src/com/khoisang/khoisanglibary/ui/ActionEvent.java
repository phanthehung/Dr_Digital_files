package com.khoisang.khoisanglibary.ui;

public class ActionEvent {
	public int eventID;
	public Object own;
	public Object parameters;

	public ActionEvent(int _eventID, Object _parameters) {
		eventID = _eventID;
		parameters = _parameters;
	}

	public void setPara(Object _parameters) {
		parameters = _parameters;
	}
}

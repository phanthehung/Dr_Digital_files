package com.khoisang.khoisanglibary.ui;

public abstract class ListViewItem {
	public ListViewItem() {
	}

	public abstract void initData(Object own, Object obj, int position);

	public abstract void setData(Object own, Object obj, int position);
}

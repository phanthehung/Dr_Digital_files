package com.khoisang.drdigital.ui;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.khoisang.khoisanglibary.ui.ListViewItem;
import com.khoisang.khoisanglibary.ui.fragment.BaseFragment;
import com.khoisang.khoisanglibary.ui.fragment.ListViewFragment;

public class FragmentLocation extends ListViewFragment implements OnClickListener {

	private ListView _address_list;
	private ImageView _support;
	private ImageView _enquiry;
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == _support) {
		}
		else if(v == _enquiry)
		{
			
		}
	}

	@Override
	protected int getLayoutID() {
		// TODO Auto-generated method stub
		return R.layout.fragment_location;
	}

	@Override
	protected void afterSetLayoutID(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		_support.setOnClickListener(this);
		_enquiry.setOnClickListener(this);
	}

	@Override
	protected void reCreateView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected int getListViewID() {
		// TODO Auto-generated method stub
		return R.id.fragment_location_address_list;
	}

	@Override
	protected int getLayoutItemID() {
		// TODO Auto-generated method stub
		return R.layout.location_list_item;
	}

	@Override
	protected ArrayList<Object> getDataToListView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onClick(AdapterView<?> listView, View itemView,
			int position, Object obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected ListViewItem getItemObject() {
		// TODO Auto-generated method stub
		return null;
	}

}

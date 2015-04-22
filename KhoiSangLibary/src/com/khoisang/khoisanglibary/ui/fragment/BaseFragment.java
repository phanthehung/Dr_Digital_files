package com.khoisang.khoisanglibary.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.khoisang.khoisanglibary.ui.ActionEvent;
import com.khoisang.khoisanglibary.ui.UICore;
import com.khoisang.khoisanglibary.ui.activity.BaseActivity;

public abstract class BaseFragment extends Fragment {

	private View mView;

	public BaseFragment() {
		// Exception
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (mView == null) {
			int layoutId = getLayoutID();
			mView = inflater.inflate(layoutId, container, false);
			autoFindID(this, mView, layoutId);
			afterSetLayoutID(savedInstanceState);
		} else {
			ViewGroup viewGroup = (ViewGroup) mView.getParent();
			if (viewGroup != null) {
				viewGroup.removeAllViews();
			}
			reCreateView();
		}
		return mView;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (mView != null) {
			ViewGroup parentViewGroup = (ViewGroup) mView.getParent();
			if (parentViewGroup != null) {
				parentViewGroup.removeAllViews();
			} // End if
		} // End if
	}

	protected abstract int getLayoutID();

	protected abstract void afterSetLayoutID(Bundle savedInstanceState);

	protected abstract void reCreateView();

	protected View findViewById(int id) {
		if (mView != null) {
			return mView.findViewById(id);
		}
		return null;
	}

	protected boolean hideKeyboard() {
		return UICore.hideKeyboard(getActivity());
	}

	protected void showKeyboard(View view) {
		UICore.showKeyboard(getActivity(), view);
	}

	protected String getResourceNameByID(int id) {
		return UICore.getResourceNameByID(getActivity(), id);
	}

	protected int getResourceIDByName(String name, String type) {
		return UICore.getResourceIDByName(getActivity(), name, type);
	}

	protected void autoFindID(Object obj, View baseView, int layoutID) {
		UICore.autoFindID(getActivity(), obj, baseView, layoutID);
	}

	protected void raiseEvent(ActionEvent actionEvent) {
		if (actionEvent.own == null)
			actionEvent.own = this;
		getBaseActivity().handleEvent(actionEvent);
	}

	public BaseActivity getBaseActivity() {
		return (BaseActivity) getActivity();
	}

	public View getView() {
		return mView;
	}

	public void showIndicator(String message, Boolean cancelable) {
		UICore uiCore = UICore.getInstance();
		uiCore.showIndicator(getActivity(), message, cancelable);
	}

	public void hideIndicator() {
		UICore uiCore = UICore.getInstance();
		uiCore.hideIndicator();
	}

	public void showToast(String message, Boolean longTime) {
		UICore uiCore = UICore.getInstance();
		uiCore.showToast(getActivity(), message, longTime);
	}

	public int getScreenWidth() {
		UICore uiCore = UICore.getInstance();
		return uiCore.getScreenWidth(getActivity());
	}

	public int getScreenHeight() {
		UICore uiCore = UICore.getInstance();
		return uiCore.getScreenHeight(getActivity());
	}

	public int getStatusBarHeight() {
		UICore uiCore = UICore.getInstance();
		return uiCore.getStatusBarHeight(getActivity());
	}
}

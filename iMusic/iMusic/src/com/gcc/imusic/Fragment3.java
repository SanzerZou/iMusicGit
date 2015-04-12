package com.gcc.imusic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Fragment3 extends Fragment{
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//return super.onCreateView(inflater, container, savedInstanceState);
		return inflateAndSetupView(inflater, container, savedInstanceState, R.layout.fragment1);
	}

	private View inflateAndSetupView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState, int layoutResourceId){
		View layout = inflater.inflate(layoutResourceId, container, false);
		return layout;
	}
}

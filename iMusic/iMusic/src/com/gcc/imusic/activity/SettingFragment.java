package com.gcc.imusic.activity;

import java.util.ArrayList;
import java.util.List;

import zrc.widget.SimpleFooter;
import zrc.widget.SimpleHeader;
import zrc.widget.ZrcListView;
import zrc.widget.ZrcListView.OnItemClickListener;
import zrc.widget.ZrcListView.OnStartListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gcc.imusic.CustomListAdapter;
import com.gcc.imusic.MusicApplication;
import com.gcc.imusic.R;
import com.gcc.imusic.CustomListAdapter.OnSpeakerItemListener;
import com.gcc.imusic.dlna.DLNAMediaController.OnRendererChangedListener;
import com.gcc.imusic.dlna.DLNAMediaController;
import com.gcc.imusic.dlna.DLNAMediaRenderer;

public class SettingFragment extends Fragment implements OnSpeakerItemListener, OnRendererChangedListener{

	private MusicApplication application;
	private ZrcListView listView;
	private List<DLNAMediaRenderer> renderers;
	private CustomListAdapter adapter;
	
	private final static String TAG = "SpeakerFragment";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.v(TAG, "onCreateView");
		application = (MusicApplication)getActivity().getApplicationContext();
		application.getDmc().setOnRendererChangedListener(this);
		
		View view = inflater.inflate(R.layout.fragment_list, container, false);
		listView = (ZrcListView)view.findViewById(R.id.fragment_list_view);
		
//		listView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				// TODO Auto-generated method stub
//				Log.v(TAG, "onItemClick "+position);
//				application.setRenderer(renderers.get(position));
//				renderers.get(position).setListener((BaseActivity)getActivity());
//				adapter.setSelectedIndex(position);
//				adapter.notifyDataSetChanged();
//			}
//		});
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(ZrcListView parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Log.v(TAG, "onItemClick "+position);
//				application.setRenderer(renderers.get(position));
//				renderers.get(position).setListener((BaseActivity)getActivity());
//				adapter.setSelectedIndex(position);
//				adapter.notifyDataSetChanged();
				Intent intent = new Intent(getActivity(), NetworkActivity.class);
				intent.putExtra("udn", renderers.get(position).getUdn().toString());
				startActivity(intent);
			}
		});
		
		
		MusicApplication application = (MusicApplication)getActivity().getApplicationContext();
		
		renderers = application.getDmc().getRenderers();
//		adapter = new SpeakerAdapter(getActivity());
		
		List<Object> list = new ArrayList<Object>();
		list.addAll(renderers);
		adapter = new CustomListAdapter(getActivity(), CustomListAdapter.SPEAKER_LIST, list);
		
		
		

		for (int i = 0; i < renderers.size(); i++) {
			if (renderers.get(i).equals(application.getRenderer())) {
				adapter.setSelectedIndex(i);
			}
		}
		
		listView.setAdapter(adapter);
		
		final DLNAMediaController dmc = this.application.getDmc();
		
		
		SimpleHeader header = new SimpleHeader(getActivity());
		header.setTextColor(Color.BLUE);
		header.setCircleColor(Color.BLUE);
		listView.setHeadable(header);
		SimpleFooter footer = new SimpleFooter(getActivity());
        footer.setCircleColor(0x00000000);
        listView.setFootable(footer);
        
		listView.setItemAnimForTopIn(R.anim.topitem_in);
		listView.setOnRefreshStartListener(new OnStartListener() {
			
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				if (dmc != null) {
					dmc.search();
				}
				Handler handler = new Handler();
				handler.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						listView.setRefreshSuccess("刷新成功");
						MusicApplication application = (MusicApplication)getActivity().getApplicationContext();
						
						renderers = application.getDmc().getRenderers();
						List<Object> list = new ArrayList<Object>();
						list.addAll(renderers);
						adapter.setDataList(list);
						adapter.notifyDataSetChanged();
					}
				}, 3000);
			}
		});
		
		return view;
	}
	
	@Override
	public void rendererChanged() {
		// TODO Auto-generated method stub
		if (getActivity() != null) {
			getActivity().runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
//					MusicApplication application = (MusicApplication)getActivity().getApplicationContext();
//					
//					renderers = application.getDmc().getRenderers();
//					adapter = new SpeakerAdapter(getActivity());
//					List<Object> list = new ArrayList<Object>();
//					list.addAll(renderers);
//					adapter = new CustomListAdapter(getActivity(), CustomListAdapter.SPEAKER_LIST, list);
//					
//					
//					listView.setAdapter(adapter);
				}
			});
		}
	}

	@Override
	public void onSpeakerItemClick(DLNAMediaRenderer renderer) {
		// TODO Auto-generated method stub
		
	}

}

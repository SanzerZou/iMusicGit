package com.gcc.imusic.activity;

import java.util.ArrayList;
import java.util.List;

import zrc.widget.SimpleFooter;
import zrc.widget.SimpleHeader;
import zrc.widget.ZrcListView;
import zrc.widget.ZrcListView.OnItemClickListener;
import zrc.widget.ZrcListView.OnStartListener;

import com.android.vtuner.data.BaseModel;
import com.gcc.imusic.CustomListAdapter;
import com.gcc.imusic.CustomListAdapter.OnServerItemListener;
import com.gcc.imusic.MusicApplication;
import com.gcc.imusic.R;
import com.gcc.imusic.activity.SpeakerFragment.SpeakerAdapter;
import com.gcc.imusic.dlna.DLNAMediaController.OnServerChangedListener;
import com.gcc.imusic.dlna.DLNAMediaController;
import com.gcc.imusic.dlna.DLNAMediaServer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class ServerFragment extends Fragment implements OnServerItemListener, OnServerChangedListener{

	private MusicApplication application;
	private ZrcListView listView;
	private List<DLNAMediaServer> servers;
	private CustomListAdapter adapter;
	
	//private OnServerItemSelectedListener listener;
	
	private final static String TAG ="ServerFragment";
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		application = (MusicApplication)getActivity().getApplicationContext();
		
		View view = inflater.inflate(R.layout.fragment_list, container, false);
		listView = (ZrcListView)view.findViewById(R.id.fragment_list_view);
		
//		listView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				// TODO Auto-generated method stub
//				Log.v(TAG, "onItemClick "+position);
//				adapter.setSelectedIndex(position);
//				adapter.notifyDataSetChanged();
//				
//				if (position < servers.size()) {
//					application.setServer(servers.get(position));
//					Intent intent = new Intent(getActivity(), ContentActivity.class);
//					intent.putExtra("contentType", ContentActivity.DMS_CONTENT_TYPE);
//					intent.putExtra("udn", servers.get(position).getUdn().toString());
//					intent.putExtra("containerId", "0");
//					intent.putExtra("title", servers.get(position).getFriendlyName());
//					intent.putExtra("label","0");
//					startActivity(intent);
//				}else {
//					application.setServer(null);
//					Intent intent = new Intent(getActivity(), RadioActivity.class);
//					intent.putExtra("radioType", RadioActivity.FIRST_MODEL);
//					ArrayList<BaseModel> list = new ArrayList<BaseModel>();
//					list.addAll(((MainActivity)getActivity()).getRootModels());
//					intent.putExtra("models", list);
//					startActivity(intent);
//				}
//				
//			}
//		});
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(ZrcListView parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Log.v(TAG, "onItemClick "+position);
				adapter.setSelectedIndex(position);
				adapter.notifyDataSetChanged();
				
				if (position < servers.size()) {
					application.setServer(servers.get(position));
					Intent intent = new Intent(getActivity(), ContentActivity.class);
					intent.putExtra("contentType", ContentActivity.DMS_CONTENT_TYPE);
					intent.putExtra("udn", servers.get(position).getUdn().toString());
					intent.putExtra("containerId", "0");
					intent.putExtra("title", servers.get(position).getFriendlyName());
					intent.putExtra("label","0");
					startActivity(intent);
				}else {
					application.setServer(null);
					Intent intent = new Intent(getActivity(), RadioActivity.class);
					intent.putExtra("radioType", RadioActivity.FIRST_MODEL);
					ArrayList<BaseModel> list = new ArrayList<BaseModel>();
					list.addAll(((MainActivity)getActivity()).getRootModels());
					intent.putExtra("models", list);
					startActivity(intent);
				}
				
			}
			
		});
		
		
		MusicApplication application = (MusicApplication)getActivity().getApplicationContext();
		servers = application.getDmc().getServers();
		List<Object> list = new ArrayList<Object>();
		list.addAll(servers);
		adapter = new CustomListAdapter(getActivity(), CustomListAdapter.SERVER_LIST, list);
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
						servers = application.getDmc().getServers();
						List<Object> list = new ArrayList<Object>();
						list.addAll(servers);
						adapter.setDataList(list);
						adapter.notifyDataSetChanged();
					}
				}, 3000);
			}
		});
		
		listView.refresh();
		return view;
	}
	
	
	
//	public OnServerItemSelectedListener getListener() {
//		return listener;
//	}
//
//	public void setListener(OnServerItemSelectedListener listener) {
//		this.listener = listener;
//	}



	public interface OnServerItemSelectedListener{
		public void onServerItemSelected(DLNAMediaServer server);
		public void onVtunerSelected();
	}

	private static class ViewHolder{
		public TextView title;
	}
	
	public class ServerAdapter extends BaseAdapter{

		private LayoutInflater mInflater;
		
		
		public ServerAdapter(Context context){
			this.mInflater = LayoutInflater.from(context);
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return servers.size() + 1;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder ;
			
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.list_item_server, null);
				holder = new ViewHolder();
				holder.title = (TextView) convertView.findViewById(R.id.server_name);
				convertView.setTag(holder);
			}else {
				holder = (ViewHolder) convertView.getTag();
				
			}
			if (position == servers.size()) {
				holder.title.setText("Radio");
			}else {
				holder.title.setText(servers.get(position).getFriendlyName());
			}
			
			
			return convertView;
		}
		
	}

	@Override
	public void serverChanged() {
		// TODO Auto-generated method stub
//		getActivity().runOnUiThread(new Runnable() {
//			
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				MusicApplication application = (MusicApplication)getActivity().getApplicationContext();
//				servers = application.getDmc().getServers();
////				List<Object> list = new ArrayList<Object>();
////				list.addAll(servers);
////				adapter = new CustomListAdapter(getActivity(), CustomListAdapter.SERVER_LIST, list);
////				listView.setAdapter(adapter);
//			}
//		});
	}
	
}

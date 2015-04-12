package com.gcc.imusic.activity;

import java.util.ArrayList;
import java.util.List;

import com.gcc.imusic.CustomListAdapter;
import com.gcc.imusic.MusicApplication;
import com.gcc.imusic.R;
import com.gcc.imusic.dlna.DLNAMediaRenderer;
import com.gcc.imusic.dlna.LocalMediaRenderer;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class PlaylistActivity extends ActionBarActivity{
	
	public final static int PLAY_LIST_MODE_SONG = 1;
	public final static int PLAY_LIST_MODE_VOLUME = 2;
	
	private ListView listView;
	private CustomListAdapter customeAdapter;
	private VolumeAdapter volumeAdapter;
	
	private Button closeButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		getSupportActionBar().hide();
		setContentView(R.layout.activity_play_list);
		closeButton = (Button)findViewById(R.id.play_list_close_button);
		listView = (ListView)findViewById(R.id.play_list_view);
		MusicApplication application  = (MusicApplication)getApplicationContext();
		Intent intent = getIntent();
		if (intent != null) {
			int mode = intent.getIntExtra("mode", 0);
			if (mode == PLAY_LIST_MODE_SONG) {
				final DLNAMediaRenderer mediaRenderer = application.getRenderer();
				if (mediaRenderer != null) {
					if (mediaRenderer.getIsLocalRenderer()) {
						if (((LocalMediaRenderer)mediaRenderer).getContentType() == LocalMediaRenderer.CONTENT_TYPE_RADIO) {
							ArrayList<Object> list = new ArrayList<Object>();
							list.addAll(((LocalMediaRenderer)mediaRenderer).getStations());
							customeAdapter = new CustomListAdapter(this, CustomListAdapter.RADIO_LIST, list);
							listView.setAdapter(customeAdapter);
							listView.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> parent,
										View view, int position, long id) {
									// TODO Auto-generated method stub
									mediaRenderer.setContentIndex(position);
									mediaRenderer.playSong();
									finish();
								}
							});
						}else {
							ArrayList<Object> list = new ArrayList<Object>();
							list.addAll(mediaRenderer.getContentItems());
							customeAdapter = new CustomListAdapter(this, CustomListAdapter.CONETNT_LIST, list);
							listView.setAdapter(customeAdapter);
							listView.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> parent,
										View view, int position, long id) {
									// TODO Auto-generated method stub
									mediaRenderer.setContentIndex(position);
									mediaRenderer.playSong();
									finish();
								}
							});
						}
					}else {
						ArrayList<Object> list = new ArrayList<Object>();
						list.addAll(mediaRenderer.getContentItems());
						customeAdapter = new CustomListAdapter(this, CustomListAdapter.CONETNT_LIST, list);
						listView.setAdapter(customeAdapter);
						listView.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								// TODO Auto-generated method stub
								mediaRenderer.setContentIndex(position);
								mediaRenderer.playSong();
								finish();
							}
						});
					}
					
				}
			}else if (mode == PLAY_LIST_MODE_VOLUME) {
				volumeAdapter = new VolumeAdapter(this, application.getDmc().getRenderers());
				listView.setAdapter(volumeAdapter);
			}
		}
		
		closeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		
	}
	
	
	private static class ViewHolder{
		public TextView title;
		public SeekBar volumeSeekBar;
	}
	
	public class VolumeAdapter extends BaseAdapter{

		private LayoutInflater layoutInflater;
		private List<DLNAMediaRenderer> mediaRenderers;
		
		public VolumeAdapter(Context context, List<DLNAMediaRenderer> list){
			this.layoutInflater = LayoutInflater.from(context);
			this.mediaRenderers = list;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return this.mediaRenderers.size();
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
			final DLNAMediaRenderer mediaRenderer = mediaRenderers.get(position);
			if (convertView == null) {
				convertView = layoutInflater.inflate(R.layout.list_item_volume, null);
				holder = new ViewHolder();
				holder.title = (TextView) convertView.findViewById(R.id.speaker_name);
				holder.volumeSeekBar = (SeekBar) convertView.findViewById(R.id.volume_seek_bar);
				holder.volumeSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
					
					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub
						mediaRenderer.setVolume(seekBar.getProgress());
					}
					
					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onProgressChanged(SeekBar seekBar, int progress,
							boolean fromUser) {
						// TODO Auto-generated method stub
						
					}
				});
				convertView.setTag(holder);
			}else {
				holder = (ViewHolder) convertView.getTag();
				
			}

			holder.title.setText(mediaRenderers.get(position).getFriendlyName());
			holder.volumeSeekBar.setProgress(mediaRenderers.get(position).getCurrentVolume());
			
			
			return convertView;
		}
		
	}
	
	
}

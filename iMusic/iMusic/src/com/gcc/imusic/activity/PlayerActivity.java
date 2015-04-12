package com.gcc.imusic.activity;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;

//import org.teleal.cling.support.model.PositionInfo;
//import org.teleal.cling.support.model.TransportInfo;
//import org.teleal.cling.support.model.TransportState;



import org.fourthline.cling.support.model.TransportInfo;
import org.fourthline.cling.support.model.TransportState;

import com.android.vtuner.data.Station;
import com.gcc.imusic.CustomListAdapter;
import com.gcc.imusic.MusicApplication;
import com.gcc.imusic.R;
import com.gcc.imusic.R.layout;
import com.gcc.imusic.R.menu;
import com.gcc.imusic.dlna.ContentItem;
import com.gcc.imusic.dlna.DLNAMediaRenderer;
import com.gcc.imusic.dlna.DLNAMediaRenderer.OnMediaRendererListener;
import com.gcc.imusic.dlna.DLNAMediaServer;
import com.gcc.imusic.dlna.LocalMediaRenderer;
import com.gcc.imusic.utils.RoundBitmap;

import android.R.anim;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.transition.Visibility;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class PlayerActivity extends ActionBarActivity implements OnClickListener,OnMediaRendererListener{

	private final static String TAG = "PlayerActivity";
	private ActionBar actionBar = null;
	private MusicApplication application;
	private ImageView albumImageView;
	private SeekBar timeSeekBar;
	private TextView currentTimeTextView;
	private TextView totalTimeTextView;
	private ImageButton playImageButton;
	private ImageButton nextImageButton;
	private ImageButton previousImageButton;
	private ContentItem contentItem;
	
	
	
	private ImageButton playlistImageButton;
	private ImageButton playModeImageButton;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player);
		
		actionBar = getSupportActionBar();
		
		albumImageView = (ImageView)findViewById(R.id.album_image);
		timeSeekBar = (SeekBar)findViewById(R.id.time_bar);
		currentTimeTextView = (TextView)findViewById(R.id.current_time);
		totalTimeTextView = (TextView)findViewById(R.id.total_time);
		playImageButton = (ImageButton)findViewById(R.id.play_pause);
		
		playlistImageButton = (ImageButton)findViewById(R.id.play_list_button);
		playModeImageButton = (ImageButton)findViewById(R.id.play_mode_button);
		
		nextImageButton = (ImageButton)findViewById(R.id.next_song);
		previousImageButton = (ImageButton)findViewById(R.id.previous_song);
		playImageButton.setImageResource(R.drawable.play);
		playImageButton.setTag(R.drawable.play);
		
		albumImageView.setImageBitmap(RoundBitmap.getRoundBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.album), 400));

		
		
		application = (MusicApplication) getApplicationContext();
		DLNAMediaRenderer mediaRenderer = application.getRenderer();
		if (mediaRenderer != null) {
			if (mediaRenderer.getIsLocalRenderer()) {
				if (((LocalMediaRenderer)mediaRenderer).getContentType() == LocalMediaRenderer.CONTENT_TYPE_RADIO) {
					Station station = ((LocalMediaRenderer)mediaRenderer).getCurrentStation();
					setStationInfo(station);
				}
			}
			contentItem = mediaRenderer.getCurrentContentItem();
			if (contentItem != null) {
				if (mediaRenderer.getIsLocalContent()) {
					if (getArtAlbum(Integer.parseInt(contentItem.getItem().getId()
							.substring(11))) != null) {
							albumImageView.setImageBitmap(RoundBitmap.getRoundBitmap(getArtAlbum(Integer.parseInt(contentItem.getItem().getId()
								.substring(11))), 400));
						}else {
							albumImageView.setImageBitmap(RoundBitmap.getRoundBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.album), 400));
						}
				}else {
					if (mediaRenderer.getAlbumBitmap() != null) {
						albumImageView.setImageBitmap(RoundBitmap.getRoundBitmap(mediaRenderer.getAlbumBitmap(), 400));
					}else {
						albumImageView.setImageBitmap(RoundBitmap.getRoundBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.album), 400));
					}
					
				}
				
				actionBar.setTitle(contentItem.getItem().getTitle());
				actionBar.setSubtitle(contentItem.getItem().getCreator());
				if (mediaRenderer.getTransportState() != null && mediaRenderer.getTransportState() == TransportState.PLAYING) {
					playImageButton.setImageResource(R.drawable.pause);
					playImageButton.setTag(R.drawable.pause);
				}else {
					playImageButton.setImageResource(R.drawable.play);
					playImageButton.setTag(R.drawable.play);
				}
				
			}
			currentTimeTextView.setText(getTimeStr((int)mediaRenderer.getCurrentTime()));
			totalTimeTextView.setText(getTimeStr((int)mediaRenderer.getDuration()));
			if (mediaRenderer.getDuration() > 0) {
				timeSeekBar.setProgress((int) (mediaRenderer.getCurrentTime() * 100/mediaRenderer.getDuration()));
			}
			mediaRenderer.setListener(this);
			if (mediaRenderer.getPlayMode() == DLNAMediaRenderer.PLAY_MODE_REPAET_ONE) {
				playModeImageButton.setImageResource(R.drawable.play_list_mode_repeat_one);
				playModeImageButton.setTag(R.drawable.play_list_mode_repeat_one);
			}else if (mediaRenderer.getPlayMode() == DLNAMediaRenderer.PLAY_MODE_SHUFFLE) {
				playModeImageButton.setImageResource(R.drawable.play_list_mode_shuffle);
				playModeImageButton.setTag(R.drawable.play_list_mode_shuffle);
			}else {
				playModeImageButton.setImageResource(R.drawable.play_list_mode_sequent);
				playModeImageButton.setTag(R.drawable.play_list_mode_sequent);
			}
			
			
		}
		
		
		playImageButton.setOnClickListener(this);
		nextImageButton.setOnClickListener(this);
		previousImageButton.setOnClickListener(this);
		playlistImageButton.setOnClickListener(this);
		playModeImageButton.setOnClickListener(this);
		
		timeSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				if (application.getRenderer() != null) {
					application.getRenderer().seek(seekBar.getProgress());
				}else {
					seekBar.setProgress(0);
				}
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
		
		
//		actionBar.setHomeAsUpIndicator(R.drawable.actionbar_home);

		actionBar.setDisplayHomeAsUpEnabled(true);
		
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		case R.id.player_setting:
			Intent intent = new Intent(PlayerActivity.this, PlaylistActivity.class);
			intent.putExtra("mode", PlaylistActivity.PLAY_LIST_MODE_VOLUME);
			startActivity(intent);
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		Log.v("play activity", "on create options menu");
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.player, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.play_pause:
			if (playImageButton.getTag().equals(R.drawable.play)) {
				if (application.getRenderer() != null) {
					application.getRenderer().play();
//					playImageButton.setTag(R.drawable.pause);
//					playImageButton.setImageResource(R.drawable.pause);
				}
				
			}else {
				if (application.getRenderer() != null) {
					application.getRenderer().pause();
//					playImageButton.setTag(R.drawable.play);
//					playImageButton.setImageResource(R.drawable.play);
				}
				
			}
			
			break;
		case R.id.next_song:
			if (application.getRenderer() != null) {
				application.getRenderer().playNextSong();
			}
			
			break;
		case R.id.previous_song:
			if (application.getRenderer() != null) {
				application.getRenderer().playPreviousSong();
			}
			
			break;
			
		case R.id.play_list_button:
			if (application.getRenderer() != null ) {
				if (application.getRenderer().getIsLocalRenderer()) {
					LocalMediaRenderer mediaRenderer = (LocalMediaRenderer)application.getRenderer();
					if (mediaRenderer.getContentType() == LocalMediaRenderer.CONTENT_TYPE_RADIO) {
						if (mediaRenderer.getStations() != null && mediaRenderer.getStations().size() > 0) {
							Intent intent = new Intent(PlayerActivity.this, PlaylistActivity.class);
							intent.putExtra("mode", PlaylistActivity.PLAY_LIST_MODE_SONG);
							startActivity(intent);
							break;
						}
					}
				}
				if(application.getRenderer().getContentItems() != null && application.getRenderer().getContentItems().size() > 0){
					Intent intent = new Intent(PlayerActivity.this, PlaylistActivity.class);
					intent.putExtra("mode", PlaylistActivity.PLAY_LIST_MODE_SONG);
					startActivity(intent);
				}
				
			}
			
			break;
			
		case R.id.play_mode_button:
			if (playModeImageButton.getTag() != null && playModeImageButton.getTag().equals(R.drawable.play_list_mode_repeat_one)) {
				playModeImageButton.setImageResource(R.drawable.play_list_mode_sequent);
				playModeImageButton.setTag(R.drawable.play_list_mode_sequent);
				if (application.getRenderer() != null) {
					application.getRenderer().setPlayMode(DLNAMediaRenderer.PLAY_MODE_SEQUENT);
				}
			}else if (playModeImageButton.getTag() != null && playModeImageButton.getTag().equals(R.drawable.play_list_mode_sequent)) {
				playModeImageButton.setImageResource(R.drawable.play_list_mode_shuffle);
				playModeImageButton.setTag(R.drawable.play_list_mode_shuffle);
				if (application.getRenderer() != null) {
					application.getRenderer().setPlayMode(DLNAMediaRenderer.PLAY_MODE_SHUFFLE);
				}
			}else {
				playModeImageButton.setImageResource(R.drawable.play_list_mode_repeat_one);
				playModeImageButton.setTag(R.drawable.play_list_mode_repeat_one);
				if (application.getRenderer() != null) {
					application.getRenderer().setPlayMode(DLNAMediaRenderer.PLAY_MODE_REPAET_ONE);
				}
			}
		default:
			break;
		}
	}

	@Override
	public void setMusicInfo(final ContentItem contentItem, final Boolean isLocalContent) {
		// TODO Auto-generated method stub
		
		playModeImageButton.setEnabled(true);
		nextImageButton.setEnabled(true);
		previousImageButton.setEnabled(true);
		
		PlayerActivity.this.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				actionBar.setTitle(contentItem.getItem().getTitle());
				actionBar.setSubtitle(contentItem.getItem().getCreator());
				
				if (isLocalContent) {
					if (getArtAlbum(Integer.parseInt(contentItem.getItem().getId()
						.substring(11))) != null) {
						albumImageView.setImageBitmap(RoundBitmap.getRoundBitmap(getArtAlbum(Integer.parseInt(contentItem.getItem().getId()
							.substring(11))),400));
					}else {
						albumImageView.setImageBitmap(RoundBitmap.getRoundBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.album), 400));
					}
				}
			}
		});
	}

	@Override
	public void setAlbumImage(Bitmap albumBitmap) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getPositionInfoReceived(final float duration, final float currentTime) {
		// TODO Auto-generated method stub
		PlayerActivity.this.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (duration > 0) {
					timeSeekBar.setProgress((int)(currentTime *100/duration));
				}
				currentTimeTextView.setText(getTimeStr((int)currentTime));
				totalTimeTextView.setText(getTimeStr((int)duration));
			}
		});
		
		
	}

	@Override
	public void getTransportInfoReceived(final TransportInfo transportInfo) {
		// TODO Auto-generated method stub
		PlayerActivity.this.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (transportInfo.getCurrentTransportState() == TransportState.PLAYING) {
					playImageButton.setImageResource(R.drawable.pause);
					playImageButton.setTag(R.drawable.pause);
				}else {
					playImageButton.setImageResource(R.drawable.play);
					playImageButton.setTag(R.drawable.play);
				}
			}
		});
	}
	
	public Bitmap getArtAlbum(long audioId) {
		String str = "content://media/external/audio/media/" + audioId
				+ "/albumart";
		Uri uri = Uri.parse(str);

		ParcelFileDescriptor pfd = null;
		try {
			pfd = this.getContentResolver().openFileDescriptor(uri, "r");
		} catch (FileNotFoundException e) {
			return null;
		}
		Bitmap bm;
		if (pfd != null) {
			FileDescriptor fd = pfd.getFileDescriptor();
			bm = BitmapFactory.decodeFileDescriptor(fd);
			return bm;
		}
		return null;
	}
	
	public String getTimeStr(int time){
		int hour = (int) time / 3600;
		int minute = (int) (time % 3600) / 60;
		int second = (int) time % 60;
		String str =  String.format("%02d", minute) + ":"
				+ String.format("%02d", second);
		return str;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
//		DLNAMediaRenderer mediaRenderer = application.getRenderer();
//        if (mediaRenderer != null) {
//        		mediaRenderer.setListener(null);
//		}
	}

	@Override
	public void setStationInfo(Station station) {
		// TODO Auto-generated method stub
		actionBar.setTitle(station.getName());
		playModeImageButton.setEnabled(false);
		nextImageButton.setEnabled(false);
		previousImageButton.setEnabled(false);
		if (application.getRenderer().getTransportState() == TransportState.PLAYING) {
			Log.v(TAG, "PLAYING");
			
			playImageButton.setImageResource(R.drawable.pause);
			playImageButton.setTag(R.drawable.pause);
		}else {
			Log.v(TAG, "PAUSE");
			playImageButton.setImageResource(R.drawable.play);
			playImageButton.setTag(R.drawable.play);
		}
		
	}
	
	
	

}

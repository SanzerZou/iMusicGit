package com.gcc.imusic.dlna;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Random;

//import org.teleal.cling.support.model.DIDLObject;
//import org.teleal.cling.support.model.TransportInfo;
//import org.teleal.cling.support.model.TransportState;




import org.fourthline.cling.support.model.DIDLObject;
import org.fourthline.cling.support.model.TransportInfo;
import org.fourthline.cling.support.model.TransportState;

import com.android.vtuner.data.BaseModel;
import com.android.vtuner.data.Station;
import com.android.vtuner.service.PlaybackBaseService.State;
import com.android.vtuner.service.PlaybackBaseServiceBinder.OnStateChangedListener;
import com.android.vtuner.service.PlaybackBaseServiceBinder.PlaybackServiceBinderCallback;
import com.gcc.imusic.callback.SeekCallBack.OnSeekListener;
import com.gcc.imusic.vtuner.PlaybackService;
import com.gcc.imusic.vtuner.PlaybackServiceBinder;

import android.R.integer;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.support.v4.media.TransportStateListener;
import android.util.Log;

public class LocalMediaRenderer extends DLNAMediaRenderer implements OnPreparedListener, OnCompletionListener, OnSeekCompleteListener, PlaybackServiceBinderCallback, OnStateChangedListener{
	
	public final static int CONTENT_TYPE_SONG = 1;
	public final static int CONTENT_TYPE_RADIO = 2;
	
	
	
	
	private MediaPlayer mediaPlayer;
	private PlaybackServiceBinder vPlayer;
	private List<BaseModel> stations;
	private Thread thread;
	private Boolean isThreadRunning;
	private int contentType;
	
	
	private final static String TAG = "LocalMediaRenderer";
	
	@Override
	public void playSong() {
		// TODO Auto-generated method stub
		
		Log.v(TAG, "play song");
		isThreadRunning = true;
		
		if (contentType == CONTENT_TYPE_SONG) {
			if (mediaPlayer != null) {
				mediaPlayer.reset();
			}else {
				mediaPlayer = new MediaPlayer();
				mediaPlayer.setOnCompletionListener(this);
				mediaPlayer.setOnPreparedListener(this);
				mediaPlayer.setOnSeekCompleteListener(this);
			}
			
			ContentItem contentItem = super.getCurrentContentItem();
			if (contentItem == null) {
				return;
			}
			super.getListener().setMusicInfo(contentItem, super.getIsLocalContent());
			if (!super.getIsLocalContent()) {
				URI uri = contentItem.getItem().getFirstPropertyValue(
						DIDLObject.Property.UPNP.ALBUM_ART_URI.class);
				if (uri != null) {
					getArtAlbum(uri.toString());
				}
			}
			
			try {
				mediaPlayer.setDataSource(contentItem.getItem().getFirstResource().getValue());
			} catch (IllegalArgumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SecurityException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IllegalStateException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			try {
				mediaPlayer.prepare();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if (contentType == CONTENT_TYPE_RADIO) {
			Station station = (Station)stations.get(super.getContentIndex());
			if(vPlayer.getCurrentState() != PlaybackService.State.stopped) {
				if (!station.getStationId().equals(vPlayer.getStation().getStationId())) {
					vPlayer.stop();	
					vPlayer.setStation(station);
					vPlayer.start();
					super.setTransportState(TransportState.PLAYING);
					super.getListener().getTransportInfoReceived(new TransportInfo(TransportState.PLAYING));
				} 
			}else {
				vPlayer.setStation(station);
				vPlayer.start();
				super.setTransportState(TransportState.PLAYING);
				super.getListener().getTransportInfoReceived(new TransportInfo(TransportState.PLAYING));
			}
			super.getListener().setStationInfo(station);
			
		}
		
		
		
		if (thread == null) {
			thread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						while (isThreadRunning) {
							if (contentType == CONTENT_TYPE_SONG) {
								if (LocalMediaRenderer.super.getListener() != null) {
									if (mediaPlayer != null && mediaPlayer.isPlaying()) {
										LocalMediaRenderer.super.setDuration(mediaPlayer.getDuration() / 1000);
										LocalMediaRenderer.super.setCurrentTime(mediaPlayer.getCurrentPosition() / 1000);
										LocalMediaRenderer.super.getListener().getPositionInfoReceived(mediaPlayer.getDuration()/1000, mediaPlayer.getCurrentPosition()/1000);
										LocalMediaRenderer.super.getListener().getTransportInfoReceived(new TransportInfo(TransportState.PLAYING));
									}else {
									
										LocalMediaRenderer.super.getListener().getTransportInfoReceived(new TransportInfo(TransportState.STOPPED));
									}
									
								}
							}
							
							Thread.sleep(1000);
							
						}
						
					} catch (Exception e) {
						// TODO: handle exception
					}
					
				}
			});
			thread.start();
		}
		
		
		
	}
	
	public Station getCurrentStation(){
		if (contentType == CONTENT_TYPE_SONG) {
			return null;
		}
		
		if (stations == null || stations.size() == 0) {
			return null;
		}
		
		if (super.getContentIndex() <0 || super.getContentIndex() >= stations.size()) {
			return null;
		}
		
		return (Station)stations.get(super.getContentIndex());
		
	}
	
	public void playVtuner(int index){
		
		Log.v(TAG, "play Vtuner" + index);
		Station station = (Station)stations.get(index);
		Log.v(TAG, "station "+ station.getName());
		Log.v(TAG, "current state "+vPlayer.getCurrentState());
		vPlayer.setStation(station);
		vPlayer.start();
		
//		if(vPlayer.getCurrentState() != PlaybackService.State.stopped) {
//			if (!station.getStationId().equals(vPlayer.getStation().getStationId())) {
//				vPlayer.stop();	
//				vPlayer.setStation(station);
//				vPlayer.start();
//			} 
//		}else {
//			vPlayer.setStation(station);
//			vPlayer.start();
//		}
		
	}
	
	public void initVPlayer(Context context){
		vPlayer = new PlaybackServiceBinder(context, this);
		vPlayer.doBindService(); 
		vPlayer.addOnStateChangedListener(this);
	}
	
	public void playNextSong(){
		if (super.getContentItems()== null || super.getContentItems().size() == 0) {
			return;
		}
		int contentIndex = super.getContentIndex();
		
		if (super.getPlayMode() == PLAY_MODE_REPAET_ONE) {
			
		}else if (super.getPlayMode() == PLAY_MODE_SHUFFLE) {
			Random r = new Random();
			contentIndex = r.nextInt(super.getContentItems().size());
		}else {
			contentIndex ++;
			if (contentIndex >= super.getContentItems().size()) {
				contentIndex = 0;
			}
		}
		super.setContentIndex(contentIndex);
		playSong();
		
	}
	
	public void playPreviousSong(){
		if (super.getContentItems()== null || super.getContentItems().size() == 0) {
			return;
		}
		int contentIndex = super.getContentIndex();
		
		if (super.getPlayMode() == PLAY_MODE_REPAET_ONE) {
			
		}else if (super.getPlayMode() == PLAY_MODE_SHUFFLE) {
			Random r = new Random();
			contentIndex = r.nextInt(super.getContentItems().size());
		}else {
			contentIndex --;
			if (contentIndex < 0) {
				contentIndex = super.getContentItems().size() -  1;
			}
		}
		super.setContentIndex(contentIndex);
		playSong();
			
		
	}
	
	public void pause(){
		super.setTransportState(TransportState.STOPPED);
		if (contentType == CONTENT_TYPE_SONG) {
			if (mediaPlayer != null && mediaPlayer.isPlaying()) {
				mediaPlayer.pause();
				super.setTransportState(TransportState.STOPPED);
				super.getListener().getTransportInfoReceived(new TransportInfo(TransportState.STOPPED));
			}
			
		}else if (contentType == CONTENT_TYPE_RADIO) {
			if (vPlayer != null && vPlayer.getCurrentState() == PlaybackService.State.playing) {
				vPlayer.pause();
				super.setTransportState(TransportState.STOPPED);
				super.getListener().getTransportInfoReceived(new TransportInfo(TransportState.STOPPED));
			}
		}
	}
	
	public void play(){
		super.setTransportState(TransportState.PLAYING);
		if (contentType == CONTENT_TYPE_SONG) {
			if (mediaPlayer != null) {
				mediaPlayer.start();
				super.setTransportState(TransportState.PLAYING);
				super.getListener().getTransportInfoReceived(new TransportInfo(TransportState.PLAYING));
			}
			
		}else if (contentType == CONTENT_TYPE_RADIO) {
			if (vPlayer != null) {
				vPlayer.play();
				super.setTransportState(TransportState.PLAYING);
				super.getListener().getTransportInfoReceived(new TransportInfo(TransportState.PLAYING));
			}
		}
	}
	
	public void seek(float progress){
		Log.v(TAG, "progress");
		if(contentType == CONTENT_TYPE_SONG){
			if (mediaPlayer != null) {
				Log.v(TAG, "seek");
				mediaPlayer.seekTo((int)(progress * super.getDuration() * 10));
			}
		}
	}
	
	

	public List<BaseModel> getStations() {
		return stations;
	}

	public void setStations(List<BaseModel> stations) {
		this.stations = stations;
	}

	public Boolean getIsThreadRunning() {
		return isThreadRunning;
	}

	public void setIsThreadRunning(Boolean isThreadRunning) {
		this.isThreadRunning = isThreadRunning;
	}
	
	

	public int getContentType() {
		return contentType;
	}

	public void setContentType(int contentType) {
		this.contentType = contentType;
	}


	
	

	@Override
	public void exit() {
		// TODO Auto-generated method stub
		super.exit();
		if (mediaPlayer != null) {
			mediaPlayer.release();
		}
		if (vPlayer != null) {
			vPlayer.doUnbindService();
		}
	}

	@Override
	public void onSeekComplete(MediaPlayer mp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		playNextSong();
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		// TODO Auto-generated method stub
		mp.start();
		super.setTransportState(TransportState.PLAYING);
	}

	@Override
	public void onStateChanged(State arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPlaybackServiceConnected() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStationReceived() {
		// TODO Auto-generated method stub
		
	}

}

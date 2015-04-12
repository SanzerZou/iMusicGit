package com.gcc.imusic.activity;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
//import org.teleal.cling.support.model.DIDLObject;
//import org.teleal.cling.support.model.PositionInfo;
//import org.teleal.cling.support.model.TransportInfo;
//import org.teleal.cling.support.model.TransportState;

import org.fourthline.cling.support.model.TransportInfo;
import org.fourthline.cling.support.model.TransportState;

import com.android.vtuner.data.Station;
import com.gcc.imusic.MusicApplication;
import com.gcc.imusic.R;
import com.gcc.imusic.dlna.ContentItem;
import com.gcc.imusic.dlna.DLNAMediaRenderer;
import com.gcc.imusic.dlna.DLNAMediaRenderer.OnMediaRendererListener;
import com.gcc.imusic.dlna.DLNAMediaServer;
import com.gcc.imusic.dlna.LocalMediaRenderer;
import com.gcc.imusic.utils.RoundBitmap;

import android.R.integer;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class BaseActivity extends ActionBarActivity implements OnClickListener, OnMediaRendererListener{
	
	public final static int SONG_PLAYER = 1;
	public final static int RADIO_PLAYER = 2;
	
	private final static String TAG = "BaseActivity";
	private ContentItem contentItem;
	private Station station;
	private MusicApplication application;
	private ImageButton playImageButton;
	private ImageButton nextImageButton;

	
	

	private ImageButton albumImageButton;
	private TextView songTitleTextView;
	private TextView artistTextView;
	
	public void initActivity(){
		application = (MusicApplication)getApplicationContext();
		
		albumImageButton = (ImageButton)findViewById(R.id.album_image);
		playImageButton = (ImageButton)findViewById(R.id.play_pause);
		nextImageButton = (ImageButton)findViewById(R.id.next_song);
		
		albumImageButton.setOnClickListener(this);
		playImageButton.setOnClickListener(this);
		nextImageButton.setOnClickListener(this);
		
		songTitleTextView = (TextView)findViewById(R.id.song_title);
		artistTextView = (TextView)findViewById(R.id.artist);
		
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.album);
        albumImageButton.setImageBitmap(RoundBitmap.getRoundBitmap(bitmap, 108));
        playImageButton.setImageResource(R.drawable.play);
		playImageButton.setTag(R.drawable.play);
//        albumImageButton.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent(BaseActivity.this, PlayerActivity.class);
//				startActivity(intent);
//			}
//		});
	
        DLNAMediaRenderer mediaRenderer = application.getRenderer();
        if (mediaRenderer != null) {
        		mediaRenderer.setListener(this);
        		if (mediaRenderer.getTransportState() == TransportState.PLAYING) {
				playImageButton.setImageResource(R.drawable.pause);
				playImageButton.setTag(R.drawable.pause);
			}else {
				playImageButton.setImageResource(R.drawable.play);
				playImageButton.setTag(R.drawable.play);
			}
		}
        
	}
	
	public void updatePlayer(){
		final DLNAMediaRenderer mediaRenderer = application.getRenderer();
		
		if (mediaRenderer != null) {
			if (mediaRenderer.getIsLocalRenderer() && ((LocalMediaRenderer)mediaRenderer).getContentType() == LocalMediaRenderer.CONTENT_TYPE_RADIO) {
				LocalMediaRenderer localMediaRenderer = ((LocalMediaRenderer)mediaRenderer);
				if (localMediaRenderer.getCurrentStation() != null) {
					final Station station = localMediaRenderer.getCurrentStation();
					BaseActivity.this.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							songTitleTextView.setText(station.getName());
							artistTextView.setText(station.getLocation());
							if (mediaRenderer.getTransportState() != null) {
								if (mediaRenderer.getTransportState() == TransportState.PLAYING) {
									playImageButton.setImageResource(R.drawable.pause);
									playImageButton.setTag(R.drawable.pause);
								}else {
									playImageButton.setImageResource(R.drawable.play);
									playImageButton.setTag(R.drawable.play);
								}
							}
							albumImageButton.setImageBitmap(RoundBitmap.getRoundBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.album), 108));
						}
					});
				}
				
			}else {
				contentItem = mediaRenderer.getCurrentContentItem();
				if (contentItem != null) {
					BaseActivity.this.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							songTitleTextView.setText(contentItem.getItem().getTitle());
							artistTextView.setText(contentItem.getItem().getCreator());
							if (mediaRenderer.getTransportState() != null) {
								if (mediaRenderer.getTransportState() == TransportState.PLAYING) {
									playImageButton.setImageResource(R.drawable.pause);
									playImageButton.setTag(R.drawable.pause);
								}else {
									playImageButton.setImageResource(R.drawable.play);
									playImageButton.setTag(R.drawable.play);
								}
							}
							
							if (mediaRenderer.getIsLocalContent()) {
								if (getArtAlbum(Integer.parseInt(contentItem.getItem().getId()
									.substring(11))) != null) {
									albumImageButton.setImageBitmap(getArtAlbum(Integer.parseInt(contentItem.getItem().getId()
										.substring(11))));
								}else {
									albumImageButton.setImageBitmap(RoundBitmap.getRoundBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.album), 108));
								}
							}else {
								if (mediaRenderer.getAlbumBitmap() != null) {
									albumImageButton.setImageBitmap(mediaRenderer.getAlbumBitmap());
								}else {
									albumImageButton.setImageBitmap(RoundBitmap.getRoundBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.album), 108));
								}
							}
							
							
							
						}
					});
				}
			}
		}
		}
	
	
//	public void setMusicInfo(){
//		final DLNAMediaRenderer mediaRenderer = application.getRenderer();
//		if (mediaRenderer != null) {
//			contentItem = mediaRenderer.getCurrentContentItem();
//			String serverUDN = mediaRenderer.getServerUDN();
//			DLNAMediaServer mediaServer = application.getDmc().getServerWithUDN(serverUDN);
//			if (mediaServer != null) {
//				if (mediaServer.getIsLocalServer()) {
//					if (getArtAlbum(Integer.parseInt(contentItem.getItem().getId()
//							.substring(11))) != null) {
////						albumImageButton.setImageBitmap(getArtAlbum(Integer
////								.parseInt(contentItem.getItem().getId()
////										.substring(11))));
//						if (mediaRenderer.getAlbumBitmap() != null) {
//							mediaRenderer.getAlbumBitmap().recycle();
//						}
//						mediaRenderer.setAlbumBitmap(getArtAlbum(Integer
//								.parseInt(contentItem.getItem().getId()
//										.substring(11))));
//					} else {
//						//albumImageButton.setImageResource(R.drawable.defaultcover);
//					}
//				}else {
//					URI uri = contentItem.getItem().getFirstPropertyValue(
//							DIDLObject.Property.UPNP.ALBUM_ART_URI.class);
//					if (uri != null) {
//						getArtAlbum(uri.toString());
//					}else {
//						//albumImage.setImageResource(R.drawable.defaultcover);
//					}
//				}
//			}
//
//			updatePlayer();
//		}
//	}
//	public void setAlbum(){
//		DLNAMediaServer mediaServer = application.getServer();
//		if (mediaServer != null) {
//			if (mediaServer.getIsLocalServer()) {
//				BaseActivity.this.runOnUiThread(new Runnable() {
//					
//					@Override
//					public void run() {
//						// TODO Auto-generated method stub
//						if (getArtAlbum(Integer.parseInt(contentItem.getItem().getId()
//								.substring(11))) != null) {
////							albumImageButton.setImageBitmap(getArtAlbum(Integer
////									.parseInt(contentItem.getItem().getId()
////											.substring(11))));
//							
//						} else {
//							//albumImageButton.setImageResource(R.drawable.defaultcover);
//						}
//					}
//				});
//			}
//		}
//	}
	
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

//	public void getArtAlbum(final String urlStr) {
//
//		new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				try {
//					HttpGet httpRequest = new HttpGet(urlStr);
//					HttpClient httpclient = new DefaultHttpClient();
//					 HttpResponse httpResponse = httpclient.execute(httpRequest);  
//			            if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){  
//			                  
//			                HttpEntity httpEntity = httpResponse.getEntity();  
//			                
//			                InputStream is = httpEntity.getContent();  
//
//			                final Bitmap bitmap = BitmapFactory.decodeStream(is);  
//			                is.close();  
//			                BaseActivity.this.runOnUiThread(new Runnable() {
//								@Override
//								public void run() {
//									// TODO Auto-generated method stub
//									if (bitmap != null) {
//										final DLNAMediaRenderer mediaRenderer = application.getRenderer();
//										albumImageButton.setImageBitmap(bitmap);
//										if (mediaRenderer.getAlbumBitmap() != null) {
//											mediaRenderer.getAlbumBitmap().recycle();
//										}
//										mediaRenderer.setAlbumBitmap(bitmap);
//									} else {
////										albumImageButton
////												.setImageResource(R.drawable.defaultcover);
//									}
//								}
//							});  
//			            } 
//					
//
//				} catch (Exception e) {
//					// TODO: handle exception
//				}
//			}
//		}).start();
//
//	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.v(TAG, "onResume");
		DLNAMediaRenderer mediaRenderer = application.getRenderer();
        if (mediaRenderer != null) {
        		Log.v(TAG, "setRendererListener");
        		mediaRenderer.setListener(this);
		}
		updatePlayer();
	}
	
	
	
	

@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.v(TAG, "onPause");
//		DLNAMediaRenderer mediaRenderer = application.getRenderer();
//		if (mediaRenderer != null) {
//			mediaRenderer.setListener(null);
//		}
	    
	}

@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.v(TAG, "onDestroy");
		
	}

@Override
public void setMusicInfo(final ContentItem contentItem, final Boolean isLocalContent) {
	// TODO Auto-generated method stub
	Log.v(TAG, "setMusicInfo");
	
		Log.v(TAG, "Local Server");
		BaseActivity.this.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				songTitleTextView.setText(contentItem.getItem().getTitle());
				artistTextView.setText(contentItem.getItem().getCreator());
				if (isLocalContent) {
					if (getArtAlbum(Integer.parseInt(contentItem.getItem().getId()
						.substring(11))) != null) {
						albumImageButton.setImageBitmap(RoundBitmap.getRoundBitmap(getArtAlbum(Integer.parseInt(contentItem.getItem().getId()
							.substring(11))), 108));
					}else {
						albumImageButton.setImageBitmap(RoundBitmap.getRoundBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.album), 108));
					}
				}
				
			}
		});

}

@Override
public void setAlbumImage(final Bitmap albumBitmap) {
	// TODO Auto-generated method stub
	BaseActivity.this.runOnUiThread(new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			albumImageButton.setImageBitmap(RoundBitmap.getRoundBitmap(albumBitmap, 108));
		}
	});  
}

@Override
public void getPositionInfoReceived(float duration, float currentTime) {
	// TODO Auto-generated method stub
	
}

@Override
public void getTransportInfoReceived(final TransportInfo transportInfo) {
	// TODO Auto-generated method stub
	BaseActivity.this.runOnUiThread(new Runnable() {
		
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

@Override
public void onClick(View v) {
	// TODO Auto-generated method stub
	switch (v.getId()) {
	case R.id.album_image:
		Intent intent = new Intent(BaseActivity.this, PlayerActivity.class);
		startActivity(intent);
		break;
	
	case R.id.play_pause:
		DLNAMediaRenderer mediaRenderer = application.getRenderer();
		if (mediaRenderer != null) {
			if (playImageButton.getTag().equals(R.drawable.play)) {
				mediaRenderer.play();
			}else {
				mediaRenderer.pause();
			}
			
		}
		break;
	case R.id.next_song:
		Log.v(TAG, "next song");
		DLNAMediaRenderer tempMediaRenderer = application.getRenderer();
		if (tempMediaRenderer != null) {
			tempMediaRenderer.playNextSong();
		}
		break;

	default:
		break;
	}
}

@Override
public void setStationInfo(Station station) {
	// TODO Auto-generated method stub
	
}
	
	
}

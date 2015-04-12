package com.gcc.imusic.activity;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.UUID;

import org.fourthline.cling.android.AndroidUpnpService;
import org.fourthline.cling.model.types.UDN;
import org.fourthline.cling.support.model.DIDLObject;
import org.fourthline.cling.support.model.PersonWithRole;
import org.fourthline.cling.support.model.Res;
//import org.fourthline.cling.support.model.Res;
import org.fourthline.cling.support.model.WriteStatus;
import org.fourthline.cling.support.model.container.Container;
import org.fourthline.cling.support.model.item.MusicTrack;
//import org.teleal.cling.android.AndroidUpnpService;
//import org.teleal.cling.model.types.UDN;
//import org.teleal.cling.support.model.DIDLObject;
//import org.teleal.cling.support.model.PersonWithRole;
//import org.teleal.cling.support.model.Res;
//import org.teleal.cling.support.model.WriteStatus;
//import org.teleal.cling.support.model.container.Container;
//import org.teleal.cling.support.model.item.MusicTrack;
//import org.teleal.common.util.MimeType;

import org.seamless.util.MimeType;

import com.gcc.imusic.MusicApplication;
import com.gcc.imusic.R;
import com.gcc.imusic.dlna.DLNAMediaController;
import com.gcc.imusic.dlna.DLNAMediaRenderer;
import com.gcc.imusic.dlna.DLNAMediaServer;
import com.gcc.imusic.dlna.LocalMediaRenderer;
import com.gcc.imusic.service.WireUpnpService;
import com.wireme.mediaserver.ContentNode;
import com.wireme.mediaserver.ContentTree;
import com.wireme.mediaserver.MediaServer;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ImageView;

public class LaunchActivity extends ActionBarActivity{

	private final static String TAG = "LaunchActivity";
	
	private AndroidUpnpService upnpService;
	private MediaServer mediaServer;
	private ServiceConnection serviceConnection;
	private static boolean serverPrepared = false;
	private MusicTrack musicTrack;
	private MusicApplication application;
	
//	private ImageView launchImageView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		

		setContentView(R.layout.activity_launch);
//		launchImageView = (ImageView)findViewById(R.id.launch_image);
//		launchImageView.setImageResource(R.drawable.launch);
		application = (MusicApplication)getApplicationContext();
		application.setLaunchActivity(this);
		serviceConnection = new ServiceConnection() {

			public void onServiceConnected(ComponentName className, IBinder service) {
				
				upnpService = (AndroidUpnpService) service;
				Log.v(TAG, "onServiceConnected");
				application.setDmc(new DLNAMediaController());
				application.getDmc().setUpnpService(upnpService);
			    startMediaServer();
			}

			public void onServiceDisconnected(ComponentName className) {
				upnpService = null;
				Log.v(TAG, "onServiceDisconnected");
			}
		};
		
		getApplicationContext().bindService(
				new Intent(LaunchActivity.this, WireUpnpService.class), serviceConnection,
				Context.BIND_AUTO_CREATE);
		
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LaunchActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
			
		}, 3000);
		
		
		
		
	}
	
	
	private void prepareMediaServer() {

		if (serverPrepared)
			return;

		ContentNode rootNode = ContentTree.getRootNode();
		// Video Container
        Cursor cursor;
        Container playlistContainer = new Container(ContentTree.PLAYLIST,
				ContentTree.AUDIO, "Playlist", "GNaP MediaServer",
				new DIDLObject.Class("object.container"), 0);
        Container albumContainer = new Container(ContentTree.ALBUM,
				ContentTree.AUDIO, "Album", "GNaP MediaServer",
				new DIDLObject.Class("object.container"), 0);
		Container artistContainer = new Container(ContentTree.ARTIST,
				ContentTree.AUDIO, "Artist", "GNaP MediaServer",
				new DIDLObject.Class("object.container"), 0);
		Container genreContainer = new Container(ContentTree.GENRE,
				ContentTree.AUDIO, "Genre", "GNaP MediaServer",
				new DIDLObject.Class("object.container"), 0);
		Container songContainer = new Container(ContentTree.SONG,
				ContentTree.AUDIO, "Song", "GNaP MediaServer",
				new DIDLObject.Class("object.container"), 0);
		artistContainer.setRestricted(true);
		artistContainer.setWriteStatus(WriteStatus.NOT_WRITABLE);
		albumContainer.setRestricted(true);
		albumContainer.setWriteStatus(WriteStatus.NOT_WRITABLE);
		playlistContainer.setRestricted(true);
		playlistContainer.setWriteStatus(WriteStatus.NOT_WRITABLE);
		genreContainer.setRestricted(true);
		genreContainer.setWriteStatus(WriteStatus.NOT_WRITABLE);
		songContainer.setRestricted(true);
		songContainer.setWriteStatus(WriteStatus.NOT_WRITABLE);
		rootNode.getContainer().addContainer(artistContainer);
		rootNode.getContainer().addContainer(albumContainer);
		rootNode.getContainer().addContainer(playlistContainer);
		rootNode.getContainer().addContainer(genreContainer);
		rootNode.getContainer().addContainer(songContainer);
		rootNode.getContainer().setChildCount(rootNode.getContainer().getChildCount() + 1);
		ContentTree.addNode(ContentTree.ARTIST, new ContentNode(
				ContentTree.ARTIST, artistContainer));
		ContentTree.addNode(ContentTree.ALBUM, new ContentNode(
				ContentTree.ALBUM, albumContainer));
		ContentTree.addNode(ContentTree.PLAYLIST, new ContentNode(
				ContentTree.PLAYLIST, playlistContainer));
		ContentTree.addNode(ContentTree.GENRE, new ContentNode(
				ContentTree.GENRE, genreContainer));
		ContentTree.addNode(ContentTree.SONG, new ContentNode(
				ContentTree.SONG, songContainer));
		HashMap<String,Container> artist_info = new HashMap<String,Container>();
        HashMap<String,Container> album_info = new HashMap<String,Container>();
        HashMap<String,Container> genre_info = new HashMap<String,Container>();
		String[] audioColumns = { MediaStore.Audio.Media._ID,
				MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA,
				MediaStore.Audio.Media.ARTIST,
				MediaStore.Audio.Media.MIME_TYPE, MediaStore.Audio.Media.SIZE,
				MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.ALBUM ,
				};
		cursor = managedQuery(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				audioColumns, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				String id = ContentTree.AUDIO_PREFIX
						+ cursor.getInt(cursor
								.getColumnIndex(MediaStore.Audio.Media._ID));
				String title = cursor.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
				String creator = cursor.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
				String filePath = cursor.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
//				if (!filePath.endsWith(".mp3"))
//					continue;
				int index = filePath.lastIndexOf(".");
				if (index > 0) {
					title = title + filePath.substring(index);
				}
				
				String mimeType = cursor
						.getString(cursor
								.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE));
				Log.v(TAG, mimeType);
				String genre = getGenres(cursor.getInt(cursor
						.getColumnIndex(MediaStore.Audio.Media._ID)));
				long size = cursor.getLong(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
				long duration = cursor
						.getLong(cursor
								.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
				String album = cursor.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
				if(album == null || album.length() == 0 || album.equals("0")){
					album = "UNKNOWN";
				}
				

				Res res = new Res(new MimeType(mimeType.substring(0,
						mimeType.indexOf('/')), mimeType.substring(mimeType
						.indexOf('/') + 1)), size, "http://"
						+ mediaServer.getAddress() + "/" + id);
				
				res.setDuration(duration / (1000 * 60 * 60) + ":"
						+ (duration % (1000 * 60 * 60)) / (1000 * 60) + ":"
						+ (duration % (1000 * 60)) / 1000);
				musicTrack = new MusicTrack(id,
						creator, title, creator, album,
						new PersonWithRole(creator, "Performer"), res);
				
				Log.v(TAG,title+"  album= "+album);
				if ( artist_info.containsKey(creator)){
				
					artist_info.get(creator).addItem(musicTrack);	
					artist_info.get(creator).setChildCount(artist_info.get(creator).getChildCount()+1);
			        ContentTree.addNode(id, new ContentNode(id, musicTrack,
					filePath));
//			        Log.v("duration",musicTrack.getFirstResource().getDuration());
					
				}
				else {
					
					Container creatorContainer = new Container(creator,
							ContentTree.ARTIST, creator, "GNaP MediaServer",
							new DIDLObject.Class("object.container"),0);
					creatorContainer.setRestricted(true);
					creatorContainer.setWriteStatus(WriteStatus.NOT_WRITABLE);
					artistContainer.addContainer(creatorContainer);
					artistContainer.setChildCount(
							artistContainer.getChildCount() + 1);
					ContentTree.addNode(creator, new ContentNode(
							creator, creatorContainer));
					artist_info.put(creator, creatorContainer);
					artist_info.get(creator).addItem(musicTrack);
					artist_info.get(creator).setChildCount(artist_info.get(creator).getChildCount()+1);
					ContentTree.addNode(id, new ContentNode(id, musicTrack,
							filePath));
//					Log.v(LOGTAG, "artist " + id + "from " + filePath);
					
				}
				if ( album_info.containsKey(album)){
					album_info.get(album).addItem(musicTrack);
					album_info.get(album).setChildCount(album_info.get(album).getChildCount()+1);
//					ContentTree.addNode(id, new ContentNode(id, musicTrack,filePath));
				}
				else {
					
					Container container = new Container(album,
							ContentTree.ALBUM, album, "GNaP MediaServer", 
							new DIDLObject.Class("object.container"),0);
					container.setRestricted(true);
					container.setWriteStatus(WriteStatus.NOT_WRITABLE);
					albumContainer.addContainer(container);
					albumContainer.setChildCount(
							albumContainer.getChildCount() + 1);
					ContentTree.addNode(album, new ContentNode(
							album, container));
					album_info.put(album, container);
					album_info.get(album).addItem(musicTrack);
					album_info.get(album).setChildCount(album_info.get(album).getChildCount()+1);
//					ContentTree.addNode(id, new ContentNode(id, musicTrack,filePath));					
				}
				if ( genre_info.containsKey(genre)){
					genre_info.get(genre).addItem(musicTrack);
					genre_info.get(genre).setChildCount(genre_info.get(genre).getChildCount()+1);
//					ContentTree.addNode(id, new ContentNode(id, musicTrack,filePath));
				}
				else {
					
					Container gcontainer = new Container(genre,
							ContentTree.GENRE, genre, "GNaP MediaServer",
							new DIDLObject.Class("object.container"),0);
					gcontainer.setRestricted(true);
					gcontainer.setWriteStatus(WriteStatus.NOT_WRITABLE);
					genreContainer.addContainer(gcontainer);
					genreContainer.setChildCount(genreContainer.getChildCount() + 1);
					ContentTree.addNode(genre, new ContentNode(
							genre, gcontainer));
					genre_info.put(genre, gcontainer);
					genre_info.get(genre).addItem(musicTrack);
					genre_info.get(genre).setChildCount(genre_info.get(genre).getChildCount()+1);
//					ContentTree.addNode(id, new ContentNode(id, musicTrack,filePath));					
				}
				songContainer.addItem(musicTrack);
				
				songContainer.setChildCount(songContainer.getChildCount() + 1);
//		        ContentTree.addNode(id, new ContentNode(id, musicTrack,filePath));
//				Log.v("songcontainer","addsuccess");
//				Log.v(LOGTAG, "" + creator+title+album+genre+"from " + filePath);
			} while (cursor.moveToNext());
			
		}	
		serverPrepared = true;
	}
	
    private String getGenres(int audioId){
        Uri uri = Uri.parse("content://media/external/audio/media/" + audioId+ "/genres");
        ContentResolver mContentResolver = this.getContentResolver();
        Cursor c = mContentResolver.query(uri, new String[]{MediaStore.Audio.GenresColumns.NAME}, null, null, null);
        if(c.moveToFirst()){
            String genre = c.getString(c.getColumnIndex(MediaStore.Audio.GenresColumns.NAME));
            if(genre.length() == 0){
            	return "UNKNOWN";
            }
            c.close();
            return genre;
        }
        
        return "UNKNOWN";
    }

	// FIXME: now only can get wifi address
	private InetAddress getLocalIpAddress() throws UnknownHostException {
		WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int ipAddress = wifiInfo.getIpAddress();
		return InetAddress.getByName(String.format("%d.%d.%d.%d",
				(ipAddress & 0xff), (ipAddress >> 8 & 0xff),
				(ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff)));
	}


	public void startMediaServer() {
		// TODO Auto-generated method stub
		Log.v(TAG, "Connected to UPnP Service");
		if (mediaServer == null) {
			try {
				Log.v(TAG, "MediaServer is null");
				
				UUID uuid = null;
				SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
				String udnStr = sharedPreferences.getString("localServerUDN", null);
				
				if (udnStr != null ) {
					uuid = UUID.fromString(udnStr);
				}else {
					uuid = UUID.randomUUID();
					Editor editor = sharedPreferences.edit();
					editor.putString("localServerUDN", uuid.toString());
					editor.commit();
				}
				
				mediaServer = new MediaServer(getLocalIpAddress(), new UDN(uuid));
				
				DLNAMediaServer server = new DLNAMediaServer(mediaServer.getDevice());
				server.setIsLocalServer(true);
				server.setUpnpService(upnpService);
				
				application.getDmc().getServers().add(server);
				
				
				
				LocalMediaRenderer renderer = new LocalMediaRenderer();
				renderer.setUdn(new UDN("0"));
				renderer.initVPlayer(application);
				renderer.setIsLocalRenderer(true);
				renderer.setFriendlyName(mediaServer.getDevice().getDetails().getFriendlyName());
				
				application.getDmc().getRenderers().add(renderer);
				upnpService.getRegistry()
				.addDevice(mediaServer.getDevice());
				
				//application.getDmc().deviceAdded(mediaServer.getDevice());
				//Log.v("dlna","add success!");
				//application.getDmc().deviceAdded(mediaServer.getDevice());
				//application.getDmc().setDeviceName(mediaServer.getDevice().getDetails().getFriendlyName());
				prepareMediaServer();
				
			} catch (Exception ex) {
				// TODO: handle exception
				Log.v(TAG, "exception "+ex);
				Log.v(TAG, "Creating demo device failed");
//				return;
			}
		}

		Log.v(TAG, "prepare media server finish");
		// Getting ready for future device advertisements
		application.getDmc().addDeviceListRegistryListener();
		Log.v(TAG, "add device list registery");
		// Refresh device list
		upnpService.getControlPoint().search();
		Log.v(TAG, "search");
	}


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//launchImageView.getDrawingCache().recycle();
	}
	
	
	public void exit(){
//		new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				LaunchActivity.this.getApplicationContext().unbindService(serviceConnection);
//				Log.v(TAG, "unbindService");
//				mediaServer.stop();
//			}
//		}).start();
		getApplicationContext().unbindService(serviceConnection);
		Log.v(TAG, "unbindService");
		mediaServer.stop();
		System.exit(0);
		
	}
	
	
}

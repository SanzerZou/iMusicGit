package com.gcc.imusic.dlna;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
//import org.teleal.cling.android.AndroidUpnpService;
//import org.teleal.cling.model.action.ActionInvocation;
//import org.teleal.cling.model.message.UpnpResponse;
//import org.teleal.cling.model.meta.Device;
//import org.teleal.cling.model.meta.Service;
//import org.teleal.cling.model.types.UDAServiceType;
//import org.teleal.cling.model.types.UDN;
//import org.teleal.cling.model.types.UnsignedIntegerFourBytes;
//import org.teleal.cling.support.model.DIDLObject;
//import org.teleal.cling.support.model.MediaInfo;
//import org.teleal.cling.support.model.PositionInfo;
//import org.teleal.cling.support.model.SeekMode;
//import org.teleal.cling.support.model.TransportInfo;
//import org.teleal.cling.support.model.TransportState;

import org.fourthline.cling.android.AndroidUpnpService;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.ServiceType;
import org.fourthline.cling.model.types.UDAServiceType;
import org.fourthline.cling.model.types.UDN;
import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;
import org.fourthline.cling.model.types.UnsignedIntegerTwoBytes;
import org.fourthline.cling.support.model.DIDLObject;
import org.fourthline.cling.support.model.PositionInfo;
import org.fourthline.cling.support.model.SeekMode;
import org.fourthline.cling.support.model.TransportInfo;
import org.fourthline.cling.support.model.TransportState;

import android.R.integer;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import com.android.vtuner.data.Station;
import com.gcc.imusic.activity.BaseActivity;
import com.gcc.imusic.callback.GetMediaInfoCallBack.OnGetMediaInfoListener;
import com.gcc.imusic.callback.GetPositionInfoCallBack;
import com.gcc.imusic.callback.GetPositionInfoCallBack.OnGetPositionInfoListener;
import com.gcc.imusic.callback.GetTransportInfoCallBack;
import com.gcc.imusic.callback.GetTransportInfoCallBack.OnGetTransportInfoListener;
import com.gcc.imusic.callback.GetVolumeCallBack;
import com.gcc.imusic.callback.GetVolumeCallBack.OnGetVolumeListener;
import com.gcc.imusic.callback.PauseCallBack;
import com.gcc.imusic.callback.PlayCallBack;
import com.gcc.imusic.callback.PauseCallBack.OnPauseListener;
import com.gcc.imusic.callback.PlayCallBack.OnPlayListener;
import com.gcc.imusic.callback.SeekCallBack;
import com.gcc.imusic.callback.SeekCallBack.OnSeekListener;
import com.gcc.imusic.callback.SetAVTransportURICallBack;
import com.gcc.imusic.callback.SetAVTransportURICallBack.OnSetAVTransportURIListener;
import com.gcc.imusic.callback.SetVolumeCallBack;
import com.gcc.imusic.callback.SetVolumeCallBack.OnSetVolumeListener;
import com.gcc.imusic.callback.StopCallBack;
import com.gcc.imusic.callback.StopCallBack.OnStopListener;
import com.gcc.imusic.callback.SetDevName;




public class DLNAMediaRenderer implements OnSetAVTransportURIListener, OnPlayListener, OnStopListener, OnPauseListener, OnGetPositionInfoListener, OnGetTransportInfoListener, OnGetVolumeListener,OnSetVolumeListener, OnSeekListener{
	private final static String TAG = "DLNAMediaRenderer";
	
	public final static int PLAY_MODE_REPAET_ONE = 1;
	public final static int PLAY_MODE_SEQUENT = 2;
	public final static int PLAY_MODE_SHUFFLE = 3;
	private int playMode;
	
	
	private int playingCount;
	
	private AndroidUpnpService upnpService;
	private UDN udn;
	private Device device;
	private String friendlyName;
	private List<ContentItem> contentItems;
	private int contentIndex = 0;
	
	private Boolean isLocalRenderer;
//	private Boolean isCurrentRenderer;
	private Boolean isThreadRunning;
	private Boolean isLocalContent;
	private Boolean isTransportLocked;
	
	private Bitmap albumBitmap;
	private float duration;
	private float currentTime;
	private TransportState transportState;
	
	private OnMediaRendererListener listener;
	
//	private String serverUDN;
	
	private Thread thread;
	
	private int currentVolume;
	
	
	
	
	
	public DLNAMediaRenderer() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DLNAMediaRenderer(Device device){
		this.device = device;
		this.udn = device.getIdentity().getUdn();
		this.friendlyName = device.getDetails().getFriendlyName();
		this.isThreadRunning = true;
		this.isTransportLocked = false;
		this.transportState = TransportState.PLAYING;
		this.playingCount = 0;
		this.playMode = PLAY_MODE_SEQUENT;
	}
	
	public AndroidUpnpService getUpnpService() {
		return upnpService;
	}
	public void setUpnpService(AndroidUpnpService upnpService) {
		this.upnpService = upnpService;
	}
	public UDN getUdn() {
		return udn;
	}
	public void setUdn(UDN udn) {
		this.udn = udn;
	}
	public Device getDevice() {
		return device;
	}
	public void setDevice(Device device) {
		this.device = device;
	}
	public String getFriendlyName() {
		return friendlyName;
	}
	public void setFriendlyName(String friendlyName) {
		this.friendlyName = friendlyName;
	}
	
	
//	public Boolean isLocalRenderer(){
//		if (device == null && udn.equals(new UDN("0"))) {
//			return true;
//		}
//		return false;
//	}

	public Boolean getIsLocalRenderer() {
		return isLocalRenderer;
	}

	public void setIsLocalRenderer(Boolean isLocalRenderer) {
		this.isLocalRenderer = isLocalRenderer;
	}
	
	
	
	public List<ContentItem> getContentItems() {
		return contentItems;
	}

	public void setContentItems(List<ContentItem> contentItems) {
		this.contentItems = contentItems;
	}

	public int getContentIndex() {
		return contentIndex;
	}

	public void setContentIndex(int contentIndex) {
		this.contentIndex = contentIndex;
	}
	
	public ContentItem getCurrentContentItem(){
		if (contentItems != null && contentItems.size() > contentIndex) {
			return contentItems.get(contentIndex);
		}
		return null;
	}

	
	public Bitmap getAlbumBitmap() {
		return albumBitmap;
	}

	public void setAlbumBitmap(Bitmap albumBitmap) {
		this.albumBitmap = albumBitmap;
	}
	
	
	

//	public String getServerUDN() {
//		return serverUDN;
//	}
//
//	public void setServerUDN(String serverUDN) {
//		this.serverUDN = serverUDN;
//	}

	public Boolean getIsThreadRunning() {
		return isThreadRunning;
	}

	public void setIsThreadRunning(Boolean isThreadRunning) {
		this.isThreadRunning = isThreadRunning;
	}

	public Boolean getIsLocalContent() {
		return isLocalContent;
	}

	public void setIsLocalContent(Boolean isLocalContent) {
		this.isLocalContent = isLocalContent;
	}

	public Boolean getIsTransportLocked() {
		return isTransportLocked;
	}

	public void setIsTransportLocked(Boolean isTransportLocked) {
		this.isTransportLocked = isTransportLocked;
	}

	public float getDuration() {
		return duration;
	}

	public void setDuration(float duration) {
		this.duration = duration;
	}

	public float getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(float currentTime) {
		this.currentTime = currentTime;
	}

	public TransportState getTransportState() {
		return transportState;
	}

	public void setTransportState(TransportState transportState) {
		this.transportState = transportState;
	}

	public OnMediaRendererListener getListener() {
		return listener;
	}

	public void setListener(OnMediaRendererListener listener) {
		this.listener = listener;
	}
	
	

	public int getCurrentVolume() {
		return currentVolume;
	}

	public void setCurrentVolume(int currentVolume) {
		this.currentVolume = currentVolume;
	}

	public void playSong(){
		
		if (contentItems.size() == 0) 
			return;
		if (contentIndex >= contentItems.size()) 
			return;
		isTransportLocked = true;
		playingCount = 0;
		if (thread == null) {
			thread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						while (isThreadRunning) {
							
//							if (!isTransportLocked) {
//								
//							}
							getPositionInfo();
							getTransportInfo();
							Thread.sleep(1000);
//							if (isCurrentRenderer) {
//								
//							}else {
//								getTransportInfo();
//							}
						}
						thread = null;
						
					} catch (Exception e) {
						// TODO: handle exception
					}
					
				}
			});
			thread.start();
		}
		
		if (listener != null) {
			listener.setMusicInfo(contentItems.get(contentIndex), isLocalContent);
		}
		if (transportState == TransportState.PLAYING) {
			stop();
		}else {
			setTransportURI();
		}
		if (!isLocalContent) {
			URI uri = contentItems.get(contentIndex).getItem().getFirstPropertyValue(
					DIDLObject.Property.UPNP.ALBUM_ART_URI.class);
			if (uri != null) {
				getArtAlbum(uri.toString());
			}
		}
		
	}
	
	public void exit(){
		isThreadRunning = false;
		if (albumBitmap != null) {
			albumBitmap.recycle();
		}
		
	}
	
	public void playNextSong() {
		if (contentItems == null || contentItems.size() == 0) {
			return;
		}
		if (playMode == PLAY_MODE_REPAET_ONE) {
			
		}else if (playMode == PLAY_MODE_SHUFFLE) {
			Random r = new Random();
			contentIndex = r.nextInt(contentItems.size());
		}else {
			contentIndex ++;
			if (contentIndex >= contentItems.size()) {
				contentIndex = 0;
			}
		}
		playSong();
		
	}
	
	public void playPreviousSong(){
		if (contentItems == null || contentItems.size() == 0) {
			return;
		}
		
		if (playMode == PLAY_MODE_REPAET_ONE) {
			
		}else if (playMode == PLAY_MODE_SHUFFLE) {
			Random r = new Random();
			contentIndex = r.nextInt(contentItems.size());
		}else {
			contentIndex --;
			if (contentIndex < 0) {
				contentIndex = contentItems.size() - 1;
			}
		}
		
		playSong();
	}

	public void getArtAlbum(final String urlStr) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					HttpGet httpRequest = new HttpGet(urlStr);
					HttpClient httpclient = new DefaultHttpClient();
					 HttpResponse httpResponse = httpclient.execute(httpRequest);  
			            if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){  
			                  
			                HttpEntity httpEntity = httpResponse.getEntity();  
			                
			                InputStream is = httpEntity.getContent();  

			                final Bitmap bitmap = BitmapFactory.decodeStream(is);  
			                is.close();  
			                if (albumBitmap != null) {
								albumBitmap.recycle();
							} 
			                albumBitmap = bitmap;
			                if (listener != null) {
								listener.setAlbumImage(albumBitmap);
							}
			            } 
					

				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}).start();

	}
	
	public void setTransportURI(){
		ContentItem contentItem = contentItems.get(contentIndex);
		setTransportURI(contentItem);
	}
	
	
	public void setTransportURI(ContentItem contentItem){
		
//		Log.v(TAG, "player renderer stop success");
//		ContentItem content = contentItems.get(contentIndex);
//		
		Log.v(TAG, "title "+contentItem.getItem().getTitle());
		
		Log.v(TAG, "url  "+contentItem.getItem().getFirstResource().getValue());
		
		String uri = contentItem.getItem().getFirstResource().getValue();
		String protocolInfo = contentItem.getItem().getFirstResource().getProtocolInfo().toString();
		Log.v(TAG, "protocolInfo  "+protocolInfo);
		for (int i = 0; i < contentItem.getItem().getProperties().size(); i++) {
			Log.v(TAG, "property "+contentItem.getItem().getProperties().get(i));
		}
		
		if (protocolInfo.contains("audio/mpeg")) {
			protocolInfo = "http-get:*:audio/mpeg:DLNA.ORG_PN=MP3;DLNA.ORG_OP=01;DLNA.ORG_CI=0;DLNA.ORG_FLAGS=01700000000000000000000000000000";
		}
		String metaData = "<DIDL-Lite xmlns=\"urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:upnp=\"urn:schemas-upnp-org:metadata-1-0/upnp/\" xmlns:dlna=\"urn:schemas-dlna-org:metadata-1-0/\"><item id=\"";
		if (contentItem.getItem().getId() != null) { 
			metaData = metaData + contentItem.getItem().getId();
		}
		metaData = metaData + "\" parentID=\"";
		if (contentItem.getItem().getParentID() != null) {
			metaData = metaData + contentItem.getItem().getParentID();
		}
		metaData = metaData + "\" restricted=\"1\"><dc:title>";
		if (contentItem.getItem().getTitle() != null) {
			metaData = metaData + contentItem.getItem().getTitle();
		}
		metaData = metaData + "</dc:title><upnp:class>";
		metaData = metaData + "object.item.audioItem.musicTrack";
		metaData = metaData + "</upnp:class><res  protocolInfo=\"";
		metaData = metaData + protocolInfo;
		metaData = metaData + "\" size=\"";
		Log.v(TAG, "size "+ contentItem.getItem().getFirstResource().getSize());
		if (contentItem.getItem().getFirstResource().getSize() != null) {
			metaData = metaData + contentItem.getItem().getFirstResource().getSize();
		}
		metaData = metaData + "\" duration=\"";
		if (contentItem.getItem().getFirstResource().getDuration() != null) {
			metaData = metaData + contentItem.getItem().getFirstResource().getDuration();
		}
		metaData = metaData + "\" bitrate=\"";
		Log.v(TAG, "bitrate "+contentItem.getItem().getFirstResource().getBitrate());
		if (contentItem.getItem().getFirstResource().getBitrate() != null) {
			metaData = metaData + contentItem.getItem().getFirstResource().getBitrate();
		}
		metaData = metaData + "\">";
		if (uri != null) {
			metaData = metaData + uri;
		}
		metaData = metaData + "</res></item></DIDL-Lite>";
		Log.v(TAG, "metadata "+ metaData);
		setAVTransportURI(uri, metaData);
	}
	
	public void seek(float progress){
		if (isLocalRenderer) {
			
		}else {
			int time = (int)(progress * duration / 100);
			String str = String.format("%02d", time / 3600)
					+ ":"
					+ String.format("%02d", (time % 3600) / 60)
					+ ":" + String.format("%02d", time % 60);
			seek(str);
		}
		
	}
	
	public void setAVTransportURI(String uri, String metadata){
		
		Service service = device.findService(new UDAServiceType(
				"AVTransport"));
		upnpService.getControlPoint().execute(new SetAVTransportURICallBack(this, new UnsignedIntegerFourBytes(0), service, uri, metadata));
		
	}
	
	public void play(){
		Service service = device.findService(new UDAServiceType(
				"AVTransport"));
		upnpService.getControlPoint().execute(new PlayCallBack(this, new UnsignedIntegerFourBytes(0), service, "1"));
	}
	
	public void stop(){
		Service service = device.findService(new UDAServiceType(
				"AVTransport"));
		upnpService.getControlPoint().execute(new StopCallBack(this, new UnsignedIntegerFourBytes(0), service));
		Log.v("dlna", "stop");
	}
	
	public void seek(String relativeTimeTarget){
		Service service = device.findService(new UDAServiceType(
				"AVTransport"));
		upnpService.getControlPoint().execute(new SeekCallBack(this, new UnsignedIntegerFourBytes(0), service, SeekMode.REL_TIME, relativeTimeTarget));
	}
	
	public void pause(){
		Service service = device.findService(new UDAServiceType(
				"AVTransport"));
		upnpService.getControlPoint().execute(new PauseCallBack(this, new UnsignedIntegerFourBytes(0), service));

	}
	
	public void getPositionInfo(){
		Service service = device.findService(new UDAServiceType(
				"AVTransport"));
		upnpService.getControlPoint().execute(new GetPositionInfoCallBack(this, new UnsignedIntegerFourBytes(0), service));
	}
	
	public void getTransportInfo(){
		Service service = device.findService(new UDAServiceType(
				"AVTransport"));
		upnpService.getControlPoint().execute(new GetTransportInfoCallBack(this, new UnsignedIntegerFourBytes(0), service));
	}
	
	public void getVolume(){
		if (isLocalRenderer) {
			
		}else {
			Service service = device.findService(new UDAServiceType(
					"RenderingControl"));
			upnpService.getControlPoint().execute(new GetVolumeCallBack(this, new UnsignedIntegerFourBytes(0), service));
		}
	}
	
	public void setVolume(int volume){
		if (isLocalRenderer) {
			
		}else {
			Service service = device.findService(new UDAServiceType(
					"RenderingControl"));
			upnpService.getControlPoint().execute(new SetVolumeCallBack(this, new UnsignedIntegerFourBytes(0), service, volume));
		}
		
	}
	
	public void SetNetwork(String ssid, String key, int index, String authAlgo, String cipherAlgo){
		ServiceType serviceType = new ServiceType("schemas-yichun-com", "YiChun");
		Service service = device.findService(serviceType);
		if (service != null) {
			upnpService.getControlPoint().execute(new com.gcc.imusic.callback.SetNetwork(service, ssid, key, new UnsignedIntegerTwoBytes(index), authAlgo, cipherAlgo));
		}
	}
	
	public void SetDevMode(){
		
	}
	
	public void GetDevMode(){
		
	}
	
	public void SetDevName(){
		Log.v(TAG, "set dev name");
		ServiceType serviceType = new ServiceType("schemas-yichun-com", "YiChun");
//		UDAServiceType serviceType = UDAServiceType.valueOf("urn:schemas-upnp-org:service:AVTransport:1");
//		Log.v(TAG,"service type "+serviceType.toString());
//		Service[] services = device.findServices();
//		for (int i = 0; i < services.length; i++) {
//			Log.v(TAG, "service name "+ services[i].toString());
//			Log.v(TAG, "service type "+services[i].getServiceType());
//			device.finds;
//		}
		Service service = device.findService(serviceType);
		if (service != null) {
			Log.v(TAG, "service is not  null");
			upnpService.getControlPoint().execute(new SetDevName(service, "Hello World"));
		}
//		upnpService.getControlPoint().execute(new com.gcc.imusic.callback.SetDevName(new UnsignedIntegerFourBytes(0), service, "Hello"));
		
	}
	
	public void UpdateFirmware(){
		
	}
	
	
	

	public int getPlayMode() {
		return playMode;
	}

	public void setPlayMode(int playMode) {
		this.playMode = playMode;
	}

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		DLNAMediaRenderer that = (DLNAMediaRenderer) o;

		if (!udn.equals(that.udn))
			return false;

		return true;
	}

	@Override
	public void setAVTransportURISuccess(ActionInvocation invocation) {
		// TODO Auto-generated method stub
		play();
	}

	@Override
	public void setAVTransportURIFailure(ActionInvocation invocation,
			UpnpResponse operation, String defaultMsg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void seekSuccess(ActionInvocation invocation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void seekFailure(ActionInvocation invocation,
			UpnpResponse operation, String defaultMsg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getVolumeReceived(ActionInvocation arg0, int arg1) {
		// TODO Auto-generated method stub
		this.currentVolume = arg1;
	}


	@Override
	public void getTransportInfoReceived(ActionInvocation invocation,
			TransportInfo transportInfo) {
		// TODO Auto-generated method stub
		transportState = transportInfo.getCurrentTransportState();
		Log.v(TAG, "transportState "+transportInfo.getCurrentTransportState());
		if (transportState == TransportState.PLAYING) {
			playingCount ++;
			if (playingCount == 2) {
				playingCount = 0;
				isTransportLocked = false;
			}
		}else if (transportState == TransportState.STOPPED) {
			if (isTransportLocked) 
				return;
			playNextSong();
		}
		
		if (listener != null) {
			listener.getTransportInfoReceived(transportInfo);
		}
	}

	@Override
	public void getPositionInfoReceived(ActionInvocation invocation,
			PositionInfo positionInfo) {
		// TODO Auto-generated method stub
		
		duration = positionInfo.getTrackDurationSeconds();
		currentTime = positionInfo.getTrackElapsedSeconds();
		Log.v(TAG, "duration "+duration + "currentTime "+currentTime);
		if (listener != null) {
			listener.getPositionInfoReceived(duration, currentTime);
		}
	}

	@Override
	public void getPositionInfoFailure(ActionInvocation invocation,
			UpnpResponse operation, String defaultMsg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pauseSuccess(ActionInvocation invocation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pauseFailure(ActionInvocation invocation,
			UpnpResponse operation, String defaultMsg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stopSuccess(ActionInvocation invocation) {
		// TODO Auto-generated method stub
		setTransportURI();
	}

	@Override
	public void stopFailure(ActionInvocation invocation,
			UpnpResponse operation, String defaultMsg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playSuccess(ActionInvocation invocation) {
		// TODO Auto-generated method stub
		isTransportLocked = false;
	}

	@Override
	public void playFailure(ActionInvocation invocation,
			UpnpResponse operation, String defaultMsg) {
		// TODO Auto-generated method stub
		
	}
	
	public interface OnMediaRendererListener {
		public void setMusicInfo(ContentItem contentItem, Boolean isLocalContent);
		public void setAlbumImage(Bitmap albumBitmap);
		public void getPositionInfoReceived(float duration, float currentTime);
		public void getTransportInfoReceived(TransportInfo transportInfo);
		public void setStationInfo(Station station);
	}

	@Override
	public void onSetVolumeSuccess(ActionInvocation invocation) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}

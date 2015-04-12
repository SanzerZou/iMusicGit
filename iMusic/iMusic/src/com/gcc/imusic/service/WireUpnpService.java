package com.gcc.imusic.service;

import org.eclipse.jetty.server.Server;
import org.fourthline.cling.UpnpServiceConfiguration;
import org.fourthline.cling.android.AndroidRouter;
import org.fourthline.cling.android.AndroidUpnpServiceConfiguration;
import org.fourthline.cling.android.AndroidUpnpServiceImpl;
import org.fourthline.cling.model.types.ServiceType;
import org.fourthline.cling.model.types.UDAServiceType;
import org.fourthline.cling.protocol.ProtocolFactory;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.util.Log;

public class WireUpnpService extends AndroidUpnpServiceImpl {

	// private OnWireUpnpServiceListener listener;
	private final static String TAG = "WireUpnpService";

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.v(TAG, "onCreate()");

		// new Thread(new Runnable(){
		//
		// @Override
		// public void run() {
		// // TODO Auto-generated method stub
		// WireUpnpService.super.onCreate();
		// if (listener != null) {
		// listener.onWireUpnpServiceConnected();
		// }
		// }
		//
		// }).start();

	}

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		Log.v(TAG, "onUnbind()");
		return super.onUnbind(intent);

	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		Log.v(TAG, "onBind()");
		return super.onBind(intent);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Log.v(TAG, "onDestroy()");
		super.onDestroy();
	}

	

	@Override
	protected AndroidRouter createRouter(
			UpnpServiceConfiguration configuration,
			ProtocolFactory protocolFactory, Context context) {
		// TODO Auto-generated method stub
		return super.createRouter(configuration, protocolFactory, context);
	}

	// public OnWireUpnpServiceListener getListener() {
	// return listener;
	// }
	//
	// public void setListener(OnWireUpnpServiceListener listener) {
	// this.listener = listener;
	// }
	//
	//
	// public interface OnWireUpnpServiceListener{
	// public void onWireUpnpServiceConnected();
	// }
}
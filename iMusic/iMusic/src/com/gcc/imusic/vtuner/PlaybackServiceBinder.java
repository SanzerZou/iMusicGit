package com.gcc.imusic.vtuner;

import android.content.Context;
import android.content.Intent;

import com.android.vtuner.service.PlaybackBaseServiceBinder;


public class PlaybackServiceBinder extends PlaybackBaseServiceBinder{

	public PlaybackServiceBinder(Context context,
			PlaybackServiceBinderCallback callback) {
		super(context, callback);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doBindService() {
		// TODO Auto-generated method stub
		 mContext.bindService(new Intent(mContext, PlaybackService.class), mConnection, Context.BIND_AUTO_CREATE);
	}

}

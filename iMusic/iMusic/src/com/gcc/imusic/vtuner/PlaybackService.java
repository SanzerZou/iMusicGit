package com.gcc.imusic.vtuner;

import com.android.vtuner.service.PlaybackBaseService;
import com.android.vtuner.utils.PlayerNotification;
import com.gcc.imusic.activity.MainActivity;
import com.gcc.imusic.activity.RadioActivity;



public class PlaybackService extends PlaybackBaseService{

	@Override
	public PlayerNotification getNotification() {
		// TODO Auto-generated method stub
		return PlayerNotification.getNotification(this, RadioActivity.class);
	}

}

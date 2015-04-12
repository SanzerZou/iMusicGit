package com.gcc.imusic.callback;

import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;
import org.fourthline.cling.support.avtransport.callback.GetMediaInfo;
import org.fourthline.cling.support.model.MediaInfo;

//import org.teleal.cling.model.action.ActionInvocation;
//import org.teleal.cling.model.message.UpnpResponse;
//import org.teleal.cling.model.meta.Service;
//import org.teleal.cling.model.types.UnsignedIntegerFourBytes;
//import org.teleal.cling.support.avtransport.callback.GetMediaInfo;
//import org.teleal.cling.support.model.MediaInfo;

public class GetMediaInfoCallBack extends GetMediaInfo{

	private OnGetMediaInfoListener listener;
	
	public GetMediaInfoCallBack(UnsignedIntegerFourBytes instanceId,
			Service service) {
		super(instanceId, service);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void received(ActionInvocation invocation, MediaInfo mediaInfo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void failure(ActionInvocation invocation, UpnpResponse operation,
			String defaultMsg) {
		// TODO Auto-generated method stub
		
	}
	
	public interface OnGetMediaInfoListener{
		public void getMediaInfoReceived(ActionInvocation invocation, MediaInfo mediaInfo); 
		public void getMediaInfoFailure(ActionInvocation invocation, UpnpResponse operation,
				String defaultMsg);
	}

}

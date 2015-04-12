package com.gcc.imusic.callback;

import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;
import org.fourthline.cling.support.avtransport.callback.Play;

//import org.teleal.cling.model.action.ActionInvocation;
//import org.teleal.cling.model.message.UpnpResponse;
//import org.teleal.cling.model.meta.Service;
//import org.teleal.cling.model.types.UnsignedIntegerFourBytes;
//import org.teleal.cling.support.avtransport.callback.Play;


public class PlayCallBack extends Play{

	private OnPlayListener listener;
	
	@SuppressWarnings("rawtypes")
	public PlayCallBack(UnsignedIntegerFourBytes instanceId, Service service,
			String speed) {
		super(instanceId, service, speed);
		// TODO Auto-generated constructor stub
	}
	
	@SuppressWarnings("rawtypes")
	public PlayCallBack(OnPlayListener listener, UnsignedIntegerFourBytes instanceId, Service service,
			String speed) {
		super(instanceId, service, speed);
		// TODO Auto-generated constructor stub
		this.listener = listener;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void success(ActionInvocation invocation) {
		// TODO Auto-generated method stub
		super.success(invocation);
//		PlayerActivity.getPlayerActivityCallBack().playSuccess();
		if (listener != null) {
			listener.playSuccess(invocation);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void failure(ActionInvocation invocation, UpnpResponse operation,
			String defaultMsg) {
		// TODO Auto-generated method stub
//		PlayerActivity.getPlayerActivityCallBack().playFailure();
		if (listener != null) {
			listener.playFailure(invocation, operation, defaultMsg);
		}
	}

	public interface OnPlayListener{
		public void playSuccess(ActionInvocation invocation); 
		public void playFailure(ActionInvocation invocation, UpnpResponse operation,
				String defaultMsg);
	}
}

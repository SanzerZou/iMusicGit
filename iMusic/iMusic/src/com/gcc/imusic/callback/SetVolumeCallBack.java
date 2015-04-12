package com.gcc.imusic.callback;

import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;
import org.fourthline.cling.support.renderingcontrol.callback.SetVolume;

//import org.teleal.cling.model.action.ActionInvocation;
//import org.teleal.cling.model.message.UpnpResponse;
//import org.teleal.cling.model.meta.Service;
//import org.teleal.cling.model.types.UnsignedIntegerFourBytes;
//import org.teleal.cling.support.renderingcontrol.callback.SetVolume;

public class SetVolumeCallBack extends SetVolume{

	private OnSetVolumeListener listener;
	
	@SuppressWarnings("rawtypes")
	public SetVolumeCallBack(OnSetVolumeListener listener, UnsignedIntegerFourBytes instanceId,
			Service service, long newVolume) {
		super(instanceId, service, newVolume);
		// TODO Auto-generated constructor stub
		this.listener = listener;
	}
	
	@SuppressWarnings("rawtypes")
	public SetVolumeCallBack(UnsignedIntegerFourBytes instanceId,
			Service service, long newVolume) {
		super(instanceId, service, newVolume);
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void failure(ActionInvocation arg0, UpnpResponse arg1, String arg2) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void success(ActionInvocation invocation) {
		// TODO Auto-generated method stub
		super.success(invocation);
		if (listener != null) {
			listener.onSetVolumeSuccess(invocation);
		}
	}

	public interface OnSetVolumeListener{
		public void onSetVolumeSuccess(ActionInvocation invocation);
	}

}

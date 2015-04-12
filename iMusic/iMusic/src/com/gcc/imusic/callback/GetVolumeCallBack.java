package com.gcc.imusic.callback;

import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;
import org.fourthline.cling.support.renderingcontrol.callback.GetVolume;

//import org.teleal.cling.model.action.ActionInvocation;
//import org.teleal.cling.model.message.UpnpResponse;
//import org.teleal.cling.model.meta.Service;
//import org.teleal.cling.model.types.UnsignedIntegerFourBytes;
//import org.teleal.cling.support.renderingcontrol.callback.GetVolume;

public class GetVolumeCallBack extends GetVolume{

	private OnGetVolumeListener listener;
	
	@SuppressWarnings("rawtypes")
	public GetVolumeCallBack(UnsignedIntegerFourBytes instanceId,
			Service service) {
		super(instanceId, service);
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("rawtypes")
	public GetVolumeCallBack(OnGetVolumeListener listener, UnsignedIntegerFourBytes instanceId,
			Service service) {
		super(instanceId, service);
		// TODO Auto-generated constructor stub
		this.listener = listener;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public void received(ActionInvocation arg0, int arg1) {
		// TODO Auto-generated method stub
		if (listener != null) {
			listener.getVolumeReceived(arg0, arg1);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void failure(ActionInvocation arg0, UpnpResponse arg1, String arg2) {
		// TODO Auto-generated method stub
		
	}
	
	public interface OnGetVolumeListener{
		public void getVolumeReceived(ActionInvocation arg0, int arg1);
	}

}

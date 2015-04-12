package com.gcc.imusic.callback;

import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;
import org.fourthline.cling.support.avtransport.callback.Seek;
import org.fourthline.cling.support.model.SeekMode;

//import org.teleal.cling.model.action.ActionInvocation;
//import org.teleal.cling.model.message.UpnpResponse;
//import org.teleal.cling.model.meta.Service;
//import org.teleal.cling.model.types.UnsignedIntegerFourBytes;
//import org.teleal.cling.support.avtransport.callback.Seek;
//import org.teleal.cling.support.model.SeekMode;


public class SeekCallBack extends Seek{

	private OnSeekListener listener;
	
	@SuppressWarnings("rawtypes")
	public SeekCallBack(UnsignedIntegerFourBytes instanceId, Service service,
			SeekMode mode, String target) {
		super(instanceId, service, mode, target);
		// TODO Auto-generated constructor stub
	}
	
	@SuppressWarnings("rawtypes")
	public SeekCallBack(OnSeekListener listener, UnsignedIntegerFourBytes instanceId, Service service,
			SeekMode mode, String target) {
		super(instanceId, service, mode, target);
		// TODO Auto-generated constructor stub
		this.listener = listener;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void failure(ActionInvocation invocation, UpnpResponse operation,
			String defaultMsg) {
		// TODO Auto-generated method stub
		super.failure(invocation, operation);
		if (listener != null) {
			listener.seekFailure(invocation, operation, defaultMsg);;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void success(ActionInvocation invocation) {
		// TODO Auto-generated method stub
		super.success(invocation);
		if (listener != null) {
			listener.seekSuccess(invocation);
		}
	}
	
	public interface OnSeekListener{
		public void seekSuccess(ActionInvocation invocation); 
		public void seekFailure(ActionInvocation invocation, UpnpResponse operation,
				String defaultMsg);
	}
	

}

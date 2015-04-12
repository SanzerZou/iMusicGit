package com.gcc.imusic.callback;

//import org.teleal.cling.model.action.ActionInvocation;
//import org.teleal.cling.model.message.UpnpResponse;
//import org.teleal.cling.model.meta.Service;
//import org.teleal.cling.model.types.UnsignedIntegerFourBytes;
//import org.teleal.cling.support.avtransport.callback.Stop;

import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;
import org.fourthline.cling.support.avtransport.callback.Stop;

import com.gcc.imusic.callback.PlayCallBack.OnPlayListener;

public class StopCallBack extends Stop{

	private OnStopListener listener;
	
	
	@SuppressWarnings("rawtypes")
	public StopCallBack(UnsignedIntegerFourBytes instanceId, Service service) {
		super(instanceId, service);
		// TODO Auto-generated constructor stub
	}
	
	@SuppressWarnings("rawtypes")
	public StopCallBack(OnStopListener listener, UnsignedIntegerFourBytes instanceId, Service service) {
		super(instanceId, service);
		// TODO Auto-generated constructor stub
		this.listener = listener;
	}
	
	
	@SuppressWarnings("rawtypes")
	@Override
	public void success(ActionInvocation invocation) {
		// TODO Auto-generated method stub
		super.success(invocation);
		if (listener != null) {
			listener.stopSuccess(invocation);
		}
	}


	@SuppressWarnings("rawtypes")
	@Override
	public void failure(ActionInvocation invocation, UpnpResponse operation,
			String defaultMsg) {
		// TODO Auto-generated method stub
		if (listener != null) {
			listener.stopFailure(invocation, operation, defaultMsg);
		}
	}
	
	public interface OnStopListener{
		public void stopSuccess(ActionInvocation invocation); 
		public void stopFailure(ActionInvocation invocation, UpnpResponse operation,
				String defaultMsg);
	}

}

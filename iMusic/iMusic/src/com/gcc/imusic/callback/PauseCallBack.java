package com.gcc.imusic.callback;

import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;
import org.fourthline.cling.support.avtransport.callback.Pause;

//import org.teleal.cling.model.action.ActionInvocation;
//import org.teleal.cling.model.message.UpnpResponse;
//import org.teleal.cling.model.meta.Service;
//import org.teleal.cling.model.types.UnsignedIntegerFourBytes;
//import org.teleal.cling.support.avtransport.callback.Pause;


public class PauseCallBack extends Pause{

	private OnPauseListener listener;
	
	@SuppressWarnings("rawtypes")
	public PauseCallBack(UnsignedIntegerFourBytes instanceId, Service service) {
		super(instanceId, service);
		// TODO Auto-generated constructor stub
	}
	
	@SuppressWarnings("rawtypes")
	public PauseCallBack(OnPauseListener listener, UnsignedIntegerFourBytes instanceId, Service service) {
		super(instanceId, service);
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
			listener.pauseFailure(invocation, operation, defaultMsg);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void success(ActionInvocation invocation) {
		// TODO Auto-generated method stub
		super.success(invocation);
		if (listener != null) {
			listener.pauseSuccess(invocation);
		}
	}
	
	public interface OnPauseListener{
		public void pauseSuccess(ActionInvocation invocation); 
		public void pauseFailure(ActionInvocation invocation, UpnpResponse operation,
				String defaultMsg);
	}

}

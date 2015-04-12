package com.gcc.imusic.callback;

import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;
import org.fourthline.cling.support.avtransport.callback.SetAVTransportURI;

//import org.teleal.cling.model.action.ActionInvocation;
//import org.teleal.cling.model.message.UpnpResponse;
//import org.teleal.cling.model.meta.Service;
//import org.teleal.cling.model.types.UnsignedIntegerFourBytes;
//import org.teleal.cling.support.avtransport.callback.SetAVTransportURI;


public class SetAVTransportURICallBack extends SetAVTransportURI{

	private OnSetAVTransportURIListener listener;
	

	@SuppressWarnings("rawtypes")
	public SetAVTransportURICallBack(UnsignedIntegerFourBytes instanceId,
			Service service, String uri, String metadata) {
		super(instanceId, service, uri, metadata);
		// TODO Auto-generated constructor stub
	}
	
	@SuppressWarnings("rawtypes")
	public SetAVTransportURICallBack(OnSetAVTransportURIListener listener, UnsignedIntegerFourBytes instanceId,
			Service service, String uri, String metadata) {
		super(instanceId, service, uri, metadata);
		// TODO Auto-generated constructor stub
		this.listener = listener;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void success(ActionInvocation invocation) {
		// TODO Auto-generated method stub
		super.success(invocation);
		if (listener != null) {
			listener.setAVTransportURISuccess(invocation);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void failure(ActionInvocation invocation, UpnpResponse operation,
			String defaultMsg) {
		// TODO Auto-generated method stub
		if (listener != null) {
			listener.setAVTransportURIFailure(invocation, operation, defaultMsg);
		}
	}
	
	public interface OnSetAVTransportURIListener{
		public void setAVTransportURISuccess(ActionInvocation invocation);
		public void setAVTransportURIFailure(ActionInvocation invocation, UpnpResponse operation,
				String defaultMsg);
	}

}

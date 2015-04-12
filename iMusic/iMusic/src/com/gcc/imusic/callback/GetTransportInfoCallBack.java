package com.gcc.imusic.callback;

import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;
import org.fourthline.cling.support.avtransport.callback.GetTransportInfo;
import org.fourthline.cling.support.model.TransportInfo;

//import org.teleal.cling.model.action.ActionInvocation;
//import org.teleal.cling.model.message.UpnpResponse;
//import org.teleal.cling.model.meta.Service;
//import org.teleal.cling.model.types.UnsignedIntegerFourBytes;
//import org.teleal.cling.support.avtransport.callback.GetTransportInfo;
//import org.teleal.cling.support.model.PositionInfo;
//import org.teleal.cling.support.model.TransportInfo;

public class GetTransportInfoCallBack extends GetTransportInfo{

	private OnGetTransportInfoListener listener;
	
	@SuppressWarnings("rawtypes")
	public GetTransportInfoCallBack(UnsignedIntegerFourBytes instanceId,
			Service service) {
		super(instanceId, service);
		// TODO Auto-generated constructor stub
	}
	
	@SuppressWarnings("rawtypes")
	public GetTransportInfoCallBack(OnGetTransportInfoListener listener, UnsignedIntegerFourBytes instanceId,
			Service service) {
		super(instanceId, service);
		// TODO Auto-generated constructor stub
		this.listener = listener;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void received(ActionInvocation invocation,
			TransportInfo transportInfo) {
		// TODO Auto-generated method stub
		if (listener != null) {
			listener.getTransportInfoReceived(invocation, transportInfo);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void failure(ActionInvocation invocation, UpnpResponse operation,
			String defaultMsg) {
		// TODO Auto-generated method stub
		
	}
	
	public interface OnGetTransportInfoListener{
		public void getTransportInfoReceived(ActionInvocation invocation,
				TransportInfo transportInfo); 
	}

}

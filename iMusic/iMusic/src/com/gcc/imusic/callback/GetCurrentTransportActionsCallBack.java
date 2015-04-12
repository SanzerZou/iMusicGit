package com.gcc.imusic.callback;

import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;
import org.fourthline.cling.support.avtransport.callback.GetCurrentTransportActions;
import org.fourthline.cling.support.model.TransportAction;

//import org.teleal.cling.model.action.ActionInvocation;
//import org.teleal.cling.model.message.UpnpResponse;
//import org.teleal.cling.model.meta.Service;
//import org.teleal.cling.model.types.UnsignedIntegerFourBytes;
//import org.teleal.cling.support.avtransport.callback.GetCurrentTransportActions;
//import org.teleal.cling.support.model.TransportAction;

public class GetCurrentTransportActionsCallBack extends GetCurrentTransportActions{

	public GetCurrentTransportActionsCallBack(
			UnsignedIntegerFourBytes instanceId, Service service) {
		super(instanceId, service);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void received(ActionInvocation actionInvocation,
			TransportAction[] actions) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void failure(ActionInvocation invocation, UpnpResponse operation,
			String defaultMsg) {
		// TODO Auto-generated method stub
		
	}

}

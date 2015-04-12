package com.gcc.imusic.callback;

import java.util.logging.Logger;

import org.fourthline.cling.controlpoint.ActionCallback;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;

public class GetDevName extends ActionCallback{

	private static Logger log = Logger.getLogger(GetDevName.class.getName());
	
	public GetDevName(UnsignedIntegerFourBytes instanceId, Service service){
		super(new ActionInvocation(service.getAction("GetVolume")));
		 getActionInvocation().setInput("InstanceID", instanceId);
	}
	
	@Override
	public void failure(ActionInvocation arg0, UpnpResponse arg1, String arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void success(ActionInvocation arg0) {
		// TODO Auto-generated method stub
		
	}

}

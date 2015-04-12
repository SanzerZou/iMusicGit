package com.gcc.imusic.callback;

import java.util.logging.Logger;

import org.fourthline.cling.controlpoint.ActionCallback;
import org.fourthline.cling.controlpoint.ControlPoint;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;
import org.fourthline.cling.model.types.UnsignedIntegerTwoBytes;
import org.fourthline.cling.support.renderingcontrol.callback.SetVolume;

public class SetDevMode extends ActionCallback{

	private static Logger log = Logger.getLogger(SetDevMode.class.getName());
	 
	public SetDevMode(UnsignedIntegerFourBytes instanceId, Service service, long newDevMode){
		super(new ActionInvocation(service.getAction("SetDevMode")));
		getActionInvocation().setInput("InstanceID", instanceId);
		getActionInvocation().setInput("DevMode", new UnsignedIntegerTwoBytes(newDevMode));
	}


	@Override
	public void failure(ActionInvocation arg0, UpnpResponse arg1, String arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void success(ActionInvocation arg0) {
		// TODO Auto-generated method stub
		log.fine("Executed successfully");
	}

}

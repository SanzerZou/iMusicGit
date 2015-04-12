package com.gcc.imusic.callback;

import org.fourthline.cling.controlpoint.ActionCallback;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.UnsignedIntegerTwoBytes;

public class SetNetwork extends ActionCallback{

	public SetNetwork (Service service, String ssid, String key, UnsignedIntegerTwoBytes index, String authAlgo, String cipherAlgo){
		super(new ActionInvocation(service.getAction("SetNetwork")));
		getActionInvocation().setInput("SSID", ssid);
		getActionInvocation().setInput("Key", key);
		getActionInvocation().setInput("Index", index);
		getActionInvocation().setInput("AuthAlgo", authAlgo);
		getActionInvocation().setInput("CipherAlgo", cipherAlgo);
		
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

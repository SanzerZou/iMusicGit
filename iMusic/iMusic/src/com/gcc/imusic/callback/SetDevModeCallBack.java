package com.gcc.imusic.callback;

import org.fourthline.cling.controlpoint.ActionCallback;
import org.fourthline.cling.controlpoint.ControlPoint;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;

public class SetDevModeCallBack extends ActionCallback{

	protected SetDevModeCallBack(ActionInvocation actionInvocation) {
		super(actionInvocation);
		// TODO Auto-generated constructor stub
	}
	
	

	public SetDevModeCallBack(ActionInvocation actionInvocation,
			ControlPoint controlPoint) {
		super(actionInvocation, controlPoint);
		// TODO Auto-generated constructor stub
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

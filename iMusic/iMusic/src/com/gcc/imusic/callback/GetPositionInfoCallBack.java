package com.gcc.imusic.callback;

import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;
import org.fourthline.cling.support.avtransport.callback.GetPositionInfo;
import org.fourthline.cling.support.model.PositionInfo;

//import org.teleal.cling.model.action.ActionInvocation;
//import org.teleal.cling.model.message.UpnpResponse;
//import org.teleal.cling.model.meta.Service;
//import org.teleal.cling.model.types.UnsignedIntegerFourBytes;
//import org.teleal.cling.support.avtransport.callback.GetPositionInfo;
//import org.teleal.cling.support.model.MediaInfo;
//import org.teleal.cling.support.model.PositionInfo;

public class GetPositionInfoCallBack extends GetPositionInfo{

	private OnGetPositionInfoListener listener;
	
	public GetPositionInfoCallBack(UnsignedIntegerFourBytes instanceId,
			Service service) {
		super(instanceId, service);
		// TODO Auto-generated constructor stub
	}
	
	public GetPositionInfoCallBack(OnGetPositionInfoListener listener, UnsignedIntegerFourBytes instanceId,
			Service service) {
		super(instanceId, service);
		// TODO Auto-generated constructor stub
		this.listener = listener;
	}

	@Override
	public void received(ActionInvocation invocation, PositionInfo positionInfo) {
		// TODO Auto-generated method stub
		if (listener != null) {
			listener.getPositionInfoReceived(invocation, positionInfo);
		}
	}

	@Override
	public void failure(ActionInvocation invocation, UpnpResponse operation,
			String defaultMsg) {
		// TODO Auto-generated method stub
		if (listener != null) {
			listener.getPositionInfoFailure(invocation, operation,
					defaultMsg);
		}
	}

	@Override
	public void success(ActionInvocation invocation) {
		// TODO Auto-generated method stub
		super.success(invocation);
	}
	
	public interface OnGetPositionInfoListener{
		public void getPositionInfoReceived(ActionInvocation invocation, PositionInfo positionInfo); 
		public void getPositionInfoFailure(ActionInvocation invocation, UpnpResponse operation,
				String defaultMsg);
	}
	

}

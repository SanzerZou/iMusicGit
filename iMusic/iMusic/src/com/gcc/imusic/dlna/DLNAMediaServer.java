package com.gcc.imusic.dlna;

//import org.teleal.cling.android.AndroidUpnpService;
//import org.teleal.cling.model.meta.Device;
//import org.teleal.cling.model.meta.Service;
//import org.teleal.cling.model.types.UDAServiceType;
//import org.teleal.cling.model.types.UDN;
//import org.teleal.cling.support.model.container.Container;

import org.fourthline.cling.android.AndroidUpnpService;
import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.UDAServiceType;
import org.fourthline.cling.model.types.UDN;
import org.fourthline.cling.support.model.container.Container;

import com.gcc.imusic.callback.ContentBrowseActionCallback;
import com.gcc.imusic.callback.ContentBrowseActionCallback.ContentBrowseActionListener;

import android.app.Activity;

public class DLNAMediaServer {

	private AndroidUpnpService upnpService;
	private UDN udn;
	private Device device;
	
	private Boolean isLocalServer;
	
	
	public AndroidUpnpService getUpnpService() {
		return upnpService;
	}

	public void setUpnpService(AndroidUpnpService upnpService) {
		this.upnpService = upnpService;
	}

	public DLNAMediaServer(Device device){
		this.device = device;
		this.udn = device.getIdentity().getUdn();
	}
	
	protected Container createRootContainer(Service service) {
		Container rootContainer = new Container();
		rootContainer.setId("0");
		rootContainer.setTitle("Content Directory on "
				+ service.getDevice().getDisplayString());
		return rootContainer;
	}
	
	public void browseDirectChildren(ContentBrowseActionListener listener, String containerId){
		Service service = device.findService(new UDAServiceType(
				"ContentDirectory"));
		upnpService.getControlPoint().execute(
				new ContentBrowseActionCallback(service,
						containerId, listener));
		
	}
	
	public String getFriendlyName(){
		if (device == null)
			return null;
		return device.getDetails().getFriendlyName();
	}
	
	
	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public UDN getUdn() {
		return udn;
	}

	public void setUdn(UDN udn) {
		this.udn = udn;
	}
	
	
	

	public Boolean getIsLocalServer() {
		return isLocalServer;
	}

	public void setIsLocalServer(Boolean isLocalServer) {
		this.isLocalServer = isLocalServer;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		DLNAMediaServer that = (DLNAMediaServer) o;

		if (!udn.equals(that.udn))
			return false;

		return true;
	}
}

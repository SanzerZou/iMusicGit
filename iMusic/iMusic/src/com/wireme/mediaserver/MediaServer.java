package com.wireme.mediaserver;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import org.fourthline.cling.binding.annotations.AnnotationLocalServiceBinder;
import org.fourthline.cling.model.DefaultServiceManager;
import org.fourthline.cling.model.ValidationException;
import org.fourthline.cling.model.meta.DeviceDetails;
import org.fourthline.cling.model.meta.DeviceIdentity;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.LocalService;
import org.fourthline.cling.model.meta.ManufacturerDetails;
import org.fourthline.cling.model.meta.ModelDetails;
import org.fourthline.cling.model.types.DeviceType;
import org.fourthline.cling.model.types.UDADeviceType;
import org.fourthline.cling.model.types.UDN;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class MediaServer {

	//private UDN udn = UDN.uniqueSystemIdentifier("GNaP-MediaServer");
	
	private UDN udn = UDN.valueOf("1212123423432534");
	private LocalDevice localDevice;

	private final static String deviceType = "MediaServer";
	private final static int version = 1;
	private final static String LOGTAG = "GNaP-MediaServer";
	private final static int port = 8192;
	private static InetAddress localAddress;
	private HttpServer httpServer;

	public MediaServer(InetAddress localAddress) throws ValidationException {
		DeviceType type = new UDADeviceType(deviceType, version);

		DeviceDetails details = new DeviceDetails(android.os.Build.MODEL,
				new ManufacturerDetails(android.os.Build.MANUFACTURER),
				new ModelDetails("GNaP", "GNaP MediaServer for Android", "v1"));

		LocalService service = new AnnotationLocalServiceBinder()
				.read(ContentDirectoryService.class);

		service.setManager(new DefaultServiceManager<ContentDirectoryService>(
				service, ContentDirectoryService.class));

		localDevice = new LocalDevice(new DeviceIdentity(udn), type, details,
				service);
		this.localAddress = localAddress;

		Log.v(LOGTAG, "MediaServer device created: ");
		Log.v(LOGTAG, "friendly name: " + details.getFriendlyName());
		Log.v(LOGTAG, "manufacturer: "
				+ details.getManufacturerDetails().getManufacturer());
		Log.v(LOGTAG, "model: " + details.getModelDetails().getModelName());
		
		//start http server
		try {
			httpServer = new HttpServer(port);
		}
		catch (IOException ioe )
		{
			System.err.println( "Couldn't start server:\n" + ioe );
			System.exit( -1 );
		}
		
		Log.v(LOGTAG, "Started Http Server on port " + port);
	}
	
	public MediaServer(InetAddress localAddress, UDN udn) throws ValidationException {
		DeviceType type = new UDADeviceType(deviceType, version);

		DeviceDetails details = new DeviceDetails(android.os.Build.MODEL,
				new ManufacturerDetails(android.os.Build.MANUFACTURER),
				new ModelDetails("GNaP", "GNaP MediaServer for Android", "v1"));

		LocalService service = new AnnotationLocalServiceBinder()
				.read(ContentDirectoryService.class);

		service.setManager(new DefaultServiceManager<ContentDirectoryService>(
				service, ContentDirectoryService.class));

		localDevice = new LocalDevice(new DeviceIdentity(udn), type, details,
				service);
		this.localAddress = localAddress;

		Log.v(LOGTAG, "MediaServer device created: ");
		Log.v(LOGTAG, "friendly name: " + details.getFriendlyName());
		Log.v(LOGTAG, "manufacturer: "
				+ details.getManufacturerDetails().getManufacturer());
		Log.v(LOGTAG, "model: " + details.getModelDetails().getModelName());
		
		//start http server
		try {
			httpServer = new HttpServer(port);
		}
		catch (IOException ioe )
		{
			System.err.println( "Couldn't start server:\n" + ioe );
			System.exit( -1 );
		}
		
		Log.v(LOGTAG, "Started Http Server on port " + port);
	}

	public LocalDevice getDevice() {
		return localDevice;
	}

	public String getAddress() {
		return localAddress.getHostAddress() + ":" + port;
	}
	
	public void stop(){
		httpServer.stop();
	}
}

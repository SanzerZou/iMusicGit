package com.gcc.imusic.dlna;

import java.util.ArrayList;

//import org.teleal.cling.android.AndroidUpnpService;
//import org.teleal.cling.model.meta.Device;
//import org.teleal.cling.model.meta.LocalDevice;
//import org.teleal.cling.model.meta.RemoteDevice;
//import org.teleal.cling.registry.DefaultRegistryListener;
//import org.teleal.cling.registry.Registry;

import org.fourthline.cling.android.AndroidUpnpService;
import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.registry.DefaultRegistryListener;
import org.fourthline.cling.registry.Registry;

import android.util.Log;

import com.gcc.imusic.dlna.DLNAMediaRenderer;
import com.gcc.imusic.dlna.DLNAMediaServer;

public class DLNAMediaController {

	private final static String TAG = "DLNAMediaController";

	private AndroidUpnpService upnpService;
	private ArrayList<DLNAMediaRenderer> renderers = new ArrayList<DLNAMediaRenderer>();
	private ArrayList<DLNAMediaServer> servers = new ArrayList<DLNAMediaServer>();

	private OnRendererChangedListener onRendererChangedListener;
	private OnServerChangedListener onServerChangedListener;

	public AndroidUpnpService getUpnpService() {
		return upnpService;
	}

	public void setUpnpService(AndroidUpnpService upnpService) {
		this.upnpService = upnpService;
	}

	public ArrayList<DLNAMediaRenderer> getRenderers() {
		return renderers;
	}

	public void setRenderers(ArrayList<DLNAMediaRenderer> renderers) {
		this.renderers = renderers;
	}

	public ArrayList<DLNAMediaServer> getServers() {
		return servers;
	}

	public void setServers(ArrayList<DLNAMediaServer> servers) {
		this.servers = servers;
	}

	public void search() {
		if (upnpService != null) {
			upnpService.getControlPoint().search();
		}
	}

	public DLNAMediaServer getServerWithUDN(String udn) {
		if (udn == null)
			return null;
		for (DLNAMediaServer mediaServer : servers) {
			if (udn.equals(mediaServer.getUdn().toString())) {
				return mediaServer;
			}
		}
		return null;
	}
	
	public DLNAMediaRenderer getRendererWithUDN(String udn){
		if (udn == null) 
			return null;
		for (DLNAMediaRenderer mediaRenderer : renderers) {
			if (udn.equals(mediaRenderer.getUdn().toString())) {
				return mediaRenderer;
			}
		}
		return null;
	}

	public OnRendererChangedListener getOnRendererChangedListener() {
		return onRendererChangedListener;
	}

	public void setOnRendererChangedListener(
			OnRendererChangedListener onRendererChangedListener) {
		this.onRendererChangedListener = onRendererChangedListener;
	}

	public OnServerChangedListener getOnServerChangedListener() {
		return onServerChangedListener;
	}

	public void setOnServerChangedListener(
			OnServerChangedListener onServerChangedListener) {
		this.onServerChangedListener = onServerChangedListener;
	}

	public void addDeviceListRegistryListener() {
		DeviceListRegistryListener deviceListRegistryListener = new DeviceListRegistryListener();
		upnpService.getRegistry().addListener(deviceListRegistryListener);
	}

	public void deviceAdded(final Device device) {
		if (device.getType().getNamespace().equals("schemas-upnp-org")
				&& device.getType().getType().equals("MediaRenderer")) {
			DLNAMediaRenderer mediaRenderer = new DLNAMediaRenderer(device);
			if (!renderers.contains(mediaRenderer)) {
				mediaRenderer.setUpnpService(upnpService);
				mediaRenderer.setIsLocalRenderer(false);
				renderers.add(mediaRenderer);
				mediaRenderer.getVolume();
				if (onRendererChangedListener != null) {
					onRendererChangedListener.rendererChanged();
				}
			} else {
				Log.v(TAG, "Device " + device.getDetails().getFriendlyName()
						+ " has existed");
			}

		}
		if (device.getType().getNamespace().equals("schemas-upnp-org")
				&& device.getType().getType().equals("MediaServer")) {
			DLNAMediaServer mediaServer = new DLNAMediaServer(device);

			if (!servers.contains(mediaServer)) {
				mediaServer.setUpnpService(upnpService);
				mediaServer.setIsLocalServer(false);
				servers.add(mediaServer);
				if (onServerChangedListener != null) {
					onServerChangedListener.serverChanged();
				}
			} else {
				Log.v(TAG, "Device " + device.getDetails().getFriendlyName()
						+ " has existed");
			}

			// int position =
			// getServerPositionWithName(mediaServer.getFriendlyName());
			// if (position == -1){
			// servers.add(mediaServer);
			// }else{
			// servers.remove(position);
			// servers.add(mediaServer);
			// }
			// if (ServerActivity.getServerActivityCallBack() != null) {
			// ServerActivity.getServerActivityCallBack().updateServers();
			// }
		}
	}

	public void deviceRemoved(final Device device) {

	}

	public class DeviceListRegistryListener extends DefaultRegistryListener {

		/* Discovery performance optimization for very slow Android devices! */

		@Override
		public void remoteDeviceDiscoveryStarted(Registry registry,
				RemoteDevice device) {
		}

		@Override
		public void remoteDeviceDiscoveryFailed(Registry registry,
				final RemoteDevice device, final Exception ex) {
		}

		/*
		 * End of optimization, you can remove the whole block if your Android
		 * handset is fast (>= 600 Mhz)
		 */

		@Override
		public void remoteDeviceAdded(Registry registry, RemoteDevice device) {

			Log.v(TAG, "remote device add device name"
					+ device.getDetails().getFriendlyName());
			Log.v(TAG, "remote device add device name"
					+ device.getIdentity().getUdn());
			DLNAMediaController.this.deviceAdded(device);

		}

		@Override
		public void remoteDeviceRemoved(Registry registry, RemoteDevice device) {

			Log.v(TAG, "remote device remove device name"
					+ device.getDetails().getFriendlyName());
			Log.v(TAG, "remote device remove device name"
					+ device.getIdentity().getUdn());
			DLNAMediaController.this.deviceRemoved(device);

		}

		@Override
		public void localDeviceAdded(Registry registry, LocalDevice device) {

			Log.v(TAG, "local device add device name"
					+ device.getDetails().getFriendlyName());
			Log.v(TAG, "local device add device name"
					+ device.getIdentity().getUdn());
			DLNAMediaController.this.deviceAdded(device);

		}

		@Override
		public void localDeviceRemoved(Registry registry, LocalDevice device) {

			Log.v(TAG, "local device remove device name"
					+ device.getDetails().getFriendlyName());
			Log.v(TAG, "local device remove device udn"
					+ device.getIdentity().getUdn());
			DLNAMediaController.this.deviceRemoved(device);

		}
	}

	public interface OnRendererChangedListener {
		public void rendererChanged();
	}

	public interface OnServerChangedListener {
		public void serverChanged();
	}

}

package com.gcc.imusic;

import com.gcc.imusic.activity.LaunchActivity;
import com.gcc.imusic.dlna.DLNAMediaController;
import com.gcc.imusic.dlna.DLNAMediaRenderer;
import com.gcc.imusic.dlna.DLNAMediaServer;

import android.app.Application;

public class MusicApplication extends Application {
	
	private DLNAMediaController dmc;
	private DLNAMediaRenderer renderer;
	private DLNAMediaServer server;
	
	private LaunchActivity launchActivity;
	
	private Boolean hasRenderer;
	private Boolean isLocalRenderer;
	private Boolean isLocalServer;
	
	private String localDeviceName;

	public DLNAMediaController getDmc() {
		return dmc;
	}

	public void setDmc(DLNAMediaController dmc) {
		this.dmc = dmc;
	}

	public DLNAMediaRenderer getRenderer() {
		return renderer;
	}

	public void setRenderer(DLNAMediaRenderer renderer) {
		this.renderer = renderer;
	}

	public DLNAMediaServer getServer() {
		return server;
	}

	public void setServer(DLNAMediaServer server) {
		this.server = server;
	}

	public Boolean getHasRenderer() {
		return hasRenderer;
	}

	public void setHasRenderer(Boolean hasRenderer) {
		this.hasRenderer = hasRenderer;
	}

	public Boolean getIsLocalRenderer() {
		return isLocalRenderer;
	}

	public void setIsLocalRenderer(Boolean isLocalRenderer) {
		this.isLocalRenderer = isLocalRenderer;
	}

	public Boolean getIsLocalServer() {
		return isLocalServer;
	}

	public void setIsLocalServer(Boolean isLocalServer) {
		this.isLocalServer = isLocalServer;
	}

	public String getLocalDeviceName() {
		return localDeviceName;
	}

	public void setLocalDeviceName(String localDeviceName) {
		this.localDeviceName = localDeviceName;
	}

	public LaunchActivity getLaunchActivity() {
		return launchActivity;
	}

	public void setLaunchActivity(LaunchActivity launchActivity) {
		this.launchActivity = launchActivity;
	}
	
	
	
	
	
}

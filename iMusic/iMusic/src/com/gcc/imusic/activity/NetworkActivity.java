package com.gcc.imusic.activity;

import java.util.ArrayList;
import java.util.List;

import com.gcc.imusic.CustomListAdapter;
import com.gcc.imusic.MusicApplication;
import com.gcc.imusic.R;
import com.gcc.imusic.dlna.DLNAMediaRenderer;

import android.app.Dialog;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class NetworkActivity extends BaseActivity{

	private static final String TAG = "NetworkActivty";
	
	private ListView listView ;
	private List<ScanResult> wifiScanResults;
	private CustomListAdapter adapter;
	private String udn = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		
		super.initActivity();
		Intent intent = getIntent();
		if (intent != null) {
			udn = intent.getStringExtra("udn");
			Log.v(TAG, udn);
		}
		
		listView = (ListView)findViewById(R.id.activity_list_view);
		WifiManager wifiManager = (WifiManager)getSystemService(WIFI_SERVICE);
		wifiScanResults  = wifiManager.getScanResults();
		if (wifiScanResults != null && wifiScanResults.size() > 0) {
			List<Object> list = new ArrayList<Object>();
			Log.v(TAG, "result size "+wifiScanResults.size());
			List<ScanResult> temp  = new ArrayList<ScanResult>();
			for ( ScanResult scanResult : wifiScanResults) {
				if (list.contains(scanResult.SSID)) {
					temp.add(scanResult);
				}else {
					list.add(scanResult.SSID);
					Log.v(TAG, "ssid "+scanResult.SSID);
				}
			}
			for (ScanResult scanResult : temp) {
				wifiScanResults.remove(scanResult);
			}
			adapter = new CustomListAdapter(this, CustomListAdapter.NORMAL_LIST, list);
			listView.setAdapter(adapter);
		}
		
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				// TODO Auto-generated method stub
				final Dialog dialog = new Dialog(NetworkActivity.this, R.style.AlertDialog);
				dialog.setContentView(R.layout.alert_dialog);
				TextView titleTextView = (TextView)dialog.findViewById(R.id.title);
				TextView contentTextView = (TextView)dialog.findViewById(R.id.content);
				Button cancelButton = (Button)dialog.findViewById(R.id.cancel);
				Button okButton = (Button)dialog.findViewById(R.id.ok);
				final EditText editText = (EditText)dialog.findViewById(R.id.edit_text);
//				titleTextView.setText("输入密码");
				String capabilities = wifiScanResults.get(position).capabilities.toUpperCase();
				
				
				Log.v(TAG, "capabilities "+wifiScanResults.get(position).capabilities.toUpperCase());
				contentTextView.setText("输入\""+wifiScanResults.get(position).SSID+"\"的密码");
				cancelButton.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
				
				okButton.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						connectWifi(wifiScanResults.get(position), editText.getText().toString());
						dialog.dismiss();
					}
				});
				
				dialog.show();
			}
		});
		
		
	}
	
	private void connectWifi(ScanResult scanResult, String key){
		MusicApplication application = (MusicApplication)getApplicationContext();
		DLNAMediaRenderer mediaRenderer = application.getDmc().getRendererWithUDN(udn);
		if (mediaRenderer != null) {
			String ssid = scanResult.SSID;
			String capabilities = scanResult.capabilities.toUpperCase();
			String authAlgo = "open";
			String cipherAlgo = "none";
			int index = 0;
			if (capabilities.contains("WPA2PSK")) {
				authAlgo = "WPA2PSK";
			}else if (capabilities.contains("WPAPSK")) {
				authAlgo = "WPAPSK";
			}else if (capabilities.contains("WPA2")){
				authAlgo = "WPA2";
			}else if (capabilities.contains("WPA")) {
				authAlgo = "WPA";
			}
			
			if (capabilities.contains("CCMP")) {
				cipherAlgo = "AES";
				index = 3;
			}else if (capabilities.contains("TKIP")) {
				cipherAlgo = "TKIP";
				index = 2;
			}else if (capabilities.contains("WEP")){
				cipherAlgo = "WEP";
				index = 1;
			}
			
			mediaRenderer.SetNetwork(ssid, key, index, authAlgo, cipherAlgo);
			
		}
	}
	

}

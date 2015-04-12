package com.gcc.imusic.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.android.vtuner.async.LoadingAndParsingTask;
import com.android.vtuner.async.TokenLoadingTask;
import com.android.vtuner.async.LoadingAndParsingTask.LoadingParsingCallback;
import com.android.vtuner.data.BaseModel;
import com.android.vtuner.data.Directory;
import com.android.vtuner.data.Previous;
import com.android.vtuner.data.Search;
import com.android.vtuner.data.ShowOnDemand;
import com.android.vtuner.data.Station;
import com.android.vtuner.service.PlaybackBaseService.State;
import com.android.vtuner.service.PlaybackBaseServiceBinder.OnStateChangedListener;
import com.android.vtuner.service.PlaybackBaseServiceBinder.PlaybackServiceBinderCallback;
import com.android.vtuner.utils.UrlBuilder;
import com.gcc.imusic.CustomListAdapter;
import com.gcc.imusic.MusicApplication;
import com.gcc.imusic.R;
import com.gcc.imusic.dlna.LocalMediaRenderer;
import com.gcc.imusic.vtuner.PlaybackServiceBinder;

import android.R.integer;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class RadioActivity extends BaseActivity implements LoadingParsingCallback{

	
	public final static int FIRST_MODEL = 1;
	public final static int NORMAL_MODEL = 2;
	
	private final static String TAG = "RadioActivity";
	
	private MusicApplication application;
	private BaseModel model;
	private List<BaseModel> models;
	private CustomListAdapter adapter;
	private ListView listView;
	
	private TokenLoadingTask tokenLoadingTask;
	private LoadingAndParsingTask loadingAndParsingTask;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		listView = (ListView)findViewById(R.id.activity_list_view);
		
		super.initActivity();
		
		application = (MusicApplication)getApplicationContext();
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		Intent intent = getIntent();
		
		if (intent != null) {
			int radioType = intent.getIntExtra("radioType", 0);
			if (radioType == FIRST_MODEL) {
				models = (List<BaseModel>)intent.getExtras().getSerializable("models");
				getSupportActionBar().setTitle("Radio");
				initAdapter(models);
			}else if (radioType == NORMAL_MODEL){
				model = (BaseModel)intent.getExtras().getSerializable("model");
				
				if (model instanceof Directory) {
					getSupportActionBar().setTitle(((Directory)model).getName());
					loadingAndParsingTask = new LoadingAndParsingTask(this, "", false);
					UrlBuilder ub = UrlBuilder.getInstance();
					ub.setRequestParameters(this); 
					String url = ub.getRequestUrlAndr(((Directory)model).getUrl());
					String backUpUrl = ub.getRequestUrlAndr(((Directory)model).getUrlBackUp());
					loadingAndParsingTask.execute(url, backUpUrl);
				}else if (model instanceof ShowOnDemand) {
					getSupportActionBar().setTitle(((ShowOnDemand)model).getName());
					loadingAndParsingTask = new LoadingAndParsingTask(this, "", false);
					UrlBuilder ub = UrlBuilder.getInstance();
					ub.setRequestParameters(this); 
					String url = ub.getRequestUrlAndr(((ShowOnDemand)model).getUrl());
					String backUpUrl = ub.getRequestUrlAndr(((ShowOnDemand)model).getUrlBackUp());
					loadingAndParsingTask.execute(url, backUpUrl);
				}
			}
		}
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				BaseModel model = models.get(position);
				adapter.setSelectedIndex(position);
				adapter.notifyDataSetChanged();
				if (model instanceof Station) {
					if (application.getRenderer() == null) {
						return;
					}
					Log.v(TAG, "renderer not null");
					if (application.getRenderer().getIsLocalRenderer()) {
						LocalMediaRenderer localRenderer  = (LocalMediaRenderer)application.getRenderer();
						localRenderer.setStations(models);
						localRenderer.setContentIndex(position);
						localRenderer.setContentType(LocalMediaRenderer.CONTENT_TYPE_RADIO);
						localRenderer.setPlayMode(LocalMediaRenderer.PLAY_MODE_SEQUENT);
						localRenderer.playSong();
//						localRenderer.playVtuner(position);
					}
				}else {
					Intent intent = new Intent(RadioActivity.this, RadioActivity.class);
					intent.putExtra("radioType", RadioActivity.NORMAL_MODEL);
					intent.putExtra("model", model);
					startActivity(intent);
				}
			}
		});
		
	}
	
	private void initAdapter(List<BaseModel> models){
		List<Object> list = new ArrayList<Object>();
		list.addAll(models);
		adapter = new CustomListAdapter(this, CustomListAdapter.RADIO_LIST, list);
		listView.setAdapter(adapter);
	}

	@Override
	public void onFailed(Throwable arg0, String arg1, boolean arg2, Bundle arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoaded(List<BaseModel> list, String location, boolean firstPage,
			JSONObject outputPayload, Bundle bundle) {
		// TODO Auto-generated method stub
		list.remove(0);
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i) instanceof Search) {
				list.remove(i);
			}
			if (list.get(i) instanceof Previous) {
				list.remove(i);
			}
		}
		
		models = list;
		initAdapter(models);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	
}

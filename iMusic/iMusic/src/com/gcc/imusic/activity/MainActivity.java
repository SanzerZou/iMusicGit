package com.gcc.imusic.activity;


import java.util.List;

import org.json.JSONObject;

import com.android.vtuner.async.LoadingAndParsingTask;
import com.android.vtuner.async.TokenLoadingTask;
import com.android.vtuner.async.LoadingAndParsingTask.LoadingParsingCallback;
import com.android.vtuner.async.TokenLoadingTask.TokenLoadingCallback;
import com.android.vtuner.data.BaseModel;
import com.android.vtuner.data.Previous;
import com.android.vtuner.data.Search;
import com.android.vtuner.utils.RequestHelper;
import com.android.vtuner.utils.UrlBuilder;
import com.gcc.imusic.MusicApplication;
import com.gcc.imusic.R;
import com.gcc.imusic.R.drawable;
import com.gcc.imusic.R.id;
import com.gcc.imusic.R.layout;
import com.gcc.imusic.R.menu;
import com.gcc.imusic.R.string;
import com.gcc.imusic.activity.ServerFragment.OnServerItemSelectedListener;
import com.gcc.imusic.activity.SpeakerFragment.OnSpeakerItemSelectedListener;
import com.gcc.imusic.dlna.DLNAMediaRenderer;
import com.gcc.imusic.dlna.DLNAMediaServer;
import com.gcc.imusic.utils.RoundBitmap;
import com.gcc.imusic.view.PlayerShortCutView;


import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar.Tab;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.os.Build;
import android.preference.PreferenceManager;

public class MainActivity extends BaseActivity implements TabListener, LoadingParsingCallback, TokenLoadingCallback {

	private MusicApplication application;
	
	private SpeakerFragment speakerFragment = new SpeakerFragment();
	private ServerFragment serverFragment = new ServerFragment();
	private SettingFragment settingFragment = new SettingFragment();
	
	private static final int TAB_INDEX_COUNT = 3;
	private static final int TAB_INDEX_ONE = 0;
	private static final int TAB_INDEX_TWO = 1;
	private static final int TAB_INDEX_THREE = 2;
	
	private ViewPager viewPager;
	private ViewPagerAdapter viewPagerAdapter;
	
	private TokenLoadingTask tokenLoadingTask = null;
	private LoadingAndParsingTask loadingAndParsingTask = null;
	SharedPreferences sharedPreferences;
	
	private List<BaseModel> rootModels;
	
	private final static String TAG = "MainActivity";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        super.initActivity();
//        LinearLayout playerShortCutLayout = (LinearLayout)findViewById(R.id.player_summary);
//        playerShortCutLayout.addView(new PlayerShortCutView(this));
        
        
        application = (MusicApplication)getApplicationContext();
        application.getDmc().setOnRendererChangedListener(speakerFragment);
        application.getDmc().setOnServerChangedListener(serverFragment);
        
        setUpActionBar();
        setUpViewPager();
        setUpTabs();
        
//        albumImageButton.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent();
//				intent.setClass(MainActivity.this, PlayerActivity.class);
//				startActivity(intent);
//			}
//		});
        
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		Editor editor = sharedPreferences.edit();
		   
		editor.putString("key", "B9FB75E98F4F0CD3957E4F7EF7BC7754");
		editor.putString("IV", "CDD84F0564C79EA1");
		editor.putString("BASE", "http://androidgoldfish.vtuner.com/setupapp/androidgoldfish/");
		editor.putString("BASE_BACKUP", "http://androidgoldfish2.vtuner.com/setupapp/androidgoldfish/");
		editor.commit();
		
		ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isAvailable()) {
			retreiveAndSetToken();
			
		}else {
			Toast.makeText(this, "网络不可用", Toast.LENGTH_SHORT).show();
		}
		
    }

    private void setUpActionBar(){
    		final ActionBar actionBar = getSupportActionBar();
    		actionBar.setHomeButtonEnabled(false);
    		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    		actionBar.setDisplayShowTitleEnabled(false);
    		actionBar.setDisplayShowHomeEnabled(false);
    }
    
    private void setUpViewPager(){
    		viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
    		
    		viewPager = (ViewPager)findViewById(R.id.pager);
    		viewPager.setAdapter(viewPagerAdapter);
    		viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){

				@Override
				public void onPageScrollStateChanged(int state) {
					// TODO Auto-generated method stub
					//super.onPageScrollStateChanged(state);
					switch (state) {
					case ViewPager.SCROLL_STATE_IDLE:
						
						break;
					case ViewPager.SCROLL_STATE_DRAGGING:
						break;
					case ViewPager.SCROLL_STATE_SETTLING:
						break;
						
					default:
						break;
					}
				}

				@Override
				public void onPageSelected(int position) {
					// TODO Auto-generated method stub
					//super.onPageSelected(position);
					final ActionBar actionBar = getSupportActionBar();
					actionBar.setSelectedNavigationItem(position);
				}
    			
    		});
    		
    }
    
    private void setUpTabs(){
    		final ActionBar actionBar = getSupportActionBar();
    		for (int i = 0; i < viewPagerAdapter.getCount(); i++) {
				actionBar.addTab(actionBar.newTab().setText(viewPagerAdapter.getPageTitle(i)).setTabListener(this));
		}
    }
    
    
	public List<BaseModel> getRootModels() {
		return rootModels;
	}

	public void setRootModels(List<BaseModel> rootModels) {
		this.rootModels = rootModels;
	}

	private void retreiveAndSetToken() {
		// TODO Auto-generated method stub

		Log.v(TAG, "Retreive and Set Token");
		RequestHelper.instance().retreiveToken(this, this);
		
	}
    
    private void connectToServer() {
		Log.v(TAG, "Connect to Server");
		RequestHelper.instance().login(this, this);
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onTabSelected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		Log.v(TAG, "onTabSelected "+arg0);
		viewPager.setCurrentItem(arg0.getPosition());
	}


	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}
	
	public class ViewPagerAdapter extends FragmentPagerAdapter{

		public ViewPagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			switch (arg0) {
			case TAB_INDEX_ONE:
				return speakerFragment;
			case TAB_INDEX_TWO:
				return serverFragment;
			case TAB_INDEX_THREE:
				return settingFragment;
				
			default:
				break;
			}
			return null;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return TAB_INDEX_COUNT;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			// TODO Auto-generated method stub
			String tabLabel = null;
			switch (position) {
			case TAB_INDEX_ONE:
				tabLabel = getString(R.string.speaker);
				break;
			case TAB_INDEX_TWO:
				tabLabel = getString(R.string.server);
				break;
			case TAB_INDEX_THREE:
				tabLabel = getString(R.string.setting);
				break;
			default:
				break;
			}
			return tabLabel;
//			return super.getPageTitle(position);
		}
		
		
		
	}

//	@Override
//	public void onSpeakerItemSelectedListener(DLNAMediaRenderer renderer) {
//		// TODO Auto-generated method stub
//		application.setRenderer(renderer);
//		if (renderer.getIsLocalRenderer()) {
//			Log.v(TAG, "Select Local Renderer");
//		}else {
//			Log.v(TAG, "Select Remote Renderer");
//			
//		}
//	}

//	@Override
//	public void onServerItemSelected(DLNAMediaServer server) {
//		// TODO Auto-generated method stub
//		
//		Intent intent = new Intent(this, ContentActivity.class);
//		intent.putExtra("contentType", ContentActivity.DMS_CONTENT_TYPE);
//		intent.putExtra("udn", server.getUdn().toString());
//		intent.putExtra("containerId", "0");
//		startActivity(intent);
//		
//		Log.v(TAG, "udn "+server.getUdn().toString());
//		Log.v(TAG, "friendly name" + server.getFriendlyName());
//		
//		if (server.getIsLocalServer()) {
//			Log.v(TAG, "Select Local Server");
////			server.browseDirectChildren(listener, containerId);
//			
//		}else {
//			Log.v(TAG, "Select Remote Server");
//		}
//	}

	

	@Override
	public void onFailed(Throwable arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (application.getDmc() == null) {
			return;
		}
//		if (application.getLaunchActivity() != null) {
//			new Thread(new Runnable() {
//				
//				@Override
//				public void run() {
//					// TODO Auto-generated method stub
//					application.getLaunchActivity().exit();
//				}
//			}).start();
//			
//		}
		List<DLNAMediaRenderer> mediaRenderers = application.getDmc().getRenderers();
		for (DLNAMediaRenderer dlnaMediaRenderer : mediaRenderers) {
			dlnaMediaRenderer.exit();
			
		}
		application.getLaunchActivity().exit();
		
	}

	@Override
	public void onLoaded(String token) {
		// TODO Auto-generated method stub
		Log.v(TAG, "Load Token "+token);
		UrlBuilder.getInstance().setMacValue(this, token);
		connectToServer();
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
		rootModels = list;
	}

//	@Override
//	public void onVtunerSelected() {
//		// TODO Auto-generated method stub
//		Intent intent = new Intent(this, ContentActivity.class);
//		intent.putExtra("contentType", ContentActivity.FIRST_RADIO_CONTENT_TYPE);
//		ArrayList<BaseModel> list = new ArrayList<BaseModel>();
//		list.addAll(rootModels);
//		intent.putExtra("models", list);
//		startActivity(intent);
//	}

}

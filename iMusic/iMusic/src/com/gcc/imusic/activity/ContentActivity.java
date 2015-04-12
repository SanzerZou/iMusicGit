package com.gcc.imusic.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

//import org.teleal.cling.model.action.ActionException;
//import org.teleal.cling.model.action.ActionInvocation;
//import org.teleal.cling.model.message.UpnpResponse;
//import org.teleal.cling.model.meta.Service;
//import org.teleal.cling.model.types.ErrorCode;
//import org.teleal.cling.support.model.DIDLContent;
//import org.teleal.cling.support.model.container.Container;
//import org.teleal.cling.support.model.item.Item;
//import org.teleal.cling.support.model.item.MusicTrack;










import org.fourthline.cling.model.action.ActionException;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.ErrorCode;
import org.fourthline.cling.support.model.DIDLContent;
import org.fourthline.cling.support.model.container.Container;
import org.fourthline.cling.support.model.item.Item;
import org.fourthline.cling.support.model.item.MusicTrack;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
import se.emilsjolander.stickylistheaders.R.styleable;

import com.android.vtuner.data.BaseModel;
import com.gcc.imusic.CharacterParser;
import com.gcc.imusic.MusicApplication;
import com.gcc.imusic.PinyinComparator;
import com.gcc.imusic.R;
import com.gcc.imusic.StickAdapter;
import com.gcc.imusic.callback.ContentBrowseActionCallback.ContentBrowseActionListener;
import com.gcc.imusic.dlna.ContentItem;
import com.gcc.imusic.dlna.DLNAMediaRenderer;
import com.gcc.imusic.dlna.DLNAMediaServer;
import com.gcc.imusic.dlna.LocalMediaRenderer;
import com.gcc.imusic.utils.RoundBitmap;
import com.wireme.mediaserver.ContentTree;

import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class ContentActivity extends BaseActivity implements ContentBrowseActionListener{
	
	public final static int DMS_CONTENT_TYPE = 1;
	public final static int FIRST_RADIO_CONTENT_TYPE = 2;
	public final static int NORMAL_RADIO_CONTENT_TYPE = 3;
	private final static String TAG = "ContentActivity";

	private ActionBar actionBar;
//	private ListView listView;
	private ArrayList<ContentItem> contentItems;
	
	private MusicApplication application;
	//private ImageButton albumImageButton;
	
	private StickyListHeadersListView listView;
	private StickAdapter adapter;
	private PinyinComparator pinyinComparator;
	private CharacterParser characterParser;
	private String sortString;
	private String label="0";
	
	private int contentType;
	
	private String containerId;
	private String udn;
	
	private BaseModel model;
	private ArrayList<BaseModel> models;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_content);
		
		super.initActivity();
		pinyinComparator = new PinyinComparator();
		actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle("魔杰座");
		
//		albumImageButton = (ImageButton)findViewById(R.id.album_summary);
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.album);
//        albumImageButton.setImageBitmap(RoundBitmap.getRoundBitmap(bitmap, 108));
		
		contentItems = new ArrayList<ContentItem>();
		application = (MusicApplication)getApplicationContext();
		
		listView = (StickyListHeadersListView)findViewById(R.id.content_list);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				adapter.setSelectedIndex(position);
				adapter.notifyDataSetChanged();

				ContentItem contentItem = contentItems.get(position);
				if (contentItem.isContainer()) {
					Intent intent = new Intent(ContentActivity.this, ContentActivity.class);
					Bundle bundle = new Bundle();
					intent.putExtra("contentType", ContentActivity.DMS_CONTENT_TYPE);
					intent.putExtra("udn", udn);
					intent.putExtra("containerId", contentItem.getContainer().getId());
					intent.putExtra("title", contentItem.getContainer().getTitle());
					intent.putExtra("label", label);
					intent.putExtras(bundle);
					startActivity(intent);
				}else {
					DLNAMediaRenderer mediaRenderer = application.getRenderer();
					if (mediaRenderer != null) {
						DLNAMediaServer mediaServer = application.getServer();
						mediaRenderer.setContentItems(contentItems);
						mediaRenderer.setContentIndex(position);
						mediaRenderer.setIsLocalContent(mediaServer.getIsLocalServer());
						if (mediaRenderer.getIsLocalRenderer()) {
							((LocalMediaRenderer)mediaRenderer).setContentType(LocalMediaRenderer.CONTENT_TYPE_SONG);
						}
						mediaRenderer.playSong();
					}
				}
			}
		});
		
		Intent intent = getIntent();
		if (intent != null) {
			contentType = intent.getIntExtra("contentType", 0);
			if (contentType == DMS_CONTENT_TYPE) {
				containerId = intent.getStringExtra("containerId");
				udn =intent.getStringExtra("udn");
				label = intent.getStringExtra("label");
				if (containerId != null && udn != null) {
					DLNAMediaServer mediaServer = application.getDmc().getServerWithUDN(udn);
					actionBar.setTitle(intent.getStringExtra("title"));
					Log.v(TAG, "udn "+mediaServer.getUdn().toString());
					Log.v(TAG, "friendly name "+mediaServer.getFriendlyName());
					if (mediaServer != null) {
						mediaServer.browseDirectChildren(this, containerId);
					}
				}
			}else if (contentType == FIRST_RADIO_CONTENT_TYPE) {
				model = (BaseModel) intent.getExtras().getSerializable("model");
				
				
				
			}else if (contentType == NORMAL_RADIO_CONTENT_TYPE){
				models = (ArrayList<BaseModel>) intent.getExtras().getSerializable("models");
				
			}
			
		}
		
	}
	
	public void initData(){
		characterParser = CharacterParser.getInstance();
		for(int i=0; i<contentItems.size(); i++){
		String pinyin = null;
		if(label.equals("1") && !contentItems.get(i).isContainer()){
			MusicTrack musicTrack = (MusicTrack)contentItems.get(i).getItem();
			pinyin = characterParser.getSelling(musicTrack.getAlbum().trim());
			sortString = pinyin.substring(0, 1).toUpperCase();
		}else {
		pinyin =  characterParser.getSelling(contentItems.get(i).getTitle().trim());
		sortString = pinyin.substring(0, 1).toUpperCase();
		}
		//Log.v("browse",sortString);
		// ������ʽ���ж�����ĸ�Ƿ���Ӣ����ĸ
		if(sortString.matches("[0-9A-Z]")){
			contentItems.get(i).setSortLetters(sortString.toUpperCase());
		}		else{
			contentItems.get(i).setSortLetters("#");
		}
		Log.v("initdata","success");
		}
	}

	@Override
	public void received(Service service, ActionInvocation actionInvocation,
			DIDLContent didl) {
		// TODO Auto-generated method stub
		try {
				contentItems.clear();
				for (Container childContainer : didl.getContainers()) {
					contentItems.add(new ContentItem(childContainer, service));
				}
				for (Item childItem : didl.getItems()) {
					contentItems.add(new ContentItem(childItem, service));
				}
				
				Log.v(TAG, "contentItems size"+contentItems.size());
				
				ContentActivity.this.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
//						adapter = new ContentAdapter(ContentActivity.this);
//						listView.setAdapter(adapter);
						initData();
						initAdapter();
					}
				});
				
		}catch (Exception ex) {
			actionInvocation.setFailure(new ActionException(
					ErrorCode.ACTION_FAILED,
					"Can't create list childs: " + ex, ex));
		}
	}

	public void initAdapter(){
		if (application.getServer().getIsLocalServer()) {
			if(containerId.equals(ContentTree.ARTIST)){
				label = "1";
			}else if (containerId.equals(ContentTree.GENRE) || containerId.equals(ContentTree.ALBUM)){
				label = "2";
			}else if (containerId.equals(ContentTree.SONG)){
				label = "3";
			}
			Log.v("DLNA","init label="+label);
			Collections.sort(contentItems,pinyinComparator);
		}
			adapter = new StickAdapter(this, contentItems,label);
			listView.setAdapter(adapter);
	}

	@Override
	public void failure(ActionInvocation invocation, UpnpResponse operation,
			String defaultMsg) {
		// TODO Auto-generated method stub
		
	}
	
	private static class ViewHolder{
		public TextView title;
	}
	
	public class ContentAdapter extends BaseAdapter{

		private LayoutInflater mInflater;
		
		
		public ContentAdapter(Context context){
			this.mInflater = LayoutInflater.from(context);
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return contentItems.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder ;
			
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.list_item_content, null);
				holder = new ViewHolder();
				holder.title = (TextView) convertView.findViewById(R.id.content_name);
				convertView.setTag(holder);
			}else {
				holder = (ViewHolder) convertView.getTag();
			
			}

			holder.title.setText(contentItems.get(position).getTitle());
			
			return convertView;
		}
		
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

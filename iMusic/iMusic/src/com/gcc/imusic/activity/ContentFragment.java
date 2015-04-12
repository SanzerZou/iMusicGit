package com.gcc.imusic.activity;

import java.util.List;

import com.gcc.imusic.R;
import com.gcc.imusic.dlna.ContentItem;
import com.gcc.imusic.dlna.DLNAMediaRenderer;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ContentFragment extends Fragment{

	private ListView listView;
	private List<ContentItem> contentItems;
	private ContentAdapter adapter;
	
	private final static String TAG = "ContentFragment";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_content, container, false);
		listView = (ListView)view.findViewById(R.id.content_list);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Log.v(TAG, "onItemClick "+position);
			}
		});
		
		return view;
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

			//holder.title.setText(contentItems.get(position).get);
			
			return convertView;
		}
		
	}

}

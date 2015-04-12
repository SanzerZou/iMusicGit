package com.gcc.imusic;

import java.util.ArrayList;
import java.util.List;

import com.android.vtuner.data.BaseModel;
import com.android.vtuner.data.Directory;
import com.android.vtuner.data.ShowOnDemand;
import com.android.vtuner.data.Station;
import com.gcc.imusic.dlna.ContentItem;
import com.gcc.imusic.dlna.DLNAMediaRenderer;
import com.gcc.imusic.dlna.DLNAMediaServer;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author mac
 *
 */
public class CustomListAdapter extends BaseAdapter{
	public final static int SPEAKER_LIST = 1;
	public final static int SERVER_LIST = 2;
	public final static int CONETNT_LIST = 3;
	public final static int RADIO_LIST = 4;
	public final static int NORMAL_LIST = 5;
	
	
	private Context context;
	private int listType;
	private List<Object> dataList;
	private List<String> titleList;
	
	private int selectedIndex = -1;
	
	private OnSpeakerItemListener onSpeakerItemListener;
	
	
	
	
	public CustomListAdapter(Context context, int listType, List<Object> dataList) {
		super();
		this.context = context;
		this.listType = listType;
		this.dataList = dataList;
		titleList = new ArrayList<String>();
		
		if (listType == SPEAKER_LIST) {
			for (Object data : dataList) {
				DLNAMediaRenderer mediaRenderer = (DLNAMediaRenderer)data;
				titleList.add(mediaRenderer.getFriendlyName());
			}
		}else if (listType == SERVER_LIST) {
			for (Object data : dataList) {
				DLNAMediaServer mediaServer = (DLNAMediaServer)data;
				titleList.add(mediaServer.getFriendlyName());
			}
			titleList.add("Radio");
		}else if (listType == RADIO_LIST){
			for (Object data : dataList) {
				BaseModel model = (BaseModel)data;
				if (model instanceof Directory){
					titleList.add(((Directory)model).getTitle());
				}else if (model instanceof ShowOnDemand) {
					titleList.add(((ShowOnDemand)model).getName());
				}else if (model instanceof Station) {
					titleList.add(((Station)model).getName());
				}
			}
		}else if (listType == CONETNT_LIST){
			for(Object data : dataList){
				ContentItem contentItem = (ContentItem)data;
				titleList.add(contentItem.getItem().getTitle());
			}
		}else if (listType == NORMAL_LIST){
			for(Object data : dataList){
				titleList.add((String)data);
			}
		}
		
	}

	
	public int getSelectedIndex() {
		return selectedIndex;
	}


	public void setSelectedIndex(int selectedIndex) {
		this.selectedIndex = selectedIndex;
	}


	public List<Object> getDataList() {
		return dataList;
	}


	public void setDataList(List<Object> dataList) {
		this.dataList = dataList;
		this.titleList.clear();
		if (listType == SPEAKER_LIST) {
			for (Object data : dataList) {
				DLNAMediaRenderer mediaRenderer = (DLNAMediaRenderer)data;
				titleList.add(mediaRenderer.getFriendlyName());
			}
		}else if (listType == SERVER_LIST) {
			for (Object data : dataList) {
				DLNAMediaServer mediaServer = (DLNAMediaServer)data;
				titleList.add(mediaServer.getFriendlyName());
			}
			titleList.add("Radio");
		}else if (listType == RADIO_LIST){
			for (Object data : dataList) {
				BaseModel model = (BaseModel)data;
				if (model instanceof Directory){
					titleList.add(((Directory)model).getTitle());
				}else if (model instanceof ShowOnDemand) {
					titleList.add(((ShowOnDemand)model).getName());
				}else if (model instanceof Station) {
					titleList.add(((Station)model).getName());
				}
			}
		}else if (listType == CONETNT_LIST){
			for(Object data : dataList){
				ContentItem contentItem = (ContentItem)data;
				titleList.add(contentItem.getItem().getTitle());
			}
		}
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return titleList.size();
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
			convertView = LayoutInflater.from(context).inflate(R.layout.list_item, null);
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.title);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
			
		}

		holder.title.setText(titleList.get(position));
		
		if (selectedIndex == position) {
			holder.title.setTextColor(Color.BLUE);
		}else {
			holder.title.setTextColor(Color.BLACK);
		}
		
		return convertView;
	}
	
	
	private static class ViewHolder{
		public TextView title;
	}
	
	public interface OnSpeakerItemListener{
		public void onSpeakerItemClick(DLNAMediaRenderer renderer);
	}
	
	public interface OnServerItemListener{
		
	}

}

package com.gcc.imusic;

import java.util.ArrayList;
import java.util.List;

//import org.teleal.cling.support.model.item.MusicTrack;






import org.fourthline.cling.support.model.item.MusicTrack;

import com.gcc.imusic.dlna.ContentItem;
import com.wireme.mediaserver.ContentTree;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class StickAdapter extends BaseAdapter implements StickyListHeadersAdapter, SectionIndexer{
	
	private Context context;
	private List<String> catalog;
	private List<String> data;
	private List<String> subData;
	private CharacterParser characterParser;
	private ArrayList<ContentItem> contentItems;
	private String label;
	private int[] sectionIndices;
	private Character[] sectionLetters;
	
	private int selectedIndex = -1;
	
	
	
	
	public int getSelectedIndex() {
		return selectedIndex;
	}

	public void setSelectedIndex(int selectedIndex) {
		this.selectedIndex = selectedIndex;
	}
	
	

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public StickAdapter(Context context, ArrayList<ContentItem> contentItems,String label){
		this.context = context;
        this.contentItems = contentItems;
        this.label = label;
        sectionIndices = getSectionIndices();
        sectionLetters = getSectionLetters();
		Log.v("DLNA","start adapter"+"lable="+label);
	}

	private int[] getSectionIndices() {
		ArrayList<Integer> msectionIndices = new ArrayList<Integer>();
		char lastFirstChar = contentItems.get(0).getSortLetters().charAt(0);
		msectionIndices.add(0);
		if(label.equals("1") && !contentItems.get(0).isContainer()){
			String album = ((MusicTrack)contentItems.get(0).getItem()).getAlbum();
			for(int i=1;i<contentItems.size();i++){
				if(!((MusicTrack)contentItems.get(i).getItem()).getAlbum().equals(album)){
					album = ((MusicTrack)contentItems.get(i).getItem()).getAlbum();
					msectionIndices.add(i);
					Log.v("DLNA","artist'album i="+i);
				}
			}
		}else {
		for (int i = 1; i < contentItems.size(); i++) {
			if (contentItems.get(i).getSortLetters().charAt(0) != lastFirstChar) {
				lastFirstChar = contentItems.get(i).getSortLetters().charAt(0);
				msectionIndices.add(i);
			}
		}
		}
		int[] sections = new int[msectionIndices.size()];
		for (int i = 0; i < msectionIndices.size(); i++) {
			sections[i] = msectionIndices.get(i);
		}
		return sections;
	}

	private Character[] getSectionLetters() {
		Character[] letters = new Character[sectionIndices.length];
		for (int i = 0; i < sectionIndices.length; i++) {
			letters[i] = contentItems.get(sectionIndices[i]).getSortLetters().charAt(0);
		}
		return letters;
	}



	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return contentItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return contentItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub	
		convertView = LayoutInflater.from(context).inflate(R.layout.content_list_item, parent,false);
		
		TextView title = (TextView)convertView.findViewById(R.id.title);
		
		
		if (selectedIndex > 0 && selectedIndex == position) {
			title.setTextColor(Color.BLUE);
		}else {
			title.setTextColor(Color.BLACK);
		}
		
		title.setText(contentItems.get(position).getTitle());
//		if (label.equals("1")){
//			if(!contentItems.get(position).isContainer()){
//				MusicTrack musicTrack = (MusicTrack)contentItems.get(position).getItem();
//				convertView = LayoutInflater.from(context).inflate(R.layout.content_list_item2, parent,false);
//				TextView title2 = (TextView)convertView.findViewById(R.id.title2);
//				TextView subTitle = (TextView)convertView.findViewById(R.id.sub_title2);
//				title2.setText(contentItems.get(position).getTitle());
//				subTitle.setVisibility(View.GONE);
//				Log.v("DLNA","album"+musicTrack.getAlbum());
//			}
		 if (label.equals("2") || label.equals("3")){
			if(!contentItems.get(position).isContainer()){
				MusicTrack musicTrack = (MusicTrack)contentItems.get(position).getItem();
				convertView = LayoutInflater.from(context).inflate(R.layout.content_list_item2, parent,false);
				TextView title2 = (TextView)convertView.findViewById(R.id.title2);
				TextView subTitle = (TextView)convertView.findViewById(R.id.sub_title2);

				if (selectedIndex > 0 && selectedIndex == position) {
					title2.setTextColor(Color.BLUE);
					subTitle.setTextColor(Color.BLUE);
				}else {
					title2.setTextColor(Color.BLACK);
					subTitle.setTextColor(Color.BLACK);
				}
				subTitle.setText(musicTrack.getCreator());
				title2.setText(musicTrack.getTitle());
				Log.v("DLNA","album"+musicTrack.getAlbum());
			}
		}
		
		return convertView;
	}

	@Override
	public View getHeaderView(int position, View convertView, ViewGroup parent) {
		HeaderViewHolder holder;
        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView =  LayoutInflater.from(context).inflate(R.layout.header, parent, false);
            
            holder.text = (TextView) convertView.findViewById(R.id.text);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }
        //set header text as first char in name
        String headerText = "" + contentItems.get(position).getSortLetters().charAt(0);
        if(label.equals("1") && !contentItems.get(position).isContainer()){
        	MusicTrack musicTrack = (MusicTrack)contentItems.get(position).getItem();
        	holder.text.setText(musicTrack.getAlbum());
        	holder.text.setGravity(Gravity.LEFT);
        	holder.text.setPadding(15, 0, 0, 0);
        	convertView.findViewById(R.id.line).setVisibility(View.GONE);
        } else if(label.equals(ContentTree.GENRE)|| label.equals("0")){
        	holder.text.setVisibility(View.GONE);
        	convertView.findViewById(R.id.line).setVisibility(View.GONE);
	    } else {       
        	holder.text.setText(headerText);
        	holder.text.setTextSize(TypedValue.COMPLEX_UNIT_DIP,26);
        }
        if(!label.equals("1") && getCount()<15){
        	holder.text.setVisibility(View.GONE);
        	convertView.findViewById(R.id.line).setVisibility(View.GONE);
        }
        
        
        return convertView;
	}
	@Override
	public long getHeaderId(int position) {
		// TODO Auto-generated method stub
//		if(label.equals("1") && !contentItems.get(position).isContainer()){
			return getSectionForPosition(position);
//		}else{
//		return contentItems.get(position).getSortLetters().charAt(0);
//		}
	}
	class HeaderViewHolder {
	        TextView text;
	    }

	class ViewHolder {
	        TextView text;
	    }

	@Override
	public Object[] getSections() {
		// TODO Auto-generated method stub
		return sectionLetters;
	}

	public int getSectionForPosition(int position) {
		for (int i = 0; i < sectionIndices.length; i++) {
			if (position < sectionIndices[i]) {
				return i - 1;
			}
		}
		return sectionIndices.length - 1;
}

/**
 * ���ݷ��������ĸ��Char asciiֵ��ȡ���һ�γ��ָ�����ĸ��λ��
 */
public int getPositionForSection(int section) {
	if (section >= sectionIndices.length) {
		section = sectionIndices.length - 1;
	} else if (section < 0) {
		section = 0;
	}
	return sectionIndices[section];
}

}

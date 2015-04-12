package com.gcc.imusic;

import java.util.Comparator;

import com.gcc.imusic.dlna.ContentItem;


/**
 * 
 * @author xiaanming
 *
 */
public class PinyinComparator implements Comparator<ContentItem> {

	public int compare(ContentItem o1, ContentItem o2) {
		if (o1.getSortLetters().equals("@")
				|| o2.getSortLetters().equals("#")) {
			return -1;
		} else if (o1.getSortLetters().equals("#")
				|| o2.getSortLetters().equals("@")) {
			return 1;
		} else {
			return o1.getSortLetters().compareTo(o2.getSortLetters());
		}
	}

}

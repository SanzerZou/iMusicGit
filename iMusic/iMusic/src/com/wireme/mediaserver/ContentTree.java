package com.wireme.mediaserver;

import java.util.HashMap;

import org.fourthline.cling.support.model.WriteStatus;
import org.fourthline.cling.support.model.container.Container;

//import org.teleal.cling.support.model.WriteStatus;
//import org.teleal.cling.support.model.container.Container;


public class ContentTree {

	public final static String AUDIO = "0";
	public final static String ARTIST = "1";
	public final static String ALBUM = "2";
	public final static String PLAYLIST = "3";
    public final static String GENRE = "4";
    public final static String SONG = "5";
	public final static String AUDIO_PREFIX = "audio-item-";
	
	private static HashMap<String, ContentNode> contentMap = new HashMap<String, ContentNode>();

	private static ContentNode rootNode = createRootNode();

	public ContentTree() {};

	protected static ContentNode createRootNode() {
		// create root container
		Container root = new Container();
		root.setId(AUDIO);
		root.setParentID("-1");
		root.setTitle("GNaP MediaServer root directory");
		root.setCreator("GNaP Media Server");
		root.setRestricted(true);
		root.setSearchable(true);
		root.setWriteStatus(WriteStatus.NOT_WRITABLE);
		root.setChildCount(0);
		ContentNode rootNode = new ContentNode(AUDIO, root);
		contentMap.put(AUDIO, rootNode);
		return rootNode;
	}
	
	public static ContentNode getRootNode() {
		return rootNode;
	}
	
	public static ContentNode getNode(String id) {
		if( contentMap.containsKey(id)) {
			return contentMap.get(id);
		}
		return null;
	}
	
	public static boolean hasNode(String id) {
		return contentMap.containsKey(id);
	}
	
	public static void addNode(String ID, ContentNode Node) {
		contentMap.put(ID, Node);
	}
}

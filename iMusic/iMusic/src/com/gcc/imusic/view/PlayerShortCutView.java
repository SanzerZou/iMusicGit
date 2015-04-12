package com.gcc.imusic.view;

import com.gcc.imusic.R;
import com.gcc.imusic.utils.RoundBitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageButton;

public class PlayerShortCutView extends FrameLayout{


	public PlayerShortCutView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		LayoutInflater.from(context).inflate(R.layout.player_summary, this);
		ImageButton albumImageButton = (ImageButton)findViewById(R.id.album_image);
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.album);
        albumImageButton.setImageBitmap(RoundBitmap.getRoundBitmap(bitmap, 108));
	}

	public PlayerShortCutView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		LayoutInflater.from(context).inflate(R.layout.player_summary, this);
		ImageButton albumImageButton = (ImageButton)findViewById(R.id.album_image);
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.album);
        albumImageButton.setImageBitmap(RoundBitmap.getRoundBitmap(bitmap, 108));
	}
	
	
}

package com.gcc.imusic.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Bitmap.Config;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

public class RoundBitmap {

	private static Bitmap getRoundCornerBitmap(Bitmap bitmap, float roundPx) {
		if (bitmap == null) 
			return null;
		
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		
		paint.setAntiAlias(true);
		paint.setColor(color);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
		
		
	}
	
	private static Bitmap scaleToSize(Bitmap bitmap, int size) {
		if (bitmap == null) 
			return null;
		
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float scale = 1;
		int min = width > height ? height : width;
		
		scale = (float)size / (float)min;
		
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);
		
		return Bitmap.createBitmap(bitmap, (width - min)/2, (height - min)/2, width, height, matrix, true);
		
	}
	
	public static Bitmap getRoundBitmap(Bitmap bitmap, int size){
		return getRoundCornerBitmap(scaleToSize(bitmap, size), size/2);
	}
}

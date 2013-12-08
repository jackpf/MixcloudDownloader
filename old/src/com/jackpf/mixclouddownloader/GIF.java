package com.jackpf.mixclouddownloader;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.view.View;

public class GIF extends View
{
	
	Movie movie, movie1;
	InputStream is = null, is1 = null;
	long moviestart;
	
	public GIF(Context context)
	{
		super(context);	
		
		is = context.getResources().openRawResource(R.drawable.wait);
		movie = Movie.decodeStream(is);
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		long now = android.os.SystemClock.uptimeMillis();
		if(moviestart == 0)
			moviestart = now;
		
		int relTime = (int) ((now - moviestart) % movie.duration()) ;
		
		movie.setTime(relTime);
		movie.draw(canvas, canvas.getWidth() / 4, 0);
		this.invalidate();
	}
}
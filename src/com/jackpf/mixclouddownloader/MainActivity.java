package com.jackpf.mixclouddownloader;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import com.jackpf.mixclouddownloader.R;
import com.jackpf.mixclouddownloader.Download.DownloadThread;

/**
 * Main activity
 */
public class MainActivity extends Activity
{
	/**
	 * On create
	 * 
	 * @param Bundle savedInstanceState
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
	}

	/**
	 * On options menu created
	 * 
	 * @param Menu menu
	 * @return boolean
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		return true;
	}
	
	/**
	 * On download button clicked
	 * 
	 * @param view
	 */
	public void onDownload(View view)
	{
		String url = ((EditText) findViewById(R.id.url)).getText().toString();
		
		new DownloadThread(this).execute(url);
	}
}

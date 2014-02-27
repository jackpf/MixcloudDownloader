package com.jackpf.mixclouddownloader;

import java.io.File;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity
{
	
	public static MainActivity instance;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		instance = this;
		
		predictMixUrl();
		populateDownloads();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		return true;
	}
	
	public void download(View view)
	{
		
		if(((CheckBox) findViewById(R.id.download_method)).isChecked())
		{
			Network.downloadMethod = Network.DOWNLOADMANAGER;
			
			((LinearLayout) findViewById(R.id.download)).setVisibility(LinearLayout.GONE);
		}
		else
		{
			Network.downloadMethod = Network.NATIVE;
			
			((LinearLayout) findViewById(R.id.download)).setVisibility(LinearLayout.VISIBLE);
		}
		
		String url = ((EditText) findViewById(R.id.url)).getText().toString();
		
		new Network().execute(url);
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static void predictMixUrl()
	{
		if(Integer.valueOf(android.os.Build.VERSION.SDK) < 11)
			return;
		
		ClipboardManager cm = (ClipboardManager) instance.getSystemService(CLIPBOARD_SERVICE);
		
		((EditText) instance.findViewById(R.id.url)).setText(cm.getText());
	}
	
	public static void populateDownloads()
	{
		File sdCard = Environment.getExternalStorageDirectory();
		final String folder = sdCard.getAbsolutePath() + "/" + Download.downloadFolder;
		
		File downloadFolder = new File(folder);
		
		File[] files = downloadFolder.listFiles();
		
		if(files != null)
		{
			for(File file : files)
			{
				if(file.getName().contains(".mp3"))
				{
					TextView tv = new TextView(instance);
					
					tv.setText(file.getName());
					tv.setTextColor(Color.BLACK);
					
					tv.setOnClickListener(new OnClickListener()
					{
						@Override
						public void onClick(View view)
						{
							Intent intent = new Intent();  
							intent.setAction(android.content.Intent.ACTION_VIEW);  
							File file = new File(folder + "/" + ((TextView) view).getText());  
							intent.setDataAndType(Uri.fromFile(file), "audio/*");  
							MainActivity.instance.startActivity(intent);
						}
					});
					
					((LinearLayout) instance.findViewById(R.id.downloads)).addView(tv);
				}
			}
		}
	}
	
	public static void errorMessage(String message)
	{
		((TextView) MainActivity.instance.findViewById(R.id.download_name)).setText(message);
	}

}

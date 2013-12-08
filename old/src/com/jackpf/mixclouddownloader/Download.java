package com.jackpf.mixclouddownloader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Download extends AsyncTask<String, Void, Void>
{
	
	public static final String downloadFolder = "MixcloudDownloads";
	
	private String errorString = null;

	@Override
	protected Void doInBackground(String... params)
	{
		String downloadUrl = params[0];
		
		if(downloadUrl == null || downloadUrl.equals(""))
		{
			errorString = "Could not extract stream";
			
			return null;
		}
		
		File sdCard = Environment.getExternalStorageDirectory();
		String folder = sdCard.getAbsolutePath() + "/" + downloadFolder;
		File dir = new File(folder);
		if(!dir.exists() && dir.mkdirs());
		
		if(Network.downloadMethod == Network.NATIVE)
		{
			try
			{
				URL url = new URL(downloadUrl);
	            URLConnection connection = url.openConnection();
	            connection.connect();
	            
	            int fileLength = connection.getContentLength();
	
	            InputStream input = new BufferedInputStream(url.openStream());
	            OutputStream output = new FileOutputStream(folder + "/" + Network.fileName);
	
	            byte data[] = new byte[1024];
	            long total = 0;
	            int count;
	            
	            while((count = input.read(data)) != -1)
	            {
	                total += count;
	                
	                int progress = (int) (total * 100 / fileLength);
	                
	                ((ProgressBar) MainActivity.instance.findViewById(R.id.download_progress)).setProgress(progress);
	                
	                output.write(data, 0, count);
	            }
	
	            output.flush();
	            output.close();
	            input.close();
	        }
			catch(Exception e)
			{
				e.printStackTrace();
				errorString = "An error has occured, please screenshot this and email it to me at jack.philip.farrelly@gmail.com: " + e.getMessage();
	        }
		}
		else if(Network.downloadMethod == Network.DOWNLOADMANAGER)
		{
			DownloadManager dm = (DownloadManager) MainActivity.instance.getSystemService(MainActivity.instance.DOWNLOAD_SERVICE);
	        Request request = new Request(Uri.parse(downloadUrl));
	        
	        request.setTitle(Network.fileName);
	        request.setDestinationInExternalPublicDir(downloadFolder, Network.fileName);
	        
	        long enqueue = dm.enqueue(request);
		}
		
        return null;
	}
	
	@Override
	protected void onPreExecute()
	{
		errorString = null;
		
		((LinearLayout) MainActivity.instance.findViewById(R.id.download)).setVisibility(View.VISIBLE);
		((TextView) MainActivity.instance.findViewById(R.id.download_name)).setText(Network.fileName);
	} 
	
	@Override
	protected void onPostExecute(Void result)
	{
		if(errorString != null)
			MainActivity.errorMessage(errorString);
		
		//refresh files list
		MainActivity.populateDownloads();
	}
}
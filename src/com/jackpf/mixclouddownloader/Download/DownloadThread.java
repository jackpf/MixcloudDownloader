package com.jackpf.mixclouddownloader.Download;

import java.security.InvalidParameterException;

import android.app.Activity;
import android.os.AsyncTask;

/**
 * Thread
 */
public class DownloadThread extends AsyncTask<String, Void, Void>
{
    /**
     * Activity instance
     */
    private Activity activity;
    
    /**
     * Constructor
     * 
     * @param Activity activity
     */
    public DownloadThread(Activity activity)
    {
        this.activity = activity;
    }
    
    /**
     * On pre execute
     */
	@Override
	protected void onPreExecute()
	{
	}

	/**
	 * Thread activity
	 * 
	 * @param String... params
	 * @return Void
	 * @throws InvalidParameterException
	 */
	@Override
	protected Void doInBackground(String... params)
	{
	    if (params.length < 1) {
	        throw new InvalidParameterException("No url parameter specified");
	    }
	    
	    String url = params[0];
	    
	    //new Downloader.download(url);
	    
		return null;
	}
	
	/**
	 * On post execute
	 */
	@Override
	protected void onPostExecute(Void result)
	{
	}
}

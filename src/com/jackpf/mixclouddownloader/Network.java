package com.jackpf.mixclouddownloader;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.AsyncTask;
import android.widget.TextView;

public class Network extends AsyncTask<String, Void, Void>
{

	private static final String OFF_URL		= "http://offliberty.com/off54.php",
						 		POST_DATA	= "track=%s&refext=";
	
	private String stream;
	public static String fileName;
	
	public static final int		NATIVE			= 1,
								DOWNLOADMANAGER	= 2;
	
	public static int downloadMethod = NATIVE;
	
	@Override
	protected Void doInBackground(String... params)
	{
		String mixUrl = params[0];
		mixUrl = mixUrl.split("\\?")[0];
		String postData	= String.format(POST_DATA, URLEncoder.encode(mixUrl));
	
		fileName = getFileName(mixUrl);
		
		try
		{
			URL url = new URL(OFF_URL);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setInstanceFollowRedirects(false); 
			connection.setRequestMethod("POST"); 
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
			connection.setRequestProperty("Charset", "UTF-8");
			connection.setRequestProperty("Content-Length", Integer.toString(postData.getBytes().length));
			connection.setUseCaches(false);
		
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(postData);
			wr.flush();
			wr.close();
			
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			
			String output = "", line = null;
			
			while((line = in.readLine()) != null)
				output += line + "\n";
			
			connection.disconnect();
			
			stream = extractStream(output);
		}
		catch(Exception e)
		{
			System.err.println(e.getMessage());
		}
	
		return null;
	}
	
	@Override
	protected void onPreExecute()
	{
		/*LinearLayout wait = (LinearLayout) MainActivity.instance.findViewById(R.id.wait);
		
		wait.setVisibility(View.VISIBLE);
		wait.addView(new GIF(MainActivity.instance));*/
		
		((TextView) MainActivity.instance.findViewById(R.id.download_name)).setText("Acquiring stream...");
	} 
	
	@Override
	protected void onPostExecute(Void result)
	{
		/*LinearLayout wait = (LinearLayout) MainActivity.instance.findViewById(R.id.wait);
		
		wait.setVisibility(View.GONE);
		wait.removeAllViews();*/
		
		new Download().execute(stream);
	}
	
	private String extractStream(String data)
	{
		String pattern = "(?i)a href=\"(.*?)\" class=\"download\"";
		Pattern p = Pattern.compile(pattern);
		
		Matcher m = p.matcher(data);
		
		if(m.find())
			return m.group(1);
		else
		{
			System.err.println("Could not extract stream");
			//MainActivity.errorMessage("Could not extract stream");
			
			return null;
		}
	}
	
	private String getFileName(String url)
	{
		String[] parts = url.split("/");
		String name = parts[parts.length - 1];
		name = name.replaceAll("-", " ") + ".mp3";
		
		return name;
	}
}
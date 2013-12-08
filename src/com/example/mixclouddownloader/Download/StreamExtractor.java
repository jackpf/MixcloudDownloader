package com.example.mixclouddownloader.Download;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * Stream extractor
 */
public class StreamExtractor
{
    public String getStream(String url) throws IOException, ClientProtocolException
    {
        if (!valid(url)) {
            throw new InvalidParameterException("Invalid url");
        }
        
        // Get the mixcloud page
        HttpClient httpClient = new DefaultHttpClient();
        
        HttpGet request = new HttpGet(url);
        HttpResponse response = httpClient.execute(request);

        String content = EntityUtils.toString(response.getEntity());
        
        // Extract the preview stream url
        String pattern = "data-preview-url=\"(.*?)\"";
        Matcher m = Pattern.compile(pattern).matcher(content);
        
        if (!m.find()) {
            throw new IOException("Unable to extract preview stream");
        }
        
        String previewUrl = m.group(0);
        
        // Format the stream url to point to the actual stream
    }
    
    private boolean valid(String url)
    {
        String pattern = "http://www.mixcloud.com/.*/.*/";
        
        return Pattern.compile(pattern).matcher(url).find();
    }
}

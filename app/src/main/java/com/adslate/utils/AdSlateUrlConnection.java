package com.adslate.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by pooja.b on 27-11-2015.
 */
public class AdSlateUrlConnection
{
    public String mEstablishConnection(URL url)
    {
        try
        {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.connect();


            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader rd = new BufferedReader(new InputStreamReader(in));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return String.valueOf(response);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }


    public String mEstablishConnectionJSON(String path,String values)
    {
        try
        {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.connect();

            DataOutputStream wr = new DataOutputStream(
                    conn.getOutputStream());
            wr.write(values.getBytes("UTF-8"));
            wr.close();
            wr.flush();



            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader rd = new BufferedReader(new InputStreamReader(in));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return String.valueOf(response);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }


}

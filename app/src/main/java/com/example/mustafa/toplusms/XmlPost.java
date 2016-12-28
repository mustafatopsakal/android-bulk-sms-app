package com.example.mustafa.toplusms;

import android.os.AsyncTask;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by mustafa on 8.08.2016.
 */
public class XmlPost extends AsyncTask<String,String,String> {

    @Override
    protected String doInBackground(String... strings) {
        String xml = strings[0].toString();
        String url = strings[1].toString();

        String result = null;
        try {
            result = makeRequest(xml,url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public String makeRequest(String xml,String Url) throws IOException {

        //MUTLUSOFT SUNUCULARINA XML İLE POST İSTEĞİ
        final URL url = new URL(Url);
        final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        String serverAnswer = "";

        try {
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-type", "text/xml");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream output = new BufferedOutputStream(conn.getOutputStream());
            output.write(xml.getBytes());
            output.flush();
            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
            byte[] contents = new byte[1024];
            int bytesRead = 0;
            while ((bytesRead = bis.read(contents)) != -1) {
                serverAnswer += new String(contents, 0, bytesRead);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }

        return serverAnswer;
    }


}

package com.liaofan_01.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.Handler;
import android.os.Message;

public class NetWorkThread extends Thread {

	private String request;
	private String url;
	private Handler mHandler;

	public NetWorkThread(String req) throws Exception {
		this.request = req;
	}

	public void setHandler(Handler handler) {
		this.mHandler = handler;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public void run() {
		try {
			URL url = new URL(this.url);
			HttpURLConnection http = (HttpURLConnection) url.openConnection();
			http.setDoOutput(true);
			http.setDoInput(true);
			http.setRequestMethod("POST");
			http.connect();
			OutputStreamWriter out = new OutputStreamWriter(
					http.getOutputStream(), "UTF-8");
			System.out.println(this.request);
			out.append(request);
			out.flush();
			out.close();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					http.getInputStream()));
			String line;
			StringBuffer buffer = new StringBuffer();
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			// System.out.println(buffer.toString());
			Message msg = this.mHandler.obtainMessage();
			msg.obj = buffer;
			this.mHandler.sendMessage(msg);
			reader.close();
			http.disconnect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

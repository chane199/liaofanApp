package com.liaofan_01.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.http.util.EncodingUtils;

import android.os.Environment;

public class Config {

	private static Config instance;
	private File file;

	public static Config getInstance() {
		if (instance == null)
			instance = new Config();
		return instance;
	}

	private Config() {
		String fileName = Environment.getExternalStorageDirectory().getPath()
				+ "/config.txt";
		file = new File(fileName);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	private final String url = "http://115.28.65.99/collegeAPI/";
	private String userName = "";

	public String getUrl() {
		return url;
	}

	public void setUserName(String user) {
		userName = user;
		try {
			FileOutputStream fout = new FileOutputStream(file);
			byte[] buffer = user.getBytes();
			fout.write(buffer);
			fout.flush();
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getUserName() {
		try {
			FileInputStream fin = new FileInputStream(file);
			int length = fin.available();
			byte[] buffer = new byte[length];
			fin.read(buffer);
			userName = EncodingUtils.getString(buffer, "UTF-8");
			fin.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userName;
	}
}

package com.liaofan_01.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.liaofan_01.R;
import com.liaofan_01.utils.Config;

public class APPActivity extends Activity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		String username = Config.getInstance().getUserName();
		System.out.println(username);
		Intent intent = new Intent();
		if (username == null || username.equals(""))
			intent.setClass(APPActivity.this, LogInActivity.class);
		else
			intent.setClass(APPActivity.this, MainActivity.class);
		startActivity(intent);
	}
}

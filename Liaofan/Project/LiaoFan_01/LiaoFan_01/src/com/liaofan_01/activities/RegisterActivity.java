package com.liaofan_01.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import com.example.liaofan_01.R;
import com.liaofan_01.utils.Config;
import com.liaofan_01.utils.NetWorkThread;

public class RegisterActivity extends Activity {

	private Button submitButton;
	private EditText nameEditText;
	private EditText pwdEditText;
	private Handler handler;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		submitButton = (Button) findViewById(R.id.registerSubmit);
		nameEditText = (EditText) findViewById(R.id.registerUserName);
		pwdEditText = (EditText) findViewById(R.id.registerPwd);
		handler = new RegisterHandler();

		submitButton.setOnClickListener(new submitButtonListener());
	}

	class submitButtonListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			System.out.println("Onclick");
			try {

				StringBuffer stringBuffer = new StringBuffer();
				stringBuffer.append("nickname="
						+ nameEditText.getText().toString() + "&" + "password="
						+ pwdEditText.getText().toString());
				NetWorkThread ntThread = new NetWorkThread(
						stringBuffer.toString());
				ntThread.setUrl(Config.getInstance().getUrl()
						+ "user/register_user.php");
				ntThread.setHandler(handler);
				ntThread.start();
				Message msg = handler.obtainMessage();
				handler.handleMessage(msg);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	class RegisterHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			JSONObject s = JSONObject.parseObject(msg.obj.toString());
			if (s.getIntValue("code") == 0) {
				new AlertDialog.Builder(RegisterActivity.this).setTitle("提示")
						.setMessage(s.getString("info"))
						.setPositiveButton("确定", null).show();
			} else {
				Intent intent = new Intent(RegisterActivity.this,
						LogInActivity.class);
				startActivity(intent);
			}
		}
	}
}

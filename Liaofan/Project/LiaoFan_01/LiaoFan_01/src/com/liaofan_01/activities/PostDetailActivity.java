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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.liaofan_01.R;
import com.liaofan_01.kindmanage.LocationActivity;
import com.liaofan_01.utils.Config;
import com.liaofan_01.utils.NetWorkThread;

public class PostDetailActivity extends Activity {
	private TextView title;
	private TextView secondTitle;
	private TextView content;
	private TextView author;
	private TextView publishTime;
	private TextView deadline;
	private ImageButton mapButton;
	private Handler handler;
	private String postID, json;
	private EditText addResponse;
	private Button submit;
	private LinearLayout responseList;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_postdetail);
		Intent intent = getIntent();
		postID = intent.getStringExtra("ID");
		title = (TextView) findViewById(R.id.postTitle);
		secondTitle = (TextView) findViewById(R.id.postSecondTitle);
		content = (TextView) findViewById(R.id.postContent);
		author = (TextView) findViewById(R.id.postAuthor);
		publishTime = (TextView) findViewById(R.id.publishTime);
		deadline = (TextView) findViewById(R.id.deadline);
		mapButton = (ImageButton) findViewById(R.id.postmap);
		addResponse = (EditText) findViewById(R.id.addresponse);
		submit = (Button) findViewById(R.id.addResSubmit);
		responseList = (LinearLayout) findViewById(R.id.responseList);

		mapButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(PostDetailActivity.this,
						LocationActivity.class);
				intent.putExtra("json", json);
				startActivity(intent);
			}
		});
		String request = "postid=" + postID;
		try {
			handler = new ResponseHandler();
			NetWorkThread nwt = new NetWorkThread(request);
			nwt.setUrl(Config.getInstance().getUrl()
					+ "response/queryResponseBase.php");
			nwt.setHandler(handler);
			nwt.start();
			Message msg = handler.obtainMessage();

			handler.handleMessage(msg);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					handler = new AddResponseHandler();
					StringBuffer str = new StringBuffer();
					str.append("postID=" + postID + "&nickname="
							+ Config.getInstance().getUserName() + "&content="
							+ addResponse.getText());
					NetWorkThread nt = new NetWorkThread(str.toString());
					nt.setUrl(Config.getInstance().getUrl()
							+ "response/addResponse.php");
					nt.setHandler(handler);
					nt.start();
					Message msg = handler.obtainMessage();
					handler.handleMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		request = "id=" + postID;
		try {
			NetWorkThread nt = new NetWorkThread(request);
			handler = new PostDetailHandler();
			nt.setUrl(Config.getInstance().getUrl() + "job/queryJobBase.php");
			nt.setHandler(handler);
			nt.start();
			Message msg = handler.obtainMessage();
			handler.handleMessage(msg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// FragmentManager fm = this.getFragmentManager();
		// FragmentTransaction ft = fm.beginTransaction();
		// ft.replace(R.id.postresponse, new ResponseListFragment(postID));
		// ft.commit();
	}

	class ResponseHandler extends Handler {
		public void handleMessage(Message msg) {
			JSONArray s = JSONObject.parseArray(msg.obj.toString());
			System.out.println(s.toJSONString());
			if (s.isEmpty())
				return;
			for (int i = 0; i < s.size(); i++) {
				JSONObject jsonObject = (JSONObject) s.get(i);
				TextView text = new TextView(PostDetailActivity.this);
				text.setText(jsonObject.getString("nickname") + ":"
						+ jsonObject.getString("content"));
				text.setTag(jsonObject.getString("nickname"));
				responseList.addView(text);
			}
		}
	}

	class AddResponseHandler extends Handler {
		public void handleMessage(Message msg) {
			JSONObject s = JSONObject.parseObject(msg.obj.toString());
			System.out.println(s.getIntValue("code"));
			if (s.getIntValue("code") == 0) {
				new AlertDialog.Builder(PostDetailActivity.this)
						.setTitle("提示信息").setMessage(s.getString("msg"))
						.setPositiveButton("确定", null).show();
			} else {
				TextView textView = new TextView(PostDetailActivity.this);
				textView.setText(Config.getInstance().getUserName() + ":"
						+ addResponse.getText());
				responseList.addView(textView, 0);
			}
		}
	}

	class PostDetailHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			JSONArray s = JSONObject.parseArray(msg.obj.toString());
			System.out.println(s.toJSONString());
			if (s.isEmpty())
				return;
			JSONObject jsonObject = (JSONObject) s.get(0);
			json = jsonObject.toJSONString();
			title.setText(jsonObject.getString("title"));
			// secondTitle.setText(jsonObject.getString(""));
			if (jsonObject.getBooleanValue("anonymous"))
				;
			else
				author.setText(jsonObject.getString("author_nickname"));
			content.setText(jsonObject.getString("content"));
			publishTime.setText(jsonObject.getString("publish_time1"));
			deadline.setText(jsonObject.getString("effective_end_time"));

		}
	}
}

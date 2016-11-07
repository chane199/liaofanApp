package com.liaofan_01.kindmanage;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.liaofan_01.R;

public class DetailPost extends Activity {

	TextView textShow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_post);

		textShow = (TextView) findViewById(R.id.testshow);
		Intent intent = getIntent();
		String postDetail = intent.getStringExtra("postDetail");
		try {
			JSONObject detail = new JSONObject(postDetail);
			textShow.setText("id=" + detail.getString("id") + "\n"
					+ "author_id=" + detail.getString("author_id")
					+ "\ncontent:="
					+ Html.fromHtml(detail.getString("content")));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detail_post, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}

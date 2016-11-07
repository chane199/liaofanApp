package com.liaofan_01.kindmanage;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.example.liaofan_01.R;

public class HomeActivity extends Activity {

	ListView kindList;
	JSONArray jsonArray;
	TextView tv1;
	ViewFlipper viewFlipper = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_home);
		tv1 = (TextView) findViewById(R.id.tv1);
		kindList = (ListView) findViewById(R.id.kindList);
		viewFlipper = (ViewFlipper) this.findViewById(R.id.details);
		viewFlipper.setOutAnimation(this, android.R.anim.slide_in_left);
		viewFlipper.setInAnimation(this, android.R.anim.slide_out_right);
		viewFlipper.startFlipping();
		viewFlipper.setFlipInterval(5000);
		// 定义发送请求的URL HttpUtil.BASE_URL + "job/queryAllJobs.php"
		String ur = HttpUtil.BASE_URL + "job/queryAllJobs.php";
		Map<String, String> map = null;
		String result = null;
		try {

			map = new HashMap<String, String>();
			map.put("kindName", "name");
			map.put("kindDesc", "desc");
			result = HttpUtil.postRequest(ur, map);
			jsonArray = new JSONArray(result); // 请求

			/**
			 * 这一部分是将获得的字符行进行整理，获得一个字符串数组 int length=jsonArray.length();//长度
			 * List<String> args = new ArrayList<String>(); for(int
			 * i=0;i<length;i++){ String kindDesc =
			 * ((JSONObject)getItem(i)).getString("title"); args.add(kindDesc);
			 * }
			 * 
			 * ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,
			 * R.layout.array_item,args); kindList.setAdapter(adapter);
			 **/

			// 使用ListView显示所有物品准种类
			kindList.setAdapter(new KindArrayAdapter(jsonArray, this));

		} catch (Exception e) {

			e.printStackTrace();
			new AlertDialog.Builder(this).setTitle("请求	")
					.setMessage("msg" + result).setPositiveButton("确定", null)
					.show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
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

	public Object getItem(int position) {
		// 获取指定列表项所包装的JSONObject
		return jsonArray.optJSONObject(position);
	}

}

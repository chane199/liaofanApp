package com.liaofan_01.kindmanage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.liaofan_01.R;

public class KindArrayAdapter extends BaseAdapter {
	// 需要包装的JSONArray
	private JSONArray kindArray;
	private Context ctx;

	public KindArrayAdapter(JSONArray kindArray, Context ctx) {
		this.kindArray = kindArray;
		this.ctx = ctx;
	}

	@Override
	public int getCount() {
		// 返回ListView包含的列表项的数量
		return kindArray.length();
	}

	@Override
	public Object getItem(int position) {
		// 获取指定列表项所包装的JSONObject
		return kindArray.optJSONObject(position);
	}

	@Override
	public long getItemId(int position) {
		try {
			return ((JSONObject) getItem(position)).getInt("id");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final String dataPost = ((JSONObject) getItem(position)).toString();
		// 定义一个线性布局管理器
		LinearLayout container = new LinearLayout(ctx);
		// 设置为垂直的线性布局管理器
		container.setOrientation(LinearLayout.VERTICAL);
		// container.setPadding(1, 5, 1, 5);
		container.layout(19, 50, 19, 10);

		// 定义一个关于帖子用户的线性布局管理器
		LinearLayout poster = new LinearLayout(ctx);
		// 设置为水平的线性布局管理器
		poster.setOrientation(LinearLayout.HORIZONTAL);
		poster.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 50));

		// 定义一个线性布局管理器
		LinearLayout linear = new LinearLayout(ctx);
		// 设置为水平的线性布局管理器
		linear.setOrientation(LinearLayout.HORIZONTAL);
		linear.setLayoutParams(new RelativeLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

		// 创建一个ImageView
		ImageView iv = new ImageView(ctx);
		iv.setPadding(10, 0, 20, 0);
		iv.setImageResource(R.drawable.item);
		// 将图片添加到LinearLayout中
		linear.addView(iv);
		// 创建一个发帖人的TextView
		TextView tvPost = new TextView(ctx);
		// 创建一个TextView
		TextView tv = new TextView(ctx);
		try {
			// 获取JSONArray数组元素的kindName属性
			String kindName = ((JSONObject) getItem(position))
					.getString("title");

			// 获取用户ID
			String category1 = ((JSONObject) getItem(position))
					.getString("category1");
			// 设置TextView所显示的内容
			tv.setText(kindName);
			if ("edu".equals(category1)) {
				tvPost.setText(Html.fromHtml("<font color='#78b6fb'>教育</font>"));
			} else if ("sport".equals(category1)) {
				tvPost.setText(Html.fromHtml("<font color='#78b6fb'>运动</font>"));
			} else if ("tour".equals(category1)) {
				tvPost.setText(Html.fromHtml("<font color='#78b6fb'>旅行</font>"));
			} else if ("friend".equals(category1)) {
				tvPost.setText(Html.fromHtml("<font color='#78b6fb'>交友</font>"));
			} else if ("study".equals(category1)) {
				tvPost.setText(Html.fromHtml("<font color='#78b6fb'>学习</font>"));
			} else
				tvPost.setText(Html.fromHtml("<font color='#78b6fb'>"
						+ category1 + "</font>"));

		} catch (JSONException e) {
			e.printStackTrace();
		}
		tv.setTextSize(18);
		tvPost.setTextSize(16);
		// 将TextView添加到LinearLayout中
		TextView laizi = new TextView(ctx);
		laizi.setText(Html.fromHtml("<font color='#999999'>来自</font>"));
		laizi.setTextSize(15);

		linear.addView(tv);
		poster.addView(laizi);
		poster.addView(tvPost);
		container.addView(poster);
		container.addView(linear);

		// 定义一个文本框来显示种类描述
		TextView descView = new TextView(ctx);
		descView.setPadding(30, 0, 0, 0);
		try {
			// 获取JSONArray数组元素的kindDesc属性
			String kindDesc = ((JSONObject) getItem(position))
					.getString("content");
			descView.setText(Html.fromHtml(kindDesc));// 去掉文本里面的HTML 标签
			descView.setTextColor(Color.GRAY);// 设置字体颜色
		} catch (JSONException e) {
			e.printStackTrace();
		}
		descView.setTextSize(16);
		container.addView(descView);

		container.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// DialogUtil.showDialog(ctx , "暂时空白" , false);

				Intent postDetail = new Intent(ctx, DetailPost.class);
				postDetail.putExtra("postDetail", dataPost);
				ctx.startActivity(postDetail);
			}

		});

		return container;
	}
}

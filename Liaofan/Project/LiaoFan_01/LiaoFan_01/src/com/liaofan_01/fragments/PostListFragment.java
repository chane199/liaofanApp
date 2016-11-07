package com.liaofan_01.fragments;

import java.util.ArrayList;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.liaofan_01.R;
import com.liaofan_01.utils.Config;
import com.liaofan_01.utils.NetWorkThread;
import com.liaofan_01.utils.PostTitleOnclickListener;

public class PostListFragment extends Fragment {
	private String category;
	private LinearLayout postlistLayout;
	private Handler handler;

	public PostListFragment(String str) {
		this.category = str;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_postlist, container,
				false);
		postlistLayout = (LinearLayout) view.findViewById(R.id.postlist);
		handler = new PostListHandler();
		NetWorkThread ntThread;

		String request = "category1=" + this.category;
		try {
			ntThread = new NetWorkThread(request);

			ntThread.setUrl(Config.getInstance().getUrl()
					+ "job/queryJobBase.php");
			ntThread.setHandler(handler);
			ntThread.start();
			Message msg = handler.obtainMessage();
			handler.handleMessage(msg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return view;
	}

	class PostListHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			JSONArray s = JSONObject.parseArray(msg.obj.toString());
			ArrayList<TextView> allPosts = new ArrayList<TextView>();
			if (s.isEmpty())
				return;
			for (int i = 0; i < s.size(); i++) {
				JSONObject jsonObject = (JSONObject) s.get(i);
				allPosts.add(new TextView(getActivity()));
				allPosts.get(i).setText(jsonObject.getString("title"));
				allPosts.get(i).setGravity(Gravity.CENTER_HORIZONTAL);
				allPosts.get(i).setTextSize(25);
				allPosts.get(i).setTag(jsonObject.getString("id"));
				allPosts.get(i).setOnClickListener(
						new PostTitleOnclickListener());
				postlistLayout.addView(allPosts.get(i));
			}
		}
	}
}

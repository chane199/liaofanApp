package com.liaofan_01.fragments;

import java.util.ArrayList;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
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

public class HomeFragment extends Fragment {

	private ViewPager pager;
	private ArrayList<View> viewContainter;
	private LinearLayout postsLayout;
	private Handler handler;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home, container, false);
		viewContainter = new ArrayList<View>();
		pager = (ViewPager) view.findViewById(R.id.hotPosts);
		handler = new TopPostHandler();
		postsLayout = (LinearLayout) view.findViewById(R.id.allPost);
		pager.setAdapter(new PagerAdapter() {
			// viewpager中的组件数量
			@Override
			public int getCount() {
				return viewContainter.size();
			}

			// 滑动切换的时候销毁当前的组件
			@Override
			public void destroyItem(ViewGroup container, int position,
					Object object) {
				((ViewPager) container).removeView(viewContainter.get(position));
			}

			// 每次滑动的时候生成的组件
			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				((ViewPager) container).addView(viewContainter.get(position));
				return viewContainter.get(position);
			}

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public int getItemPosition(Object object) {
				return super.getItemPosition(object);
			}

		});
		try {
			NetWorkThread nt = new NetWorkThread("");
			nt.setUrl(Config.getInstance().getUrl() + "job/queryAllJobs.php");
			nt.setHandler(handler);
			nt.start();
			Message msg = handler.obtainMessage();
			handler.handleMessage(msg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return view;
	}

	class TopPostHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			JSONArray s = JSONObject.parseArray(msg.obj.toString());
			ArrayList<TextView> allPosts = new ArrayList<TextView>();
			ArrayList<TextView> hotPosts = new ArrayList<TextView>();
			if (s.isEmpty())
				return;
			for (int i = 0, j = 0; i < s.size(); i++) {
				JSONObject jsonObject = (JSONObject) s.get(i);
				allPosts.add(new TextView(getActivity()));
				allPosts.get(i).setText(jsonObject.getString("title"));
				allPosts.get(i).setGravity(Gravity.CENTER_HORIZONTAL);
				allPosts.get(i).setTextSize(25);
				allPosts.get(i).setTag(jsonObject.getString("id"));
				allPosts.get(i).setOnClickListener(
						new PostTitleOnclickListener());
				postsLayout.addView(allPosts.get(i));
				if (jsonObject.getIntValue("push_top_status") == 1) {
					hotPosts.add(new TextView(getActivity()));
					hotPosts.get(j).setText(jsonObject.getString("title"));
					hotPosts.get(j).setGravity(Gravity.CENTER_HORIZONTAL);
					hotPosts.get(j).setTextSize(25);
					hotPosts.get(j).setTag(jsonObject.getString("id"));
					hotPosts.get(j).setOnClickListener(
							new PostTitleOnclickListener());
					j++;
				}
			}
			int nums = hotPosts.size() / 5;
			if (hotPosts.size() % 5 > 0)
				nums++;
			for (int i = 0; i < hotPosts.size(); i += 5) {
				LinearLayout lin = new LinearLayout(getActivity());
				lin.setId(R.layout.hotposts);
				lin.setOrientation(LinearLayout.VERTICAL);
				for (int j = i; j < i + 5 && j < hotPosts.size(); j++) {
					lin.addView(hotPosts.get(j));
				}
				TextView t = new TextView(getActivity());
				int temp = i / 5 + 1;
				t.setText(temp + " / " + nums);
				t.setGravity(Gravity.BOTTOM);
				lin.addView(t);
				viewContainter.add(lin);
			}
			pager.getAdapter().notifyDataSetChanged();
		}
	}
}

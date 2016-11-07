package com.liaofan_01.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.liaofan_01.R;
import com.liaofan_01.kindmanage.SystemBarTintManager;

@SuppressLint("ResourceAsColor")
public abstract class FragmentActivity extends Activity {
	private static final int ROOT_CONTAINER_ID = 0x90001;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LinearLayout layout = new LinearLayout(this);
		setContentView(layout);
		layout.setId(ROOT_CONTAINER_ID);
		getFragmentManager().beginTransaction()
				.replace(ROOT_CONTAINER_ID, getFragment()).commit();

		// 创建状态栏的管理实例
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		// 激活状态栏设置
		tintManager.setStatusBarTintEnabled(true);
		// 激活导航栏设置
		tintManager.setNavigationBarTintEnabled(true);

		// 设置一个颜色给系统栏
		tintManager.setTintColor(R.color.blue);
	}

	protected abstract Fragment getFragment();
}

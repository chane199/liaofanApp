package com.liaofan_01.activities;

import java.lang.Thread.UncaughtExceptionHandler;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.example.liaofan_01.R;
import com.liaofan_01.fragments.CategoryFragment;
import com.liaofan_01.fragments.HomeFragment;
import com.liaofan_01.fragments.MessageFragment;
import com.liaofan_01.fragments.MyPageFragment;
import com.liaofan_01.fragments.PostFragment;
import com.liaofan_01.kindmanage.EditPostActivity;
import com.liaofan_01.kindmanage.SystemBarTintManager;

@SuppressLint("ResourceAsColor")
public class MainActivity extends Activity implements OnClickListener {

	private LinearLayout homeMenu;
	private LinearLayout categoryMenu;
	private LinearLayout editMenu;
	private LinearLayout messageMenu;
	private LinearLayout myPageMenu;
	// 创建状态栏的管理实例
	SystemBarTintManager tintManager;
	private int menuId = 0;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 创建状态栏的管理实例
		tintManager = new SystemBarTintManager(this);
		// 激活状态栏设置
		tintManager.setStatusBarTintEnabled(true);
		// 激活导航栏设置
		tintManager.setNavigationBarTintEnabled(true);

		FragmentManager fm = getFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		transaction.replace(R.id.mainContent, new HomeFragment());
		transaction.commit();
		this.menuId = R.id.menuHome;

		// //设定状态栏的颜色，当版本大于4.4时起作用
		// if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
		// SystemBarTintManager tintManager = new SystemBarTintManager(this);
		// tintManager.setStatusBarTintEnabled(true);
		// //此处可以重新指定状态栏颜色
		// tintManager.setStatusBarTintResource(R.color.blue);
		// }
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {

			@Override
			public void uncaughtException(Thread thread, Throwable ex) {
				// TODO Auto-generated method stub
				System.out.println("dandy's uncaught: " + ex.toString());
			}
		});

		setContentView(R.layout.activity_main);

		homeMenu = (LinearLayout) findViewById(R.id.menuHome);
		categoryMenu = (LinearLayout) findViewById(R.id.menuCategory);
		editMenu = (LinearLayout) findViewById(R.id.menuEdit);
		messageMenu = (LinearLayout) findViewById(R.id.menuMessage);
		myPageMenu = (LinearLayout) findViewById(R.id.menuMyPage);

		homeMenu.setOnClickListener(this);
		categoryMenu.setOnClickListener(this);
		editMenu.setOnClickListener(this);
		messageMenu.setOnClickListener(this);
		myPageMenu.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// if (v.getId() == this.menuId)
		// return;
		Intent intent = null;
		FragmentManager fm = getFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		switch (v.getId()) {
		case R.id.menuHome:
			transaction.replace(R.id.mainContent, new HomeFragment());
			// this.menuId = R.id.menuHome;
			// intent=new Intent(MainActivity.this,HomeActivity.class);
			break;
		case R.id.menuCategory:
			transaction.replace(R.id.mainContent, new CategoryFragment());
			// this.menuId = R.id.menuCategory;
			break;
		case R.id.menuEdit:
			// intent = new Intent(MainActivity.this,EditActivity.class);
			// startActivity(intent);

			// 设置一个颜色给系统栏
			tintManager.setTintColor(Color.BLUE);

			transaction.replace(R.id.mainContent, new EditPostActivity());
			break;
		case R.id.menuMyPage:
			transaction.replace(R.id.mainContent, new PostFragment());
			// this.menuId = R.id.menuMyPage;
			break;
		case R.id.menuMessage:
			transaction.replace(R.id.mainContent, new MessageFragment());
			// this.menuId = R.id.menuMessage;
			break;
		default:
			break;
		}

		transaction.commit();
	}

	@TargetApi(19)
	private void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}

	// @Override
	// protected void onRestoreInstanceState(Bundle savedInstanceState) {
	// super.onRestoreInstanceState(savedInstanceState);
	// applySelectedColor();
	// }
	private void applySelectedColor() {

		tintManager.setTintColor(Color.BLUE);
	}

}

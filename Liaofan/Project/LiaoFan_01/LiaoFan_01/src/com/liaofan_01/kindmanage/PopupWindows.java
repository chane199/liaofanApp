package com.liaofan_01.kindmanage;

import java.io.File;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.example.liaofan_01.R;

public class PopupWindows extends PopupWindow {

	public PopupWindows(Context mContext, View parent) {

		View view = View.inflate(mContext, R.layout.select_popupwindows, null);
		view.startAnimation(AnimationUtils.loadAnimation(mContext,
				R.anim.fade_ins));
		LinearLayout ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
		// ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
		// R.anim.push_bottom_in_2));

		setWidth(LayoutParams.FILL_PARENT);
		setHeight(LayoutParams.FILL_PARENT);
		setBackgroundDrawable(new BitmapDrawable());
		setFocusable(true);
		setOutsideTouchable(true);
		setContentView(view);
		showAtLocation(parent, Gravity.BOTTOM, 0, 0);
		update();

		Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
		Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
		Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);
		bt1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				photo();
				dismiss();
			}
		});
		bt2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(
						// 相册
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				// startActivityForResult(i, RESULT_LOAD_IMAGE);
				dismiss();
			}
		});
		bt3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dismiss();
			}
		});

	}

	private static final int TAKE_PICTURE = 0;
	private static final int RESULT_LOAD_IMAGE = 1;
	private static final int CUT_PHOTO_REQUEST_CODE = 2;
	private static final int SELECTIMG_SEARCH = 3;
	private String path = "";
	private Uri photoUri;

	public void photo() {
		try {
			Intent openCameraIntent = new Intent(
					MediaStore.ACTION_IMAGE_CAPTURE);

			String sdcardState = Environment.getExternalStorageState();
			String sdcardPathDir = android.os.Environment
					.getExternalStorageDirectory().getPath()
					+ Constants.CACHE_DIR + "/photo/";
			// String sdcardPathDir = FileUtils.SDPATH1;
			File file = null;
			if (Environment.MEDIA_MOUNTED.equals(sdcardState)) {
				// 有sd卡，是否有myImage文件夹
				File fileDir = new File(sdcardPathDir);
				if (!fileDir.exists()) {
					fileDir.mkdirs();
				}
				// 是否有headImg文件
				file = new File(sdcardPathDir + System.currentTimeMillis()
						+ ".jpg");
			}
			if (file != null) {
				path = file.getPath();
				photoUri = Uri.fromFile(file);
				openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

				// startActivityForResult(openCameraIntent, TAKE_PICTURE);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

package com.liaofan_01.kindmanage;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.View;

import com.example.liaofan_01.R;

public class DialogUtil {
	// ����һ����ʾ��Ϣ�ĶԻ���
	public static void showDialog(final Context ctx, String msg,
			boolean closeSelf) {
		// ����һ��AlertDialog.Builder����
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx).setMessage(
				msg).setCancelable(false);
		if (closeSelf) {
			builder.setPositiveButton(R.string.sure, new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// ����ǰActivity
					((Activity) ctx).finish();
				}
			});
		} else {
			builder.setPositiveButton(R.string.sure, null);
		}
		builder.create().show();
	}

	// ����һ����ʾָ������ĶԻ���
	public static void showDialog(Context ctx, View view) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx)
				.setView(view).setCancelable(false)
				.setPositiveButton(R.string.sure, null);
		builder.create().show();
	}
}

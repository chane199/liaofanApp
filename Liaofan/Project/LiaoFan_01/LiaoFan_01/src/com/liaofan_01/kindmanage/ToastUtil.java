/**
 * 
 */
package com.liaofan_01.kindmanage;

import android.content.Context;
import android.widget.Toast;

import com.amap.api.maps2d.model.LatLng;

public class ToastUtil {

	public static void show(Context context, String info) {
		Toast.makeText(context, info, Toast.LENGTH_LONG).show();
	}

	public static void show(Context context, int info) {
		Toast.makeText(context, info, Toast.LENGTH_LONG).show();
	}

	public static void show(Context context, LatLng mTarget) {
		// TODO Auto-generated method stub
		Toast.makeText(context, mTarget.toString(), Toast.LENGTH_LONG).show();
	}
}

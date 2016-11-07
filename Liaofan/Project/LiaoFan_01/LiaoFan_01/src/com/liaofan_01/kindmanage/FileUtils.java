package com.liaofan_01.kindmanage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

public class FileUtils {

	/**
	 * @param args
	 */

	public static String SDPATH = Environment.getExternalStorageDirectory()
			+ Constants.CACHE_DIR + "/photo/thumb";
	public static String SDPATH1 = Environment.getExternalStorageDirectory()
			+ Constants.CACHE_DIR + "/photo/";

	public static void saveBitmap(Bitmap bm, String picName) {
		try {
			if (!isFileExist("")) {
				File tempf = createSDDir("");
			}
			File f = new File(SDPATH, picName + ".jpg");
			if (f.exists()) {
				f.delete();
			}
			FileOutputStream out = new FileOutputStream(f);
			bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.flush();
			out.close();
			Log.e("", "已经保存");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static File createSDDir(String string) throws IOException {
		// TODO Auto-generated method stub
		File dir = new File(SDPATH + string);
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			// 判断目录存创建成功
		}
		return dir;
	}

	public static boolean isFileExist(String string) {
		// TODO Auto-generated method stub
		File file = new File(SDPATH + string);
		file.isFile();
		return file.exists();
	}

	public static void delFile(String fileName) {
		File file = new File(SDPATH + fileName);
		if (file.isFile()) {
			file.delete();
		}
		file.exists();
	}

	public static void deleteDir(String path) {
		File dir = new File(path);
		if (dir == null || !dir.exists() || !dir.isDirectory())
			return;

		for (File file : dir.listFiles()) {
			if (file.isFile())
				file.delete(); // 删除所有文件
			else if (file.isDirectory())
				deleteDir(path); // 递规的方式删除文件夹
		}
		dir.delete();// 删除目录本身
	}
}

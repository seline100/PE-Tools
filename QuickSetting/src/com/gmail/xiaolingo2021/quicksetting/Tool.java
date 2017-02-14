package com.gmail.xiaolingo2021.quicksetting;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.text.TextPaint;
import android.util.Log;
import android.widget.Toast;

public class Tool {
	public static void ShowMessage(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}

	public static void ShowStatus(Context context, boolean type, boolean open,
			String msg) {
		if (type) {
			if (!open) {
				msg = "正在关闭" + msg;
			} else {
				msg = "正在打开" + msg;
			}
		} else {
			if (!open) {
				msg = msg + "已关闭";
			} else {
				msg = msg + "已打开 ";
			}
		}
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}

	// 删除指定文件夹所有文件
	public static boolean deleteFiles(String filePath) {
		try {
			File root = new File(filePath);
			File files[] = root.listFiles();
			if (files != null) {
				for (File f : files) {
					String path = f.getPath();
					File myFile = new File(path);
					if (myFile.exists()) {
						myFile.delete();
					}
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// 取文件名
	public static String getFileName(String apath) {
		int start = apath.lastIndexOf("/");
		int end = apath.lastIndexOf(".");
		if (start != -1 && end != -1) {
			return apath.substring(start + 1, end);
		} else {
			return "";
		}
	}

	/**
	 * 把Bitmap转Byte
	 */
	public static byte[] Bitmap2Bytes(Bitmap bm, int quality) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, quality, baos);
		return baos.toByteArray();
	}

	/**
	 * @param 图片缩放
	 * @param bitmap
	 *            对象
	 * @param w
	 *            要缩放的宽度
	 * @param h
	 *            要缩放的高度
	 * @return newBmp 新 Bitmap对象
	 */
	public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidth = ((float) w / width);
		float scaleHeight = ((float) h / height);
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap newBmp = Bitmap.createBitmap(bitmap, 0, 0, width, height,
				matrix, true);
		return newBmp;
	}

	public static Bitmap getBitmap(String file_path) {
		Bitmap bitmap = null;
		try {
			FileInputStream fis = new FileInputStream(file_path);
			bitmap = BitmapFactory.decodeStream(fis);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	public static void saveBitmap(Bitmap bm, String file_Path, int quality) {
		try {
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(file_Path));
			// 采用压缩文件的方法
			bm.compress(Bitmap.CompressFormat.JPEG, quality, bos);
			// 更新BufferedStream
			bos.flush();
			// 结束OutputStream
			bos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 格式化系统时间 yyyyMMddHHmmss
	public static String GetSystemFormat() {
		Date d = new Date();
		// 方式1 按系统的日期方式
		// Calendar cal = Calendar.getInstance();
		// cal.setTimeInMillis( time );
		// return cal.getTime().toLocaleString();

		// 方式2 按自定义格式
		SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMddHHmmss");
		Date currentTime = new Date(d.getTime());
		return format1.format(currentTime);
	}
	
	// 字体加粗
	public static void setFakeBoldText(TextPaint tp){
		tp.setFakeBoldText(true);
	}
}

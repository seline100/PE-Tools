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
				msg = "���ڹر�" + msg;
			} else {
				msg = "���ڴ�" + msg;
			}
		} else {
			if (!open) {
				msg = msg + "�ѹر�";
			} else {
				msg = msg + "�Ѵ� ";
			}
		}
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}

	// ɾ��ָ���ļ��������ļ�
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

	// ȡ�ļ���
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
	 * ��BitmapתByte
	 */
	public static byte[] Bitmap2Bytes(Bitmap bm, int quality) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, quality, baos);
		return baos.toByteArray();
	}

	/**
	 * @param ͼƬ����
	 * @param bitmap
	 *            ����
	 * @param w
	 *            Ҫ���ŵĿ��
	 * @param h
	 *            Ҫ���ŵĸ߶�
	 * @return newBmp �� Bitmap����
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
			// ����ѹ���ļ��ķ���
			bm.compress(Bitmap.CompressFormat.JPEG, quality, bos);
			// ����BufferedStream
			bos.flush();
			// ����OutputStream
			bos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ��ʽ��ϵͳʱ�� yyyyMMddHHmmss
	public static String GetSystemFormat() {
		Date d = new Date();
		// ��ʽ1 ��ϵͳ�����ڷ�ʽ
		// Calendar cal = Calendar.getInstance();
		// cal.setTimeInMillis( time );
		// return cal.getTime().toLocaleString();

		// ��ʽ2 ���Զ����ʽ
		SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMddHHmmss");
		Date currentTime = new Date(d.getTime());
		return format1.format(currentTime);
	}
	
	// ����Ӵ�
	public static void setFakeBoldText(TextPaint tp){
		tp.setFakeBoldText(true);
	}
}

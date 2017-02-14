package com.gmail.xiaolingo2021.quicksetting;

import java.lang.reflect.Method;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

public class Gprs {
	private Context context;

	public Gprs(Context context) {
		this.context = context;
	}

	/**
	 * 开启系统设置里的移动网络
	 */
	public boolean setGprs() {
		ConnectivityManager mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		Object[] arg = null;
		boolean isMobileDataEnable = false;
		try {
			isMobileDataEnable = invokeMethod("getMobileDataEnabled", arg);
			if (!isMobileDataEnable) {
//				Tool.ShowStatus(context, true, true, "Gprs");
				invokeBooleanArgMethod("setMobileDataEnabled", true);
//				Tool.ShowStatus(context, false, true, "Gprs");
			} else {
//				Tool.ShowStatus(context, true, false, "Gprs");
				invokeBooleanArgMethod("setMobileDataEnabled", false);
//				Tool.ShowStatus(context, false, false, "Gprs");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isMobileDataEnable;
	}

	public void setGprs(boolean open) {
		try {
//			Tool.ShowStatus(context, true, open, "Gprs");
			invokeBooleanArgMethod("setMobileDataEnabled", open);
//			Tool.ShowStatus(context, false, open, "Gprs");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean invokeMethod(String methodName, Object[] arg)
			throws Exception {

		ConnectivityManager mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

		Class ownerClass = mConnectivityManager.getClass();

		Class[] argsClass = null;
		if (arg != null) {
			argsClass = new Class[1];
			argsClass[0] = arg.getClass();
		}

		Method method = ownerClass.getMethod(methodName, argsClass);

		Boolean isOpen = (Boolean) method.invoke(mConnectivityManager, arg);

		return isOpen;
	}

	public Object invokeBooleanArgMethod(String methodName, boolean value)
			throws Exception {

		ConnectivityManager mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

		Class ownerClass = mConnectivityManager.getClass();

		Class[] argsClass = new Class[1];
		argsClass[0] = boolean.class;

		Method method = ownerClass.getMethod(methodName, argsClass);

		return method.invoke(mConnectivityManager, value);
	}

	private Object getSystemService(String arg0) {
		return context.getSystemService(arg0);
	}
}

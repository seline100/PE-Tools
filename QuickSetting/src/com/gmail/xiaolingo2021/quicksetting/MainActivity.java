package com.gmail.xiaolingo2021.quicksetting;

import android.location.LocationManager;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar.OnSeekBarChangeListener;
import net.youmi.android.AdManager;
import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;

public class MainActivity extends Activity {
	private WifiManager wifiManager;
	private BluetoothAdapter adapter;
	protected View btnCancelAirPlane;
	TelephonyManager telephonyManager;
	private Context context;
	private Gprs gprs;
	private int flag;
	private AudioManager audioManager;
	private SeekBar lights, music, phone, alarm;
	private ImageButton light, sound;
	private Button lightMin, light25, light50, light75, lightMax;
	private AudioManager mAudioManager;
	private RadioButton lock15, lock30, lock1, lock5;
	private RadioGroup group;
	private NotificationManager mNotificationManager;
	private ImageView musicv, phonev, alarmv;
	private int music2, tmp, phone2, alarm2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// 初始化应用的发布ID和密钥，以及设置测试模式
		AdManager.getInstance(this).init("4e67910595548da9",
				"d25dd56d54f3b415", false);
		// 实例化广告条
		AdView adView = new AdView(this, AdSize.FIT_SCREEN);
		// 获取要嵌入广告条的布局
		LinearLayout adLayout = (LinearLayout) findViewById(R.id.adLayout);
		// 将广告条加入到布局中
		adLayout.addView(adView);

		wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		adapter = BluetoothAdapter.getDefaultAdapter();
		context = this;
		gprs = new Gprs(context);
		telephonyManager = (TelephonyManager) getApplicationContext()
				.getSystemService(Context.TELEPHONY_SERVICE);
		audioManager = (AudioManager) this.getSystemService(AUDIO_SERVICE);
		// 声音调节，声音图片点击监听事件
		mAudioManager = (AudioManager) this
				.getSystemService(Context.AUDIO_SERVICE);
		lights = (SeekBar) this.findViewById(R.id.seekBar1);
		music = (SeekBar) this.findViewById(R.id.seekBar2);
		phone = (SeekBar) this.findViewById(R.id.seekBar3);
		alarm = (SeekBar) this.findViewById(R.id.seekBar4);
		addListener();
		lightMin = (Button) findViewById(R.id.Min);
		light25 = (Button) findViewById(R.id.l25);
		light50 = (Button) findViewById(R.id.l50);
		light75 = (Button) findViewById(R.id.l75);
		lightMax = (Button) findViewById(R.id.Max);

		musicv = (ImageView) findViewById(R.id.imageView2);
		phonev = (ImageView) findViewById(R.id.imageView4);
		alarmv = (ImageView) findViewById(R.id.imageView5);

		lightMin.setOnClickListener(new lightListenr());
		light25.setOnClickListener(new lightListenr());
		light50.setOnClickListener(new lightListenr());
		light75.setOnClickListener(new lightListenr());
		lightMax.setOnClickListener(new lightListenr());

		group = (RadioGroup) findViewById(R.id.radioGroup1);
		lock15 = (RadioButton) findViewById(R.id.radio0);
		lock30 = (RadioButton) findViewById(R.id.radio1);
		lock1 = (RadioButton) findViewById(R.id.radio2);
		lock5 = (RadioButton) findViewById(R.id.radio3);

		// 创建一个 NotificationManager的引用
		String ns = Context.NOTIFICATION_SERVICE;
		mNotificationManager = (NotificationManager) getSystemService(ns);
		// 定义Notification的各种属性
		int icon = R.drawable.icon; // 通知图标
		CharSequence tickerText = "欢迎使用PE设置助手"; // 状态栏显示的通知文本提示
		long when = System.currentTimeMillis(); // 通知产生的时间，会在通知信息里显示
		// 用上面的属性初始化 Nofification
		Notification notification = new Notification(icon, tickerText, when);

		notification.flags = Notification.FLAG_ONGOING_EVENT;
		notification.flags = Notification.FLAG_NO_CLEAR;

		RemoteViews contentView = new RemoteViews(getPackageName(),
				R.layout.notification);
		contentView.setImageViewResource(R.id.image, R.drawable.icon);
		contentView.setTextViewText(R.id.text, "快速设定");
		contentView.setTextViewText(R.id.text1, "点击进行快速修改系统设定及参数");
		notification.contentView = contentView;
		Intent notificationIntent = new Intent(this, MainActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, 0);
		notification.contentIntent = contentIntent;
		// 把Notification传递给NotificationManager
		mNotificationManager.notify(0, notification);

		// 媒体音量按钮
		tmp = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC); // 获取当前音乐音量
		if (tmp == 0) {
			((ImageView) musicv).setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.sound));
		} else {
			((ImageView) musicv).setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.sound1));
		}
		musicv.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				music2 = mAudioManager
						.getStreamVolume(AudioManager.STREAM_MUSIC); // 获取当前音乐音量
				if (music2 != 0) {
					((ImageView) musicv).setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.sound));
					music.setProgress(0);
					mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0,
							0);
					tmp = music2;
				} else {
					((ImageView) musicv).setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.sound1));
					music.setProgress(tmp);
					mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
							tmp, AudioManager.FLAG_PLAY_SOUND);
				}
			}
		});
		// phone音量
		tmp = mAudioManager.getStreamVolume(AudioManager.STREAM_RING); // 获取当前电话音量
		if (tmp == 0) {
			((ImageView) phonev).setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.phoneoff));
		} else {
			((ImageView) phonev).setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.phonesound));
		}
		phonev.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				phone2 = mAudioManager
						.getStreamVolume(AudioManager.STREAM_RING); // 获取当前电话音量
				if (phone2 != 0) {
					((ImageView) phonev).setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.phoneoff));
					phone.setProgress(0);
					mAudioManager.setStreamVolume(AudioManager.STREAM_RING, 0,
							0);
					tmp = phone2;
				} else {
					((ImageView) phonev).setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.phonesound));
					phone.setProgress(tmp);
					mAudioManager.setStreamVolume(AudioManager.STREAM_RING,
							tmp, AudioManager.FLAG_PLAY_SOUND);
				}
			}
		});
		// 闹钟声音
		tmp = mAudioManager.getStreamVolume(AudioManager.STREAM_ALARM); // 获取当前闹钟音量
		if (tmp == 0) {
			((ImageView) alarmv).setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.clock));
		} else {
			((ImageView) alarmv).setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.alartsound));
		}
		alarmv.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				alarm2 = mAudioManager
						.getStreamVolume(AudioManager.STREAM_ALARM); // 获取当前电话音量
				if (alarm2 != 0) {
					((ImageView) alarmv).setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.clock));
					alarm.setProgress(0);
					mAudioManager.setStreamVolume(AudioManager.STREAM_ALARM, 0,
							0);
					tmp = alarm2;
				} else {
					((ImageView) alarmv).setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.alartsound));
					alarm.setProgress(tmp);
					mAudioManager.setStreamVolume(AudioManager.STREAM_ALARM,
							tmp, AudioManager.FLAG_PLAY_SOUND);
				}
			}
		});
		// 锁屏时间
		int time;
		time = getScreenOffTime();
		if (time == 15000) {
			lock15.setChecked(true);
		} else if (time == 30000) {
			lock30.setChecked(true);
		} else if (time == 60000) {
			lock1.setChecked(true);
		} else if (time == 900000) {
			lock5.setChecked(true);
		}
		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// // 根据ID判断选择的按钮
				if (checkedId == R.id.radio0) {
					setScreenOffTime(15000);
				} else if (checkedId == R.id.radio1) {
					setScreenOffTime(30000);
				} else if (checkedId == R.id.radio2) {
					setScreenOffTime(60000);
				} else if (checkedId == R.id.radio3) {
					setScreenOffTime(900000);
				}
			}
		});
		// 蓝牙：
		final ImageButton bluetooth = (ImageButton) findViewById(R.id.bluetooth);
		if (adapter.isEnabled()) {
			((ImageButton) bluetooth).setImageDrawable(getResources()
					.getDrawable(R.drawable.bluetooth_on));
		} else {
			((ImageButton) bluetooth).setImageDrawable(getResources()
					.getDrawable(R.drawable.bluetooth));
		}
		bluetooth.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (adapter.isEnabled()) {
					((ImageButton) bluetooth).setImageDrawable(getResources()
							.getDrawable(R.drawable.bluetooth));
					Toast.makeText(MainActivity.this, "蓝牙关闭",
							Toast.LENGTH_SHORT).show();
					adapter.disable();
				} else {
					((ImageButton) bluetooth).setImageDrawable(getResources()
							.getDrawable(R.drawable.bluetooth_on));
					Toast.makeText(MainActivity.this, "蓝牙启动与关闭需要一定的时间，请耐心等待",
							Toast.LENGTH_SHORT).show();
					adapter.enable();
				}
			}
		});
		// wifi：
		final ImageButton wifi = (ImageButton) findViewById(R.id.wifi);
		if (wifiManager.isWifiEnabled()) {
			((ImageButton) wifi).setImageDrawable(getResources().getDrawable(
					R.drawable.wifi_on));
		} else {
			((ImageButton) wifi).setImageDrawable(getResources().getDrawable(
					R.drawable.wifi));
		}
		wifi.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (wifiManager.isWifiEnabled()) {
					((ImageButton) wifi).setImageDrawable(getResources()
							.getDrawable(R.drawable.wifi));
					Toast.makeText(MainActivity.this, "Wifi关闭",
							Toast.LENGTH_SHORT).show();
					wifiManager.setWifiEnabled(false);
				} else {
					((ImageButton) wifi).setImageDrawable(getResources()
							.getDrawable(R.drawable.wifi_on));
					Toast.makeText(MainActivity.this, "Wifi打开",
							Toast.LENGTH_SHORT).show();
					wifiManager.setWifiEnabled(true);
				}
			}
		});

		// flightmode：
		final ImageButton flightmode = (ImageButton) findViewById(R.id.flightmode);
		int modeIdx = isAirplaneModeOn();
		if (modeIdx == 1) {
			((ImageButton) flightmode).setImageDrawable(getResources()
					.getDrawable(R.drawable.flightmode_on));
		} else {
			((ImageButton) flightmode).setImageDrawable(getResources()
					.getDrawable(R.drawable.flightmode));
		}
		flightmode.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				int modeIdx = isAirplaneModeOn();
				if (modeIdx == 1) {
					((ImageButton) flightmode).setImageDrawable(getResources()
							.getDrawable(R.drawable.flightmode));
					setAirplaneMode(false);
					Toast.makeText(MainActivity.this, "飞行模式已关闭",
							Toast.LENGTH_SHORT).show();
				} else {
					((ImageButton) flightmode).setImageDrawable(getResources()
							.getDrawable(R.drawable.flightmode_on));
					setAirplaneMode(true);
					Toast.makeText(MainActivity.this, "飞行模式已打开",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		// 飞行模式的组件通信
		IntentFilter intentFilter = new IntentFilter(
				"android.intent.action.SERVICE_STATE");
		BroadcastReceiver receiver = new BroadcastReceiver() {
			public void onReceive(Context context, Intent intent) {
				Log.d("ANDROID_INFO", "Service state changed");
				Bundle bundle = intent.getExtras();
				if (bundle != null) {
					int state = bundle.getInt("state");
					Log.d("ANDROID_INFO", "state = " + state);
					switch (state) {
					case 0x00:
						Log.d("ANDROID_INFO", "Connect the net successfully.");
						break;
					case 0x01:
						Log.d("ANDROID_INFO", "Try to connect the net.");
						break;
					case 0x03:
						Log.d("ANDROID_INFO", "Set AirPlaneMode Successful.");
						break;
					}
				} else {
					Log.d("ANDROID_INFO", "bundle is null");
				}
			}
		};
		registerReceiver(receiver, intentFilter);
		// gps
		final ImageButton gps = (ImageButton) findViewById(R.id.gps);
		final LocationManager alm = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		if (alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
			((ImageButton) gps).setImageDrawable(getResources().getDrawable(
					R.drawable.gps_on));
		} else {
			((ImageButton) gps).setImageDrawable(getResources().getDrawable(
					R.drawable.gps));
		}
		gps.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
					((ImageButton) gps).setImageDrawable(getResources()
							.getDrawable(R.drawable.gps));
					Intent myIntent = new Intent(
							Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					startActivity(myIntent);
					Toast.makeText(MainActivity.this, "gps需要系统权限支持",
							Toast.LENGTH_SHORT).show();
				} else {
					((ImageButton) gps).setImageDrawable(getResources()
							.getDrawable(R.drawable.gps_on));
					Intent myIntent = new Intent(
							Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					startActivity(myIntent);
					Toast.makeText(MainActivity.this, "gps需要系统权限支持",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		// 数据流量
		final ImageButton data = (ImageButton) findViewById(R.id.data);
		if (telephonyManager.getDataState() == TelephonyManager.DATA_CONNECTED) {
			((ImageButton) data).setImageDrawable(getResources().getDrawable(
					R.drawable.data_on));
		} else {
			((ImageButton) data).setImageDrawable(getResources().getDrawable(
					R.drawable.data));
		}
		data.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				if (telephonyManager.getDataState() == TelephonyManager.DATA_CONNECTED) {
					Toast.makeText(MainActivity.this, "数据连接关闭",
							Toast.LENGTH_SHORT).show();
					gprs.setGprs();
					((ImageButton) data).setImageDrawable(getResources()
							.getDrawable(R.drawable.data));
				} else {
					Toast.makeText(MainActivity.this, "数据连接打开",
							Toast.LENGTH_SHORT).show();
					gprs.setGprs();
					((ImageButton) data).setImageDrawable(getResources()
							.getDrawable(R.drawable.data_on));
				}
			}
		});
		// 旋转屏幕：
		final ImageButton changescream = (ImageButton) findViewById(R.id.screanchange);
		flag = Settings.System.getInt(getContentResolver(),
				Settings.System.ACCELEROMETER_ROTATION, 0);
		if (flag == 1) {
			((ImageButton) changescream).setImageDrawable(getResources()
					.getDrawable(R.drawable.screanchange_on));
		} else {
			((ImageButton) changescream).setImageDrawable(getResources()
					.getDrawable(R.drawable.screanchange));
		}
		changescream.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				flag = Settings.System.getInt(getContentResolver(),
						Settings.System.ACCELEROMETER_ROTATION, 0);
				if (flag == 1) {
					((ImageButton) changescream)
							.setImageDrawable(getResources().getDrawable(
									R.drawable.screanchange));
					Toast.makeText(MainActivity.this, "旋转屏幕关闭",
							Toast.LENGTH_SHORT).show();
					Settings.System.putInt(getContentResolver(),
							Settings.System.ACCELEROMETER_ROTATION, 0);
				} else {
					((ImageButton) changescream)
							.setImageDrawable(getResources().getDrawable(
									R.drawable.screanchange_on));
					Toast.makeText(MainActivity.this, "旋转屏幕开启",
							Toast.LENGTH_SHORT).show();
					Settings.System.putInt(getContentResolver(),
							Settings.System.ACCELEROMETER_ROTATION, 1);
				}
			}
		});

		// 自动调节亮度：
		light = (ImageButton) findViewById(R.id.light_off);
		if (getScreenMode() == 1) {
			((ImageButton) light).setImageDrawable(getResources().getDrawable(
					R.drawable.lightauto2));
		} else {
			((ImageButton) light).setImageDrawable(getResources().getDrawable(
					R.drawable.light_off));
		}
		light.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (getScreenMode() == 1) {
					((ImageButton) light).setImageDrawable(getResources()
							.getDrawable(R.drawable.light_off));
					Toast.makeText(MainActivity.this, "背光开启",
							Toast.LENGTH_SHORT).show();
					saveScreenBrightness(255);
					setScreenBrightness(255);
					setScreenMode(1);
					setScreenMode(0);
					lights.setProgress(255);
				} else {
					((ImageButton) light).setImageDrawable(getResources()
							.getDrawable(R.drawable.lightauto2));
					Toast.makeText(MainActivity.this, "自动调节亮度启用",
							Toast.LENGTH_SHORT).show();
					setScreenMode(1);
				}
			}
		});

		// 声音模式：
		sound = (ImageButton) findViewById(R.id.sound);
		if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL) {
			((ImageButton) sound).setImageDrawable(getResources().getDrawable(
					R.drawable.sound_on));
		} else {
			((ImageButton) sound).setImageDrawable(getResources().getDrawable(
					R.drawable.sound));
		}
		sound.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL) {
					((ImageButton) sound).setImageDrawable(getResources()
							.getDrawable(R.drawable.sound));
					Toast.makeText(MainActivity.this, "铃声关闭",
							Toast.LENGTH_SHORT).show();
					audioManager
							.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
				} else {
					((ImageButton) sound).setImageDrawable(getResources()
							.getDrawable(R.drawable.sound_on));
					Toast.makeText(MainActivity.this, "铃声开启",
							Toast.LENGTH_SHORT).show();
					audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
				}
				int mVolume2 = mAudioManager
						.getStreamVolume(AudioManager.STREAM_RING);
				phone.setProgress(mVolume2);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.finish:
			// mNotificationManager = (NotificationManager)
			// getSystemService(NOTIFICATION_SERVICE);
			mNotificationManager.cancel(0);
			this.finish();
			return true;
		default:
			return false;
		}

	}

	class lightListenr implements OnClickListener {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.Min:
				lights.setProgress(1);
				break;
			case R.id.l25:
				lights.setProgress(64);
				break;
			case R.id.l50:
				lights.setProgress(127);
				break;
			case R.id.l75:
				lights.setProgress(192);
				break;
			case R.id.Max:
				lights.setProgress(255);
				break;
			default:
				break;
			}
		}
	}

	private int isAirplaneModeOn() {
		// 返回值是1时表示处于飞行模式
		int modeIdx = Settings.System.getInt(getContentResolver(),
				Settings.System.AIRPLANE_MODE_ON, 0);
		return modeIdx;
	}

	private void setAirplaneMode(boolean setAirPlane) {
		Settings.System.putInt(getContentResolver(),
				Settings.System.AIRPLANE_MODE_ON, setAirPlane ? 1 : 0);
		// 广播飞行模式信号的改变，让相应的程序可以处理。
		// 不发送广播时，在非飞行模式下，Android 2.2.1上测试关闭了Wifi,不关闭正常的通话网络(如GMS/GPRS等)。
		// 不发送广播时，在飞行模式下，Android 2.2.1上测试无法关闭飞行模式。
		Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
		// intent.putExtra("Sponsor", "Sodino");
		// 2.3及以后，需设置此状态，否则会一直处于与运营商断连的情况
		intent.putExtra("state", setAirPlane);
		sendBroadcast(intent);
		Toast toast = Toast.makeText(this, "飞行模式启动与关闭需要一定的时间，请耐心等待",
				Toast.LENGTH_SHORT);
		toast.show();
	}

	// 亮度调节
	/**
	 * 获得当前屏幕亮度的模式 SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度
	 * SCREEN_BRIGHTNESS_MODE_MANUAL=0 为手动调节屏幕亮度
	 */
	private int getScreenMode() {
		int screenMode = 0;
		try {
			screenMode = Settings.System.getInt(getContentResolver(),
					Settings.System.SCREEN_BRIGHTNESS_MODE);
		} catch (Exception localException) {

		}
		return screenMode;
	}

	/**
	 * 获得当前屏幕亮度值 0--255
	 */
	private int getScreenBrightness() {
		int screenBrightness = 255;
		try {
			screenBrightness = Settings.System.getInt(getContentResolver(),
					Settings.System.SCREEN_BRIGHTNESS);
		} catch (Exception localException) {

		}
		return screenBrightness;
	}

	/**
	 * 设置当前屏幕亮度的模式 SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度
	 * SCREEN_BRIGHTNESS_MODE_MANUAL=0 为手动调节屏幕亮度
	 */
	private void setScreenMode(int paramInt) {
		try {
			Settings.System.putInt(getContentResolver(),
					Settings.System.SCREEN_BRIGHTNESS_MODE, paramInt);
		} catch (Exception localException) {
			localException.printStackTrace();
		}
	}

	/**
	 * 设置当前屏幕亮度值 0--255
	 */
	private void saveScreenBrightness(int paramInt) {
		try {
			Settings.System.putInt(getContentResolver(),
					Settings.System.SCREEN_BRIGHTNESS, paramInt);
		} catch (Exception localException) {
			localException.printStackTrace();
		}
	}

	/**
	 * 保存当前的屏幕亮度值，并使之生效
	 */
	private void setScreenBrightness(int paramInt) {
		Window localWindow = getWindow();
		WindowManager.LayoutParams localLayoutParams = localWindow
				.getAttributes();
		float f = paramInt / 255.0F;
		localLayoutParams.screenBrightness = f;
		localWindow.setAttributes(localLayoutParams);
	}

	// seekbar
	public void addListener() {
		lights.setProgress(getScreenBrightness());
		lights.setOnSeekBarChangeListener(new lightsSeekListener());

		int mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC); // 获取当前音乐音量
		music.setMax(mAudioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC)); // SEEKBAR设置为音量的最大阶数
		music.setSecondaryProgress(mAudioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
		music.setProgress(mVolume); // 设置seekbar为当前音量进度
		music.setOnSeekBarChangeListener(new musicSeekListener());

		int mVolume2 = mAudioManager.getStreamVolume(AudioManager.STREAM_RING);
		phone.setProgress(mVolume2);
		phone.setMax(mAudioManager.getStreamMaxVolume(AudioManager.STREAM_RING)); // SEEKBAR设置为音量的最大阶数
		phone.setSecondaryProgress(mAudioManager
				.getStreamMaxVolume(AudioManager.STREAM_RING));
		phone.setOnSeekBarChangeListener(new phoneSeekListener());

		int mVolume3 = mAudioManager.getStreamVolume(AudioManager.STREAM_ALARM);
		alarm.setMax(mAudioManager
				.getStreamMaxVolume(AudioManager.STREAM_ALARM)); // SEEKBAR设置为音量的最大阶数
		alarm.setSecondaryProgress(mAudioManager
				.getStreamMaxVolume(AudioManager.STREAM_ALARM));
		alarm.setProgress(mVolume3);
		alarm.setOnSeekBarChangeListener(new alarmSeekListener());

	}

	// 响应对应的事件
	class lightsSeekListener implements OnSeekBarChangeListener {

		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			if (progress == 0) {
				saveScreenBrightness(1);
				setScreenBrightness(1);
				setScreenMode(1);
				setScreenMode(0);
				((ImageButton) light).setImageDrawable(getResources()
						.getDrawable(R.drawable.light_off));
			} else {
				saveScreenBrightness(progress);
				setScreenBrightness(progress);
				setScreenMode(1);
				setScreenMode(0);
				((ImageButton) light).setImageDrawable(getResources()
						.getDrawable(R.drawable.light_off));
			}
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// status.setText("开始滑动");
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// status.setText("停止滑动");
			// saveScreenBrightness(pro);
			// setScreenBrightness(pro);
		}
	}

	// 响应对应的事件
	class musicSeekListener implements OnSeekBarChangeListener {

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			if(progress==0){
				((ImageView) musicv).setBackgroundDrawable(getResources()
						.getDrawable(R.drawable.sound));
			}else{
				((ImageView) musicv).setBackgroundDrawable(getResources()
						.getDrawable(R.drawable.sound1));
			}
			mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress,
					AudioManager.FLAG_PLAY_SOUND);

			// 拖动seekbar时改变音量
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// status.setText("开始滑动");
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// status.setText("停止滑动");
		}
	}

	// 响应对应的事件
	class phoneSeekListener implements OnSeekBarChangeListener {
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			if (progress == 0) {
				((ImageButton) sound).setImageDrawable(getResources()
						.getDrawable(R.drawable.sound));
				((ImageView) phonev).setBackgroundDrawable(getResources()
						.getDrawable(R.drawable.phoneoff));
			} else {
				((ImageButton) sound).setImageDrawable(getResources()
						.getDrawable(R.drawable.sound_on));
				((ImageView) phonev).setBackgroundDrawable(getResources()
						.getDrawable(R.drawable.phonesound));
			}
			mAudioManager.setStreamVolume(AudioManager.STREAM_RING, progress,
					AudioManager.FLAG_PLAY_SOUND);

		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
		}
	}

	class alarmSeekListener implements OnSeekBarChangeListener {
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			if(progress==0){
				((ImageView) alarmv).setBackgroundDrawable(getResources()
						.getDrawable(R.drawable.clock));
			}else{
				((ImageView) alarmv).setBackgroundDrawable(getResources()
						.getDrawable(R.drawable.alartsound));
			}
			mAudioManager.setStreamVolume(AudioManager.STREAM_ALARM, progress,
					AudioManager.FLAG_PLAY_SOUND);
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
		}
	}

	// 锁定屏幕时间
	/**
	 * 获得锁屏时间 毫秒
	 */
	private int getScreenOffTime() {
		int screenOffTime = 0;
		try {
			screenOffTime = Settings.System.getInt(getContentResolver(),
					Settings.System.SCREEN_OFF_TIMEOUT);
		} catch (Exception localException) {

		}
		return screenOffTime;
	}

	/**
	 * 设置背光时间 毫秒
	 */
	private void setScreenOffTime(int paramInt) {
		try {
			Settings.System.putInt(getContentResolver(),
					Settings.System.SCREEN_OFF_TIMEOUT, paramInt);
		} catch (Exception localException) {
			localException.printStackTrace();
		}
	}

}

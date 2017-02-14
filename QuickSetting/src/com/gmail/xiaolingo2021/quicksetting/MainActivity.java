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

		// ��ʼ��Ӧ�õķ���ID����Կ���Լ����ò���ģʽ
		AdManager.getInstance(this).init("4e67910595548da9",
				"d25dd56d54f3b415", false);
		// ʵ���������
		AdView adView = new AdView(this, AdSize.FIT_SCREEN);
		// ��ȡҪǶ�������Ĳ���
		LinearLayout adLayout = (LinearLayout) findViewById(R.id.adLayout);
		// ����������뵽������
		adLayout.addView(adView);

		wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		adapter = BluetoothAdapter.getDefaultAdapter();
		context = this;
		gprs = new Gprs(context);
		telephonyManager = (TelephonyManager) getApplicationContext()
				.getSystemService(Context.TELEPHONY_SERVICE);
		audioManager = (AudioManager) this.getSystemService(AUDIO_SERVICE);
		// �������ڣ�����ͼƬ��������¼�
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

		// ����һ�� NotificationManager������
		String ns = Context.NOTIFICATION_SERVICE;
		mNotificationManager = (NotificationManager) getSystemService(ns);
		// ����Notification�ĸ�������
		int icon = R.drawable.icon; // ֪ͨͼ��
		CharSequence tickerText = "��ӭʹ��PE��������"; // ״̬����ʾ��֪ͨ�ı���ʾ
		long when = System.currentTimeMillis(); // ֪ͨ������ʱ�䣬����֪ͨ��Ϣ����ʾ
		// ����������Գ�ʼ�� Nofification
		Notification notification = new Notification(icon, tickerText, when);

		notification.flags = Notification.FLAG_ONGOING_EVENT;
		notification.flags = Notification.FLAG_NO_CLEAR;

		RemoteViews contentView = new RemoteViews(getPackageName(),
				R.layout.notification);
		contentView.setImageViewResource(R.id.image, R.drawable.icon);
		contentView.setTextViewText(R.id.text, "�����趨");
		contentView.setTextViewText(R.id.text1, "������п����޸�ϵͳ�趨������");
		notification.contentView = contentView;
		Intent notificationIntent = new Intent(this, MainActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, 0);
		notification.contentIntent = contentIntent;
		// ��Notification���ݸ�NotificationManager
		mNotificationManager.notify(0, notification);

		// ý��������ť
		tmp = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC); // ��ȡ��ǰ��������
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
						.getStreamVolume(AudioManager.STREAM_MUSIC); // ��ȡ��ǰ��������
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
		// phone����
		tmp = mAudioManager.getStreamVolume(AudioManager.STREAM_RING); // ��ȡ��ǰ�绰����
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
						.getStreamVolume(AudioManager.STREAM_RING); // ��ȡ��ǰ�绰����
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
		// ��������
		tmp = mAudioManager.getStreamVolume(AudioManager.STREAM_ALARM); // ��ȡ��ǰ��������
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
						.getStreamVolume(AudioManager.STREAM_ALARM); // ��ȡ��ǰ�绰����
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
		// ����ʱ��
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
				// // ����ID�ж�ѡ��İ�ť
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
		// ������
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
					Toast.makeText(MainActivity.this, "�����ر�",
							Toast.LENGTH_SHORT).show();
					adapter.disable();
				} else {
					((ImageButton) bluetooth).setImageDrawable(getResources()
							.getDrawable(R.drawable.bluetooth_on));
					Toast.makeText(MainActivity.this, "����������ر���Ҫһ����ʱ�䣬�����ĵȴ�",
							Toast.LENGTH_SHORT).show();
					adapter.enable();
				}
			}
		});
		// wifi��
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
					Toast.makeText(MainActivity.this, "Wifi�ر�",
							Toast.LENGTH_SHORT).show();
					wifiManager.setWifiEnabled(false);
				} else {
					((ImageButton) wifi).setImageDrawable(getResources()
							.getDrawable(R.drawable.wifi_on));
					Toast.makeText(MainActivity.this, "Wifi��",
							Toast.LENGTH_SHORT).show();
					wifiManager.setWifiEnabled(true);
				}
			}
		});

		// flightmode��
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
					Toast.makeText(MainActivity.this, "����ģʽ�ѹر�",
							Toast.LENGTH_SHORT).show();
				} else {
					((ImageButton) flightmode).setImageDrawable(getResources()
							.getDrawable(R.drawable.flightmode_on));
					setAirplaneMode(true);
					Toast.makeText(MainActivity.this, "����ģʽ�Ѵ�",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		// ����ģʽ�����ͨ��
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
					Toast.makeText(MainActivity.this, "gps��ҪϵͳȨ��֧��",
							Toast.LENGTH_SHORT).show();
				} else {
					((ImageButton) gps).setImageDrawable(getResources()
							.getDrawable(R.drawable.gps_on));
					Intent myIntent = new Intent(
							Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					startActivity(myIntent);
					Toast.makeText(MainActivity.this, "gps��ҪϵͳȨ��֧��",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		// ��������
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
					Toast.makeText(MainActivity.this, "�������ӹر�",
							Toast.LENGTH_SHORT).show();
					gprs.setGprs();
					((ImageButton) data).setImageDrawable(getResources()
							.getDrawable(R.drawable.data));
				} else {
					Toast.makeText(MainActivity.this, "�������Ӵ�",
							Toast.LENGTH_SHORT).show();
					gprs.setGprs();
					((ImageButton) data).setImageDrawable(getResources()
							.getDrawable(R.drawable.data_on));
				}
			}
		});
		// ��ת��Ļ��
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
					Toast.makeText(MainActivity.this, "��ת��Ļ�ر�",
							Toast.LENGTH_SHORT).show();
					Settings.System.putInt(getContentResolver(),
							Settings.System.ACCELEROMETER_ROTATION, 0);
				} else {
					((ImageButton) changescream)
							.setImageDrawable(getResources().getDrawable(
									R.drawable.screanchange_on));
					Toast.makeText(MainActivity.this, "��ת��Ļ����",
							Toast.LENGTH_SHORT).show();
					Settings.System.putInt(getContentResolver(),
							Settings.System.ACCELEROMETER_ROTATION, 1);
				}
			}
		});

		// �Զ��������ȣ�
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
					Toast.makeText(MainActivity.this, "���⿪��",
							Toast.LENGTH_SHORT).show();
					saveScreenBrightness(255);
					setScreenBrightness(255);
					setScreenMode(1);
					setScreenMode(0);
					lights.setProgress(255);
				} else {
					((ImageButton) light).setImageDrawable(getResources()
							.getDrawable(R.drawable.lightauto2));
					Toast.makeText(MainActivity.this, "�Զ�������������",
							Toast.LENGTH_SHORT).show();
					setScreenMode(1);
				}
			}
		});

		// ����ģʽ��
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
					Toast.makeText(MainActivity.this, "�����ر�",
							Toast.LENGTH_SHORT).show();
					audioManager
							.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
				} else {
					((ImageButton) sound).setImageDrawable(getResources()
							.getDrawable(R.drawable.sound_on));
					Toast.makeText(MainActivity.this, "��������",
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
		// ����ֵ��1ʱ��ʾ���ڷ���ģʽ
		int modeIdx = Settings.System.getInt(getContentResolver(),
				Settings.System.AIRPLANE_MODE_ON, 0);
		return modeIdx;
	}

	private void setAirplaneMode(boolean setAirPlane) {
		Settings.System.putInt(getContentResolver(),
				Settings.System.AIRPLANE_MODE_ON, setAirPlane ? 1 : 0);
		// �㲥����ģʽ�źŵĸı䣬����Ӧ�ĳ�����Դ���
		// �����͹㲥ʱ���ڷǷ���ģʽ�£�Android 2.2.1�ϲ��Թر���Wifi,���ر�������ͨ������(��GMS/GPRS��)��
		// �����͹㲥ʱ���ڷ���ģʽ�£�Android 2.2.1�ϲ����޷��رշ���ģʽ��
		Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
		// intent.putExtra("Sponsor", "Sodino");
		// 2.3���Ժ������ô�״̬�������һֱ��������Ӫ�̶��������
		intent.putExtra("state", setAirPlane);
		sendBroadcast(intent);
		Toast toast = Toast.makeText(this, "����ģʽ������ر���Ҫһ����ʱ�䣬�����ĵȴ�",
				Toast.LENGTH_SHORT);
		toast.show();
	}

	// ���ȵ���
	/**
	 * ��õ�ǰ��Ļ���ȵ�ģʽ SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 Ϊ�Զ�������Ļ����
	 * SCREEN_BRIGHTNESS_MODE_MANUAL=0 Ϊ�ֶ�������Ļ����
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
	 * ��õ�ǰ��Ļ����ֵ 0--255
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
	 * ���õ�ǰ��Ļ���ȵ�ģʽ SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 Ϊ�Զ�������Ļ����
	 * SCREEN_BRIGHTNESS_MODE_MANUAL=0 Ϊ�ֶ�������Ļ����
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
	 * ���õ�ǰ��Ļ����ֵ 0--255
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
	 * ���浱ǰ����Ļ����ֵ����ʹ֮��Ч
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

		int mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC); // ��ȡ��ǰ��������
		music.setMax(mAudioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC)); // SEEKBAR����Ϊ������������
		music.setSecondaryProgress(mAudioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
		music.setProgress(mVolume); // ����seekbarΪ��ǰ��������
		music.setOnSeekBarChangeListener(new musicSeekListener());

		int mVolume2 = mAudioManager.getStreamVolume(AudioManager.STREAM_RING);
		phone.setProgress(mVolume2);
		phone.setMax(mAudioManager.getStreamMaxVolume(AudioManager.STREAM_RING)); // SEEKBAR����Ϊ������������
		phone.setSecondaryProgress(mAudioManager
				.getStreamMaxVolume(AudioManager.STREAM_RING));
		phone.setOnSeekBarChangeListener(new phoneSeekListener());

		int mVolume3 = mAudioManager.getStreamVolume(AudioManager.STREAM_ALARM);
		alarm.setMax(mAudioManager
				.getStreamMaxVolume(AudioManager.STREAM_ALARM)); // SEEKBAR����Ϊ������������
		alarm.setSecondaryProgress(mAudioManager
				.getStreamMaxVolume(AudioManager.STREAM_ALARM));
		alarm.setProgress(mVolume3);
		alarm.setOnSeekBarChangeListener(new alarmSeekListener());

	}

	// ��Ӧ��Ӧ���¼�
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
			// status.setText("��ʼ����");
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// status.setText("ֹͣ����");
			// saveScreenBrightness(pro);
			// setScreenBrightness(pro);
		}
	}

	// ��Ӧ��Ӧ���¼�
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

			// �϶�seekbarʱ�ı�����
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// status.setText("��ʼ����");
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// status.setText("ֹͣ����");
		}
	}

	// ��Ӧ��Ӧ���¼�
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

	// ������Ļʱ��
	/**
	 * �������ʱ�� ����
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
	 * ���ñ���ʱ�� ����
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

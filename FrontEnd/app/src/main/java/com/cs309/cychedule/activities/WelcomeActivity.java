package com.cs309.cychedule.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.cs309.cychedule.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * WelcomeActivity is the activity of the welcome page
 * It is a welcome animation and will show up immediately when we open the app
 */
public class WelcomeActivity extends Activity implements View.OnClickListener {
	private int recLen = 0;//倒计时秒数
	private TextView skip;
	private Timer timer = new Timer();
	private Handler handler;
	private Runnable runnable;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
		//设置当前窗体为全屏显示
		getWindow().setFlags(flag, flag);
		setContentView(R.layout.activity_welcome);
		initView();
		//等待时间一秒，停顿时间一秒
		timer.schedule(task, 1000, 1000);
		//正常情况下不点击跳过
		handler = new Handler();
		handler.postDelayed(runnable = new Runnable() {
			@Override
			public void run() {
				toLogin();//这里写登录判断
			}
		}, recLen*1000);//延迟recLen秒后发送handler信息
		// String brand,model,androidversion,romname,romversion,sign,sdk;
		// String device,product,cpu,board,display,id,version_codes_base,maker,user,tags;
		// String hardware,host,unknown,type,time,radio,serial,cpu2;
		// product = "产品 : " + android.os.Build.PRODUCT;
		// cpu= " CPU_ABI : " + android.os.Build.CPU_ABI;
		// model= " 型号 : " + android.os.Build.MODEL;
		// androidversion= " Android 版本 : " + android.os.Build.VERSION.RELEASE;
		// device= " 驱动 : " + android.os.Build.DEVICE;
		// board= " 基板 : " + android.os.Build.BOARD;
		// sign= " 设备标识 : " + android.os.Build.FINGERPRINT;
		// maker= " 制造商 : " + android.os.Build.MANUFACTURER;
		// user= " 用户 : " + android.os.Build.USER;
		// cpu2=" CPU_ABI2 : "+android.os.Build.CPU_ABI2;
		// hardware=" 硬件 : "+ android.os.Build.HARDWARE;
		// time =" 时间 : "+String.valueOf(android.os.Build.TIME);
		// serial=" 序列号 : "+android.os.Build.SERIAL;
		// new AlertDialog.Builder(this).setTitle("Info").setMessage(
		// 		""+sign).show();
		
	}
	
	/**
	 * 跳转到LoginActivity
	 */
	private void toLogin() {
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
		finish();
	}
	
	
	public void onClick(View view) {
		//点击logo或者文字直接进入
		if (view.getId() == R.id.skip || view.getId() == R.id.shimmer_view_container) {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			finish();
			if (runnable != null) {
				handler.removeCallbacks(runnable);
			}
		}
	}
	
	/**
	 * 延迟时间进入,并且清空倒计时
	 */
	private void initView() {
		skip = findViewById(R.id.skip);//跳过
		skip.setOnClickListener(this);//跳过监听
	}
	
	TimerTask task = new TimerTask() {
		@Override
		public void run() {
			runOnUiThread(new Runnable() { // UI thread
				@Override
				public void run() {
					recLen--;
					skip.setText("Click me! " + recLen + "s");
					if (recLen < 0) {
						timer.cancel();
						//倒计时跳到0->gone
						skip.setVisibility(View.GONE);
					}
				}
			});
		}
	};
	
	/**
	 * 延迟时间进入
	 */
	private void init() {
		timer.schedule(new TimerTask() {
			               @Override
			               public void run() {
				               //	Log.e("WelcomeActivity", "current Tread is: " +Thread.currentThread());
				               // 	toMain();
				               //这里写验证是否已登录的逻辑(本地化?
				               toLogin();
			               }
		               }
				, recLen*1000);
	}
	
	
}


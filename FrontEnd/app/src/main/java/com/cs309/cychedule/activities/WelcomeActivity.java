package com.cs309.cychedule.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.cs309.cychedule.R;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends BaseActivity implements View.OnClickListener {
	private int recLen = 4;//倒计时秒数
	private TextView skip;
	Timer timer = new Timer();
	private Handler handler;
	private Runnable runnable;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
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
	}
	

	
	/**
	 * 跳转到MainActivity
	 */
	private void toMain() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
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
		if (view.getId() == R.id.skip||view.getId() == R.id.shimmer_view_container) {
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
					skip.setText("Click me! "+recLen+"s");
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


package edu.cs309.timeAPP.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;
import edu.cs309.timeAPP.R;

public class WelcomeActivity extends BaseActivity implements View.OnClickListener {
	private int recLen = 5;//跳过倒计时提示5秒
	private TextView skip;
	Timer timer = new Timer();
	private Handler handler;
	private Runnable runnable;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		//定义全屏参数
		int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
		//设置当前窗体为全屏显示
		getWindow().setFlags(flag, flag);
		setContentView(R.layout.activity_welcome);
		initView();
		timer.schedule(task, 1000, 1000);//等待时间一秒，停顿时间一秒
		//正常情况下不点击跳过
		handler = new Handler();
		handler.postDelayed(runnable = new Runnable() {
			@Override
			public void run() {
				toLogin();
			}
		}, 5000);//延迟5S后发送handler信息
	}
	
	/**
	 * 延迟时间进入
	 */
	private void init() {
		timer.schedule(new TimerTask() {
			               @Override
			               public void run() {
				               //	Log.e("WelcomeActivity", "current Tread is: " +Thread.currentThread());
				               // 	toMain();
				               toLogin();
			               }
		               }
				, 3*1000);
		
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
	
	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.skip) {
			toLogin();
			if (runnable != null) {
				handler.removeCallbacks(runnable);
			}
		}
	}
	
	
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
					skip.setText(recLen+"s or click!");
					if (recLen < 0) {
						timer.cancel();
						skip.setVisibility(View.GONE);//倒计时到0隐藏字体
					}
				}
			});
		}
	};
}


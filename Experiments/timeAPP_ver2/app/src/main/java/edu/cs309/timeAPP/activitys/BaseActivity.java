package edu.cs309.timeAPP.activitys;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.IdRes;
import edu.cs309.timeAPP.R;

public class BaseActivity extends Activity {
	private ImageView mIvBack, mIvMe;
	private TextView mTvTitle;
	
	/**Find view by ID*/
	protected <T extends View> T findView (@IdRes int id) {
		return findViewById(id);
	}
	
	/**initialize navigation bar*/
	protected void initNavBar (boolean isShowBack, String title, boolean isShowMe){
		mIvBack = findView(R.id.iv_back);
		mTvTitle = findView(R.id.tv_title);
		mIvMe = findView(R.id.iv_me);
		
		mIvBack.setVisibility(isShowBack ? View.VISIBLE : View.GONE);
		mIvMe.setVisibility(isShowMe ? View.VISIBLE : View.GONE);
		mTvTitle.setText(title);
		
		/**click the back img*/
		mIvBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		
		mIvMe.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(BaseActivity.this, MeActivity.class));
			}
		});
		
	}
}

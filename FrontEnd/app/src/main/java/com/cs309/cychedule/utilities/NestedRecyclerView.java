package com.cs309.cychedule.utilities;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/** In nested recyclerview, the outer recyclerview would prevent inner recyclerview
 * from getting focus, resulting the inner recyclerview being not scrollable
 *
 * Ref: https://blog.csdn.net/jiang19921002/article/details/82527132
 */

public class NestedRecyclerView extends RecyclerView {

    private int lastVisibleItemPosition;
    private int firstVisibleItemPosition;
    private float mLastY = 0;// 记录上次Y位置
    private boolean isTopToBottom = false;
    private boolean isBottomToTop = false;

    public NestedRecyclerView(Context context) {
        this(context, null);
    }

    public NestedRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NestedRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastY = event.getY();
                //不允许父View拦截事件
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                float nowY = event.getY();
                isIntercept(nowY);
                if (isBottomToTop||isTopToBottom){
                    getParent().requestDisallowInterceptTouchEvent(false);
                    return false;
                }else{
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                mLastY = nowY;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        return super.onTouchEvent(event);
    }

    private void isIntercept(float nowY){

        isTopToBottom = false;
        isBottomToTop = false;

        RecyclerView.LayoutManager layoutManager = getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            //得到当前界面，最后一个子视图对应的position
            lastVisibleItemPosition = ((GridLayoutManager) layoutManager)
                    .findLastVisibleItemPosition();
            //得到当前界面，第一个子视图的position
            firstVisibleItemPosition = ((GridLayoutManager) layoutManager)
                    .findFirstVisibleItemPosition();
        }
        //得到当前界面可见数据的大小
        int visibleItemCount = layoutManager.getChildCount();
        //得到RecyclerView对应所有数据的大小
        int totalItemCount = layoutManager.getItemCount();
//        LogUtils.d("nestScrolling","onScrollStateChanged");
        if (visibleItemCount>0) {
            if (lastVisibleItemPosition == totalItemCount - 1) {
                //最后视图对应的position等于总数-1时，说明上一次滑动结束时，触底了
//                LogUtils.d("nestScrolling", "触底了");
                if (NestedRecyclerView.this.canScrollVertically(-1) && nowY < mLastY) {
                    // 不能向上滑动
//                    LogUtils.d("nestScrolling", "不能向上滑动");
                    isBottomToTop = false;
                } else {
//                    LogUtils.d("nestScrolling", "向下滑动");
                }
            } else if (firstVisibleItemPosition == 0) {
                //第一个视图的position等于0，说明上一次滑动结束时，触顶了
//                LogUtils.d("nestScrolling", "触顶了");
                if (NestedRecyclerView.this.canScrollVertically(1) && nowY > mLastY) {
                    // 不能向下滑动
//                    LogUtils.d("nestScrolling", "不能向下滑动");
                    isTopToBottom = false;
                } else {
//                    LogUtils.d("nestScrolling", "向上滑动");
                }
            }
        }
    }

}

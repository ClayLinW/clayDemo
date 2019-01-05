package com.beauty.claydemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

/**
 * Created by zlw on 2018/11/13.
 * <p>
 * 自定义ScrollView实现在底部滑动消失
 */
public class CustomGlideScrollView extends ScrollView
{

	private int mScrollY;        //滚动的距离，主要是用来判断是否滚动的顶部
	private int mScrollViewHeight;    //view的高度，主要用来判断松开后是恢复还是消失
	private float mInScroolValue;    //整体向下滑动的偏移值
	private boolean mIsUpScroll = true;        //是否view整体向下滑动，并用来帮助获取滑动的起始值
	private float mInitScrollValue;            //记录整体开始滑动时的初始值

	public CustomGlideScrollView(Context context)
	{
		super(context);
		init();
	}

	public CustomGlideScrollView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

	public CustomGlideScrollView(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init()
	{
		// 获取view的高度
		post(new Runnable()
		{
			@Override
			public void run()
			{
				mScrollViewHeight = getHeight();
			}
		});
		setOnTouchListener(new View.OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				return setScrollViewPadding(event);
			}
		});
	}

	@Override
	protected void onScrollChanged(int scrollX, int scrollY, int oldScrollX, int oldScrollY)
	{
		super.onScrollChanged(scrollX, scrollY, oldScrollX, oldScrollY);
		mScrollY = scrollY;
	}

	/**
	 * 处理触摸事件
	 *
	 * @param event
	 * @return
	 */
	private boolean setScrollViewPadding(MotionEvent event)
	{
		if(mScrollViewHeight <= 0)
		{
			return false;
		}
		// 整体向下偏移
		if(mScrollY == 0 && event.getAction() == MotionEvent.ACTION_MOVE)
		{
			if(mIsUpScroll)
			{
				// 记录偏移的起始值，并且表示可向上滑动了
				mIsUpScroll = false;
				mInitScrollValue = event.getRawY();
			}
			// 计算偏移量
			mInScroolValue = event.getRawY() - mInitScrollValue;
			if(mInScroolValue <= 0)
			{
				// 表示只是内容在滑动，无需对view进行偏移
				mInScroolValue = 0;
				return false;
			}
			setPadding(0, (int)mInScroolValue, 0, 0);
			return false;
		}
		// 整体向上偏移
		if(mInScroolValue != 0 && event.getAction() == MotionEvent.ACTION_MOVE && !mIsUpScroll)
		{
			// 计算偏移量
			mInScroolValue = event.getRawY() - mInitScrollValue;
			if(mInitScrollValue < 0)
			{
				mInitScrollValue = 0;
			}
			setPadding(0, (int)mInScroolValue, 0, 0);
			return true;
		}
		// 恢复或消失
		if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)
		{
			if(mInScroolValue <= mScrollViewHeight / 2)
			{
				// 恢复，重置初始值
				mInScroolValue = 0;
				mIsUpScroll = true;
				setPadding(0, 0, 0, 0);
			}
			else
			{
				// 整体消失
				setPadding(0, mScrollViewHeight, 0, 0);
			}
		}
		return false;
	}
}

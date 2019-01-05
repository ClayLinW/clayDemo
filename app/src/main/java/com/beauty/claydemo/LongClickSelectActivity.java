package com.beauty.claydemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.beauty.popupwindowlibs.PopupWindowUtils;

/**
 * Android长按弹出popupwindow直接滑动松开选择条目
 * <p>
 * 最近在项目中需要使用到长按某个Textview弹出PopupWindow，然后不松手在弹出的窗口中滑动选择一个表情或点赞或爱心等消息进行发送，特将我的实现方式在此记录下来。
 * <p>
 * 1.其实这个功能的实现方式也比较简单，主要是使用Textview的setOnTouchListener获得MotionEvent事件，然后通过MotionEvent的getX()、getY() 获取相对于view左上角为原点的坐标，
 * 2.查看Popupwindow在view的弹出位置，通过计算可以获得弹出的PopupWindow中的每个view相对于Textview坐标原点的坐标
 * 3.根据触摸监听滑动的位置坐标是否跟弹出的view包含的坐标相交，如果是则记录下来,这样，松手后PopupWindow消失，同时也可以获得选中的是那个view，然后做出相应的操作。
 */
public class LongClickSelectActivity extends AppCompatActivity
{

	private TextView tvLongclick;
	private PopupWindow normalPopupWindow;
	private TextView tvFirstLove;
	private TextView tvFirstSmiling;
	private TextView tvSecondLove;
	private TextView tvSecondSmiling;
	private int colorGray;
	private int colorPrimary;
	private int colorAccent;

	private int padding;
	private int popupWidth;
	private int popupHeight;
	private int childWidth;
	private int childHeight;
	private int rootWidth;
	private int rootHeight;


	/**
	 * 标记选中
	 */
	private boolean isFirstLove;
	private boolean isFirstsmiling;
	private boolean isSecondLove;
	private boolean isSecondSmiling;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lcselect_vanish);

		colorGray = getResources().getColor(R.color.colorGray);
		colorPrimary = getResources().getColor(R.color.colorPrimary);
		colorAccent = getResources().getColor(R.color.colorAccent);

		tvLongclick = findViewById(R.id.tv_longclick);

		tvLongclick.setOnLongClickListener(new View.OnLongClickListener()
		{
			@Override
			public boolean onLongClick(View v)
			{
				clickDispose(v);
				return true;
			}
		});

		tvLongclick.setOnTouchListener(new View.OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				computeGestureValue(event);
				return false;
			}
		});
	}

	/**
	 * 通过手势移动获取坐标值，然后根据坐标判断
	 *
	 * @param event
	 */
	private void computeGestureValue(MotionEvent event)
	{
		float rawX = event.getX();
		float rawY = event.getY();

		if(rawX >= padding && rawX <= (childWidth + padding))
		{
			if(rawY >= getRangeUpY(1) && rawY <= getRangeDownY(1))
			{
				isFirstLove = true;
				tvFirstLove.setBackgroundColor(colorGray);
			}
			else
			{
				isFirstLove = false;
				tvFirstLove.setBackgroundColor(colorPrimary);
			}

			if(rawY >= getRangeUpY(2) && rawY <= getRangeDownY(2))
			{
				isFirstsmiling = true;
				tvFirstSmiling.setBackgroundColor(colorGray);
			}
			else
			{
				isFirstsmiling = false;
				tvFirstSmiling.setBackgroundColor(colorAccent);
			}

			if(rawY >= getRangeUpY(3) && rawY <= getRangeDownY(3))
			{
				isSecondLove = true;
				tvSecondLove.setBackgroundColor(colorGray);
			}
			else
			{
				isSecondLove = false;
				tvSecondLove.setBackgroundColor(colorPrimary);
			}

			if(rawY >= getRangeUpY(4) && rawY <= getRangeDownY(4))
			{
				isSecondSmiling = true;
				tvSecondSmiling.setBackgroundColor(colorGray);
			}
			else
			{
				isSecondSmiling = false;
				tvSecondSmiling.setBackgroundColor(colorAccent);
			}
		}
		else
		{
			if(tvFirstLove != null)
			{
				isFirstLove = false;
				isFirstsmiling = false;
				isSecondLove = false;
				isSecondSmiling = false;
				tvFirstLove.setBackgroundColor(colorPrimary);
				tvFirstSmiling.setBackgroundColor(colorAccent);
				tvSecondLove.setBackgroundColor(colorPrimary);
				tvSecondSmiling.setBackgroundColor(colorAccent);
			}
		}


		if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)
		{
			if(normalPopupWindow != null)
			{
				normalPopupWindow.dismiss();
			}
		}
	}

	/**
	 * 弹出的每个条目的view的高度范围值---上
	 *
	 * @param position
	 * @return
	 */
	private int getRangeUpY(int position)
	{
		return rootHeight + position * padding + (position - 1) * childHeight;
	}

	/**
	 * 弹出的每个条目的view的高度范围值---下
	 *
	 * @param position
	 * @return
	 */
	private int getRangeDownY(int position)
	{
		return rootHeight + position * padding + position * childHeight;
	}

	private void clickDispose(View v)
	{
		if(normalPopupWindow == null)
		{
			normalPopupWindow = PopupWindowUtils.getNormalPopupWindow(this, R.layout.item_window, true, PopupWindowUtils.DIALOG_ANIMATION, new PopupWindowUtils.PopupWindowCallBack()
			{
				@Override
				public void disposePopView(final View popView)
				{
					tvFirstLove = popView.findViewById(R.id.tv_firstLove);
					tvFirstSmiling = popView.findViewById(R.id.tv_firstSmiling);
					tvSecondLove = popView.findViewById(R.id.tv_secondLove);
					tvSecondSmiling = popView.findViewById(R.id.tv_secondSmiling);

					popView.post(new Runnable()
					{
						@Override
						public void run()
						{
							rootWidth = tvLongclick.getWidth();
							rootHeight = tvLongclick.getHeight();
							padding = popView.getPaddingTop();
							popupWidth = popView.getWidth();
							popupHeight = popView.getHeight();
							childWidth = tvFirstLove.getWidth();
							childHeight = tvFirstLove.getHeight();
						}
					});
				}

				@Override
				public void onDismiss()
				{
					if(isFirstLove)
					{
						Toast.makeText(LongClickSelectActivity.this, tvFirstLove.getText().toString(), Toast.LENGTH_SHORT).show();
					}
					if(isFirstsmiling)
					{
						Toast.makeText(LongClickSelectActivity.this, tvFirstSmiling.getText().toString(), Toast.LENGTH_SHORT).show();
					}
					if(isSecondLove)
					{
						Toast.makeText(LongClickSelectActivity.this, tvSecondLove.getText().toString(), Toast.LENGTH_SHORT).show();
					}
					if(isSecondSmiling)
					{
						Toast.makeText(LongClickSelectActivity.this, tvSecondSmiling.getText().toString(), Toast.LENGTH_SHORT).show();
					}
				}
			});
		}
		PopupWindowUtils.showAsDropDown(this, normalPopupWindow, v, 0.8f);
	}
}

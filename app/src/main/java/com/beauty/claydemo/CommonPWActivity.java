package com.beauty.claydemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.beauty.popupwindowlibs.PopupWindowUtils;

public class CommonPWActivity extends AppCompatActivity implements View.OnClickListener
{

	private TextView tvUp;
	private TextView tvDown;
	private TextView tvCenter;
	private TextView tvLeft;
	private TextView tvRight;
	private PopupWindow mPopouWindowUp;
	private PopupWindow mPopouWindowDown;
	private PopupWindow mPopupWindowCenter;
	private PopupWindow mPopouWindowLeft;
	private PopupWindow mPopouWindowRight;

	/**
	 * Android 封装一个通用的PopupWindow
	 *
	 * 弹窗在android的每个项目中基本上都会使用到的，主要功能是给用户一些提示和操作，所以我就稍微封装了一个简单的PopupWindUtils,这样方便项目中使用，使用的方法
	 * 也非常简单，我这里简单提示下，这里提供了gif，如果有需要的可以直接去下载代码，在代码中的注释已经非常详细了，相信你可以一目了然。
	 *
	 * 如果使用showAsDropDown，默认是弹出在view的下方
	 * 如果使用showAtLocation，就是显示在整个界面的指定位置，比如在屏幕的中间弹出，使用Gravity.CENTER（其它Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL等）
	 * 使用popupwindow主要的地方就是设置显示的位置，其实就是给popupwindow设置x和y的偏移量。这里可参考下弹出顶部的实现，理解完这个基本上就可以给PopupWindow任意设置弹出位置了。
	 *
	 * @param savedInstanceState
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_common_pw);

		initView();
		initListener();
	}

	private void initView()
	{
		tvUp = findViewById(R.id.tv_up);
		tvDown = findViewById(R.id.tv_down);
		tvCenter = findViewById(R.id.tv_center);
		tvLeft = findViewById(R.id.tv_left);
		tvRight = findViewById(R.id.tv_right);
	}

	private void initListener()
	{
		tvUp.setOnClickListener(this);
		tvDown.setOnClickListener(this);
		tvCenter.setOnClickListener(this);
		tvLeft.setOnClickListener(this);
		tvRight.setOnClickListener(this);
	}

	@Override
	public void onClick(View view)
	{
		if(view == tvUp)
		{
			setDisplayUp();
		}
		else if(view == tvDown)
		{
			setDisplayDown();
		}
		else if(view == tvCenter)
		{
			setDisplayCenter();
		}
		else if(view == tvLeft)
		{
			setDisplayLeft();
		}
		else if(view == tvRight)
		{
			setDisplayRight();
		}
	}

	private void setDisplayRight()
	{
		int[] location = new int[2];
		tvRight.getLocationOnScreen(location);

		if(mPopouWindowRight == null)
		{
			mPopouWindowRight = PopupWindowUtils.getNormalPopupWindow(this, R.layout.dialog_right, true, PopupWindowUtils.DIALOG_ANIMATION, new PopupWindowUtils.PopupWindowCallBack()
			{
				@Override
				public void disposePopView(final View popView)
				{

				}

				@Override
				public void onDismiss()
				{

				}
			});
		}
		mPopouWindowRight.getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
		int width = tvUp.getWidth();
//		int height = tvUp.getHeight();
//		int measuredWidth = mPopouWindowRight.getContentView().getMeasuredWidth();
		int measuredHeight = mPopouWindowRight.getContentView().getMeasuredHeight();
		PopupWindowUtils.showAsDropDown(this, mPopouWindowRight, tvRight, 1f, width, -measuredHeight);
	}

	private void setDisplayLeft()
	{
		int[] location = new int[2];
		tvLeft.getLocationOnScreen(location);

		if(mPopouWindowLeft == null)
		{
			mPopouWindowLeft = PopupWindowUtils.getNormalPopupWindow(this, R.layout.dialog_left, true, PopupWindowUtils.DIALOG_ANIMATION, new PopupWindowUtils.PopupWindowCallBack()
			{
				@Override
				public void disposePopView(final View popView)
				{

				}

				@Override
				public void onDismiss()
				{

				}
			});
		}
		mPopouWindowLeft.getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
//		int width = tvUp.getWidth();
//		int height = tvUp.getHeight();
		int measuredWidth = mPopouWindowLeft.getContentView().getMeasuredWidth();
		int measuredHeight = mPopouWindowLeft.getContentView().getMeasuredHeight();
		PopupWindowUtils.showAsDropDown(this, mPopouWindowLeft, tvLeft, 1f, -measuredWidth, -measuredHeight);
	}

	private void setDisplayCenter()
	{
		if(mPopupWindowCenter == null)
		{
			mPopupWindowCenter = PopupWindowUtils.getNormalPopupWindow(this, R.layout.dialog_center, true, PopupWindowUtils.DIALOG_ANIMATION, new PopupWindowUtils.PopupWindowCallBack()
			{
				@Override
				public void disposePopView(View popView)
				{

				}

				@Override
				public void onDismiss()
				{

				}
			});
		}
		PopupWindowUtils.showAtLocation(this, mPopupWindowCenter, Gravity.CENTER, 0.5f);
	}

	private void setDisplayDown()
	{
		if(mPopouWindowDown == null)
		{
			mPopouWindowDown = PopupWindowUtils.getNormalPopupWindow(this, R.layout.dialog_down, true, PopupWindowUtils.DIALOG_ANIMATION, new PopupWindowUtils.PopupWindowCallBack()
			{
				@Override
				public void disposePopView(final View popView)
				{

				}

				@Override
				public void onDismiss()
				{

				}
			});
		}
		PopupWindowUtils.showAsDropDown(this, mPopouWindowDown, tvDown, 1f);
	}

	private void setDisplayUp()
	{
		int[] location = new int[2];
		tvUp.getLocationOnScreen(location);

		// 这是为了避免比如在聊天列表中条目靠近状态栏顶部导致显示出现问题
		int titleBarHeight = Utils.Dp2Px(this, 45);
		int statusBarHeight = Utils.getStatusBarHeight(this);
		if(location[1] < titleBarHeight + statusBarHeight)
		{
			return;
		}

		if(mPopouWindowUp == null)
		{
			mPopouWindowUp = PopupWindowUtils.getNormalPopupWindow(this, R.layout.dialog_up, true, PopupWindowUtils.DIALOG_ANIMATION, new PopupWindowUtils.PopupWindowCallBack()
			{
				@Override
				public void disposePopView(final View popView)
				{
					TextView tvDelete = popView.findViewById(R.id.tv_delete);
					TextView tvCopy = popView.findViewById(R.id.tv_copy);
					tvDelete.setOnClickListener(new View.OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
							mPopouWindowUp.dismiss();
						}
					});
					tvCopy.setOnClickListener(new View.OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
							mPopouWindowUp.dismiss();
						}
					});
				}

				@Override
				public void onDismiss()
				{

				}
			});
		}
		mPopouWindowUp.getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
		int width = tvUp.getWidth();
		int height = tvUp.getHeight();
		int measuredWidth = mPopouWindowUp.getContentView().getMeasuredWidth();
		int measuredHeight = mPopouWindowUp.getContentView().getMeasuredHeight();
		PopupWindowUtils.showAsDropDown(this, mPopouWindowUp, tvUp, 1f, (width - measuredWidth) / 2, -height - measuredHeight);
	}
}

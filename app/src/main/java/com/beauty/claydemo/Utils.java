package com.beauty.claydemo;

import android.content.Context;

import java.lang.reflect.Field;

/**
 * Created by zlw on 2018/11/19.
 */
public class Utils
{
	public static int Dp2Px(Context context, float dp)
	{
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int)(dp * scale + 0.5f);
	}

	//获取手机状态栏高度
	public static int getStatusBarHeight(Context context)
	{
		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0, statusBarHeight = 0;
		try
		{
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			statusBarHeight = context.getResources().getDimensionPixelSize(x);
		}
		catch(Exception e1)
		{
			e1.printStackTrace();
		}
		return statusBarHeight;
	}
}

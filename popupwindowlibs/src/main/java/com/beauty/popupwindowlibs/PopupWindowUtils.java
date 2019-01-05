package com.beauty.popupwindowlibs;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

public class PopupWindowUtils
{

    public static final int DIALOG_ANIMATION = android.R.style.Animation_Dialog;    //dialog动画，渐现
    public static final int CUSTOM_UP_TO_DOWN_ANIMATION = R.style.popwin_anim_style;      //自定义设置从小往上的的动画

    // 设置屏幕背景透明度
    public static void backgroundAlpha(Activity activity, float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        activity.getWindow().setAttributes(lp);
    }

    /**
     * 获取自适应popupwindow
     *
     * @param activity
     * @param resource
     * @param isTouch
     * @param animation
     * @param callBack
     * @return
     */
    public static PopupWindow getNormalPopupWindow(Activity activity, @LayoutRes int resource, boolean isTouch, int animation, PopupWindowCallBack callBack) {
        return createPopupWidow(activity, resource, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, isTouch, animation, callBack);
    }

    /**
     * 获取宽度占全屏的popupwindow
     *
     * @param activity
     * @param resource
     * @param isTouch
     * @param animation
     * @param callBack
     * @return
     */
    public static PopupWindow getAllScreenPopupWindow(Activity activity, @LayoutRes int resource, boolean isTouch, int animation, PopupWindowCallBack callBack) {
        return createPopupWidow(activity, resource, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, isTouch, animation, callBack);
    }

    /**
     * @param activity
     * @param resource  布局资源
     * @param width     宽度  eg: view.getMeasuredWidth()
     * @param height    高度
     * @param isTouch   点击外部，PopupWindow是否消失
     * @param animation 显示动画
     * @param callBack
     * @return
     */
    public static PopupWindow createPopupWidow(final Activity activity, @LayoutRes int resource, int width, int height, boolean isTouch,
                                               int animation, final PopupWindowCallBack callBack) {
        System.out.println();
        //初始化PopupWindow的布局
        View popView = activity.getLayoutInflater().inflate(resource, null);
        //popView即popupWindow的布局，ture设置focusAble
        PopupWindow popupWindow = new PopupWindow(popView, width, height, isTouch);
        //必须设置BackgroundDrawable后setOutsideTouchable(true)才会有效
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        //点击外部关闭。
        popupWindow.setOutsideTouchable(isTouch);
        //设置一个动画。
        popupWindow.setAnimationStyle(animation);
        // popupWindow消失监听
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(activity, 1f);
                if (callBack != null) {
                    callBack.onDismiss();
                }
            }
        });
        //设置map的swift的点击监听
        if (callBack != null) {
            callBack.disposePopView(popView);
        }
        return popupWindow;
    }

    /**
     * 显示在指定view的下方
     *
     * @param activity
     * @param popupWindow
     * @param view
     * @param bgAlpha
     */
    public static void showAsDropDown(Activity activity, PopupWindow popupWindow, View view, float bgAlpha) {
        showAsDropDown(activity, popupWindow, view, bgAlpha, 0, 0);
    }

    /**
     * 显示在指定view的下方,设置偏移
     *
     * @param activity
     * @param popupWindow
     * @param view
     * @param bgAlpha
     * @param xOff
     * @param yOff
     */
    public static void showAsDropDown(Activity activity, PopupWindow popupWindow, View view, float bgAlpha, int xOff, int yOff) {
        backgroundAlpha(activity, bgAlpha);
        popupWindow.showAsDropDown(view, xOff, yOff);
    }

    /**
     * 显示在整个界面的指定位置
     *
     * @param activity
     * @param popupWindow
     * @param gravity     eg: Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL or Gravity.CENTER
     * @param bgAlpha
     */
    public static void showAtLocation(Activity activity, PopupWindow popupWindow, int gravity, float bgAlpha) {
        showAtLocation(activity, popupWindow, gravity, bgAlpha, 0, 0);
    }

    /**
     * 显示在整个界面的指定位置,设置偏移
     *
     * @param activity
     * @param popupWindow
     * @param gravity
     * @param bgAlpha
     * @param xOff
     * @param yOff
     */
    public static void showAtLocation(Activity activity, PopupWindow popupWindow, int gravity, float bgAlpha, int xOff, int yOff) {
        backgroundAlpha(activity, bgAlpha);
        View parent = ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
        popupWindow.showAtLocation(parent, gravity, xOff, yOff);
    }


    public interface PopupWindowCallBack {
        void disposePopView(View popView);

        void onDismiss();
    }
}

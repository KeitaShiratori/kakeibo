package com.android_mvc.sample_project.activities.common;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;

import com.android_mvc.framework.ui.UIUtil;
import com.android_mvc.framework.ui.view.MButton;
import com.android_mvc.framework.ui.view.MLinearLayout;
import com.android_mvc.framework.ui.view.MPopup;
import com.android_mvc.framework.ui.view.MTextView;
import com.android_mvc.sample_project.R;
import com.android_mvc.sample_project.R.drawable;
import com.android_mvc.sample_project.activities.accountbook.AccountBookShowActivity;
import com.android_mvc.sample_project.activities.accountbook.BudgetShowActivity;
import com.android_mvc.sample_project.activities.accountbook.CostDetailEditActivity;
import com.android_mvc.sample_project.activities.accountbook.CostDetailShowActivity;
import com.android_mvc.sample_project.activities.accountbook.IncomeDetailEditActivity;
import com.android_mvc.sample_project.activities.accountbook.IncomeDetailShowActivity;
import com.android_mvc.sample_project.activities.accountbook.SettleShowActivity;
import com.android_mvc.sample_project.activities.main.TopActivity;
import com.android_mvc.sample_project.controller.common.CommonController;

public class HooterMenu extends MLinearLayout {

    private HooterMenu hooterMenu;
    private MButton button1;
    private MButton button2;
    private MButton button3;
    private MButton button4;
    private MButton button5;

    public HooterMenu(Context context) {
        super(context);
    }

    public HooterMenu getHooterMenu(Activity context) {
        return getHooterMenu(context, context);
    }

    public HooterMenu getHooterMenu(Context context, final Activity activity) {
        if (hooterMenu == null) {
            hooterMenu = new HooterMenu(context);
        }

        button1 = new MButton(context)
                .backgroundDrawable(drawable.button_design)
                .text("目標")
                .longClick(setCaption(activity, "目標貯金額の設定"));

        button2 = new MButton(context)
                .backgroundDrawable(drawable.button_design)
                .text("登録")
                .longClick(setCaption(activity, "支出・収入の登録"));
        ;

        button3 = new MButton(context)
                .backgroundDrawable(drawable.button_design)
                .text("照会")
                .longClick(setCaption(activity, "支出・収入の照会"));

        button4 = new MButton(context)
                .backgroundDrawable(drawable.button_design)
                .text("分析")
                .longClick(setCaption(activity, "予定・実績の表示"));

        button5 = new MButton(context)
                .backgroundDrawable(drawable.button_design)
                .text("TOP")
                .longClick(setCaption(activity, "TOP画面の表示"));

        currentView(activity);

        hooterMenu
                .orientationHorizontal()
                .widthWrapContent()
                .heightWrapContent()
                .paddingPx(10)
                .add(
                        button1.click(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                CommonController.submit(activity, "SHOW_ACCOUNT_BOOK");
                            }

                        })

                        ,
                        button2.click(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                CommonController.submit(activity, "EDIT_TAB_HOST");
                            }

                        })

                        ,
                        button3.click(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // TODO 自動生成されたメソッド・スタブ
                                CommonController.submit(activity, "SHOW_TAB_HOST");

                            }
                        })
                        ,
                        button4.click(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                CommonController.submit(activity, "ANALYSIS_TAB_HOST");
                            }

                        })

                        ,
                        button5.click(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // TODO 自動生成されたメソッド・スタブ
                                CommonController.submit(activity, "TOP");
                            }
                        })
                );

        return this.hooterMenu;
    }

    private OnLongClickListener setCaption(final Activity activity, final String s) {
        return new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                // TODO 自動生成されたメソッド・スタブ
                UIUtil.longToast(activity, s);
                return false;
            }
        };
    }

    private void currentView(Activity activity) {
        // 表示中の画面に対応するボタンを押下状態にする。
        if (activity.getClass().equals(AccountBookShowActivity.class)) {
            button1.backgroundResource(R.drawable.button_design_pressed);
        } else if (activity.getClass().equals(CostDetailEditActivity.class)
                || activity.getClass().equals(IncomeDetailEditActivity.class)) {
            button2.backgroundResource(R.drawable.button_design_pressed);
        } else if (activity.getClass().equals(CostDetailShowActivity.class)
                || activity.getClass().equals(IncomeDetailShowActivity.class)) {
            button3.backgroundResource(R.drawable.button_design_pressed);
        } else if (activity.getClass().equals(BudgetShowActivity.class)
                || activity.getClass().equals(SettleShowActivity.class)) {
            button4.backgroundResource(R.drawable.button_design_pressed);
        } else if (activity.getClass().equals(TopActivity.class)) {
            button5.backgroundResource(R.drawable.button_design_pressed);
        }
    }

    // //
    // --------------------------------------------------------------------------
    // // フリックされた方向を算出する
    // //
    // --------------------------------------------------------------------------
    // private class FlickTouchListener implements View.OnTouchListener
    // {
    // // 最後にタッチされた座標
    // private float startTouchX;
    // private float startTouchY;
    //
    // // 現在タッチ中の座標
    // private float nowTouchedX;
    // private float nowTouchedY;
    //
    // private final Activity activity;
    // private final MPopup mPopup1;
    // private final MPopup mPopup2;
    // private final MPopup mPopup3;
    // private final View view;
    //
    // public FlickTouchListener(Activity activity, View view) {
    // this.activity = activity;
    // this.view = view;
    //
    // this.mPopup1 = new MPopup(view.getContext())
    // .makePopupWindow(new MTextView(activity)
    // .gravity(Gravity.CENTER_VERTICAL)
    // .text("支出照会")
    // .backgroundDrawable(R.drawable.button_design)
    // );
    // this.mPopup2 = new MPopup(view.getContext())
    // .makePopupWindow(new MTextView(activity)
    // .gravity(Gravity.CENTER_VERTICAL)
    // .text("収入登録")
    // .backgroundDrawable(R.drawable.button_design)
    // );
    // this.mPopup3 = new MPopup(view.getContext())
    // .makePopupWindow(new MTextView(activity)
    // .gravity(Gravity.CENTER_VERTICAL)
    // .text("収入照会")
    // .backgroundDrawable(R.drawable.button_design)
    // );
    // }
    //
    // private void actionCancel() {
    // mPopup1.dismiss();
    // mPopup2.dismiss();
    // mPopup3.dismiss();
    // view.setBackgroundResource(R.drawable.button_design);
    // }
    //
    // @Override
    // public boolean onTouch(View v_, MotionEvent event_)
    // {
    // // タッチされている指の本数
    // Log.v("motionEvent", "--touch_count = " + event_.getPointerCount());
    //
    // // タッチされている座標
    // Log.v("Y", "" + event_.getY());
    // Log.v("X", "" + event_.getX());
    //
    // int offX = view.getWidth();
    // int offY = view.getHeight();
    // Log.d("offset", "offX = " + offX + ", offY = " + offY);
    //
    // // フリックの遊び部分（最低限移動しないといけない距離）
    // float adjustX = offX;
    // float adjustY = offY;
    //
    // switch (event_.getAction()) {
    //
    // // タッチ
    // case MotionEvent.ACTION_DOWN:
    // Log.v("motionEvent", "--ACTION_DOWN");
    // view.setBackgroundResource(R.drawable.button_design_pressed);
    // startTouchX = event_.getX();
    // startTouchY = event_.getY();
    // break;
    //
    // // タッチ中に追加でタッチした場合
    // case MotionEvent.ACTION_POINTER_DOWN:
    // Log.v("motionEvent", "--ACTION_POINTER_DOWN");
    // break;
    //
    // // スライド
    // case MotionEvent.ACTION_MOVE:
    // Log.v("motionEvent", "--ACTION_MOVE");
    // mPopup1.showPopupWindow(view, -offX, 0);
    // mPopup2.showPopupWindow(view, 0, -offY);
    // mPopup3.showPopupWindow(view, offX, 0);
    // break;
    //
    // // タッチが離れた
    // case MotionEvent.ACTION_UP:
    // Log.v("motionEvent", "--ACTION_UP");
    // nowTouchedX = event_.getX();
    // nowTouchedY = event_.getY();
    //
    // if (startTouchY > nowTouchedY) {
    // if (startTouchX > nowTouchedX) {
    // if ((startTouchY - nowTouchedY) > (startTouchX - nowTouchedX)) {
    // if (startTouchY > nowTouchedY + adjustY) {
    // Log.v("Flick", "--上");
    // // 上フリック時の処理を記述する
    // CommonController.submit(activity, "EDIT_INCOME_DETAIL");
    // break;
    //
    // }
    // }
    // else if ((startTouchY - nowTouchedY) < (startTouchX - nowTouchedX)) {
    // if (startTouchX > nowTouchedX + adjustX) {
    // Log.v("Flick", "--左");
    // // 左フリック時の処理を記述する
    // CommonController.submit(activity, "SHOW_COST_DETAIL");
    // break;
    // }
    // }
    // }
    // else if (startTouchX < nowTouchedX) {
    // if ((startTouchY - nowTouchedY) > (nowTouchedX - startTouchX)) {
    // if (startTouchY > nowTouchedY + adjustY) {
    // Log.v("Flick", "--上");
    // // 上フリック時の処理を記述する
    // CommonController.submit(activity, "EDIT_INCOME_DETAIL");
    // break;
    // }
    // }
    // else if ((startTouchY - nowTouchedY) < (nowTouchedX - startTouchX)) {
    // if (startTouchX < nowTouchedX + adjustX) {
    // Log.v("Flick", "--右");
    // // 右フリック時の処理を記述する
    // CommonController.submit(activity, "SHOW_INCOME_DETAIL");
    // break;
    // }
    // }
    // }
    // }
    // else if (startTouchY < nowTouchedY) {
    // if (startTouchX > nowTouchedX) {
    // if ((nowTouchedY - startTouchY) > (startTouchX - nowTouchedX)) {
    // if (startTouchY < nowTouchedY + adjustY) {
    // Log.v("Flick", "--下");
    // // 下フリック時の処理を記述する
    // actionCancel();
    // break;
    // }
    // }
    // else if ((nowTouchedY - startTouchY) < (startTouchX - nowTouchedX)) {
    // if (startTouchX > nowTouchedX + adjustX) {
    // Log.v("Flick", "--左");
    // // 左フリック時の処理を記述する
    // CommonController.submit(activity, "SHOW_COST_DETAIL");
    // break;
    // }
    // }
    // }
    // else if (startTouchX < nowTouchedX) {
    // if ((nowTouchedY - startTouchY) > (nowTouchedX - startTouchX)) {
    // if (startTouchY < nowTouchedY + adjustY) {
    // Log.v("Flick", "--下");
    // // 下フリック時の処理を記述する
    // actionCancel();
    // break;
    // }
    // }
    // else if ((nowTouchedY - startTouchY) < (nowTouchedX - startTouchX)) {
    // if (startTouchX < nowTouchedX + adjustX) {
    // Log.v("Flick", "--右");
    // // 右フリック時の処理を記述する
    // CommonController.submit(activity, "SHOW_INCOME_DETAIL");
    // break;
    // }
    // }
    // }
    // }
    //
    // Log.v("Flick", "--フリックなし");
    // CommonController.submit(activity, "EDIT_TAB_HOST");
    //
    // break;
    //
    // // アップ後にほかの指がタッチ中の場合
    // case MotionEvent.ACTION_POINTER_UP:
    // Log.v("motionEvent", "--ACTION_POINTER_UP");
    // break;
    //
    // // UP+DOWNの同時発生(タッチのキャンセル）
    // case MotionEvent.ACTION_CANCEL:
    // Log.v("motionEvent", "--ACTION_CANCEL");
    // actionCancel();
    // break;
    //
    // // ターゲットとするUIの範囲外を押下
    // case MotionEvent.ACTION_OUTSIDE:
    // Log.v("motionEvent", "--ACTION_OUTSIDE");
    // actionCancel();
    // break;
    // }
    // return (true);
    // }
    // }

    // private class FlickTouchListener3 implements View.OnTouchListener
    // {
    // // 最後にタッチされた座標
    // private float startTouchX;
    // private float startTouchY;
    //
    // // 現在タッチ中の座標
    // private float nowTouchedX;
    // private float nowTouchedY;
    //
    // private final Activity activity;
    // private final MPopup mPopup1;
    // private final View view;
    //
    // public FlickTouchListener3(Activity activity, View view) {
    // this.activity = activity;
    // this.view = view;
    // this.mPopup1 = new MPopup(view.getContext())
    // .makePopupWindow(new MTextView(activity)
    // .gravity(Gravity.CENTER_VERTICAL)
    // .text("実績照会")
    // .backgroundDrawable(R.drawable.button_design)
    // );
    // }
    //
    // private void actionCancel() {
    // mPopup1.dismiss();
    // view.setBackgroundResource(R.drawable.button_design);
    // }
    //
    // @Override
    // public boolean onTouch(View v_, MotionEvent event_)
    // {
    // // タッチされている指の本数
    // Log.v("motionEvent", "--touch_count = " + event_.getPointerCount());
    //
    // int offX = view.getWidth();
    // int offY = view.getHeight();
    // Log.d("offset", "offX = " + offX + ", offY = " + offY);
    //
    // // フリックの遊び部分（最低限移動しないといけない距離）
    // float adjustX = offX;
    // float adjustY = offY;
    //
    // // タッチされている座標
    // Log.v("Y", "" + event_.getY());
    // Log.v("X", "" + event_.getX());
    //
    // switch (event_.getAction()) {
    //
    // // タッチ
    // case MotionEvent.ACTION_DOWN:
    // Log.v("motionEvent", "--ACTION_DOWN");
    // view.setBackgroundResource(R.drawable.button_design_pressed);
    // startTouchX = event_.getX();
    // startTouchY = event_.getY();
    // break;
    //
    // // タッチ中に追加でタッチした場合
    // case MotionEvent.ACTION_POINTER_DOWN:
    // Log.v("motionEvent", "--ACTION_POINTER_DOWN");
    // break;
    //
    // // スライド
    // case MotionEvent.ACTION_MOVE:
    // Log.v("motionEvent", "--ACTION_MOVE");
    // mPopup1.showPopupWindow(view, -offX, 0);
    // break;
    //
    // // タッチが離れた
    // case MotionEvent.ACTION_UP:
    // Log.v("motionEvent", "--ACTION_UP");
    // nowTouchedX = event_.getX();
    // nowTouchedY = event_.getY();
    //
    // if (startTouchY > nowTouchedY) {
    // if (startTouchX > nowTouchedX) {
    // if ((startTouchY - nowTouchedY) > (startTouchX - nowTouchedX)) {
    // if (startTouchY > nowTouchedY + adjustY) {
    // Log.v("Flick", "--上");
    // // 上フリック時の処理を記述する
    // actionCancel();
    // break;
    //
    // }
    // }
    // else if ((startTouchY - nowTouchedY) < (startTouchX - nowTouchedX)) {
    // if (startTouchX > nowTouchedX + adjustX) {
    // Log.v("Flick", "--左");
    // // 左フリック時の処理を記述する
    // CommonController.submit(activity, "SHOW_SETTLE_SHOW");
    // break;
    // }
    // }
    // }
    // else if (startTouchX < nowTouchedX) {
    // if ((startTouchY - nowTouchedY) > (nowTouchedX - startTouchX)) {
    // if (startTouchY > nowTouchedY + adjustY) {
    // Log.v("Flick", "--上");
    // // 上フリック時の処理を記述する
    // actionCancel();
    // break;
    // }
    // }
    // else if ((startTouchY - nowTouchedY) < (nowTouchedX - startTouchX)) {
    // if (startTouchX < nowTouchedX + adjustX) {
    // Log.v("Flick", "--右");
    // // 右フリック時の処理を記述する
    // actionCancel();
    // break;
    // }
    // }
    // }
    // }
    // else if (startTouchY < nowTouchedY) {
    // if (startTouchX > nowTouchedX) {
    // if ((nowTouchedY - startTouchY) > (startTouchX - nowTouchedX)) {
    // if (startTouchY < nowTouchedY + adjustY) {
    // Log.v("Flick", "--下");
    // // 下フリック時の処理を記述する
    // actionCancel();
    // break;
    // }
    // }
    // else if ((nowTouchedY - startTouchY) < (startTouchX - nowTouchedX)) {
    // if (startTouchX > nowTouchedX + adjustX) {
    // Log.v("Flick", "--左");
    // // 左フリック時の処理を記述する
    // CommonController.submit(activity, "SHOW_SETTLE_SHOW");
    // break;
    // }
    // }
    // }
    // else if (startTouchX < nowTouchedX) {
    // if ((nowTouchedY - startTouchY) > (nowTouchedX - startTouchX)) {
    // if (startTouchY < nowTouchedY + adjustY) {
    // Log.v("Flick", "--下");
    // // 下フリック時の処理を記述する
    // actionCancel();
    // break;
    // }
    // }
    // else if ((nowTouchedY - startTouchY) < (nowTouchedX - startTouchX)) {
    // if (startTouchX < nowTouchedX + adjustX) {
    // Log.v("Flick", "--右");
    // // 右フリック時の処理を記述する
    // actionCancel();
    // break;
    // }
    // }
    // }
    // }
    //
    // Log.v("Flick", "--フリックなし");
    // CommonController.submit(activity, "SHOW_BUDGET_SHOW");
    // break;
    //
    // // アップ後にほかの指がタッチ中の場合
    // case MotionEvent.ACTION_POINTER_UP:
    // Log.v("motionEvent", "--ACTION_POINTER_UP");
    // actionCancel();
    // break;
    //
    // // UP+DOWNの同時発生(タッチのキャンセル）
    // case MotionEvent.ACTION_CANCEL:
    // Log.v("motionEvent", "--ACTION_CANCEL");
    // actionCancel();
    // break;
    //
    // // ターゲットとするUIの範囲外を押下
    // case MotionEvent.ACTION_OUTSIDE:
    // Log.v("motionEvent", "--ACTION_OUTSIDE");
    // actionCancel();
    // break;
    // }
    // return (true);
    // }
    // }

}

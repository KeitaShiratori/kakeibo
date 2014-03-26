package com.android_mvc.sample_project.activities.main;

import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;

import com.android_mvc.framework.activities.base.BaseNormalActivity;
import com.android_mvc.framework.ui.UIBuilder;
import com.android_mvc.framework.ui.view.MButton;
import com.android_mvc.framework.ui.view.MLinearLayout;
import com.android_mvc.framework.ui.view.MTextView;
import com.android_mvc.sample_project.R;
import com.android_mvc.sample_project.controller.MainController;

/**
 * サンプルのトップ画面。
 * 
 * @author id:language_and_engineering
 * 
 */
public class TopActivity extends BaseNormalActivity
{
    // コンテンツエリアの親レイアウト
    MLinearLayout layout0;

    // UI部品
    private MLinearLayout ll1;
    private MTextView tv10;
    private MButton button11;
    private MButton button12;

    private MLinearLayout ll2;
    private MTextView tv20;
    private MButton button21;
    private MButton button22;

    private MLinearLayout ll3;
    private MTextView tv30;
    private MButton button31;
    private MButton button32;
    private MButton button33;
    // private MButton button34;

    private MLinearLayout ll4;
    private MTextView tv40;
    private MButton button41;
    private MButton button42;

    private TopActivity activity;

    @Override
    public void defineContentView() {
        activity = this;

        // ここに，画面上のUI部品の定義を記述する。
        // 目標セクション
        tv10 = new MTextView(activity)
                .text("目標: 目標貯金額の照会・設定を行います。")
                .gravity(Gravity.LEFT)
                .backgroundResource(R.drawable.header_design_h40_w345);
        ll1 = new MLinearLayout(activity)
                .orientationHorizontal()
                .heightWrapContent()
                .widthFillParent()
                .paddingBottomPx(10);
        button11 = new MButton(activity)
                .text("目標")
                .gravity(Gravity.CENTER_VERTICAL)
                .backgroundResource(R.drawable.button_design_h60_w173);
        button12 = new MButton(activity)
                .text("クレジットカード設定")
                .gravity(Gravity.CENTER_VERTICAL)
                .backgroundResource(R.drawable.button_design_h60_w172);
        button11.click(submit("SHOW_ACCOUNT_BOOK"));
        button12.click(submit("CREDIT_CARD_SETTING"));
        ll1.add(button11, button12);

        // 登録セクション
        tv20 = new MTextView(activity)
                .text("登録: 支出・収入の登録を行います。")
                .gravity(Gravity.LEFT)
                .backgroundResource(R.drawable.header_design_h40_w345);
        ll2 = new MLinearLayout(activity)
                .orientationHorizontal()
                .heightWrapContent()
                .widthFillParent()
                .paddingBottomPx(10);
        button21 = new MButton(activity)
                .text("支出登録")
                .gravity(Gravity.CENTER_VERTICAL)
                .backgroundResource(R.drawable.button_design_h60_w173);
        button22 = new MButton(activity)
                .text("収入登録")
                .gravity(Gravity.CENTER_VERTICAL)
                .backgroundResource(R.drawable.button_design_h60_w172);
        button21.click(submit("EDIT_COST_DETAIL"));
        button22.click(submit("EDIT_INCOME_DETAIL"));
        ll2.add(button21, button22);

        // 照会セクション
        tv30 = new MTextView(activity)
                .text("照会: 支出・収入の照会を行います。")
                .gravity(Gravity.LEFT)
                .backgroundResource(R.drawable.header_design_h40_w345);
        ll3 = new MLinearLayout(activity)
                .orientationHorizontal()
                .heightWrapContent()
                .widthFillParent()
                .paddingBottomPx(10);
        button31 = new MButton(activity)
                .text("支出照会")
                .gravity(Gravity.CENTER_VERTICAL)
                .backgroundResource(R.drawable.button_design_h60_w115);
        button32 = new MButton(activity)
                .text("収入照会")
                .gravity(Gravity.CENTER_VERTICAL)
                .backgroundResource(R.drawable.button_design_h60_w115);
        button33 = new MButton(activity)
                .text("財布の中身")
                .gravity(Gravity.CENTER_VERTICAL)
                .backgroundResource(R.drawable.button_design_h60_w115);
        button31.click(submit("SHOW_COST_DETAIL"));
        button32.click(submit("SHOW_INCOME_DETAIL"));
        button33.click(submit(s(R.string.MYWALLET)));
        ll3.add(button31, button32, button33);

        // 分析セクション
        tv40 = new MTextView(activity)
                .text("分析: 予定・実績の表示を行います。")
                .gravity(Gravity.LEFT)
                .backgroundResource(R.drawable.header_design_h40_w345);
        ll4 = new MLinearLayout(activity)
                .orientationHorizontal()
                .heightWrapContent()
                .widthFillParent()
                .paddingBottomPx(10);
        button41 = new MButton(activity)
                .text("予定分析")
                .gravity(Gravity.CENTER_VERTICAL)
                .backgroundResource(R.drawable.button_design_h60_w173);
        button42 = new MButton(activity)
                .text("実績分析")
                .gravity(Gravity.CENTER_VERTICAL)
                .backgroundResource(R.drawable.button_design_h60_w172);
        button41.click(submit("SHOW_BUDGET_SHOW"));
        button42.click(submit("SHOW_SETTLE_SHOW"));
        ll4.add(button41, button42);

        layout0 = new MLinearLayout(context)
                .orientationVertical()
                .widthMatchParent()
                .paddingPx(10)
                .heightWrapContent();

        // 入力フォームUIを動的に構築する。
        new UIBuilder(context)
                .setDisplayHeaderText("TOP")
                .add(
                        layout0
                )
                .display();

        layout0.add(
                tv10,
                ll1,
                tv20,
                ll2,
                tv30,
                ll3,
                tv40,
                ll4
                );
        
        layout0.inflateInside();
    }

    private OnClickListener submit(final String btnType) {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                // ボタンタイプにしたがって画面遷移する
                MainController.submit(activity, btnType);
            }
        };
    }

    @Override
    public void onBackPressed()
    {
        // 戻るキーが押されたら終了
        moveTaskToBack(true);
    }

}

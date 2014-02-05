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
    // UI部品
    private MTextView tv10;
    private MButton button11;
    private MTextView tv20;
    private MButton button21;
    private MButton button22;
    private MTextView tv30;
    private MButton button31;
    private MButton button32;
    private MTextView tv40;
    private MButton button41;
    private MButton button42;
    private MButton button5;
    private TopActivity activity;

    @Override
    public void defineContentView() {
        activity = this;

        // ここに，画面上のUI部品の定義を記述する。
        // 目標セクション
        tv10 = new MTextView(activity)
                .text("目標: 目標貯金額の照会・設定を行います。")
                .widthFillParent()
                .gravity(Gravity.LEFT)
                .backgroundResource(R.drawable.header_design);
        button11 = new MButton(activity)
                .text("目標")
                .widthFillParent()
                .gravity(Gravity.LEFT)
                .backgroundResource(R.drawable.button_design_1);

        // 登録セクション
        tv20 = new MTextView(activity)
                .text("登録: 支出・収入の登録を行います。")
                .widthFillParent()
                .gravity(Gravity.LEFT)
                .backgroundResource(R.drawable.header_design);
        button21 = new MButton(activity)
                .text("支出登録")
                .widthFillParent()
                .gravity(Gravity.LEFT)
                .backgroundResource(R.drawable.button_design_1);
        button22 = new MButton(activity)
                .text("収入登録")
                .widthFillParent()
                .gravity(Gravity.LEFT)
                .backgroundResource(R.drawable.button_design_1);

        // 照会セクション
        tv30 = new MTextView(activity)
                .text("照会: 支出・収入の照会を行います。")
                .widthFillParent()
                .gravity(Gravity.LEFT)
                .backgroundResource(R.drawable.header_design);
        button31 = new MButton(activity)
                .text("支出照会")
                .widthFillParent()
                .gravity(Gravity.LEFT)
                .backgroundResource(R.drawable.button_design_1);
        button32 = new MButton(activity)
                .text("収入照会")
                .widthFillParent()
                .gravity(Gravity.LEFT)
                .backgroundResource(R.drawable.button_design_1);

        // 分析セクション
        tv40 = new MTextView(activity)
                .text("分析: 予定・実績の表示を行います。")
                .widthFillParent()
                .gravity(Gravity.LEFT)
                .backgroundResource(R.drawable.header_design);
        button41 = new MButton(activity)
                .text("予定分析")
                .widthFillParent()
                .gravity(Gravity.LEFT)
                .backgroundResource(R.drawable.button_design_1);
        button42 = new MButton(activity)
                .text("実績分析")
                .widthFillParent()
                .gravity(Gravity.LEFT)
                .backgroundResource(R.drawable.button_design_1);

        // その他セクション
        button5 = new MButton(activity)
                .text("財布の中身\n財布の中身を確認します。")
                .widthFillParent()
                .gravity(Gravity.LEFT)
                .backgroundResource(R.drawable.button_design_1);

        new UIBuilder(context)
                .setDisplayHeaderText("TOP")
                .add(
                        new MLinearLayout(activity)
                                .orientationVertical()
                                .widthFillParent()
                                .heightFillParent()
                                .add(
                                        tv10
                                        ,
                                        button11.click(submit("SHOW_ACCOUNT_BOOK"))
                                        ,
                                        tv20
                                        ,
                                        button21.click(submit("EDIT_COST_DETAIL"))
                                        ,
                                        button22.click(submit("EDIT_INCOME_DETAIL"))
                                        ,
                                        tv30
                                        ,
                                        button31.click(submit("SHOW_COST_DETAIL"))
                                        ,
                                        button32.click(submit("SHOW_INCOME_DETAIL"))
                                        ,
                                        tv40
                                        ,
                                        button41.click(submit("SHOW_BUDGET_SHOW"))
                                        ,
                                        button42.click(submit("SHOW_SETTLE_SHOW"))

                                )
                )
                .display();
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

package com.android_mvc.sample_project.activities.main;

import android.view.View;
import android.view.View.OnClickListener;

import com.android_mvc.framework.activities.base.BaseNormalActivity;
import com.android_mvc.framework.ui.UIBuilder;
import com.android_mvc.framework.ui.menu.OptionMenuBuilder;
import com.android_mvc.framework.ui.menu.OptionMenuDescription;
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

    // これらのメンバ宣言は，書かなくても動作する。
    MLinearLayout ll1;
    MTextView tv1;
    MTextView tv2;
    MTextView tv3;
    MTextView tv4;
    MButton button1;
    MButton button2;
    MButton button3;

    @Override
    public void defineContentView() {
        final TopActivity activity = this;

        // ここに，画面上のUI部品の定義を記述する。

        new UIBuilder(context)
                .add(

                        tv1 = new MTextView(context)
                                .text("このアプリの名称：" + $._(R.string.app_name) + "\n")
                                .widthWrapContent()
                        ,

                        tv2 = new MTextView(context)
                                .text("\n変動費をDBに登録する：")
                                .widthWrapContent()
                        ,

                        button1 = new MButton(context)
                                .text("変動費登録")
                                .click(new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        MainController.submit(activity, "EDIT_COST_DETAIL");
                                    }
                                })
                        ,
                        new MButton(context)
                                .text("収入登録")
                                .click(new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        MainController.submit(activity, "EDIT_INCOME_DETAIL");
                                    }
                                })
                        ,
                        tv2 = new MTextView(context)
                                .text("\n変動費一覧を表示する：")
                                .widthWrapContent()
                        ,

                        button2 = new MButton(context)
                                .text("変動費照会")
                                .click(new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        MainController.submit(activity, "SHOW_COST_DETAIL");
                                    }
                                })
                        ,

                        tv4 = new MTextView(context)
                                .text("\n目標金額を表示する：")
                                .widthWrapContent()
                        ,

                        button3 = new MButton(context)
                                .text("目標金額照会")
                                .click(new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        MainController.submit(activity, "SHOW_ACCOUNT_BOOK");
                                    }
                                })
                        ,
                        new MButton(context)
                                .text("予定表示")
                                .click(new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        MainController.submit(activity, "SHOW_BUDGET_SHOW");
                                    }
                                })
                        ,
                        new MButton(context)
                                .text("実績表示")
                                .click(new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        MainController.submit(activity, "SHOW_SETTLE_SHOW");
                                    }
                                })
                // button9 = new MButton(context)
                // .text("タブと通信のサンプルへ")
                // .click(new OnClickListener(){
                // @Override
                // public void onClick(View v) {
                // MainController.submit(activity, "TAB_SAMPLE");
                // }
                // })
                // ,

                )
                .display();
    }

    @Override
    public OptionMenuBuilder defineMenu()
    {
        final TopActivity activity = this;

        // オプションメニューを構築
        return new OptionMenuBuilder(context)
                .add(
                new OptionMenuDescription()
                {
                    @Override
                    protected String displayText() {
                        return "変動費登録";
                    }

                    @Override
                    protected void onSelected() {
                        // 画面遷移
                        MainController.submit(activity, "EDIT_COST_DETAIL");
                    }
                }
                );
    }

    @Override
    public void onBackPressed()
    {
        // 戻るキーが押されたら終了
        moveTaskToBack(true);
    }

}

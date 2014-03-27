package com.android_mvc.sample_project.activities.installation;

import java.util.Calendar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;

import com.android_mvc.framework.activities.base.BaseNormalActivity;
import com.android_mvc.framework.controller.validation.ActivityParams;
import com.android_mvc.framework.ui.UIBuilder;
import com.android_mvc.framework.ui.view.MButton;
import com.android_mvc.framework.ui.view.MLinearLayout;
import com.android_mvc.framework.ui.view.MTextView;
import com.android_mvc.sample_project.R;
import com.android_mvc.sample_project.common.Util;
import com.android_mvc.sample_project.controller.MainController;
import com.android_mvc.sample_project.db.schema.ColumnDefinition.AccountBookCol;

/**
 * サンプルのインストール完了画面。
 * 
 * @author id:language_and_engineering
 * 
 */
public class InstallCompletedActivity extends BaseNormalActivity
{
    MButton button0;
    MTextView tv0;

    MLinearLayout layout1;
    MTextView tv11;
    MTextView tv12;
    MTextView tv13;

    MLinearLayout layout2;
    MTextView tv21;
    MTextView tv22;
    MTextView tv23;

    MLinearLayout layout3;
    MTextView tv31;
    MTextView tv32;
    MTextView tv33;

    final Calendar calendar = Calendar.getInstance();
    int day = 0;

    // final int year = calendar.get(Calendar.YEAR);
    // final int month = calendar.get(Calendar.MONTH);
    // final int day = calendar.get(Calendar.DAY_OF_MONTH);

    @Override
    public void defineContentView() {
        final InstallCompletedActivity activity = this;

        // 基準日の初期表示内容を設定
        if (calendar.get(Calendar.DAY_OF_MONTH) > 28) {
            day = 1;
        }
        else {
            day = calendar.get(Calendar.DAY_OF_MONTH);
        }

        // ここに，画面上のUI部品の定義を記述する。
        tv31 = new MTextView(context)
                .gravity(Gravity.CENTER_VERTICAL)
                .backgroundDrawable(R.drawable.header_design)
                .text("基準日")
                .widthWrapContent();
        tv32 = new MTextView(context)
                .gravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT)
                .backgroundDrawable(R.drawable.button_design_1)
                .text(day + "")
                .widthWrapContent();
        tv32.click(Util.createNumberPickerDialog(context, tv32, 28));
        tv33 = new MTextView(context)
                .text("日")
                .gravity(Gravity.CENTER_VERTICAL)
                .widthWrapContent();

        tv11 = new MTextView(context)
                .gravity(Gravity.CENTER_VERTICAL)
                .backgroundDrawable(R.drawable.header_design)
                .text("目標金額")
                .widthWrapContent();

        tv12 = new MTextView(context)
                .gravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT)
                .hint("必須入力")
                .backgroundDrawable(R.drawable.button_design_1);
        tv12.click(Util.createIntInputDialog(activity, tv12));

        tv13 = new MTextView(context)
                .text("円")
                .gravity(Gravity.CENTER_VERTICAL)
                .widthWrapContent();

        tv21 = new MTextView(context)
                .gravity(Gravity.CENTER_VERTICAL)
                .text("目標期間")
                .backgroundDrawable(R.drawable.header_design)
                .widthWrapContent();
        tv22 = new MTextView(context)
                .gravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT)
                .hint("必須入力")
                .backgroundDrawable(R.drawable.button_design_1);
        tv22.click(Util.createIntInputDialog(activity, tv22));
        tv23 = new MTextView(context)
                .text("ヶ月")
                .gravity(Gravity.CENTER_VERTICAL)
                .widthWrapContent();

        new UIBuilder(context)
                .setDisplayHeaderText("目標金額設定")
                .add(
                        tv0 = new MTextView(context)
                                .gravity(Gravity.CENTER_VERTICAL)
                                .text("目標金額と目標期間を設定してください。")
                                .widthWrapContent()
                        ,

                        layout3 = new MLinearLayout(context)
                                .orientationHorizontal()
                                .widthFillParent()
                                .add(
                                        tv31,
                                        tv32,
                                        tv33
                                ),

                        layout1 = new MLinearLayout(context)
                                .orientationHorizontal()
                                .widthFillParent()
                                .add(
                                        tv11,
                                        tv12,
                                        tv13
                                ),

                        layout2 = new MLinearLayout(context)
                                .orientationHorizontal()
                                .widthFillParent()
                                .add(
                                        tv21,
                                        tv22,
                                        tv23
                                ),
                        button0 = new MButton(context)
                                .widthFillParent()
                                .text("目標を設定してトップ画面へ")
                                .backgroundDrawable(R.drawable.button_design_1)
                                .click(new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        MainController.submit(activity);
                                    }
                                })
                )
                .displayWithoutHooter();

    }

    @Override
    public ActivityParams toParams() {
        // 基準日を取得
        Calendar startDate = calendar;
        startDate.set(Calendar.DAY_OF_MONTH, Integer.parseInt(tv32.text()));

        // 入力された値をすべて回収
        return new ActivityParams()
                .add("目標金額", AccountBookCol.MOKUHYOU_KINGAKU, tv12.text())
                .add("目標期間", AccountBookCol.MOKUHYOU_KIKAN, tv22.text())
                .add("使用年月日", AccountBookCol.START_DATE, startDate);
    }

    @Override
    public void onBackPressed()
    {
        // 戻るボタンで戻させない
    }

    @Override
    public void onResume() {
        super.onResume();

        if (pref.getTutorialDoneFlagInstallComplete(context)) {
            // チュートリアルが完了していたら何もしない
            return;
        }

        // チュートリアルが完了していない場合
        final InstallCompletedActivity activity = this;
        final int icon = R.drawable.icon;

        // 1つめのダイアログの設定
        String title1 = s(R.string.app_name) + "へようこそ";
        String content1 = s(R.string.app_name) + "をインストールしていただきありがとうございます。";

        // 2つめのダイアログの設定
        final String title2 = "チュートリアル";
        final String content2 = "まずは、目標を決めましょう。"
                + "\n"
                + "\n欲しいものはなんですか？"
                + "\nやりたいことはなんですか？"
                + "\n"
                + "\nそれを実現するために必要な金額を思い浮かべてください。"
                + "\n何か月で貯められるか、イメージしてください。"
                + "\n"
                + "\n目標が決まったら、『必ず貯めるんだ！』という決意とともに、目標を入力してください。";

        // 1つ目のダイアログ生成
        Util.createAlertDialogWithOKButton(
                activity, title1, content1, icon,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 2つ目のダイアログ生成
                        Util.createAlertDialogWithOKButton(activity, title2, content2, icon, null);
                    }
                });
    }

}

package com.android_mvc.sample_project.activities.accountbook;

import java.util.Calendar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.android_mvc.framework.controller.validation.ActivityParams;
import com.android_mvc.framework.ui.UIBuilder;
import com.android_mvc.framework.ui.UIUtil;
import com.android_mvc.framework.ui.view.MButton;
import com.android_mvc.framework.ui.view.MEditText;
import com.android_mvc.framework.ui.view.MLinearLayout;
import com.android_mvc.framework.ui.view.MTextView;
import com.android_mvc.sample_project.R;
import com.android_mvc.sample_project.R.drawable;
import com.android_mvc.sample_project.activities.accountbook.lib.AccountBookAppUserBaseActivity;
import com.android_mvc.sample_project.common.Util;
import com.android_mvc.sample_project.controller.IncomeDetailController;
import com.android_mvc.sample_project.db.entity.CategoryType;
import com.android_mvc.sample_project.db.entity.PayType;
import com.android_mvc.sample_project.db.schema.ColumnDefinition.IncomeDetailCol;

/**
 * アプリの初期化処理を実行する画面。 アプリ内でのLAUNCHERアクティビティ。
 * 
 * @author id:language_and_engineering
 * 
 */
public class IncomeDetailEditActivity extends AccountBookAppUserBaseActivity {

    MLinearLayout layout1;
    MTextView tv1;
    MTextView bYMD;
    DatePickerDialog dpd1;

    MLinearLayout layout2;
    MTextView tv2;
    MTextView et2;

//    MLinearLayout layout3;
//    MTextView tv3;
//    Spinner sp3;

    MLinearLayout layout5;
    MTextView tv5;
    MTextView et5;

//    MLinearLayout layout6;
//    MTextView tv6;
//    Spinner sp6;

    MButton button1;

    final Calendar calendar = Calendar.getInstance();
    final int year = calendar.get(Calendar.YEAR);
    final int month = calendar.get(Calendar.MONTH);
    final int day = calendar.get(Calendar.DAY_OF_MONTH);

    @Override
    public void defineContentView() {

        final IncomeDetailEditActivity activity = this;

        setContentValue();

        // 入力フォームUIを動的に構築する。
        new UIBuilder(context)
                .setDisplayHeaderText("収入登録")
                .add(
                        layout1,
                        layout2,
//                        layout3,
                        layout5,
//                        layout6,
                        button1 = new MButton(context)
                                .backgroundDrawable(R.drawable.button_design_3)
                                .text("登録")

                                .click(new OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        IncomeDetailController.submit(activity);
                                    }

                                })
                ).display();

    }

    private void setContentValue() {
        tv1 = new MTextView(context)
                .gravity(Gravity.CENTER_VERTICAL)
                .text("予定年月日")
                .backgroundDrawable(R.drawable.header_design)
                .widthWrapContent();

        bYMD = new MTextView(context)
                .gravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT)
                .backgroundDrawable(drawable.button_design_1)
                .text(year + "/" + (month + 1) + "/" + day)
                .drawableLeft(android.R.drawable.ic_menu_month);

        layout1 = new MLinearLayout(context)
                .orientationHorizontal()
                .widthFillParent()
                .add(
                        tv1
                        ,
                        bYMD.click(Util.createDatePickerDialog(context, bYMD, calendar))
                );

        layout2 = new MLinearLayout(context)
                .orientationHorizontal()
                .widthFillParent()
                .add(

                        tv2 = new MTextView(context)
                                .gravity(Gravity.CENTER_VERTICAL)
                                .text("予定金額")
                                .backgroundDrawable(R.drawable.header_design)
                                .widthWrapContent()
                        ,

                        et2 = new MTextView(context)
                                .gravity(Gravity.CENTER_VERTICAL)
                                .hint("必須入力")
                                .backgroundDrawable(R.drawable.button_design_1)
                                .click(new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        final MEditText temp = new MEditText(context)
                                                .text(et2.text())
                                        ;

                                        new AlertDialog.Builder(IncomeDetailEditActivity.this)
                                                .setIcon(android.R.drawable.ic_dialog_info)
                                                .setTitle("予定金額を入力してください")
                                                .setView(temp)
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int whichButton) {
                                                        try {
                                                            Integer NumberFormatExceptionCatcher = Integer.parseInt(temp.getText().toString());
                                                            et2.text(temp.text());
                                                        } catch (NumberFormatException e) {
                                                            UIUtil.longToast(context, "数値を入力してください");
                                                        }
                                                    }

                                                })
                                                .setNegativeButton("キャンセル",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                                // 何もしない
                                                            }
                                                        }).show();

                                    }

                                })

                );

//        layout3 = new MLinearLayout(context)
//                .orientationHorizontal()
//                .widthFillParent()
//                .add(
//
//                        tv3 = new MTextView(context)
//                                .gravity(Gravity.CENTER_VERTICAL)
//                                .text("カテゴリ")
//                                .backgroundDrawable(R.drawable.header_design)
//                                .widthWrapContent()
//                        ,
//
//                        sp3 = new CategoryType().getSpinner(context)
//                );

        layout5 = new MLinearLayout(context)
                .orientationHorizontal()
                .widthFillParent()
                .add(

                        tv5 = new MTextView(context)
                                .gravity(Gravity.CENTER_VERTICAL)
                                .text("実績金額")
                                .backgroundDrawable(R.drawable.header_design)
                                .widthWrapContent()
                        ,

                        et5 = new MTextView(context)
                                .gravity(Gravity.CENTER_VERTICAL)
                                .hint("未入力")
                                .backgroundDrawable(R.drawable.button_design_1)
                                .click(new OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        final MEditText temp = new MEditText(context)
                                                .text(et5.text());
                                        ;
                                        new AlertDialog.Builder(IncomeDetailEditActivity.this)
                                                .setIcon(android.R.drawable.ic_dialog_info)
                                                .setTitle("実績金額を入力してください")
                                                .setView(temp)
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int whichButton) {
                                                        try {
                                                            Integer NumberFormatExceptionCatcher = Integer.parseInt(temp.getText().toString());
                                                            et5.text(temp.text());
                                                        } catch (NumberFormatException e) {
                                                            UIUtil.longToast(context, "数値を入力してください");
                                                        }
                                                    }

                                                })
                                                .setNegativeButton("キャンセル",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                                // 何もしない
                                                            }
                                                        }).show();

                                    }

                                })

                );

//        layout6 = new MLinearLayout(context)
//                .orientationHorizontal()
//                .widthFillParent()
//                .add(
//
//                        tv6 = new MTextView(context)
//                                .gravity(Gravity.CENTER_VERTICAL)
//                                .text("支払方法")
//                                .backgroundDrawable(R.drawable.header_design)
//                                .widthWrapContent()
//                        ,
//
//                        sp6 = new PayType().getSpinner(context)
//                );

    }

    @Override
    public ActivityParams toParams() {
        // 入力された値をすべて回収
        return new ActivityParams()
                .add("予定年月日", IncomeDetailCol.BUDGET_YMD, Util.toCalendar(bYMD.getText().toString()))
                .add("予算費用", IncomeDetailCol.BUDGET_INCOME, et2.text())
//                .add("カテゴリ名", IncomeDetailCol.CATEGORY_TYPE, (sp3.getSelectedItemPosition() + 1))
                .add("実績費用", IncomeDetailCol.SETTLE_INCOME, et5.text())
//                .add("支払方法", IncomeDetailCol.PAY_TYPE, (sp6.getSelectedItemPosition() + 1))
                ;
    }

}

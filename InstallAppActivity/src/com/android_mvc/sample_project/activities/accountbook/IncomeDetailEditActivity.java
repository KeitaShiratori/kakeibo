package com.android_mvc.sample_project.activities.accountbook;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.android_mvc.framework.activities.base.BaseNormalActivity;
import com.android_mvc.framework.controller.validation.ActivityParams;
import com.android_mvc.framework.ui.UIBuilder;
import com.android_mvc.framework.ui.UIUtil;
import com.android_mvc.framework.ui.view.MButton;
import com.android_mvc.framework.ui.view.MEditText;
import com.android_mvc.framework.ui.view.MLinearLayout;
import com.android_mvc.framework.ui.view.MTextView;
import com.android_mvc.sample_project.R;
import com.android_mvc.sample_project.R.drawable;
import com.android_mvc.sample_project.common.Util;
import com.android_mvc.sample_project.controller.FuncDBController;
import com.android_mvc.sample_project.db.entity.CategoryType;
import com.android_mvc.sample_project.db.entity.PayType;
import com.android_mvc.sample_project.db.schema.ColumnDefinition.IncomeDetailCol;

/**
 * アプリの初期化処理を実行する画面。 アプリ内でのLAUNCHERアクティビティ。
 * 
 * @author id:language_and_engineering
 * 
 */
public class IncomeDetailEditActivity extends BaseNormalActivity {

    MLinearLayout layout1;
    MTextView tv1;
    MButton b1;
    MTextView bYMD;
    DatePickerDialog dpd1;

    MLinearLayout layout2;
    MTextView tv2;
    MEditText et2;

    MLinearLayout layout3;
    MTextView tv3;
    Spinner sp3;

    MLinearLayout layout4;
    MTextView tv4;
    MButton b4;
    MTextView sYMD;
    DatePickerDialog dpd4;

    MLinearLayout layout5;
    MTextView tv5;
    MEditText et5;

    MLinearLayout layout6;
    MTextView tv6;
    Spinner sp6;

    MButton button1;

    final Calendar calendar = Calendar.getInstance();
    final int year = calendar.get(Calendar.YEAR);
    final int month = calendar.get(Calendar.MONTH);
    final int day = calendar.get(Calendar.DAY_OF_MONTH);
    private boolean settleYmdInputedFlag = false;

    @Override
    public void defineContentView() {

        final IncomeDetailEditActivity activity = this;

        // 入力フォームUIを動的に構築する。
        new UIBuilder(context)
                .add(

                        layout1 = new MLinearLayout(context)
                                .orientationHorizontal()
                                .widthFillParent()
                                .add(

                                        tv1 = new MTextView(context)
                                                .text("予定年月日:")
                                                .widthWrapContent()
                                        ,

                                        bYMD = new MTextView(context)
                                                .text(year + "/" + (month + 1) + "/" + day)
                                                .widthWrapContent()
                                        ,

                                        b1 = new MButton(context)
                                                .text("入力")
                                                .click(new OnClickListener() {

                                                    @Override
                                                    public void onClick(View v) {
                                                        dpd1 = new DatePickerDialog(
                                                                context,
                                                                new DatePickerDialog.OnDateSetListener() {
                                                                    @Override
                                                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                                                        bYMD.setText(
                                                                                String.valueOf(year) + "/" +
                                                                                        String.valueOf(monthOfYear + 1) + "/" +
                                                                                        String.valueOf(dayOfMonth));
                                                                    }
                                                                },
                                                                year, month, day);
                                                        dpd1.show();
                                                    }

                                                })
                                ),

                        layout2 = new MLinearLayout(context)
                                .orientationHorizontal()
                                .widthFillParent()
                                .add(

                                        tv2 = new MTextView(context)
                                                .text("予算費用:")
                                                .widthWrapContent()
                                        ,

                                        et2 = new MEditText(context)
                                                .widthPx(300)

                                )
                        ,

                        layout3 = new MLinearLayout(context)
                                .orientationHorizontal()
                                .widthFillParent()
                                .add(

                                        tv3 = new MTextView(context)
                                                .text("カテゴリ:")
                                                .widthWrapContent()
                                        ,

                                        sp3 = new CategoryType().getSpinner(context)
                                )
                        ,

                        layout4 = new MLinearLayout(context)
                                .orientationHorizontal()
                                .widthFillParent()
                                .add(

                                        tv4 = new MTextView(context)
                                                .text("使用年月日:")
                                                .widthWrapContent()
                                        ,

                                        sYMD = new MTextView(context)
                                                .text("")
                                                .widthWrapContent()
                                        ,

                                        b4 = new MButton(context)
                                                .text("入力")
                                                .click(new OnClickListener() {

                                                    @Override
                                                    public void onClick(View v) {
                                                        dpd4 = new DatePickerDialog(
                                                                context,
                                                                new DatePickerDialog.OnDateSetListener() {
                                                                    @Override
                                                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                                                        sYMD.setText(
                                                                                String.valueOf(year) + "/" +
                                                                                        String.valueOf(monthOfYear + 1) + "/" +
                                                                                        String.valueOf(dayOfMonth));
                                                                        settleYmdInputedFlag = true;
                                                                    }
                                                                },
                                                                year, month, day);
                                                        dpd4.show();
                                                    }

                                                })
                                ),

                        layout5 = new MLinearLayout(context)
                                .orientationHorizontal()
                                .widthFillParent()
                                .add(

                                        tv5 = new MTextView(context)
                                                .text("実績費用:")
                                                .widthWrapContent()
                                        ,

                                        et5 = new MEditText(context)
                                                .widthPx(300)

                                )
                        ,

                        layout6 = new MLinearLayout(context)
                                .orientationHorizontal()
                                .widthFillParent()
                                .add(

                                        tv6 = new MTextView(context)
                                                .text("支払方法:")
                                                .widthWrapContent()
                                        ,

                                        sp6 = new PayType().getSpinner(context)
                                )
                        ,

                        button1 = new MButton(context, null, R.attr.button_style_a1)
                                .text("登録")
                                .click(new OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        FuncDBController.submit(activity);
                                    }

                                })
                )
                .display();

    }

    @Override
    public void afterViewDefined()
    {
        if ($.intentHasKey("hoge"))
        {
            // Intentから受け取った値をToastで表示
            UIUtil.longToast(this, $.extras().getString("変動費DBへの登録が完了しました。"));
        }
    }

    @Override
    public ActivityParams toParams() {
        // 入力された値をすべて回収
        String settleYmd = null;
        if (settleYmdInputedFlag) {
            settleYmd = sYMD.getText().toString();
        }
        return new ActivityParams()
                .add("予定年月日", "budget_ymd", Util.toCalendar(bYMD.getText().toString()))
                .add("予算費用", "budget_income", et2.text())
                .add("カテゴリ名", IncomeDetailCol.CATEGORY_TYPE, (sp3.getSelectedItemPosition() + 1))
                .add("使用年月日", "settle_ymd", Util.toCalendar(sYMD.getText().toString()))
                .add("実績費用", "settle_income", et5.text())
                .add("支払方法", IncomeDetailCol.PAY_TYPE, (sp6.getSelectedItemPosition() + 1));
    }

}

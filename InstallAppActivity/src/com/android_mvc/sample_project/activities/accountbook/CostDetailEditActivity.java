package com.android_mvc.sample_project.activities.accountbook;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
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
import com.android_mvc.sample_project.controller.CostDetailController;
import com.android_mvc.sample_project.db.dao.PayTypeDAO;
import com.android_mvc.sample_project.db.entity.CategoryType;
import com.android_mvc.sample_project.db.entity.PayType;
import com.android_mvc.sample_project.db.schema.ColumnDefinition.CostDetailCol;

/**
 * アプリの初期化処理を実行する画面。 アプリ内でのLAUNCHERアクティビティ。
 * 
 * @author id:language_and_engineering
 * 
 */
public class CostDetailEditActivity extends AccountBookAppUserBaseActivity {

    MLinearLayout layout1;
    MTextView tv1;
    MTextView bYMD;
    DatePickerDialog dpd1;

    MLinearLayout layout2;
    MTextView tv2;
    MTextView et2;

    MLinearLayout layout3;
    MTextView tv3;
    Spinner sp3;

    MLinearLayout layout4;
    MTextView tv4;
    MTextView sYMD;
    DatePickerDialog dpd4;

    MLinearLayout layout5;
    MTextView tv5;
    MTextView et5;

    MLinearLayout layout6;
    MTextView tv6;
    Spinner sp6;

    // 繰り返し区分
    MLinearLayout layout7;
    MTextView tv7;
    Spinner sp7;

    MButton button1;

    final Calendar calendar = Calendar.getInstance();
    final int year = calendar.get(Calendar.YEAR);
    final int month = calendar.get(Calendar.MONTH);
    final int day = calendar.get(Calendar.DAY_OF_MONTH);
    private boolean settleYmdInputedFlag = false;

    @Override
    public void defineContentView() {

        final CostDetailEditActivity activity = this;

        setContentsValue();

        // 入力フォームUIを動的に構築する。
        new UIBuilder(context)
                .setDisplayHeaderText("変動費登録")
                .add(
                        layout1,
                        layout2,
                        layout3,
                        layout4,
                        layout5,
                        layout6,
                        layout7,
                        button1 = new MButton(context)
                                .backgroundDrawable(R.drawable.button_design_3)
                                .text("登録")
                                .click(new OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        CostDetailController.submit(activity);
                                    }

                                })

                )
                .display();

    }

    private void setContentsValue() {

        layout1 = new MLinearLayout(context)
                .orientationHorizontal()
                .widthFillParent()
                .add(

                        tv1 = new MTextView(context)
                                .gravity(Gravity.CENTER_VERTICAL)
                                .text("使用予定日")
                                .backgroundDrawable(R.drawable.header_design)
                                .widthWrapContent()
                        ,

                        bYMD = new MTextView(context)
                                .gravity(Gravity.CENTER_VERTICAL)
                                .backgroundDrawable(drawable.button_design_1)
                                .text(year + "/" + (month + 1) + "/" + day)
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

                );

        layout2 = new MLinearLayout(context)
                .orientationHorizontal()
                .widthFillParent()
                .add(

                        tv2 = new MTextView(context)
                                .text("予定金額")
                                .gravity(Gravity.CENTER_VERTICAL)
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

                                        new AlertDialog.Builder(CostDetailEditActivity.this)
                                                .setIcon(android.R.drawable.ic_dialog_info)
                                                .setTitle("使用予定金額を入力してください")
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

        layout3 = new MLinearLayout(context)
                .orientationHorizontal()
                .widthFillParent()
                .add(

                        tv3 = new MTextView(context)
                                .text("カテゴリ")
                                .gravity(Gravity.CENTER_VERTICAL)
                                .backgroundDrawable(R.drawable.header_design)
                                .widthWrapContent()
                        ,

                        sp3 = new CategoryType().getSpinner(context)
                );

        layout4 = new MLinearLayout(context)
                .orientationHorizontal()
                .widthFillParent()
                .add(

                        tv4 = new MTextView(context)
                                .text("使用実績日")
                                .gravity(Gravity.CENTER_VERTICAL)
                                .backgroundDrawable(R.drawable.header_design)
                                .widthWrapContent()
                        ,

                        sYMD = new MTextView(context)
                                .gravity(Gravity.CENTER_VERTICAL)
                                .backgroundDrawable(drawable.button_design_1)
                                .hint("未入力")
                                .click(new OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        dpd4 = new DatePickerDialog(context,
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

                                        // Dialog の Negative Button を設定
                                        dpd4.setButton(
                                                DialogInterface.BUTTON_NEGATIVE,
                                                "未入力",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        // Negative
                                                        // Buttonがクリックされた時の動作
                                                        sYMD.setText("");
                                                        settleYmdInputedFlag = false;
                                                    }
                                                }
                                                );

                                        dpd4.show();
                                    }

                                })

                );

        layout5 = new MLinearLayout(context)
                .orientationHorizontal()
                .widthFillParent()
                .add(

                        tv5 = new MTextView(context)
                                .text("実績金額")
                                .gravity(Gravity.CENTER_VERTICAL)
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
                                        new AlertDialog.Builder(CostDetailEditActivity.this)
                                                .setIcon(android.R.drawable.ic_dialog_info)
                                                .setTitle("実際に使用した金額を入力してください")
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

        layout6 = new MLinearLayout(context)
                .orientationHorizontal()
                .widthFillParent()
                .add(

                        tv6 = new MTextView(context)
                                .text("支払方法")
                                .gravity(Gravity.CENTER_VERTICAL)
                                .backgroundDrawable(R.drawable.header_design)
                                .widthWrapContent()
                        ,

                        sp6 = new PayType().getSpinner(context)
                );

        layout7 = new MLinearLayout(context)
                .orientationHorizontal()
                .widthFillParent()
                .add(

                        tv7 = new MTextView(context)
                                .text("繰り返し区分")
                                .gravity(Gravity.CENTER_VERTICAL)
                                .backgroundDrawable(R.drawable.header_design)
                                .widthWrapContent()
                        ,

                        sp7 = getSpinner()
                );

    }

    private Spinner getSpinner() {
        Spinner ret = new Spinner(context);
        ret.setBackgroundResource(R.drawable.button_design_1);
        List<CharSequence> list = new ArrayList<CharSequence>();
        list.add("繰り返しなし");
        list.add("平日のみ");
        list.add("土日のみ");
        list.add("毎日");
        ArrayAdapter<CharSequence> Adapter = new ArrayAdapter<CharSequence>(context, android.R.layout.simple_spinner_item, list);

        ret.setAdapter(Adapter);
        return ret;
    }

    @Override
    public void afterViewDefined()
    {
        if ($.intentHasKey("hoge"))
        {
            // Intentから受け取った値をToastで表示
            UIUtil.longToast(this, $.extras().getString("変動費明細の登録が完了しました。"));
        }
    }

    @Override
    public ActivityParams toParams() {
        // 入力された値をすべて回収
        String settleYmd = null;
        if (settleYmdInputedFlag) {
            settleYmd = sYMD.text();
        }
        return new ActivityParams()
                .add("予定年月日", "budget_ymd", Util.toCalendar(bYMD.text()))
                .add("予算費用", "budget_cost", et2.text())
                .add("カテゴリ名", CostDetailCol.CATEGORY_TYPE, (sp3.getSelectedItemPosition() + 1))
                .add("使用年月日", "settle_ymd", Util.toCalendar(settleYmd))
                .add("実績費用", "settle_cost", et5.text())
                .add("支払方法", CostDetailCol.PAY_TYPE, (sp6.getSelectedItemPosition()) + 1)
                .add("繰り返し区分", "repeat_dvn", sp7.getSelectedItem());
    }

}

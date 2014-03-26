package com.android_mvc.sample_project.activities.accountbook;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.simonvt.datepicker.DatePickerDialog;
import net.simonvt.numberpicker.NumberPicker;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
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

    // コンテンツエリアの親レイアウト
    MLinearLayout layout0;

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

    MLinearLayout layout5;
    MTextView tv5;
    MTextView et5;

    MLinearLayout layout6;
    MTextView tv6;
    Spinner sp6;

    MLinearLayout layout61;
    MTextView tv61;
    MTextView tv62;

    // 繰り返し区分
    MLinearLayout layout7;
    MTextView tv7;
    Spinner sp7;

    MButton button1;

    final Calendar calendar = Calendar.getInstance();
    final int year = calendar.get(Calendar.YEAR);
    final int month = calendar.get(Calendar.MONTH);
    final int day = calendar.get(Calendar.DAY_OF_MONTH);

    @Override
    public void defineContentView() {

        final CostDetailEditActivity activity = this;

        setContentsValue();

        layout0 = new MLinearLayout(context)
                .orientationVertical()
                .widthMatchParent()
                .paddingLeftPx(10)
                .heightWrapContent();

        // 入力フォームUIを動的に構築する。
        new UIBuilder(context)
                .setDisplayHeaderText("変動費登録")
                .add(
                        layout0
                )
                .display();

        layout0.add(
                layout1,
                layout2,
                layout3,
                layout5,
                layout6,
                layout61,
                layout7,
                new MTextView(context)
                        .paddingPx(5)
                        .textsize(1)
                ,
                button1 = new MButton(context)
                        .backgroundDrawable(R.drawable.button_design_h40_w345)
                        .text("登録")
                        .textSize(18)
                        .click(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CostDetailController.submit(activity);
                            }
                        })
                );
        layout0.inflateInside();

    }

    protected void setContentsValue() {

        tv1 = new MTextView(context)
                .gravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT)
                .text("使用予定日")
                .backgroundDrawable(R.drawable.header_design_h40_w115)
                .widthWrapContent();

        bYMD = new MTextView(context)
                .gravity(Gravity.CENTER)
                .backgroundDrawable(drawable.button_design_h40_w230)
                .text(year + "/" + (month + 1) + "/" + day);

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
                                .text("予定金額")
                                .gravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT)
                                .backgroundDrawable(R.drawable.header_design_h40_w115)
                                .widthWrapContent()
                        ,

                        et2 = new MTextView(context)
                                .gravity(Gravity.CENTER)
                                .hint("必須入力")
                                .hintTextColor(android.graphics.Color.argb(128, 255, 0, 0))
                                .backgroundDrawable(R.drawable.button_design_h40_w230)
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
                                .gravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT)
                                .backgroundDrawable(R.drawable.header_design_h40_w115)
                                .widthWrapContent()
                        ,

                        sp3 = new CategoryType().getSpinner(context)
                );

        layout5 = new MLinearLayout(context)
                .orientationHorizontal()
                .widthFillParent()
                .add(

                        tv5 = new MTextView(context)
                                .text("実績金額")
                                .gravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT)
                                .backgroundDrawable(R.drawable.header_design_h40_w115)
                                .widthWrapContent()
                        ,

                        et5 = new MTextView(context)
                                .gravity(Gravity.CENTER)
                                .hint("未入力")
                                .backgroundDrawable(R.drawable.button_design_h40_w230)
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
                                .gravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT)
                                .backgroundDrawable(R.drawable.header_design_h40_w115)
                                .widthWrapContent()
                        ,

                        sp6 = new PayType().getSpinner(context)
                );
        layout61 = new MLinearLayout(context)
                .orientationHorizontal()
                .widthFillParent()
                .add(

                        tv61 = new MTextView(context)
                                .text("支払回数")
                                .gravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT)
                                .backgroundDrawable(R.drawable.header_design_h40_w115)
                                .widthWrapContent()
                        ,

                        tv62 = new MTextView(context)
                                .hint(s(R.string.MI_NYUURYOKU))
                                .gravity(Gravity.CENTER)
                                .backgroundDrawable(drawable.button_design_h40_w230)
                                .widthWrapContent()
                                .click(ifPossibleCreateNumberPickerDialog())
                );

        layout7 = new MLinearLayout(context)
                .orientationHorizontal()
                .widthFillParent()
                .add(

                        tv7 = new MTextView(context)
                                .text("繰り返し区分")
                                .gravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT)
                                .backgroundDrawable(R.drawable.header_design_h40_w115)
                                .widthWrapContent()
                        ,

                        sp7 = getSpinner()
                );

    }

    private OnClickListener ifPossibleCreateNumberPickerDialog() {
        final CostDetailEditActivity activity = this;
        return new OnClickListener() {

            @Override
            public void onClick(View v) {
                // Spinnerの設定値が2（クレジット）の場合はNumberPickerDialogを表示する。
                if (sp6.getSelectedItemPosition() + 1 != 2) {
                    UIUtil.longToast(activity, "支払方法がクレジットの場合のみ入力可能です。");
                    tv62.text("");
                }
                else {
                    final NumberPicker np1 = new NumberPicker(context);
                    np1.setMaxValue(10);
                    np1.setMinValue(1);
                    np1.setBackgroundColor(context.getResources().getColor(R.color.white));
                    np1.setFocusable(true);
                    np1.setFocusableInTouchMode(true);

                    new AlertDialog.Builder(context)
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setTitle(Util._(context, R.string.MSG_00003))
                            .setView(np1)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    tv62.text(String.valueOf(np1.getValue()));
                                }

                            })
                            .setNegativeButton("クリア",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            tv62.text("");
                                        }
                                    }).show();

                }
                ;
            }

        };
    }

    private Spinner getSpinner() {
        Spinner ret = new Spinner(context);
        ret.setBackgroundResource(R.drawable.button_design_h40_w230);
        List<CharSequence> list = new ArrayList<CharSequence>();
        list.add("繰り返しなし");
        list.add("平日のみ");
        list.add("土日のみ");
        list.add("毎日");
        list.add("毎週");
        list.add("毎月");
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
        // 支払方法が2（クレジット）でない場合、支払回数をnullにする。
        Integer divideNum = null;
        if (sp6.getSelectedItemPosition() + 1 == 2) {
            divideNum = Integer.parseInt(tv62.text());
        }

        return new ActivityParams()
                .add("予定年月日", CostDetailCol.BUDGET_YMD, Util.toCalendar(bYMD.text()))
                .add("予算費用", CostDetailCol.BUDGET_COST, et2.text())
                .add("カテゴリ名", CostDetailCol.CATEGORY_TYPE, (sp3.getSelectedItemPosition() + 1))
                .add("実績費用", CostDetailCol.SETTLE_COST, et5.text())
                .add("支払方法", CostDetailCol.PAY_TYPE, (sp6.getSelectedItemPosition()) + 1)
                .add("繰り返し区分", "repeat_dvn", sp7.getSelectedItem())
                .add("支払回数", CostDetailCol.DIVIDE_NUM, divideNum);
    }

}

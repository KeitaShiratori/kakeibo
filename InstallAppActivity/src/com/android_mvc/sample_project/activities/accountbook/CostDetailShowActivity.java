package com.android_mvc.sample_project.activities.accountbook;

import java.util.Calendar;
import java.util.List;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.LinearLayout;

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
import com.android_mvc.sample_project.db.dao.CostDetailDAO;
import com.android_mvc.sample_project.db.entity.CostDetail;
import com.android_mvc.sample_project.db.entity.lib.LPUtil;
import com.android_mvc.sample_project.db.schema.ColumnDefinition.CostDetailCol;

/**
 * サンプルのDB参照アクティビティ。
 * 
 * @author id:language_and_engineering
 * 
 */
public class CostDetailShowActivity extends AccountBookAppUserBaseActivity {

    private MLinearLayout layout1;
    private MTextView tv1;
    private MTextView tv2;
    private Calendar LabelYMD;
    private String mode;
    private Calendar startDate;
    private MLinearLayout modeButtons;
    private MButton btnDayMode;
    private MButton btnWeekMode;
    private MButton btnMonthMode;
    private MButton btnAfter;
    private MButton btnBefore;

    // モード定義
    public static String NEW_RECORD_MODE = "NEW_RECORD_MODE";
    public static String DAY_MODE = "DAY_MODE";
    public static String WEEK_MODE = "WEEK_MODE";
    public static String MONTH_MODE = "MONTH_MODE";
    public static String ALL_MODE = "ALL_MODE";
    // 前後
    public static String BEFORE = "BEFORE";
    public static String AFTER = "AFTER";

    // 変動費明細のリスト
    List<CostDetail> CostDetails;

    @Override
    public boolean requireProcBeforeUI() {
        // UI構築前に処理を要求する
        return true;
    }

    // UI構築前に別スレッドで実行される処理
    @Override
    public void procAsyncBeforeUI() {
        //
        Util.d("UI構築前に実行される処理です。");

    }

    @Override
    public void defineContentView() {
        final CostDetailShowActivity activity = this;

        // 初期処理
        layout1 = new MLinearLayout(context)
                .orientationVertical()
                .widthMatchParent()
                .heightWrapContent();

        // モード判定
        mode = ShowTabHostActivity.mode;
        startDate = ShowTabHostActivity.startDate;

        Util.d("表示モード: " + mode);
        Util.d("startDate: " + startDate);

        // 新規登録直後
        if (mode.equals(NEW_RECORD_MODE)) {
            CostDetails = ShowTabHostActivity.costDetails;
            ShowTabHostActivity.costDetails = null;
            tv1 = new MTextView(activity)
                    .text("支出が登録されました")
                    .textColor(R.color.red);
            layout1.add(tv1);
        }
        // 全件モード
        else if (mode.equals(ALL_MODE)) {
            CostDetails = new CostDetailDAO(this).findOrderBy(CostDetailCol.BUDGET_YMD);
        }
        // 日、週、月のいずれかのモード
        else {
            String[] days = null;

            // モードにしたがい、DBの検索範囲を設定
            if (mode.equals(DAY_MODE)) {
                days = new String[1];
                days[0] = LPUtil.encodeCalendarToText(startDate);
            }
            else if (mode.equals(WEEK_MODE)) {
                days = new String[7];
                days[0] = LPUtil.encodeCalendarToText(startDate);
                Calendar tmp = (Calendar) startDate.clone();
                for (int i = 1; i < days.length; i++) {
                    tmp.add(Calendar.DATE, 1);
                    days[i] = LPUtil.encodeCalendarToText(tmp);
                }
                tmp = null;
            }
            else if (mode.equals(MONTH_MODE)) {
                days = new String[startDate.getActualMaximum(Calendar.DAY_OF_MONTH)];
                Calendar tmp = (Calendar) startDate.clone();
                tmp.set(Calendar.DAY_OF_MONTH, 1);
                for (int i = 1; i < days.length; i++) {
                    tmp.add(Calendar.DATE, 1);
                    days[i] = LPUtil.encodeCalendarToText(tmp);
                }
                tmp = null;
            }

            // 全CostDetailをDBからロード
            CostDetails = new CostDetailDAO(this).findWhereIn(CostDetailCol.BUDGET_YMD, days);
        }

        defineModeButtons();

        // まず親レイアウトを定義
        new UIBuilder(context)
                .setDisplayHeader(modeButtons)
                .add(
                        layout1
                )
                .display();

        // 変動費明細レコードが取得できた場合、ヘッダ行を表示する
        if (!CostDetails.isEmpty()) {
            LabelYMD = CostDetails.get(0).getBudgetYmd();
            layout1.add(getLabelYMD(CostDetails.get(0).getBudgetYmd(), CostDetails));
        }

        // レイアウト内に動的に全変動費明細の情報を表示。
        for (int i = 0; i < CostDetails.size(); i++) {
            CostDetail c = CostDetails.get(i);

            // 予定日が変わったら、空白行を挟んで、新しいヘッダ行を追加する。LabelYMDを更新する。
            if (!LabelYMD.equals(c.getBudgetYmd())) {
                layout1.add(
                        new MTextView(context)
                                .paddingPx(5)
                        ,
                        getLabelYMD(c.getBudgetYmd(), CostDetails));
                LabelYMD = c.getBudgetYmd();
            }

            layout1.add(
                    c.getDescription(activity, context, update(this, c), delete(this, c))
                    );

        }

        // 描画
        layout1.inflateInside();

        // 友達登録操作の直後の場合
        if ($.hasActionResult())
        {
            showRegisteredNewCostDetail();
        }
    }

    private void defineModeButtons() {

        btnBefore = new MButton(context)
                .backgroundDrawable(R.drawable.button_design)
                .text("<<")
                .click(submit(BEFORE));

        btnDayMode = new MButton(context)
                .backgroundResource(R.drawable.button_design)
                .text("日")
                .click(submit(DAY_MODE));
        if (mode.equals(DAY_MODE)) {
            btnDayMode.backgroundResource(R.drawable.button_design_pressed);
        }

        btnWeekMode = new MButton(context)
                .backgroundResource(R.drawable.button_design)
                .text("週")
                .click(submit(WEEK_MODE));
        if (mode.equals(WEEK_MODE)) {
            btnWeekMode.backgroundResource(R.drawable.button_design_pressed);
        }

        btnMonthMode = new MButton(context)
                .backgroundResource(R.drawable.button_design)
                .text("月")
                .click(submit(MONTH_MODE));
        if (mode.equals(MONTH_MODE)) {
            btnMonthMode.backgroundResource(R.drawable.button_design_pressed);
        }

        btnAfter = new MButton(context)
                .backgroundResource(R.drawable.button_design)
                .text(">>")
                .click(submit(AFTER));

        modeButtons = new MLinearLayout(context)
                .orientationHorizontal()
                .widthFillParent()
                .heightWrapContent()
                .add(
                        btnBefore
                        ,
                        btnDayMode
                        ,
                        btnWeekMode
                        ,
                        btnMonthMode
                        ,
                        btnAfter
                );

    }

    private OnClickListener submit(final String modeBtn) {
        final CostDetailShowActivity activity = this;
        final Calendar offset = (Calendar) startDate.clone();

        if (modeBtn.equals(BEFORE)) {
            if (mode.equals(DAY_MODE)) {
                offset.add(Calendar.DAY_OF_MONTH, -1);
            }
            else if (mode.equals(WEEK_MODE)) {
                offset.add(Calendar.DAY_OF_MONTH, -7);
            }
            else if (mode.equals(MONTH_MODE)) {
                offset.add(Calendar.MONTH, -1);
            }
            return new OnClickListener() {

                @Override
                public void onClick(View v) {
                    CostDetailController.submit(activity, mode, offset);
                }
            };

        }
        else if (modeBtn.equals(AFTER)) {
            if (mode.equals(DAY_MODE)) {
                offset.add(Calendar.DAY_OF_MONTH, 1);
            }
            else if (mode.equals(WEEK_MODE)) {
                offset.add(Calendar.DAY_OF_MONTH, 7);
            }
            else if (mode.equals(MONTH_MODE)) {
                offset.add(Calendar.MONTH, 1);
            }
            return new OnClickListener() {

                @Override
                public void onClick(View v) {
                    CostDetailController.submit(activity, mode, offset);
                }
            };
        }

        return new OnClickListener() {

            @Override
            public void onClick(View v) {
                CostDetailController.submit(activity, modeBtn, startDate);
            }
        };
    }

    // 新規登録されたばかりの新しい友達情報を表示
    private void showRegisteredNewCostDetail() {
        if ($.actionResultHasKey("new_cost_detail"))
        {
            // Intentから情報を取得
            CostDetail v = (CostDetail) ($.getActionResult().get("new_cost_detail"));

        }
    }

    public LinearLayout getLabelYMD(Calendar budgetYmd, List<CostDetail> costDetails) {

        Integer budgetCostSum = 0;
        Integer settleCostSum = 0;

        for (CostDetail c : costDetails) {
            if (c.getBudgetYmd().equals(budgetYmd)) {
                budgetCostSum += c.getBudgetCost();
                settleCostSum += c.getEffectiveSettleCost();
            }
        }
        // 使用予定日のラベルを１個追加
        return new MLinearLayout(this)
                .orientationHorizontal()
                .widthFillParent()
                .heightWrapContent()
                .paddingLeftPx(10)
                .add(

                        new MTextView(this)
                                .gravity(Gravity.CENTER_VERTICAL)
                                .text(budgetYmd.get(Calendar.YEAR) + "/"
                                        + (budgetYmd.get(Calendar.MONTH) + 1) + "/"
                                        + budgetYmd.get(Calendar.DAY_OF_MONTH) + "\n")
                                .backgroundDrawable(R.drawable.header_design)
                                .click(homeru(budgetCostSum, settleCostSum))
                        ,
                        new MTextView(this)
                                .gravity(Gravity.CENTER_VERTICAL)
                                .text("予定合計\n" + budgetCostSum + "円")
                                .backgroundDrawable(R.drawable.header_design)
                        ,
                        new MTextView(this)
                                .gravity(Gravity.CENTER_VERTICAL)
                                .text("実績合計\n" + settleCostSum + "円")
                                .backgroundDrawable(R.drawable.header_design)

                );
    }

    private OnClickListener homeru(final Integer budgetCostSum, final Integer settleCostSum) {
        final CostDetailShowActivity activity = this;
        return new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (budgetCostSum > settleCostSum) {
                    UIUtil.longToast(activity, "おめでとう！予定金額を守りました！");
                }
                else {
                    UIUtil.longToast(activity, "残念。予定金額より使いすぎています。\n明日は頑張ろう！");
                }
            }
        };
    }

    public OnClickListener delete(final CostDetailShowActivity activity, final CostDetail c) {

        return new OnClickListener() {

            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(activity)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setTitle("この明細を削除してもよろしいですか？")
                        .setPositiveButton("削除", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                try {
                                    // DBから削除へ
                                    CostDetailController.submit(activity, "DELETE_COST_DETAIL", c.getId());
                                } catch (Exception e) {
                                    UIUtil.longToast(context, "削除に失敗しました。");
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
        };
    }

    public OnClickListener update(final CostDetailShowActivity activity, final CostDetail c) {
        return new OnClickListener() {

            @Override
            public void onClick(View v) {
                final MEditText et1 = new MEditText(activity)
                        .hint("実績金額")
                        .widthMatchParent();

                final Calendar calendar = Calendar.getInstance();
                final int year = calendar.get(Calendar.YEAR);
                final int month = calendar.get(Calendar.MONTH);
                final int day = calendar.get(Calendar.DAY_OF_MONTH);

                final MTextView sYMD = new MTextView(context)
                        .hint("実績日付")
                        .widthMatchParent()
                        .gravity(Gravity.CENTER_VERTICAL)
                        .backgroundDrawable(drawable.button_design_1);
                sYMD.click(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        final DatePickerDialog dpd1 = new DatePickerDialog(
                                context,
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                        sYMD.text(
                                                String.valueOf(year) + "/" +
                                                        String.valueOf(monthOfYear + 1) + "/" +
                                                        String.valueOf(dayOfMonth));
                                    }
                                },
                                year, month, day);
                        dpd1.show();
                    }

                });

                final MLinearLayout ll1 = new MLinearLayout(activity)
                        .orientationVertical()
                        .add(et1, sYMD);
                ll1.inflateInside();

                new AlertDialog.Builder(activity)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setTitle("実際に使用した日付、金額を入力してください。")
                        .setView(ll1)
                        .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                try {
                                    Integer temp1 = Integer.parseInt(et1.text());
                                    Calendar temp2 = Util.toCalendar(sYMD.text());
                                    if (temp2 == null) {
                                        throw new NullPointerException();
                                    }
                                    c.setSettleCost(temp1);
                                    c.setSettleYmd(temp2);

                                    // DB更新へ
                                    CostDetailController.submit(activity, "UPDATE_COST_DETAIL", c);
                                } catch (Exception e) {
                                    UIUtil.longToast(context, "更新に失敗しました。\n実績金額と日付を正しく入力して下さい。");
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
        };
    }
}
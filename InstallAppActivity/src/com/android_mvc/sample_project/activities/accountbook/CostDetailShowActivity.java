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
import com.android_mvc.framework.ui.view.MEditText;
import com.android_mvc.framework.ui.view.MLinearLayout;
import com.android_mvc.framework.ui.view.MTextView;
import com.android_mvc.sample_project.R;
import com.android_mvc.sample_project.R.drawable;
import com.android_mvc.sample_project.activities.accountbook.lib.AccountBookAppUserBaseActivity;
import com.android_mvc.sample_project.activities.common.AccordionUtil;
import com.android_mvc.sample_project.common.Util;
import com.android_mvc.sample_project.controller.CostDetailController;
import com.android_mvc.sample_project.db.dao.CostDetailDAO;
import com.android_mvc.sample_project.db.entity.CostDetail;
import com.android_mvc.sample_project.db.schema.ColumnDefinition.CostDetailCol;

/**
 * サンプルのDB参照アクティビティ。
 * 
 * @author id:language_and_engineering
 * 
 */
public class CostDetailShowActivity extends AccountBookAppUserBaseActivity {

    MLinearLayout layout1;
    MTextView tv1;
    MTextView tv2;
    private Calendar LabelYMD;

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

        // 全CostDetailをDBからロード
        CostDetails = new CostDetailDAO(this).findOrderBy(CostDetailCol.BUDGET_YMD);

        // まず親レイアウトを定義
        new UIBuilder(context)
                .setDisplayHeaderText("変動費明細照会")
                .add(
                        layout1 = new MLinearLayout(context)
                                .orientationVertical()
                                .widthMatchParent()
                                .heightWrapContent()
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
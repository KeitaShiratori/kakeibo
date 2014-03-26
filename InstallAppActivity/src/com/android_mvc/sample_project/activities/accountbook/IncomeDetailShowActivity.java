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
import com.android_mvc.sample_project.common.Util;
import com.android_mvc.sample_project.controller.IncomeDetailController;
import com.android_mvc.sample_project.db.dao.IncomeDetailDAO;
import com.android_mvc.sample_project.db.entity.IncomeDetail;

/**
 * サンプルのDB参照アクティビティ。
 * 
 * @author id:language_and_engineering
 * 
 */
public class IncomeDetailShowActivity extends AccountBookAppUserBaseActivity {

    MLinearLayout layout1;
    MTextView tv1;
    private Calendar LabelYMD;

    // 全収入明細のリスト
    List<IncomeDetail> IncomeDetails;

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

        // 全IncomeDetailをDBからロード
        IncomeDetails = new IncomeDetailDAO(this).findOrderByBudgetYmd();
    }

    @Override
    public void defineContentView() {
        final IncomeDetailShowActivity activity = this;

        // まず親レイアウトを定義
        new UIBuilder(context)
                .setDisplayHeaderText("収入明細照会")
                .add(
                        layout1 = new MLinearLayout(context)
                                .orientationVertical()
                                .widthMatchParent()
                                .heightWrapContent()
                )
                .display();

        if (!IncomeDetails.isEmpty()) {
            // 収入明細レコードが取得できた場合、ヘッダ行を表示する
            LabelYMD = IncomeDetails.get(0).getBudgetYmd();
            layout1.add(getLabelYMD(IncomeDetails.get(0).getBudgetYmd(), IncomeDetails));
        }
        else {
            // 収入明細が存在しない場合、メッセージを表示する。
            layout1.add(new MTextView(context)
                    .text("収入明細が存在しません")
                    .textColor(R.color.red)
                    );
        }

        for (final IncomeDetail i : IncomeDetails)
        {
            // 予定日が変わったら空白行を追加して新しいヘッダ行を追加する。LabelYMDを更新する。
            if (!LabelYMD.equals(i.getBudgetYmd())) {
                layout1.add(
                        new MTextView(context)
                                .paddingPx(5)
                        ,
                        getLabelYMD(i.getBudgetYmd(), IncomeDetails));
                LabelYMD = i.getBudgetYmd();
            }

            layout1.add(
                    i.getDescription(activity, context, update(this, i), delete(this, i))
                    );

        }

        // 描画
        layout1.inflateInside();

        // 友達登録操作の直後の場合
        if ($.hasActionResult())
        {
            showRegisteredNewIncomeDetail();
        }
    }

    // 新規登録されたばかりの新しい友達情報を表示
    private void showRegisteredNewIncomeDetail() {
        if ($.actionResultHasKey("new_income_detail"))
        {
            // Intentから情報を取得
            IncomeDetail v = (IncomeDetail) ($.getActionResult().get("new_income_detail"));
        }
    }

    public LinearLayout getLabelYMD(Calendar budgetYmd, List<IncomeDetail> incomeDetails) {

        Integer budgetIncomeSum = 0;
        Integer settleIncomeSum = 0;

        for (IncomeDetail i : incomeDetails) {
            if (i.getBudgetYmd().equals(budgetYmd)) {
                budgetIncomeSum += i.getBudgetIncome();
                settleIncomeSum += i.getEffectiveSettleIncome();
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
                                .text("予定合計\n" + budgetIncomeSum + "円")
                                .backgroundDrawable(R.drawable.header_design)
                        ,
                        new MTextView(this)
                                .gravity(Gravity.CENTER_VERTICAL)
                                .text("実績合計\n" + settleIncomeSum + "円")
                                .backgroundDrawable(R.drawable.header_design)
                );
    }

    public OnClickListener delete(final IncomeDetailShowActivity activity, final IncomeDetail i) {

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
                                    IncomeDetailController.submit(activity, "DELETE_INCOME_DETAIL", i.getId());
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

    public OnClickListener update(final IncomeDetailShowActivity activity, final IncomeDetail i) {
        return new OnClickListener() {

            @Override
            public void onClick(View v) {
                final MEditText et1 = new MEditText(activity)
                        .hint("実績収入")
                        .widthMatchParent();

                final MLinearLayout ll1 = new MLinearLayout(activity)
                        .orientationVertical()
                        .add(et1);
                ll1.inflateInside();

                new AlertDialog.Builder(activity)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setTitle("実際の金額を入力してください。")
                        .setView(ll1)
                        .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                try {
                                    Integer temp1 = Integer.parseInt(et1.text());
                                    i.setSettleIncome(temp1);

                                    // DB更新へ
                                    IncomeDetailController.submit(activity, "UPDATE_INCOME_DETAIL", i);
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
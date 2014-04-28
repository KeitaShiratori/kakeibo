package com.android_mvc.sample_project.activities.accountbook;

import java.util.Calendar;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android_mvc.framework.ui.UIBuilder;
import com.android_mvc.framework.ui.UIUtil;
import com.android_mvc.framework.ui.view.MCalculatorView;
import com.android_mvc.framework.ui.view.MEditText;
import com.android_mvc.framework.ui.view.MGridLayout;
import com.android_mvc.framework.ui.view.MLinearLayout;
import com.android_mvc.framework.ui.view.MTextView;
import com.android_mvc.sample_project.R;
import com.android_mvc.sample_project.activities.accountbook.lib.AccountBookAppUserBaseActivity;
import com.android_mvc.sample_project.common.Util;
import com.android_mvc.sample_project.controller.CostDetailController;
import com.android_mvc.sample_project.controller.IncomeDetailController;
import com.android_mvc.sample_project.db.dao.AccountBookDAO;
import com.android_mvc.sample_project.db.dao.AccountBookDetailDAO;
import com.android_mvc.sample_project.db.dao.IncomeDetailDAO;
import com.android_mvc.sample_project.db.entity.AccountBookDetail;
import com.android_mvc.sample_project.db.entity.CostDetail;
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

    // DAO
    private IncomeDetailDAO incomeDetailDAO;
    private AccountBookDAO accountBookDAO;
    private AccountBookDetailDAO accountBookDetailDAO;

    // 制御用変数
    private Calendar simeLastDay;

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
    }

    @Override
    public void defineContentView() {
        final IncomeDetailShowActivity activity = this;

        // 全IncomeDetailをDBからロード
        incomeDetailDAO = new IncomeDetailDAO(this);
        IncomeDetails = incomeDetailDAO.findOrderByAscBudgetYmd();

        // 締め処理済みの期間の最終日を取得する。
        accountBookDAO = new AccountBookDAO(this);
        accountBookDetailDAO = new AccountBookDetailDAO(this);

        List<AccountBookDetail> accountBookDetails = accountBookDetailDAO.findAll();

        for (int i = accountBookDetails.size() - 1; i >= 0; i--) {
            if (accountBookDetails.get(i).getSimeFlag()) {
                simeLastDay = accountBookDetails.get(i).getMokuhyouMonth();
            }
            break;
        }
        if (simeLastDay != null) {
            simeLastDay.add(Calendar.MONTH, 1);
            simeLastDay.set(Calendar.DAY_OF_MONTH, accountBookDAO.findAll().get(0).getStartDate().get(Calendar.DAY_OF_MONTH));
        }

        layout1 = new MLinearLayout(context)
                .orientationVertical()
                .widthMatchParent()
                .paddingLeftPx(10)
                .heightWrapContent();

        // まず親レイアウトを定義
        new UIBuilder(context)
                .setDisplayHeaderText("収入明細照会")
                .add(
                        layout1
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
                                .paddingPx(1)
                                .textsize(5)
                        ,
                        getLabelYMD(i.getBudgetYmd(), IncomeDetails)
                        );
                LabelYMD = i.getBudgetYmd();
            }

            MGridLayout record = new MGridLayout(activity)
                    .columnCount(3)
                    .rowCount(2)
                    .heightWrapContent()
                    .backgroundDrawable(R.drawable.border);
            MTextView categoryTypeView = i.getCategoryTypeView(this);
            MTextView payTypeVeiw = i.getPayTypeView(this);
            MTextView budgetCostView = i.getBudgetIncomeView(this);
            MTextView settleCostView = i.getSettleIncomeView(this);

            MTextView updateButton = new MTextView(activity)
                    .gravity(Gravity.CENTER_VERTICAL)
                    .text(i.isKurikosi() || isSime(i.getBudgetYmd()) ? "変更不可" : "変更")
                    .backgroundDrawable(i.isKurikosi() || isSime(i.getBudgetYmd()) ? R.drawable.record_design : R.drawable.button_design_1)
                    .click(i.isKurikosi() || isSime(i.getBudgetYmd()) ? null : inputDialogUpdateSettleIncome(i, settleCostView));

            MTextView deleteButton = new MTextView(activity)
                    .gravity(Gravity.CENTER_VERTICAL)
                    .text(i.isKurikosi() || isSime(i.getBudgetYmd()) ? "削除不可" : "削除")
                    .backgroundDrawable(i.isKurikosi() || isSime(i.getBudgetYmd()) ? R.drawable.record_design : R.drawable.button_design_1)
                    .click(i.isKurikosi() || isSime(i.getBudgetYmd()) ? null : delete(this, i));

            record.add(
                    categoryTypeView,
                    budgetCostView,
                    updateButton,
                    payTypeVeiw,
                    settleCostView,
                    deleteButton
                    );

            layout1.add(
                    record
                    );

        }

        // 描画
        layout1.inflateInside();

    }

    private boolean isSime(Calendar target) {
        if (simeLastDay != null) {
            return simeLastDay.after(target);
        }
        return false;
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

    // 月別目標金額を更新するためのイベント
    private OnClickListener inputDialogUpdateSettleIncome(final IncomeDetail i, final MTextView target) {
        return new OnClickListener() {

            @Override
            public void onClick(View v) {
                String title = "実際の金額を入力してください。";
                // 最終目標金額入力用View
                Integer initVal = 0;
                if (target != null && target.text() != null && !target.text().isEmpty()) {
                    try {
                        String tmp = target.text().replace("円", "");
                        tmp = tmp.replace("実績: ", "");
                        tmp = tmp.replace("未入力", "");
                        if (!tmp.isEmpty()) {
                            initVal = Integer.parseInt(tmp);
                        }
                    } catch (NumberFormatException e) {
                        UIUtil.longToast(context, "数値を入力してください");
                        return;
                    }
                }

                createCalculaterDialogWith2Button(IncomeDetailShowActivity.this, title, null, 0,
                        target, initVal, "円", i);
            }
        };
    }

    /**
     * 2つのボタンを持つ電卓ダイアログを表示する。 各ボタンを押した時のイベントはclickに渡す。
     * 
     * @param activity
     * @param title
     * @param content
     * @param icon
     * @param click
     * @return
     */
    public static Builder createCalculaterDialogWith2Button(final IncomeDetailShowActivity activity, String title, String content, int icon,
            final TextView v, Integer initVal, final String valUnit, final IncomeDetail i) {

        AlertDialog.Builder ret = new AlertDialog.Builder(activity);

        final MCalculatorView calc = new MCalculatorView(activity, null, initVal);

        // ダイアログの設定
        ret.setTitle(title); // タイトル
        ret.setMessage(content); // 内容
        ret.setIcon(icon); // アイコン設定
        ret.setView(calc);

        ret.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                v.setText("実績： " + calc.getValue() + valUnit);
                i.setSettleIncome(Integer.parseInt((String) calc.getValue()));
                // DB更新へ
                IncomeDetailController.submit(activity, "UPDATE_INCOME_DETAIL", i);
            }
        });

        ret.setNegativeButton("キャンセル", null);

        calc.inflateInside();
        ret.create();
        ret.show();

        return ret;
    }

}
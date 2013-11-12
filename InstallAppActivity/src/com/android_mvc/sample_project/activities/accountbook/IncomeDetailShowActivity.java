package com.android_mvc.sample_project.activities.accountbook;

import java.util.Calendar;
import java.util.List;

import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.android_mvc.framework.activities.base.BaseNormalActivity;
import com.android_mvc.framework.ui.UIBuilder;
import com.android_mvc.framework.ui.view.MButton;
import com.android_mvc.framework.ui.view.MLinearLayout;
import com.android_mvc.framework.ui.view.MTextView;
import com.android_mvc.sample_project.R.drawable;
import com.android_mvc.sample_project.common.Util;
import com.android_mvc.sample_project.controller.FuncDBController;
import com.android_mvc.sample_project.db.dao.IncomeDetailDAO;
import com.android_mvc.sample_project.db.entity.IncomeDetail;

/**
 * サンプルのDB参照アクティビティ。
 * 
 * @author id:language_and_engineering
 * 
 */
public class IncomeDetailShowActivity extends BaseNormalActivity {

    MLinearLayout layout1;
    MTextView tv1;
    MTextView tv2;
    private Calendar LabelYMD;

    // 全友達のリスト
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
        IncomeDetails = new IncomeDetailDAO(this).findAll();
    }

    @Override
    public void defineContentView() {
        final IncomeDetailShowActivity activity = this;

        // まず親レイアウトを定義
        new UIBuilder(context)
                .add(
                        layout1 = new MLinearLayout(context)
                                .orientationVertical()
                                .widthMatchParent()
                                .heightWrapContent()
                                .add(

                                        tv1 = new MTextView(context)
                                                .text("ここにDBの中身が列挙されます。")
                                                .widthWrapContent()
                                                .paddingPx(10)
                                        ,

                                        tv2 = new MTextView(context)
                                                .invisible()
                                                .textColor(Color.RED)
                                                .widthWrapContent()
                                                .paddingPx(10)

                                )
                )
                .display();

        // 変動費明細レコードが取得できた場合、ヘッダ行を表示する
        if (!IncomeDetails.isEmpty()) {
            LabelYMD = IncomeDetails.get(0).getBudgetYmd();
            layout1.add(getLabelYMD(IncomeDetails.get(0).getBudgetYmd(), IncomeDetails));
        }

        // レイアウト内に動的に全友達の情報を表示。Adapterは不要。
        for (final IncomeDetail v : IncomeDetails)
        {
            // ※↑friendをfinal宣言してるのは，Clickイベントの中から参照する事が目的

            if (!LabelYMD.equals(v.getBudgetYmd())) {
                layout1.add(getLabelYMD(v.getBudgetYmd(), IncomeDetails));
                LabelYMD = v.getBudgetYmd();
            }

            layout1.add(
                    new MLinearLayout(activity)
                            .orientationHorizontal()
                            .widthFillParent()
                            .heightWrapContent()
                            .backgroundDrawable(drawable.border)
                            .add(

                                    v.getDescription(activity)
                                            .backgroundColor(com.android_mvc.sample_project.R.color.snow)
                                    ,
                                    new MButton(context)
                                            .backgroundDrawable(drawable.button_design)
                                            .backgroundDrawable(drawable.edit)
                                            .click(new OnClickListener() {

                                                @Override
                                                public void onClick(View v) {
                                                    // DBから削除へ
                                                    // FuncDBController.submit(activity,
                                                    // "DELETE_FRIEND",
                                                    // v.getId());
                                                }

                                            }))

                    );
        }

        // 画面遷移ボタン
        layout1.add(
                new MButton(context)
                        .text("収入明細DBに登録する")
                        .click(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                FuncDBController.submit(activity, "EDIT_VRIALBEL_INCOME_DETAIL", null);
                            }

                        })
                ,
                new MButton(context)
                        .text("トップに戻る")
                        .click(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                FuncDBController.submit(activity, "BACK_TO_TOP", null);
                            }

                        })
                );

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

            // UIに表示
            tv2.text("たった今新規登録されました。").visible();
        }
    }

    public LinearLayout getLabelYMD(Calendar budgetYmd, List<IncomeDetail> incomeDetails) {

        Integer budgetIncomeSum = 0;
        Integer settleIncomeSum = 0;

        for (IncomeDetail c : incomeDetails) {
            if (c.getBudgetYmd().equals(budgetYmd)) {
                budgetIncomeSum += c.getBudgetIncome();
                settleIncomeSum += c.getEffectiveSettleIncome();
            }
        }
        // 使用予定日のラベルを１個追加
        return new MLinearLayout(this)
                .orientationHorizontal()
                .widthFillParent()
                .heightWrapContent()
                .weight(6)
                .paddingPx(10)
                .add(

                        new MTextView(this)
                                .text(budgetYmd.get(Calendar.YEAR) + "/"
                                        + (budgetYmd.get(Calendar.MONTH) + 1) + "/"
                                        + budgetYmd.get(Calendar.DAY_OF_MONTH))
                                .widthWrapContent()
                                .paddingPx(10)
                        ,
                        new MTextView(this)
                                .text("予定合計: " + budgetIncomeSum + "円")
                                .widthWrapContent()
                                .paddingPx(10)
                        ,
                        new MTextView(this)
                                .text("実績合計: " + settleIncomeSum + "円")
                                .widthWrapContent()
                                .paddingPx(10)

                );
    }

}
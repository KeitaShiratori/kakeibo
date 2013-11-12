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
import com.android_mvc.sample_project.R;
import com.android_mvc.sample_project.R.drawable;
import com.android_mvc.sample_project.R.style;
import com.android_mvc.sample_project.common.Util;
import com.android_mvc.sample_project.controller.FuncDBController;
import com.android_mvc.sample_project.db.dao.CostDetailDAO;
import com.android_mvc.sample_project.db.entity.CostDetail;
import com.android_mvc.sample_project.db.schema.ColumnDefinition.CostDetailCol;

/**
 * サンプルのDB参照アクティビティ。
 * 
 * @author id:language_and_engineering
 * 
 */
public class CostDetailShowActivity extends BaseNormalActivity {

    MLinearLayout layout1;
    MTextView tv1;
    MTextView tv2;
    private Calendar LabelYMD;

    // 全友達のリスト
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

        // 全CostDetailをDBからロード
        CostDetails = new CostDetailDAO(this).findOrderBy(CostDetailCol.BUDGET_YMD);
    }

    @Override
    public void defineContentView() {
        final CostDetailShowActivity activity = this;

        // まず親レイアウトを定義
        new UIBuilder(context)
                .add(
                        layout1 = new MLinearLayout(context)
                                .orientationVertical()
                                .widthMatchParent()
                                .heightWrapContent()
                                .add(
                                        tv1 = new MTextView(context, null, R.attr.text_view_style_h1)
                                                    .text("支出明細")
                                )
                )
                .display();

        // 変動費明細レコードが取得できた場合、ヘッダ行を表示する
        if (!CostDetails.isEmpty()) {
            LabelYMD = CostDetails.get(0).getBudgetYmd();
            layout1.add(getLabelYMD(CostDetails.get(0).getBudgetYmd(), CostDetails));
        }

        // レイアウト内に動的に全友達の情報を表示。Adapterは不要。
        for (final CostDetail v : CostDetails)
        {
            // ※↑friendをfinal宣言してるのは，Clickイベントの中から参照する事が目的

            if (!LabelYMD.equals(v.getBudgetYmd())) {
                layout1.add(getLabelYMD(v.getBudgetYmd(), CostDetails));
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
                        .text("変動費DBに登録する")
                        .click(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                FuncDBController.submit(activity, "EDIT_COST_DETAIL", null);
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
                                .text("予定合計: " + budgetCostSum + "円")
                                .widthWrapContent()
                                .paddingPx(10)
                        ,
                        new MTextView(this)
                                .text("実績合計: " + settleCostSum + "円")
                                .widthWrapContent()
                                .paddingPx(10)

                );
    }
}
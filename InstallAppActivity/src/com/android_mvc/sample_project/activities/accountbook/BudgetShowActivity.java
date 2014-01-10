package com.android_mvc.sample_project.activities.accountbook;

import java.util.Calendar;
import java.util.List;

import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;

import com.android_mvc.framework.ui.UIBuilder;
import com.android_mvc.framework.ui.UIUtil;
import com.android_mvc.framework.ui.view.MLinearLayout;
import com.android_mvc.framework.ui.view.MTextView;
import com.android_mvc.sample_project.R;
import com.android_mvc.sample_project.activities.accountbook.data.BudgetRecordData;
import com.android_mvc.sample_project.activities.accountbook.data.BudgetShowActivityData;
import com.android_mvc.sample_project.activities.accountbook.lib.AccountBookAppUserBaseActivity;
import com.android_mvc.sample_project.common.Util;
import com.android_mvc.sample_project.db.dao.CostDetailDAO;
import com.android_mvc.sample_project.db.entity.CostDetail;
import com.android_mvc.sample_project.db.schema.ColumnDefinition.CostDetailCol;

/**
 * サンプルのDB参照アクティビティ。
 * 
 * @author id:language_and_engineering
 * 
 */
public class BudgetShowActivity extends AccountBookAppUserBaseActivity {

    // View定義
    MLinearLayout layout1;
    MTextView tv1;
    MTextView tv2;

    // メンバ変数
    BudgetShowActivityData budgetShowActivityData;

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

        // データの初期化
        budgetShowActivityData = new BudgetShowActivityData();
        budgetShowActivityData.init(this);

    }

    @Override
    public void defineContentView() {
        final BudgetShowActivity activity = this;

        // まず親レイアウトを定義
        new UIBuilder(context)
                .setDisplayHeaderText("予定照会")
                .add(layout1 = new MLinearLayout(activity)
                        .orientationVertical()
                        .widthFillParent()
                        .heightWrapContent())
                .display();

        // 本日の予定金額表示ボタン
        layout1.add(new MTextView(context)
                .text("本日の残り金額表示")
                .gravity(Gravity.CENTER_VERTICAL)
                .backgroundResource(R.drawable.button_design_1)
                .widthMatchParent()
                .click(showTodayYoteiKingaku(budgetShowActivityData))
                );

        for (BudgetRecordData b : budgetShowActivityData.getBudgetRecordData()) {
            layout1.add(
                    b.getDescription(context)
                    );
        }

        // 描画
        layout1.inflateInside();
    }

    private OnClickListener showTodayYoteiKingaku(final BudgetShowActivityData budgetShowActivityData) {
        return new OnClickListener() {

            final Calendar today = Calendar.getInstance();

            @Override
            public void onClick(View v) {
                // 全CostDetailをDBからロード
                List<CostDetail> costDetails = new CostDetailDAO(context).findOrderBy(CostDetailCol.BUDGET_YMD);

                Integer budgetCostSum = 0;
                Integer settleCostSum = 0;
                Integer kasyobun = 0;
                Integer result = 0;

                for (CostDetail c : costDetails) {
                    if (c.getBudgetYmd().get(Calendar.YEAR) == today.get(Calendar.YEAR)
                            && c.getBudgetYmd().get(Calendar.MONTH) == today.get(Calendar.MONTH)
                            && c.getBudgetYmd().get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH)) {
                        budgetCostSum += c.getBudgetCost();
                        settleCostSum += c.getEffectiveSettleCost();
                    }
                }

                for (BudgetRecordData b : budgetShowActivityData.getBudgetRecordData()) {
                    // 当月の可処分所得を使って計算する
                    if (b.getYoteiYYYYMM().get(Calendar.YEAR) == today.get(Calendar.YEAR)
                            && b.getYoteiYYYYMM().get(Calendar.MONTH) == today.get(Calendar.MONTH)) {
                        // 可処分所得を計算し、当月の残り日数で日割りする。
                        kasyobun = (b.getIncomeSum() - b.getMokuhyouKingaku() - b.getCostSum());
                        kasyobun = kasyobun / (today.getActualMaximum(Calendar.DAY_OF_MONTH) - today.get(Calendar.DAY_OF_MONTH) + 1);
                        break;
                    }
                }

                result = kasyobun + budgetCostSum - settleCostSum;
                UIUtil.longToast(context, "本日の残り金額は" + result + "円です。"
                        + "\n\n内訳"
                        + "\n使用予定金額合計: " + budgetCostSum + "円"
                        + "\n使用済み金額合計: " + settleCostSum + "円"
                        + "\n残り金額（日割り）: " + kasyobun + "円");

            }
        };
    }
}
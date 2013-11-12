package com.android_mvc.sample_project.controller;

import com.android_mvc.framework.controller.BaseController;
import com.android_mvc.framework.controller.ControlFlowDetail;
import com.android_mvc.framework.controller.action.ActionResult;
import com.android_mvc.framework.controller.action.BLExecutor;
import com.android_mvc.framework.controller.routing.Router;
import com.android_mvc.framework.controller.routing.RoutingTable;
import com.android_mvc.sample_project.activities.accountbook.AccountBookShowActivity;
import com.android_mvc.sample_project.activities.accountbook.BudgetShowActivity;
import com.android_mvc.sample_project.activities.accountbook.CostDetailEditActivity;
import com.android_mvc.sample_project.activities.accountbook.CostDetailShowActivity;
import com.android_mvc.sample_project.activities.main.TopActivity;
import com.android_mvc.sample_project.db.entity.AccountBookDetail;
import com.android_mvc.sample_project.domain.AccountBookShowAction;

/**
 * 家計簿表示画面のコントローラ。
 * 
 * @author id:language_and_engineering
 * 
 */
public class AccountBookShowController extends BaseController
{

    /**
     * DB登録画面からの遷移時
     * 
     * @param abd
     */
    public static void submit(final AccountBookShowActivity activity, final AccountBookDetail abd)
    {
        new ControlFlowDetail<AccountBookShowActivity>(activity)
                .setBL(new BLExecutor() {
                    @Override
                    public ActionResult doAction()
                    {
                        // BL
                        return new AccountBookShowAction(activity, abd).exec();

                    }
                })
                .onBLExecuted(
                        // BL実行後の遷移先の一覧
                        new RoutingTable().map("success", AccountBookShowActivity.class)

                // onBLExecutedにこれを渡せば，BLの実行結果にかかわらず画面遷移を常に抑止。
                // STAY_THIS_PAGE_ALWAYS

                // BL実行結果が特定の状況のときのみ，画面遷移を抑止することも可能。
                // new RoutingTable().map("success", STAY_THIS_PAGE )

                )
                .setDialogText("お待ちください")
                .startControl();
        ;
    }

    /**
     * 家計簿照会画面からの遷移時
     */
    public static void submit(final AccountBookShowActivity activity, String action_type, final Long account_book_id)
    {
        Router.goByRoutingTable(activity, action_type,
                new RoutingTable()
                        .map("BACK_TO_TOP", TopActivity.class, "トップ画面へ")
                        .map("EDIT_COST_DETAIL", CostDetailEditActivity.class, "変動費一覧画面へ")
                        .map("SHOW_COST_DETAIL", CostDetailShowActivity.class, "変動費一覧画面へ")
                        .map("SHOW_BUDGET_SHOW", BudgetShowActivity.class, "予定表示へ")
                );
    }

}

package com.android_mvc.sample_project.controller.common;

import android.app.Activity;

import com.android_mvc.framework.controller.BaseController;
import com.android_mvc.framework.controller.ControlFlowDetail;
import com.android_mvc.framework.controller.action.ActionResult;
import com.android_mvc.framework.controller.action.BLExecutor;
import com.android_mvc.framework.controller.routing.Router;
import com.android_mvc.framework.controller.routing.RoutingTable;
import com.android_mvc.framework.controller.validation.ValidationExecutor;
import com.android_mvc.framework.controller.validation.ValidationResult;
import com.android_mvc.sample_project.activities.accountbook.AccountBookShowActivity;
import com.android_mvc.sample_project.activities.accountbook.BudgetShowActivity;
import com.android_mvc.sample_project.activities.accountbook.CostDetailEditActivity;
import com.android_mvc.sample_project.activities.accountbook.CostDetailShowActivity;
import com.android_mvc.sample_project.activities.accountbook.IncomeDetailEditActivity;
import com.android_mvc.sample_project.activities.accountbook.IncomeDetailShowActivity;
import com.android_mvc.sample_project.activities.accountbook.SettleShowActivity;
import com.android_mvc.sample_project.activities.main.TopActivity;
import com.android_mvc.sample_project.controller.FuncDBValidation;
import com.android_mvc.sample_project.domain.CostDetailEditAction;
import com.android_mvc.sample_project.domain.IncomeDetailEditAction;

/**
 * DB操作系画面のコントローラ。
 * 
 * @author id:language_and_engineering
 * 
 */
public class CommonController extends BaseController
{

    /**
     * DB登録画面からの遷移時
     */
    public static void submit(final Activity activity, String button_type)
    {
        Router.goByRoutingTable(activity, button_type,
                new RoutingTable()
                    .map("SHOW_ACCOUNT_BOOK", AccountBookShowActivity.class, "目標金額照会へ")
                    .map("SHOW_BUDGET_SHOW", BudgetShowActivity.class, "予定表示へ")
                    .map("SHOW_SETTLE_SHOW", SettleShowActivity.class, "実績表示へ")
            );

    }


}

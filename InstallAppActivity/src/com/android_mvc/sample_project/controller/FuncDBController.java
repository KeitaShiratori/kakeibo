package com.android_mvc.sample_project.controller;

import com.android_mvc.framework.controller.BaseController;
import com.android_mvc.framework.controller.ControlFlowDetail;
import com.android_mvc.framework.controller.action.ActionResult;
import com.android_mvc.framework.controller.action.BLExecutor;
import com.android_mvc.framework.controller.routing.Router;
import com.android_mvc.framework.controller.routing.RoutingTable;
import com.android_mvc.framework.controller.routing.TabContentMapping;
import com.android_mvc.framework.controller.validation.ValidationExecutor;
import com.android_mvc.framework.controller.validation.ValidationResult;
import com.android_mvc.sample_project.activities.accountbook.AnalysisTabHostActivity;
import com.android_mvc.sample_project.activities.accountbook.BudgetShowActivity;
import com.android_mvc.sample_project.activities.accountbook.CostDetailEditActivity;
import com.android_mvc.sample_project.activities.accountbook.CostDetailShowActivity;
import com.android_mvc.sample_project.activities.accountbook.EditTabHostActivity;
import com.android_mvc.sample_project.activities.accountbook.IncomeDetailEditActivity;
import com.android_mvc.sample_project.activities.accountbook.IncomeDetailShowActivity;
import com.android_mvc.sample_project.activities.accountbook.SettleShowActivity;
import com.android_mvc.sample_project.activities.accountbook.ShowTabHostActivity;
import com.android_mvc.sample_project.activities.main.TopActivity;
import com.android_mvc.sample_project.domain.CostDetailEditAction;
import com.android_mvc.sample_project.domain.IncomeDetailEditAction;

/**
 * DB操作系画面のコントローラ。
 * 
 * @author id:language_and_engineering
 * 
 */
public class FuncDBController extends BaseController
{

    /**
     * DB登録画面からの遷移時
     */
    public static void submit(final CostDetailEditActivity activity)
    {
        new ControlFlowDetail<CostDetailEditActivity>(activity)
                .setValidation(new ValidationExecutor() {
                    @Override
                    public ValidationResult doValidate()
                    {
                        // バリデーション処理
                        return new FuncDBValidation().validate(activity);
                    }

                    @Override
                    public void onValidationFailed()
                    {
                        showErrMessages();

                        // バリデーション失敗時の遷移先
                        // goOnValidationFailed( CostDetailEditActivity.class );
                        stayInThisPage();
                    }
                })
                .setBL(new BLExecutor() {
                    @Override
                    public ActionResult doAction()
                    {
                        // BL
                        return new CostDetailEditAction(activity).exec();
                    }
                })
                .onBLExecuted(
                        // BL実行後の遷移先の一覧
                        new RoutingTable().map("success", CostDetailShowActivity.class)

                // onBLExecutedにこれを渡せば，BLの実行結果にかかわらず画面遷移を常に抑止。
                // STAY_THIS_PAGE_ALWAYS

                // BL実行結果が特定の状況のときのみ，画面遷移を抑止することも可能。
                // new RoutingTable().map("success", STAY_THIS_PAGE )

                )
                .setDialogText("お待ちください")
                .startControl();
        ;

    }

    public static void submit(final IncomeDetailEditActivity activity)
    {
        new ControlFlowDetail<IncomeDetailEditActivity>(activity)
                .setValidation(new ValidationExecutor() {
                    @Override
                    public ValidationResult doValidate()
                    {
                        // バリデーション処理
                        return new FuncDBValidation().validate(activity);
                    }

                    @Override
                    public void onValidationFailed()
                    {
                        showErrMessages();

                        // バリデーション失敗時の遷移先
                        // goOnValidationFailed( CostDetailEditActivity.class );
                        stayInThisPage();
                    }
                })
                .setBL(new BLExecutor() {
                    @Override
                    public ActionResult doAction()
                    {
                        // BL
                        return new IncomeDetailEditAction(activity).exec();
                    }
                })
                .onBLExecuted(
                        // BL実行後の遷移先の一覧
                        new RoutingTable().map("success", IncomeDetailShowActivity.class)

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
     * DB参照画面からの遷移時
     */
    public static void submit(final CostDetailShowActivity activity, String action_type, final Long cost_detail_id)
    {
        if ("BACK_TO_TOP".equals(action_type))
        {
            // TOPに戻る
            Router.go(activity, TopActivity.class);
        } else if ("EDIT_COST_DETAIL".equals(action_type)) {
            // 変動費明細登録画面に遷移する。
            Router.go(activity, CostDetailEditActivity.class);
        }

    }

    /**
     * DB参照画面からの遷移時
     */
    public static void submit(final IncomeDetailShowActivity activity, String action_type, final Long income_detail_id)
    {
        if ("BACK_TO_TOP".equals(action_type))
        {
            // TOPに戻る
            Router.go(activity, TopActivity.class);
        } else if ("EDIT_VRIALBEL_INCOME_DETAIL".equals(action_type)) {
            // 変動費明細登録画面に遷移する。
            Router.go(activity, IncomeDetailEditActivity.class);
        }

    }

    /**
     * タブ親登録画面から呼び出される子画面のリスト
     */
    public static TabContentMapping getChildActivities(EditTabHostActivity activity)
    {
        // タブのタグ文字列に対応するアクティビティを指定する。
        return new TabContentMapping()
            .add( "EDIT_COST_DETAIL", CostDetailEditActivity.class )
            .add( "EDIT_INCOME_DETAIL", IncomeDetailEditActivity.class )
        ;
    }

    /**
     * タブ親照会画面から呼び出される子画面のリスト
     */
    public static TabContentMapping getChildActivities(ShowTabHostActivity activity)
    {
        // タブのタグ文字列に対応するアクティビティを指定する。
        return new TabContentMapping()
            .add( "SHOW_COST_DETAIL", CostDetailShowActivity.class )
            .add( "SHOW_INCOME_DETAIL", IncomeDetailShowActivity.class )
        ;
    }
    /**
     * タブ親分析画面から呼び出される子画面のリスト
     */
    public static TabContentMapping getChildActivities(AnalysisTabHostActivity activity)
    {
        // タブのタグ文字列に対応するアクティビティを指定する。
        return new TabContentMapping()
            .add( "SHOW_BUDGET_SHOW", BudgetShowActivity.class )
            .add( "SHOW_SETTLE_SHOW", SettleShowActivity.class )
        ;
    }

}

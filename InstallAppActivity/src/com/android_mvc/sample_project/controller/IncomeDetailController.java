package com.android_mvc.sample_project.controller;

import com.android_mvc.framework.controller.BaseController;
import com.android_mvc.framework.controller.ControlFlowDetail;
import com.android_mvc.framework.controller.action.ActionResult;
import com.android_mvc.framework.controller.action.BLExecutor;
import com.android_mvc.framework.controller.routing.Router;
import com.android_mvc.framework.controller.routing.RoutingTable;
import com.android_mvc.framework.controller.validation.ValidationExecutor;
import com.android_mvc.framework.controller.validation.ValidationResult;
import com.android_mvc.sample_project.activities.accountbook.IncomeDetailEditActivity;
import com.android_mvc.sample_project.activities.accountbook.IncomeDetailShowActivity;
import com.android_mvc.sample_project.activities.accountbook.ShowTabHostActivity;
import com.android_mvc.sample_project.activities.main.TopActivity;
import com.android_mvc.sample_project.db.entity.IncomeDetail;
import com.android_mvc.sample_project.domain.IncomeDetailDeleteAction;
import com.android_mvc.sample_project.domain.IncomeDetailEditAction;
import com.android_mvc.sample_project.domain.IncomeDetailUpdateAction;

/**
 * DB操作系画面のコントローラ。
 * 
 * @author id:language_and_engineering
 * 
 */
public class IncomeDetailController extends BaseController
{

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
                        // バリデーション失敗時の処理
                        showErrMessages();
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
                        new RoutingTable().map("success", ShowTabHostActivity.class)

                )
                .setDialogText("お待ちください")
                .startControl();
        ;

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
        } else if ("EDIT_INCOME_DETAIL".equals(action_type)) {
            // 変動費明細登録画面に遷移する。
            Router.go(activity, IncomeDetailEditActivity.class);
        }
        else if ("DELETE_INCOME_DETAIL".equals(action_type))
        {
            // DBから削除
            new ControlFlowDetail<IncomeDetailShowActivity>(activity)
                    .setBL(new BLExecutor() {
                        @Override
                        public ActionResult doAction()
                        {
                            return new IncomeDetailDeleteAction(activity, income_detail_id).exec();
                        }
                    })
                    .onBLExecuted(
                            new RoutingTable().map("success", ShowTabHostActivity.class)
                    )
                    .startControl();
            ;
        }

    }

    public static void submit(final IncomeDetailShowActivity activity, String action_type, final IncomeDetail income_detail)
    {
        if ("UPDATE_INCOME_DETAIL".equals(action_type))
        {
            // DB更新
            new ControlFlowDetail<IncomeDetailShowActivity>(activity)
                    .setBL(new BLExecutor() {
                        @Override
                        public ActionResult doAction()
                        {
                            return new IncomeDetailUpdateAction(activity, income_detail).exec();
                        }
                    })
                    .onBLExecuted(
                            new RoutingTable().map("success", ShowTabHostActivity.class)
                    )
                    .startControl();
            ;
        }
    }

}

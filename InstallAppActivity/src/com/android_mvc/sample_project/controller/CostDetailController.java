package com.android_mvc.sample_project.controller;

import java.util.Calendar;

import android.content.Intent;

import com.android_mvc.framework.controller.BaseController;
import com.android_mvc.framework.controller.ControlFlowDetail;
import com.android_mvc.framework.controller.action.ActionResult;
import com.android_mvc.framework.controller.action.BLExecutor;
import com.android_mvc.framework.controller.routing.Router;
import com.android_mvc.framework.controller.routing.RoutingTable;
import com.android_mvc.framework.controller.validation.ValidationExecutor;
import com.android_mvc.framework.controller.validation.ValidationResult;
import com.android_mvc.sample_project.activities.accountbook.CostDetailEditActivity;
import com.android_mvc.sample_project.activities.accountbook.CostDetailShowActivity;
import com.android_mvc.sample_project.activities.accountbook.ShowTabHostActivity;
import com.android_mvc.sample_project.activities.main.TopActivity;
import com.android_mvc.sample_project.db.entity.CostDetail;
import com.android_mvc.sample_project.domain.CostDetailDeleteAction;
import com.android_mvc.sample_project.domain.CostDetailEditAction;
import com.android_mvc.sample_project.domain.CostDetailUpdateAction;

/**
 * DB操作系画面のコントローラ。
 * 
 * 
 * @author id:language_and_engineering
 * 
 */
public class CostDetailController extends BaseController
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
                        return new CostDetailEditAction(activity).exec();
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
        else if ("DELETE_COST_DETAIL".equals(action_type))
        {
            // DBから削除
            new ControlFlowDetail<CostDetailShowActivity>(activity)
                    .setBL(new BLExecutor() {
                        @Override
                        public ActionResult doAction()
                        {
                            return new CostDetailDeleteAction(activity, cost_detail_id).exec();
                        }
                    })
                    .onBLExecuted(
                            new RoutingTable().map("success", ShowTabHostActivity.class)
                    )
                    .startControl();
            ;
        }

    }

    /**
     * 支出照会画面でモード変更時の画面遷移
     * 
     */
    public static void submit(final CostDetailShowActivity activity, String btnMode, Calendar startDate) {
        if (btnMode.equals("DAY_MODE")
                || btnMode.equals("WEEK_MODE")
                || btnMode.equals("MONTH_MODE")){
            Router.goWithData(activity, ShowTabHostActivity.class, "登録画面へ",
                    new Intent()
                            .putExtra("FIRST_TAB", "SHOW_COST_DETAIL")
                            .putExtra("MODE", btnMode)
                            .putExtra("START_DATE", startDate)
                    );
        }
    }

    public static void submit(final CostDetailShowActivity activity, String action_type, final CostDetail cost_detail)
    {
        if ("UPDATE_COST_DETAIL".equals(action_type))
        {
            // DB更新
            new ControlFlowDetail<CostDetailShowActivity>(activity)
                    .setBL(new BLExecutor() {
                        @Override
                        public ActionResult doAction()
                        {
                            return new CostDetailUpdateAction(activity, cost_detail).exec();
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

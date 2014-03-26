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
import com.android_mvc.sample_project.R;
import com.android_mvc.sample_project.activities.accountbook.MyWalletShowActivity;
import com.android_mvc.sample_project.activities.accountbook.ShowTabHostActivity;
import com.android_mvc.sample_project.common.Util;
import com.android_mvc.sample_project.domain.CostDetailEditAction;
import com.android_mvc.sample_project.domain.MyWalletAction;

/**
 * DB操作系画面のコントローラ。
 * 
 * 
 * @author id:language_and_engineering
 * 
 */
public class MyWalletController extends BaseController
{
    /**
     * 財布の中身画面からの遷移時（財布の中身登録）
     */
    public static void submit(final MyWalletShowActivity activity)
    {
        new ControlFlowDetail<MyWalletShowActivity>(activity)
                .setValidation(new ValidationExecutor() {
                    @Override
                    public ValidationResult doValidate()
                    {
                        // バリデーション処理
                        return new MyWalletValidation().validate(activity);
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
                        return new MyWalletAction(activity).exec();
                    }
                })
                .onBLExecuted(
                        // BL実行後の遷移先の一覧
                        new RoutingTable().map("success", MyWalletShowActivity.class)
                )
                // .setDialogText("お待ちください")
                .startControl();
        ;

    }

    /**
     * 財布の中身画面からの遷移時（変動費明細登録）
     */
    public static void autoInputCostDetail(final MyWalletShowActivity activity)
    {
        new ControlFlowDetail<MyWalletShowActivity>(activity)
                .setValidation(new ValidationExecutor() {
                    @Override
                    public ValidationResult doValidate()
                    {
                        // バリデーション処理
                        return new MyWalletValidation().validateCostDetail(activity);
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
                        new RoutingTable().map("success", MyWalletShowActivity.class)
                )
                // .setDialogText("お待ちください")
                .startControl();
        ;

    }

    /**
     * 財布の中身画面でモード変更時の画面遷移
     * 
     */
    public static void submit(final MyWalletShowActivity activity, String btnMode, Calendar startDate) {
        if (btnMode.equals("DAY_MODE")
                || btnMode.equals("WEEK_MODE")
                || btnMode.equals("MONTH_MODE")) {
            Router.goWithData(activity, ShowTabHostActivity.class, "財布の中身画面へ",
                    new Intent()
                            .putExtra(Util._(activity, R.string.FIRST_TAB),
                                    Util._(activity, R.string.MYWALLET))
                            .putExtra(Util._(activity, R.string.MODE), btnMode)
                            .putExtra(Util._(activity, R.string.START_DATE), startDate)
                    );
        }
    }

    public static void submitToCostShow(final MyWalletShowActivity activity, String btnMode, Calendar startDate) {
        if (btnMode.equals(Util._(activity, R.string.DAY_MODE))) {
            Router.goWithData(activity, ShowTabHostActivity.class, "変動費照会画面へ",
                    new Intent()
                            .putExtra(Util._(activity, R.string.FIRST_TAB),
                                    Util._(activity, R.string.SHOW_COST_DETAIL))
                            .putExtra(Util._(activity, R.string.MODE), btnMode)
                            .putExtra(Util._(activity, R.string.START_DATE), startDate)
                    );
        }
    }

}

package com.android_mvc.sample_project.controller;

import com.android_mvc.framework.controller.BaseController;
import com.android_mvc.framework.controller.ControlFlowDetail;
import com.android_mvc.framework.controller.action.ActionResult;
import com.android_mvc.framework.controller.action.BLExecutor;
import com.android_mvc.framework.controller.routing.RoutingTable;
import com.android_mvc.sample_project.activities.accountbook.CreditCardActivity;
import com.android_mvc.sample_project.domain.CreditCardAction;

/**
 * DB操作系画面のコントローラ。
 * 
 * 
 * @author id:language_and_engineering
 * 
 */
public class CreditCardController extends BaseController
{
    /**
     * DB登録画面からの遷移時
     */
    public static void submit(final CreditCardActivity activity)
    {
        new ControlFlowDetail<CreditCardActivity>(activity)
//                .setValidation(new ValidationExecutor() {
//                    @Override
//                    public ValidationResult doValidate()
//                    {
//                        // バリデーション処理
//                        return new MyWalletValidation().validate(activity);
//                    }
//
//                    @Override
//                    public void onValidationFailed()
//                    {
//                        // バリデーション失敗時の処理
//                        showErrMessages();
//                        stayInThisPage();
//                    }
//                })
                .setBL(new BLExecutor() {
                    @Override
                    public ActionResult doAction()
                    {
                        // BL
                        return new CreditCardAction(activity).exec();
                    }
                })
                .onBLExecuted(
                        // BL実行後の遷移先の一覧
                        new RoutingTable().map("success", CreditCardActivity.class)
                )
//                .setDialogText("お待ちください")
                .startControl();
        ;

    }

}

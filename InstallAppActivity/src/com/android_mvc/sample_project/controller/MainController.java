package com.android_mvc.sample_project.controller;

import android.content.Intent;

import com.android_mvc.framework.controller.BaseController;
import com.android_mvc.framework.controller.ControlFlowDetail;
import com.android_mvc.framework.controller.action.ActionResult;
import com.android_mvc.framework.controller.action.BLExecutor;
import com.android_mvc.framework.controller.routing.Router;
import com.android_mvc.framework.controller.routing.RoutingTable;
import com.android_mvc.framework.controller.validation.ValidationExecutor;
import com.android_mvc.framework.controller.validation.ValidationResult;
import com.android_mvc.sample_project.activities.accountbook.AccountBookShowActivity;
import com.android_mvc.sample_project.activities.accountbook.AnalysisTabHostActivity;
import com.android_mvc.sample_project.activities.accountbook.EditTabHostActivity;
import com.android_mvc.sample_project.activities.accountbook.ShowTabHostActivity;
import com.android_mvc.sample_project.activities.installation.InstallAppActivity;
import com.android_mvc.sample_project.activities.installation.InstallCompletedActivity;
import com.android_mvc.sample_project.activities.main.TopActivity;
import com.android_mvc.sample_project.domain.AccountBookEditAction;

/**
 * メイン系画面のコントローラ。
 * 
 * @author id:language_and_engineering
 * 
 */
public class MainController extends BaseController
{

    // 遷移元となるActivityごとに，submit()をオーバーロードする。

    /**
     * インストール画面からの遷移時
     */
    public static void submit(InstallAppActivity installAppActivity, boolean installExecutedFlag)
    {
        // インストールをスキップしたかどうか
        if (installExecutedFlag)
        {
            // インストール完了画面へ
            Router.go(installAppActivity, InstallCompletedActivity.class);
        }
        else
        {
            // トップ画面へ
            Router.go(installAppActivity, TopActivity.class);
        }
    }

    /**
     * インストール完了画面からの遷移時
     */
    public static void submit(final InstallCompletedActivity activity)
    {
        new ControlFlowDetail<InstallCompletedActivity>(activity)
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
                        return new AccountBookEditAction(activity).exec();
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
     * TOP画面からの遷移時
     */
    public static void submit(TopActivity activity, String button_type) {

        // 登録画面へ遷移
        if (button_type.equals("EDIT_COST_DETAIL")
                || button_type.equals("EDIT_INCOME_DETAIL")) {
            Router.goWithData(activity, EditTabHostActivity.class, "登録画面へ",
                    new Intent().putExtra("FIRST_TAB", button_type)
                    );
        }
        // 照会画面へ遷移
        else if (button_type.equals("SHOW_COST_DETAIL")
                || button_type.equals("SHOW_INCOME_DETAIL")) {
            Router.goWithData(activity, ShowTabHostActivity.class, "照会画面へ",
                    new Intent().putExtra("FIRST_TAB", button_type)
                    );
        }
        // 分析画面へ遷移
        else if (button_type.equals("SHOW_BUDGET_SHOW")
                || button_type.equals("SHOW_SETTLE_SHOW")) {
            Router.goWithData(activity, AnalysisTabHostActivity.class, "分析画面へ",
                    new Intent().putExtra("FIRST_TAB", button_type)
                    );
        }
        // 送られてきたボタンタイプに応じて，遷移先を振り分ける。
        else {
            Router.goByRoutingTable(activity, button_type,
                    new RoutingTable()
                            .map("SHOW_ACCOUNT_BOOK", AccountBookShowActivity.class, "目標設定画面へ")
                            .map("EDIT_TAB_HOST", EditTabHostActivity.class, "登録画面親タブへ")
                            .map("SHOW_TAB_HOST", ShowTabHostActivity.class, "照会画面親タブへ")
                            .map("ANALYSIS_TAB_HOST", AnalysisTabHostActivity.class, "分析画面親タブへ")
                    // .map("SHOW_MY_WALLET", ShowMyWalletActivity.class,
                    // "財布の中身へ")
                    );
        }
    }

}

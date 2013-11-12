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
import com.android_mvc.sample_project.activities.accountbook.BudgetShowActivity;
import com.android_mvc.sample_project.activities.accountbook.CostDetailEditActivity;
import com.android_mvc.sample_project.activities.accountbook.CostDetailShowActivity;
import com.android_mvc.sample_project.activities.accountbook.IncomeDetailEditActivity;
import com.android_mvc.sample_project.activities.accountbook.SettleShowActivity;
import com.android_mvc.sample_project.activities.installation.InstallAppActivity;
import com.android_mvc.sample_project.activities.installation.InstallCompletedActivity;
import com.android_mvc.sample_project.activities.main.TopActivity;
import com.android_mvc.sample_project.domain.AccountBookEditAction;


/**
 * メイン系画面のコントローラ。
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
        if( installExecutedFlag )
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
        new ControlFlowDetail<InstallCompletedActivity>( activity )
            .setValidation( new ValidationExecutor(){
                @Override
                public ValidationResult doValidate()
                {
                    // バリデーション処理
                    return new FuncDBValidation().validate( activity );
                }

                @Override
                public void onValidationFailed()
                {
                    showErrMessages();

                    // バリデーション失敗時の遷移先
                    //goOnValidationFailed( CostDetailEditActivity.class );
                    stayInThisPage();
                }
            })
            .setBL( new BLExecutor(){
                @Override
                public ActionResult doAction()
                {
                    // BL
                    return new AccountBookEditAction( activity ).exec();
                }
            })
            .onBLExecuted(
                // BL実行後の遷移先の一覧
                new RoutingTable().map("success", AccountBookShowActivity.class )

                // onBLExecutedにこれを渡せば，BLの実行結果にかかわらず画面遷移を常に抑止。
                //STAY_THIS_PAGE_ALWAYS

                // BL実行結果が特定の状況のときのみ，画面遷移を抑止することも可能。
//                new RoutingTable().map("success", STAY_THIS_PAGE )

            )
            .setDialogText("お待ちください")
            .startControl();
        ;

    }


    /**
     * TOP画面からの遷移時
     */
    public static void submit(TopActivity activity, String button_type) {

        // 送られてきたボタンタイプに応じて，遷移先を振り分ける。

        // extra付きの遷移を実行
        if( "EDIT_COST_DETAIL".equals(button_type) )
        {
            Router.goWithData(activity, CostDetailEditActivity.class, "変動費登録画面へ",
                new Intent().putExtra("hoge", "Intentで値を渡すテスト").putExtra("fuga", 1)
            );
        } else if( "EDIT_INCOME_DETAIL".equals(button_type) )
        {
            Router.goWithData(activity, IncomeDetailEditActivity.class, "収入登録画面へ",
                new Intent().putExtra("hoge", "Intentで値を渡すテスト").putExtra("fuga", 1)
            );
        }
        else {
            // extraのない遷移を実行
            Router.goByRoutingTable(activity, button_type,
                new RoutingTable()
                    .map("SHOW_COST_DETAIL", CostDetailShowActivity.class, "変動費一覧画面へ")
                    .map("SHOW_ACCOUNT_BOOK", AccountBookShowActivity.class, "目標金額照会へ")
                    .map("SHOW_BUDGET_SHOW", BudgetShowActivity.class, "予定表示へ")
                    .map("SHOW_SETTLE_SHOW", SettleShowActivity.class, "実績表示へ")
            );
        }

/*
    NOTE: 下記のように書くのと同じ。

        if( "VIEW_DB".equals(button_type) )
        {
            // 一覧画面へ
            Router.go(activity, DBListActivity.class);
        }
        if( "TAB_SAMPLE".equals(button_type) )
        {
            // タブ画面へ
            Router.go(activity, SampleTabHostActivity.class);
        }
        if( "MAP_SAMPLE".equals(button_type) )
        {
            // マップ画面へ
            Router.go(activity, SampleMapActivity.class);
        }
        if( "HTML_SAMPLE".equals(button_type) )
        {
            // HTMLのサンプル画面へ
            Router.go(activity, SampleHtmlActivity.class);
        }
        if( "JQUERY_SAMPLE".equals(button_type) )
        {
            // jQuery Mobileのサンプル画面へ
            Router.go(activity, SampleJQueryMobileActivity.class);
        }
        if( "ANIM_SAMPLE".equals(button_type) )
        {
            // アニメーションのサンプル画面へ
            Router.go(activity, SampleAnimationActivity.class);
        }
*/

    }


}

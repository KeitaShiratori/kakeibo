package com.android_mvc.sample_project.domain;


import android.app.Activity;

import com.android_mvc.framework.controller.action.ActionResult;
import com.android_mvc.framework.controller.action.BaseAction;
import com.android_mvc.framework.ui.UIUtil;
import com.android_mvc.sample_project.activities.accountbook.IncomeDetailShowActivity;
import com.android_mvc.sample_project.db.dao.IncomeDetailDAO;
import com.android_mvc.sample_project.db.entity.IncomeDetail;

/**
 * DB登録に関するBL。
 * @author id:language_and_engineering
 *
 */
public class IncomeDetailUpdateAction extends BaseAction
{

    private IncomeDetailShowActivity activity;
    private IncomeDetail income_detail;

    public IncomeDetailUpdateAction(IncomeDetailShowActivity activity, IncomeDetail income_detail) {
        this.activity = activity;
        this.income_detail = income_detail;
    }

    // BL本体
    @Override
    public ActionResult exec()
    {
        // DB登録（すでに非同期でラップされているので，DB操作も同期的でよい）
        new IncomeDetailDAO(activity).update(income_detail);

        // 実行結果を返す
        return new IncomeDetailUpdateActionResult()
            .setRouteId("success")
            .add("FIRST_TAB", "SHOW_INCOME_DETAIL")
        ;
    }


    // 実行結果オブジェクト
    static class IncomeDetailUpdateActionResult extends ActionResult
    {
        private static final long serialVersionUID = 1L;

        @Override
        public void onNextActivityStarted(final Activity activity)
        {
            UIUtil.longToast(activity, "収入明細を更新しました。");
        }
    }
    // NOTE: この内部クラスは，execメソッド中で匿名クラスとして定義することができない。
    // staticな内部クラスとして実装する必要がある。
    // 理由は，JavaのインナークラスとSerializableの仕様のため。
    // @see http://d.hatena.ne.jp/language_and_engineering/20120313/p1


}

package com.android_mvc.sample_project.domain;


import android.app.Activity;

import com.android_mvc.framework.controller.action.ActionResult;
import com.android_mvc.framework.controller.action.BaseAction;
import com.android_mvc.framework.controller.validation.ActivityParams;
import com.android_mvc.framework.ui.UIUtil;
import com.android_mvc.sample_project.activities.accountbook.IncomeDetailShowActivity;
import com.android_mvc.sample_project.db.dao.IncomeDetailDAO;

/**
 * DB登録に関するBL。
 * @author id:language_and_engineering
 *
 */
public class IncomeDetailDeleteAction extends BaseAction
{

    private IncomeDetailShowActivity activity;
    private Long income_detail_id;

    public IncomeDetailDeleteAction(IncomeDetailShowActivity activity, Long income_detail_id) {
        this.activity = activity;
        this.income_detail_id = income_detail_id;
    }

    // BL本体
    @Override
    public ActionResult exec()
    {
        // DB登録（すでに非同期でラップされているので，DB操作も同期的でよい）
        new IncomeDetailDAO(activity).deleteById(income_detail_id);

        // 実行結果を返す
        return new IncomeDetailDeleteActionResult()
            .setRouteId("success")
            .add("FIRST_TAB", "SHOW_INCOME_DETAIL")
        ;
    }


    // 実行結果オブジェクト
    static class IncomeDetailDeleteActionResult extends ActionResult
    {
        private static final long serialVersionUID = 1L;

        @Override
        public void onNextActivityStarted(final Activity activity)
        {
            UIUtil.longToast(activity, "収入明細を削除しました。");
        }
    }
    // NOTE: この内部クラスは，execメソッド中で匿名クラスとして定義することができない。
    // staticな内部クラスとして実装する必要がある。
    // 理由は，JavaのインナークラスとSerializableの仕様のため。
    // @see http://d.hatena.ne.jp/language_and_engineering/20120313/p1


}

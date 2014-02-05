package com.android_mvc.sample_project.domain;


import android.app.Activity;

import com.android_mvc.framework.controller.action.ActionResult;
import com.android_mvc.framework.controller.action.BaseAction;
import com.android_mvc.framework.ui.UIUtil;
import com.android_mvc.sample_project.activities.accountbook.CostDetailShowActivity;
import com.android_mvc.sample_project.db.dao.CostDetailDAO;
import com.android_mvc.sample_project.db.entity.CostDetail;

/**
 * DB登録に関するBL。
 * @author id:language_and_engineering
 *
 */
public class CostDetailUpdateAction extends BaseAction
{

    private CostDetailShowActivity activity;
    private CostDetail cost_detail;

    public CostDetailUpdateAction(CostDetailShowActivity activity, CostDetail cost_detail) {
        this.activity = activity;
        this.cost_detail = cost_detail;
    }

    // BL本体
    @Override
    public ActionResult exec()
    {
        // DB登録（すでに非同期でラップされているので，DB操作も同期的でよい）
        CostDetail c = new CostDetailDAO(activity).update(cost_detail);

        // 実行結果を返す
        return new CostDetailDeleteActionResult()
            .setRouteId("success")
            .add("update_cost_detail", c)
            .add("FIRST_TAB", "SHOW_COST_DETAIL")
        ;
    }


    // 実行結果オブジェクト
    static class CostDetailDeleteActionResult extends ActionResult
    {
        private static final long serialVersionUID = 1L;

        @Override
        public void onNextActivityStarted(final Activity activity)
        {
            UIUtil.longToast(activity, "変動費明細を更新しました。");
        }
    }
    // NOTE: この内部クラスは，execメソッド中で匿名クラスとして定義することができない。
    // staticな内部クラスとして実装する必要がある。
    // 理由は，JavaのインナークラスとSerializableの仕様のため。
    // @see http://d.hatena.ne.jp/language_and_engineering/20120313/p1


}

package com.android_mvc.sample_project.domain;


import java.util.Calendar;

import android.app.Activity;

import com.android_mvc.framework.controller.action.ActionResult;
import com.android_mvc.framework.controller.action.BaseAction;
import com.android_mvc.framework.controller.validation.ActivityParams;
import com.android_mvc.framework.ui.UIUtil;
import com.android_mvc.sample_project.activities.accountbook.CostDetailEditActivity;
import com.android_mvc.sample_project.db.dao.CostDetailDAO;
import com.android_mvc.sample_project.db.entity.CostDetail;

/**
 * DB登録に関するBL。
 * @author id:language_and_engineering
 *
 */
public class CostDetailEditAction extends BaseAction
{

    private CostDetailEditActivity activity;


    public CostDetailEditAction(CostDetailEditActivity activity) {
        this.activity = activity;
    }


    // BL本体
    @Override
    public ActionResult exec()
    {
        ActivityParams params = activity.toParams();

        // 登録用の値を取得（バリデ通過済み）
        Integer category_type = (Integer) params.getValue("category_type");
        Integer pay_type = (Integer)params.getValue("pay_type");
        Calendar budget_ymd = (Calendar)params.getValue("budget_ymd");
        Integer budget_cost = Integer.parseInt((String)params.getValue("budget_cost"));
        Calendar settle_ymd = (Calendar)params.getValue("settle_ymd");
        Integer settle_cost = null;
        String tmp = (String)params.getValue("settle_cost");
        if(!tmp.isEmpty()){
            settle_cost = Integer.parseInt((String)params.getValue("settle_cost"));
        }

        // DB登録（すでに非同期でラップされているので，DB操作も同期的でよい）
        CostDetail v = new CostDetailDAO(activity).create( category_type, pay_type, budget_ymd, budget_cost, settle_ymd, settle_cost );

        // 実行結果を返す
        return new DBEDitActionResult()
            .setRouteId("success")
            .add("new_cost_detail", v)
        ;
    }


    // 実行結果オブジェクト
    static class DBEDitActionResult extends ActionResult
    {
        private static final long serialVersionUID = 1L;

        @Override
        public void onNextActivityStarted(final Activity activity)
        {
            UIUtil.longToast(activity, "変動費明細を登録しました。");
        }
    }
    // NOTE: この内部クラスは，execメソッド中で匿名クラスとして定義することができない。
    // staticな内部クラスとして実装する必要がある。
    // 理由は，JavaのインナークラスとSerializableの仕様のため。
    // @see http://d.hatena.ne.jp/language_and_engineering/20120313/p1


}

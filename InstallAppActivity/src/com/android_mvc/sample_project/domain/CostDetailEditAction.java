package com.android_mvc.sample_project.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;

import com.android_mvc.framework.activities.base.BaseNormalActivity;
import com.android_mvc.framework.controller.action.ActionResult;
import com.android_mvc.framework.controller.action.BaseAction;
import com.android_mvc.framework.controller.validation.ActivityParams;
import com.android_mvc.framework.ui.UIUtil;
import com.android_mvc.sample_project.common.Util;
import com.android_mvc.sample_project.db.dao.AccountBookDAO;
import com.android_mvc.sample_project.db.dao.AccountBookDetailDAO;
import com.android_mvc.sample_project.db.dao.CostDetailDAO;
import com.android_mvc.sample_project.db.entity.CostDetail;
import com.android_mvc.sample_project.db.schema.ColumnDefinition.AccountBookDetailCol;
import com.android_mvc.sample_project.db.schema.ColumnDefinition.CostDetailCol;

/**
 * DB登録に関するBL。
 * 
 * @author id:language_and_engineering
 * 
 */
public class CostDetailEditAction extends BaseAction
{

    private BaseNormalActivity activity;

    public CostDetailEditAction(BaseNormalActivity activity) {
        this.activity = activity;
    }

    // BL本体
    @Override
    public ActionResult exec()
    {
        ActivityParams params = activity.toParams();

        // 登録用の値を取得（バリデ通過済み）
        Integer category_type = (Integer) params.getValue("category_type");
        Integer pay_type = (Integer) params.getValue("pay_type");
        Calendar budget_ymd = (Calendar) params.getValue("budget_ymd");
        Integer budget_cost = Integer.parseInt((String) params.getValue("budget_cost"));
        Integer settle_cost = null;
        String tmp = (String) params.getValue("settle_cost");
        if (!tmp.isEmpty()) {
            settle_cost = Integer.parseInt((String) params.getValue("settle_cost"));
        }
        Integer divideNum = (Integer) params.getValue(CostDetailCol.DIVIDE_NUM);

        DBEDitActionResult ret = new DBEDitActionResult();
        List<Calendar> days = new ArrayList<Calendar>();

        Calendar lastDay = new AccountBookDetailDAO(activity).findOrderByKeyDesc(AccountBookDetailCol.MOKUHYOU_MONTH).get(0).getMokuhyouMonth();
        lastDay.add(Calendar.MONTH, 1);
        int accountBookStartDate = new AccountBookDAO(activity).findAll().get(0).getStartDate().get(Calendar.DAY_OF_MONTH);
        lastDay.set(Calendar.DAY_OF_MONTH, accountBookStartDate);
        lastDay.add(Calendar.DAY_OF_MONTH, -1);
        // int dayCounter = budget_ymd.getActualMaximum(Calendar.DAY_OF_MONTH) -
        // budget_ymd.get(Calendar.DAY_OF_MONTH) + 1;
        long dayCounter = (lastDay.getTimeInMillis() - budget_ymd.getTimeInMillis()) / (1000 * 60 * 60 * 24);
        dayCounter += 1;

        if (params.getValue("repeat_dvn").toString().equals("繰り返しなし")) {
            days.add(budget_ymd);
        }
        else if (params.getValue("repeat_dvn").toString().equals("毎日")) {
            // 残り日数分、daysに日付をセット
            for (int i = 0; i < dayCounter; i++) {
                days.add((Calendar) budget_ymd.clone());
                budget_ymd.add(Calendar.DAY_OF_MONTH, 1);
            }
        }
        else if (params.getValue("repeat_dvn").toString().equals("平日のみ")) {
            // 残り日数分繰り返し
            for (int i = 0; i < dayCounter; i++) {
                // 予定日が土日でない場合、daysに日付をセット
                if (budget_ymd.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY
                        && budget_ymd.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                    days.add((Calendar) budget_ymd.clone());
                }
                budget_ymd.add(Calendar.DAY_OF_MONTH, 1);
            }
        }
        else if (params.getValue("repeat_dvn").toString().equals("土日のみ")) {
            // 残り日数分繰り返し
            for (int i = 0; i < dayCounter; i++) {
                // 予定日が土日の場合、daysに日付をセット
                if (budget_ymd.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
                        || budget_ymd.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    days.add((Calendar) budget_ymd.clone());
                }
                budget_ymd.add(Calendar.DAY_OF_MONTH, 1);
            }
        }
        else if (params.getValue("repeat_dvn").toString().equals("毎週")) {
            final Calendar inputYMD = (Calendar) budget_ymd.clone();
            // 残り日数分繰り返し
            for (int i = 0; i < dayCounter; i++) {
                // 予定日の曜日と同じ曜日の場合、daysに日付をセット
                if (budget_ymd.get(Calendar.DAY_OF_WEEK) == inputYMD.get(Calendar.DAY_OF_WEEK)) {
                    days.add((Calendar) budget_ymd.clone());
                    Util.d("登録対象の日付：" + days.get(days.size()-1));
                }
                budget_ymd.add(Calendar.DAY_OF_MONTH, 1);
            }
        }
        else if (params.getValue("repeat_dvn").toString().equals("毎月")) {
            final Calendar inputYMD = (Calendar) budget_ymd.clone();
            // 残り日数分繰り返し
            for (int i = 0; i < dayCounter; i++) {
                // 予定日の日と同じ日の場合、daysに日付をセット
                if (budget_ymd.get(Calendar.DAY_OF_MONTH) == inputYMD.get(Calendar.DAY_OF_MONTH)) {
                    days.add((Calendar) budget_ymd.clone());
                    Util.d("登録対象の日付：" + days.get(days.size()-1));
                }
                budget_ymd.add(Calendar.DAY_OF_MONTH, 1);
            }
        }

        for (int i = 0; i < days.size(); i++) {
            // DB登録（すでに非同期でラップされているので，DB操作も同期的でよい）
            CostDetail c = new CostDetailDAO(activity).create(category_type, pay_type, days.get(i), budget_cost, settle_cost, divideNum);
            ret.setRouteId("success")
                    .add("new_cost_detail" + i, c);
            Util.d("登録内容" 
                    + "日付：" + ((CostDetail) ret.get("new_cost_detail" + i)).getBudgetYmd().getTime()
                    + "予定金額："+((CostDetail) ret.get("new_cost_detail" + i)).getBudgetCost()
                    + "実績金額："+((CostDetail) ret.get("new_cost_detail" + i)).getSettleCost());
        }

        ret.add("FIRST_TAB", "SHOW_COST_DETAIL");

        // 実行結果を返す
        return ret;
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

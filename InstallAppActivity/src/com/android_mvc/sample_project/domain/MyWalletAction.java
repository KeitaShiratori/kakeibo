package com.android_mvc.sample_project.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;

import com.android_mvc.framework.controller.action.ActionResult;
import com.android_mvc.framework.controller.action.BaseAction;
import com.android_mvc.framework.controller.validation.ActivityParams;
import com.android_mvc.framework.ui.UIUtil;
import com.android_mvc.sample_project.R;
import com.android_mvc.sample_project.activities.accountbook.MyWalletShowActivity;
import com.android_mvc.sample_project.common.Util;
import com.android_mvc.sample_project.db.dao.MyWalletDAO;
import com.android_mvc.sample_project.db.entity.MyWallet;
import com.android_mvc.sample_project.db.schema.ColumnDefinition.MyWalletCol;

/**
 * DB登録に関するBL。
 * 
 * @author id:language_and_engineering
 * 
 */
public class MyWalletAction extends BaseAction
{

    private MyWalletShowActivity activity;

    public MyWalletAction(MyWalletShowActivity activity) {
        this.activity = activity;
    }

    // BL本体
    @Override
    public ActionResult exec()
    {
        // 画面入力値を取得
        ActivityParams params = activity.toParams();

        MyWalletActionResult ret = new MyWalletActionResult();

        // DB登録（すでに非同期でラップされているので，DB操作も同期的でよい）
        // 登録対象の年月日で検索。すでに同一日付で登録があれば上書きする。
        MyWalletDAO dao = new MyWalletDAO(activity);
        List<MyWallet> m = dao.findWhere(MyWalletCol.YMD, params.getValue(MyWalletCol.YMD));

        MyWallet newRecord = new MyWallet();

        if (m.isEmpty()) {
            newRecord.setYmd((Calendar) params.getValue(MyWalletCol.YMD));
        } else {
            newRecord = m.get(0);
        }
        newRecord.setKingaku((Integer) params.getValue(MyWalletCol.KINGAKU));
        newRecord.setZandaka((Integer) params.getValue(MyWalletCol.ZANDAKA));
        newRecord.setHikidashi((Integer) params.getValue(MyWalletCol.HIKIDASHI));
        dao.create(newRecord);

        ret.setRouteId("success")
                .add(Util._(activity, R.string.MYWALLET), newRecord);
        ret.add(Util._(activity, R.string.FIRST_TAB), Util._(activity, R.string.MYWALLET));
        // 実行結果を返す
        return ret;
    }

    // 実行結果オブジェクト
    static class MyWalletActionResult extends ActionResult
    {
        private static final long serialVersionUID = 1L;

        @Override
        public void onNextActivityStarted(final Activity activity)
        {
            UIUtil.longToast(activity, "財布の中身を登録しました。");
        }
    }
    // NOTE: この内部クラスは，execメソッド中で匿名クラスとして定義することができない。
    // staticな内部クラスとして実装する必要がある。
    // 理由は，JavaのインナークラスとSerializableの仕様のため。
    // @see http://d.hatena.ne.jp/language_and_engineering/20120313/p1

}

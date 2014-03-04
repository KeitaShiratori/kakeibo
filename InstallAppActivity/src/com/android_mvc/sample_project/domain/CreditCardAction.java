package com.android_mvc.sample_project.domain;

import java.util.Calendar;

import android.app.Activity;

import com.android_mvc.framework.controller.action.ActionResult;
import com.android_mvc.framework.controller.action.BaseAction;
import com.android_mvc.framework.controller.validation.ActivityParams;
import com.android_mvc.framework.ui.UIUtil;
import com.android_mvc.sample_project.R;
import com.android_mvc.sample_project.activities.accountbook.CreditCardActivity;
import com.android_mvc.sample_project.common.Util;
import com.android_mvc.sample_project.db.dao.CreditCardSettingDAO;
import com.android_mvc.sample_project.db.entity.CreditCardSetting;
import com.android_mvc.sample_project.db.entity.MyWallet;
import com.android_mvc.sample_project.db.schema.ColumnDefinition.CreditCardSettingCol;
import com.android_mvc.sample_project.db.schema.ColumnDefinition.MyWalletCol;

/**
 * DB登録に関するBL。
 * 
 * @author id:language_and_engineering
 * 
 */
public class CreditCardAction extends BaseAction
{

    private CreditCardActivity activity;

    public CreditCardAction(CreditCardActivity activity) {
        this.activity = activity;
    }

    // BL本体
    @Override
    public ActionResult exec()
    {
        // 画面入力値を取得
        ActivityParams params = activity.toParams();

        CreditCardActionResult ret = new CreditCardActionResult();

        // DB登録（すでに非同期でラップされているので，DB操作も同期的でよい）
        // 登録対象の年月日で検索。すでに同一日付で登録があれば上書きする。
        CreditCardSettingDAO dao = new CreditCardSettingDAO(activity);
//        CreditCardSetting c = dao.findNewestOne();

        CreditCardSetting newRecord = new CreditCardSetting();

//        if (c.isEmpty()) {
//            newRecord.setSimeYmd((Calendar) params.getValue(CreditCardSettingCol.SIME_YMD));
//            newRecord.setSiharaiYmd((Calendar) params.getValue(CreditCardSettingCol.SIHARAI_YMD));
//        } else {
//            newRecord = c.get(0);
//        }
        newRecord.setSimeYmd((Calendar) params.getValue(CreditCardSettingCol.SIME_YMD));
        newRecord.setSiharaiYmd((Calendar) params.getValue(CreditCardSettingCol.SIHARAI_YMD));
        dao.create(newRecord);

        ret.setRouteId("success")
                .add("CREDIT_CARD_SETTING", newRecord);
        // 実行結果を返す
        return ret;
    }

    // 実行結果オブジェクト
    static class CreditCardActionResult extends ActionResult
    {
        private static final long serialVersionUID = 1L;

        @Override
        public void onNextActivityStarted(final Activity activity)
        {
            UIUtil.longToast(activity, "クレジットカードを登録しました。");
        }
    }
    // NOTE: この内部クラスは，execメソッド中で匿名クラスとして定義することができない。
    // staticな内部クラスとして実装する必要がある。
    // 理由は，JavaのインナークラスとSerializableの仕様のため。
    // @see http://d.hatena.ne.jp/language_and_engineering/20120313/p1

}

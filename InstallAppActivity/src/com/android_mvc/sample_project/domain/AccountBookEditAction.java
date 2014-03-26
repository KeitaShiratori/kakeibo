package com.android_mvc.sample_project.domain;


import java.util.Calendar;

import android.app.Activity;

import com.android_mvc.framework.controller.action.ActionResult;
import com.android_mvc.framework.controller.action.BaseAction;
import com.android_mvc.framework.controller.validation.ActivityParams;
import com.android_mvc.framework.ui.UIUtil;
import com.android_mvc.sample_project.activities.installation.InstallCompletedActivity;
import com.android_mvc.sample_project.db.dao.AccountBookDAO;
import com.android_mvc.sample_project.db.dao.AccountBookDetailDAO;
import com.android_mvc.sample_project.db.dao.PrefDAO;
import com.android_mvc.sample_project.db.entity.AccountBook;
import com.android_mvc.sample_project.db.entity.AccountBookDetail;

/**
 * DB登録に関するBL。
 * @author id:language_and_engineering
 *
 */
public class AccountBookEditAction extends BaseAction
{
	private InstallCompletedActivity activity;

    public AccountBookEditAction(InstallCompletedActivity activity) {
        this.activity = activity;
    }

    // BL本体
    @Override
    public ActionResult exec()
    {
        ActivityParams params = activity.toParams();
        PrefDAO pref = new PrefDAO();

        // 登録用の値を取得（バリデ通過済み）
        Integer mokuhyou_kingaku = Integer.valueOf((String)params.getValue("mokuhyou_kingaku"));
        Integer mokuhyou_kikan = Integer.valueOf((String)params.getValue("mokuhyou_kikan"));
        Calendar start_date = (Calendar) params.getValue("start_date");


        // DB登録（すでに非同期でラップされているので，DB操作も同期的でよい）
        AccountBook a = new AccountBookDAO(activity).create( mokuhyou_kingaku, mokuhyou_kikan, start_date);

        // 自動的に家計簿明細テーブルにレコードを登録する。
        Integer mokuhyouMonthKingaku = mokuhyou_kingaku / mokuhyou_kikan;
        Calendar mokuhyouMonth = start_date;
        for (int i = 0; i < mokuhyou_kikan; i++){
            AccountBookDetail abd = new AccountBookDetailDAO(activity).create(mokuhyouMonthKingaku, mokuhyouMonth, true);
            mokuhyouMonth.add(Calendar.MONTH, 1);
        }
 
        // インストール画面のチュートリアル済みフラグが立っていなければフラグを立てる。
        if(!pref.getTutorialDoneFlagInstallComplete(activity)){
            pref.updateTutorialDoneFlagInstallComplete(activity, true);
        }

        // 実行結果を返す
        return new AccountBookEditActionResult()
            .setRouteId("success")
            .add("new_account_book", a)
        ;
    }


    // 実行結果オブジェクト
    static class AccountBookEditActionResult extends ActionResult
    {
        private static final long serialVersionUID = 1L;

        @Override
        public void onNextActivityStarted(final Activity activity)
        {
            UIUtil.longToast(activity, "目標を設定しました。");
        }
    }
    // NOTE: この内部クラスは，execメソッド中で匿名クラスとして定義することができない。
    // staticな内部クラスとして実装する必要がある。
    // 理由は，JavaのインナークラスとSerializableの仕様のため。
    // @see http://d.hatena.ne.jp/language_and_engineering/20120313/p1


}

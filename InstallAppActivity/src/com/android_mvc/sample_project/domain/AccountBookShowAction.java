package com.android_mvc.sample_project.domain;

import java.util.List;

import android.app.Activity;

import com.android_mvc.framework.controller.action.ActionResult;
import com.android_mvc.framework.controller.action.BaseAction;
import com.android_mvc.framework.ui.UIUtil;
import com.android_mvc.sample_project.activities.accountbook.AccountBookShowActivity;
import com.android_mvc.sample_project.db.dao.AccountBookDAO;
import com.android_mvc.sample_project.db.dao.AccountBookDetailDAO;
import com.android_mvc.sample_project.db.entity.AccountBookDetail;

/**
 * DB登録に関するBL。
 * 
 * @author id:language_and_engineering
 * 
 */
public class AccountBookShowAction extends BaseAction
{
    private AccountBookShowActivity activity;
    // DB登録用の家計簿明細オブジェクト
    private AccountBookDetail abd;

    public AccountBookShowAction(AccountBookShowActivity activity, AccountBookDetail abd) {
        this.activity = activity;
        this.abd = abd;
    }

    // BL本体
    @Override
    public ActionResult exec()
    {

        final AccountBookDetailDAO abdDAO = new AccountBookDetailDAO(activity);
        abdDAO.update(abd);

        // 自動的に家計簿明細テーブルを更新する。
        List<AccountBookDetail> accountBookDetails = abdDAO.findAll();
        Integer auto = calcMokuhyouMonthKingaku(accountBookDetails);

        for (AccountBookDetail a : accountBookDetails) {
            // 自動入力フラグがONの場合には、目標金額を更新する。
            if (a.getAutoInputFlag()) {
                a.setMokuhyouMonthKingaku(auto);
                abdDAO.update(a);
            }
        }
        // 実行結果を返す
        return new AccountBookEditActionResult()
                .setRouteId("success");
    }

    private Integer calcMokuhyouMonthKingaku(
            List<AccountBookDetail> accountBookDetails) {
        Integer ret = 0;

        // 家計簿テーブルの目標金額を取得する。
        Integer mokuhyouKingaku = new AccountBookDAO(activity).findAll().get(0).getMokuhyouKingaku();

        // 家計簿明細テーブルから手動入力金額の合計と自動入力フラグの数を取得する
        Integer manualInputKingaku = 0;
        Integer autoInputCount = 0;
        for (AccountBookDetail a : accountBookDetails) {
            if (a.getAutoInputFlag()) {
                autoInputCount++;
            } else {
                manualInputKingaku += a.getMokuhyouMonthKingaku();
            }
        }

        // 自動入力する金額を計算する。自動入力ONのレコードがない場合、retの初期値0を返す。
        if (autoInputCount != 0) {
            ret = (mokuhyouKingaku - manualInputKingaku) / autoInputCount;
        }

        return ret;
    }

    // 実行結果オブジェクト
    static class AccountBookEditActionResult extends ActionResult
    {
        private static final long serialVersionUID = 1L;

        @Override
        public void onNextActivityStarted(final Activity activity)
        {
            UIUtil.longToast(activity, "月目標を設定しました。");
        }
    }
    // NOTE: この内部クラスは，execメソッド中で匿名クラスとして定義することができない。
    // staticな内部クラスとして実装する必要がある。
    // 理由は，JavaのインナークラスとSerializableの仕様のため。
    // @see http://d.hatena.ne.jp/language_and_engineering/20120313/p1

}

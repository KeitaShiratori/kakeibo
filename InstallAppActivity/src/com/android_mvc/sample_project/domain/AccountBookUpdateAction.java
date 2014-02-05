package com.android_mvc.sample_project.domain;


import java.util.List;

import android.app.Activity;

import com.android_mvc.framework.controller.action.ActionResult;
import com.android_mvc.framework.controller.action.BaseAction;
import com.android_mvc.framework.ui.UIUtil;
import com.android_mvc.sample_project.activities.accountbook.AccountBookShowActivity;
import com.android_mvc.sample_project.db.dao.AccountBookDAO;
import com.android_mvc.sample_project.db.dao.AccountBookDetailDAO;
import com.android_mvc.sample_project.db.entity.AccountBook;
import com.android_mvc.sample_project.db.entity.AccountBookDetail;

/**
 * DB登録に関するBL。
 * @author id:language_and_engineering
 *
 */
public class AccountBookUpdateAction extends BaseAction
{

    private AccountBookShowActivity activity;
    private AccountBook account_book;

    public AccountBookUpdateAction(AccountBookShowActivity activity, AccountBook account_book) {
        this.activity = activity;
        this.account_book = account_book;
    }

    // BL本体
    @Override
    public ActionResult exec()
    {
        // DB登録（すでに非同期でラップされているので，DB操作も同期的でよい）
        new AccountBookDAO(activity).update(account_book);

        // 家計簿明細テーブルを更新する。
        final AccountBookDetailDAO abdDAO = new AccountBookDetailDAO(activity);

        // 自動的に家計簿明細テーブルを更新する。
        List<AccountBookDetail> accountBookDetails = abdDAO.findAll();
        Integer auto = calcMokuhyouMonthKingaku(accountBookDetails);
        
        for (AccountBookDetail a : accountBookDetails){
            // 自動入力フラグがONの場合には、目標金額を更新する。
            if (a.getAutoInputFlag()){
                a.setMokuhyouMonthKingaku(auto);
                abdDAO.update(a);
            }
        }

        
        // 実行結果を返す
        return new AccountBookUpdateActionResult()
            .setRouteId("success")
        ;
    }


    // 実行結果オブジェクト
    static class AccountBookUpdateActionResult extends ActionResult
    {
        private static final long serialVersionUID = 1L;

        @Override
        public void onNextActivityStarted(final Activity activity)
        {
            UIUtil.longToast(activity, "最終目標金額を更新しました。");
        }
    }
    // NOTE: この内部クラスは，execメソッド中で匿名クラスとして定義することができない。
    // staticな内部クラスとして実装する必要がある。
    // 理由は，JavaのインナークラスとSerializableの仕様のため。
    // @see http://d.hatena.ne.jp/language_and_engineering/20120313/p1

    private Integer calcMokuhyouMonthKingaku(
            List<AccountBookDetail> accountBookDetails) {
        Integer ret = 0;
        
        // 家計簿テーブルの目標金額を取得する。
        Integer mokuhyouKingaku = account_book.getMokuhyouKingaku();
        
        // 家計簿明細テーブルから手動入力金額の合計と自動入力フラグの数を取得する
        Integer manualInputKingaku = 0;
        Integer autoInputCount = 0;
        for(AccountBookDetail a : accountBookDetails){
            if (a.getAutoInputFlag()){
                autoInputCount ++;
            }else{
                manualInputKingaku += a.getMokuhyouMonthKingaku();
            }
        }

        // 自動入力する金額を計算する。自動入力ONのレコードがない場合、retの初期値0を返す。
        if (autoInputCount != 0){
            ret = (mokuhyouKingaku - manualInputKingaku) / autoInputCount;
        }
        
        return ret;
    }


}

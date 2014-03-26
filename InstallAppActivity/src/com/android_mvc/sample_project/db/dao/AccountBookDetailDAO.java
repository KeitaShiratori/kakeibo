package com.android_mvc.sample_project.db.dao;

import java.util.Calendar;
import java.util.List;

import android.content.Context;

import com.android_mvc.framework.db.DBHelper;
import com.android_mvc.framework.db.dao.BaseDAO;
import com.android_mvc.framework.db.dao.Finder;
import com.android_mvc.sample_project.db.entity.AccountBookDetail;
import com.android_mvc.sample_project.db.entity.MyWallet;

/**
 * 変動費明細を読み書きするクラス。
 * @author id:language_and_engineering
 */
public class AccountBookDetailDAO extends BaseDAO<AccountBookDetail>
{

    public AccountBookDetailDAO(Context context) {
        helper = new DBHelper(context);
    }

    // ------------ C --------------




	/**
     * 1件の変動費明細を保存。
     */
    public AccountBookDetail create(Integer mokuhyouMonthKingaku, Calendar mokuhyouMonth, Boolean autoInputFlag)
    {
        // 論理エンティティを構築
        AccountBookDetail a = new AccountBookDetail();
        a.setMokuhyouMonthKingaku(mokuhyouMonthKingaku);
        a.setMokuhyouMonth(mokuhyouMonth);
        a.setAutoInputFlag(autoInputFlag);

        // DB登録
        a.save(helper);

        return a;
    }


    // ------------ R --------------


    /**
     * 家計簿明細を全て登録順に返す。
     */
    public List<AccountBookDetail> findAll()
    {
        return findAll(helper, AccountBookDetail.class);
    }


    /**
     * 特定のIDの友達を１人返す。
     */
    public AccountBookDetail findById(Long account_book_detail_id)
    {
        return findById( helper, AccountBookDetail.class, account_book_detail_id );
    }

        // NOTE: 細かい条件で検索したい場合は，Finderを利用すること。
        // findAllやfindByIdの実装を参照。

    /**
     * 任意のKeyで降順にソートした結果を返す。KeyにはMyWalletColクラスのメンバを指定する。
     * 
     * @param orderKey
     * @return
     */
    public List<AccountBookDetail> findOrderByKeyDesc(String orderKey) {

        return new Finder<AccountBookDetail>(helper)
                .where("id > 0")
                .orderBy(orderKey + " DESC")
                .findAll(AccountBookDetail.class);
    }

    /**
     * 任意のKeyで昇順にソートした結果を返す。KeyにはMyWalletColクラスのメンバを指定する。
     * 
     * @param orderKey
     * @return
     */
    public List<AccountBookDetail> findOrderByKeyAsc(String orderKey) {

        return new Finder<AccountBookDetail>(helper)
                .where("id > 0")
                .orderBy(orderKey + " ASC")
                .findAll(AccountBookDetail.class);
    }


    // ------------ U --------------

    public void update(AccountBookDetail a){
    	a.save(helper);
    }

    // ------------ D --------------


    /**
     * 特定のIDの変動費明細を削除。
     */
    public void deleteById( Long account_book_detail_id )
    {
        AccountBookDetail a = findById(account_book_detail_id);

        // DBからの削除を実行
        a.delete(helper);
    }


}

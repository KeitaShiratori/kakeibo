package com.android_mvc.sample_project.db.dao;

import java.util.Calendar;
import java.util.List;

import android.accounts.Account;
import android.content.Context;

import com.android_mvc.framework.db.DBHelper;
import com.android_mvc.framework.db.dao.BaseDAO;
import com.android_mvc.framework.db.dao.Finder;
import com.android_mvc.sample_project.db.entity.AccountBook;
import com.android_mvc.sample_project.db.entity.IncomeDetail;

/**
 * 変動費明細を読み書きするクラス。
 * 
 * @author id:language_and_engineering
 */
public class AccountBookDAO extends BaseDAO<AccountBook>
{

    public AccountBookDAO(Context context) {
        helper = new DBHelper(context);
    }

    // ------------ C --------------

    /**
     * 1件の変動費明細を保存。
     */
    public AccountBook create(Integer mokuhyouKingaku, Integer mokuhyouKikan, Calendar startDate)
    {
        // 論理エンティティを構築
        AccountBook a = new AccountBook();
        a.setMokuhyouKingaku(mokuhyouKingaku);
        a.setMokuhyouKikan(mokuhyouKikan);
        a.setStartDate(startDate);

        // DB登録
        a.save(helper);

        return a;
    }

    // ------------ R --------------

    /**
     * 変動費明細を全て登録順に返す。
     */
    public List<AccountBook> findAll()
    {
        return findAll(helper, AccountBook.class);
    }

    /**
     * 家計簿明細のレコードを指定した順番で返す
     */
    public List<AccountBook> findByOrder() {
//        return findAll(helper, AccountBook.class);

        // 有効な主キーを持つ全件を降順に
        return new Finder<AccountBook>(helper)
            .where("id > 0")
            .orderBy("id DESC")
            .findAll(AccountBook.class)
        ;
    }

    /**
     * 特定のIDの友達を１人返す。
     */
    public AccountBook findById(Long account_book_id)
    {
        return findById(helper, AccountBook.class, account_book_id);
    }

    // NOTE: 細かい条件で検索したい場合は，Finderを利用すること。
    // findAllやfindByIdの実装を参照。

    // ------------ U --------------

    public AccountBook update(AccountBook a) {
        if (findById(helper, AccountBook.class, a.getId()) != null) {
            a.save(helper);
        }
        return a;
    }

    // ------------ D --------------

    /**
     * 特定のIDの変動費明細を削除。
     */
    public void deleteById(Long account_book_id)
    {
        AccountBook a = findById(account_book_id);

        // DBからの削除を実行
        a.delete(helper);
    }

}

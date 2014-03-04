package com.android_mvc.sample_project.db.dao;

import java.util.Calendar;
import java.util.List;

import android.content.Context;

import com.android_mvc.framework.db.DBHelper;
import com.android_mvc.framework.db.dao.BaseDAO;
import com.android_mvc.framework.db.dao.Finder;
import com.android_mvc.sample_project.common.Util;
import com.android_mvc.sample_project.db.entity.AccountList;
import com.android_mvc.sample_project.db.schema.ColumnDefinition.AccountListCol;

/**
 * 変動費明細を読み書きするクラス。
 * 
 * @author id:language_and_engineering
 */
public class AccountListDAO extends BaseDAO<AccountList>
{

    public AccountListDAO(Context context) {
        helper = new DBHelper(context);
    }

    // ------------ C --------------

    /**
     * 1件の変動費明細を保存。
     */
    public AccountList create(Calendar ymd, Integer budgetKingaku, Integer settleKingaku)
    {
        // 論理エンティティを構築
        AccountList m = new AccountList();
        m.setYmd(ymd);
        m.setBudgetKingaku(budgetKingaku);
        m.setSettleKingaku(settleKingaku);

        // DB登録
        m.save(helper);

        return m;
    }

    public AccountList create(AccountList a) {
        return create(
                a.getYmd(),
                a.getBudgetKingaku(),
                a.getSettleKingaku()
                );
    }

    // ------------ R --------------

    /**
     * 変動費明細を全て登録順に返す。
     */
    public List<AccountList> findAll()
    {
        return findAll(helper, AccountList.class);
    }

    /**
     * YMDの降順で全件返す。
     * 
     * @return
     */
    public List<AccountList> findOrderByYmdDesc() {
        // 有効な主キーを持つ全件を降順に
        return new Finder<AccountList>(helper)
                .where("id > 0")
                .orderBy(AccountListCol.YMD + " DESC")
                .findAll(AccountList.class);
    }

    /**
     * YMDの昇順で全件返す。
     * 
     * @return
     */
    public List<AccountList> findOrderByYmdAsc() {
        // 有効な主キーを持つ全件を降順に
        return new Finder<AccountList>(helper)
                .where("id > 0")
                .orderBy(AccountListCol.YMD + " ASC")
                .findAll(AccountList.class);
    }

    /**
     * 任意のKeyで降順にソートした結果を返す。KeyにはAccountListColクラスのメンバを指定する。
     * 
     * @param orderKey
     * @return
     */
    public List<AccountList> findOrderByKeyDesc(String orderKey) {

        return new Finder<AccountList>(helper)
                .where("id > 0")
                .orderBy(orderKey + " DESC")
                .findAll(AccountList.class);
    }

    /**
     * 任意のKeyで昇順にソートした結果を返す。KeyにはAccountListColクラスのメンバを指定する。
     * 
     * @param orderKey
     * @return
     */
    public List<AccountList> findOrderByKeyAsc(String orderKey) {

        return new Finder<AccountList>(helper)
                .where("id > 0")
                .orderBy(orderKey + " ASC")
                .findAll(AccountList.class);
    }

    /**
     * 任意のKeyで、In句にvaluesを設定して検索した結果を返す。 KeyにはAccountListColクラスのメンバを指定する。
     * 
     * @param key
     * @param values
     * @return
     */
    public List<AccountList> findWhereInValues(String key, String... values) {
        // where句を設定
        StringBuilder where = new StringBuilder();
        where.append(key + " IN (");

        for (String v : values) {
            where.append("'" + v + "', ");
        }

        where.append("'')");

        Util.d("where句: " + where.toString());

        return new Finder<AccountList>(helper)
                .where(where.toString())
                .orderBy(AccountListCol.YMD + " asc")
                .findAll(AccountList.class);
    }

    /**
     * 任意のKeyで、where条件を設定して検索した結果を返す。 KeyにはAccountListColクラスのメンバを指定する。
     * 
     * @param key
     * @param values
     * @return
     */
    public List<AccountList> findWhere(String key, Object value) {

        // where句を設定
        StringBuilder where = new StringBuilder();

        where.append(key + " = '");
        where.append(value.toString());
        where.append("'");

        Util.d("where句: " + where.toString());

        return new Finder<AccountList>(helper)
                .where(where.toString())
                .findAll(AccountList.class);
    }

    /**
     * 特定のIDの友達を１人返す。
     */
    public AccountList findById(Long id)
    {
        return findById(helper, AccountList.class, id);
    }

    // NOTE: 細かい条件で検索したい場合は，Finderを利用すること。
    // findAllやfindByIdの実装を参照。

    // ------------ U --------------

    public AccountList update(AccountList a) {
        if (findById(helper, AccountList.class, a.getId()) != null) {
            a.save(helper);
        }
        return a;
    }

    // ------------ D --------------

    /**
     * 特定のIDの変動費明細を削除。
     */
    public void deleteById(Long id)
    {
        AccountList a = findById(id);

        // DBからの削除を実行
        a.delete(helper);
    }

}

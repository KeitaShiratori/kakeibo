package com.android_mvc.sample_project.db.dao;

import java.util.Calendar;
import java.util.List;

import android.content.Context;

import com.android_mvc.framework.db.DBHelper;
import com.android_mvc.framework.db.dao.BaseDAO;
import com.android_mvc.framework.db.dao.Finder;
import com.android_mvc.sample_project.common.Util;
import com.android_mvc.sample_project.db.entity.AccountSheetCell;
import com.android_mvc.sample_project.db.schema.ColumnDefinition.AccountSheetCellCol;

/**
 * 変動費明細を読み書きするクラス。
 * 
 * @author id:language_and_engineering
 */
public class AccountSheetCellDAO extends BaseDAO<AccountSheetCell>
{

    public AccountSheetCellDAO(Context context) {
        helper = new DBHelper(context);
    }

    // ------------ C --------------

    /**
     * 1件の変動費明細を保存。
     */
    public AccountSheetCell create(Calendar ymd, Integer budgetKingaku, Integer settleKingaku)
    {
        // 論理エンティティを構築
        AccountSheetCell m = new AccountSheetCell();
        m.setYmd(ymd);
        m.setBudgetKingaku(budgetKingaku);
        m.setSettleKingaku(settleKingaku);

        // DB登録
        m.save(helper);

        return m;
    }

    public AccountSheetCell create(AccountSheetCell a) {
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
    public List<AccountSheetCell> findAll()
    {
        return findAll(helper, AccountSheetCell.class);
    }

    /**
     * YMDの降順で全件返す。
     * 
     * @return
     */
    public List<AccountSheetCell> findOrderByYmdDesc() {
        // 有効な主キーを持つ全件を降順に
        return new Finder<AccountSheetCell>(helper)
                .where("id > 0")
                .orderBy(AccountSheetCellCol.YMD + " DESC")
                .findAll(AccountSheetCell.class);
    }

    /**
     * YMDの昇順で全件返す。
     * 
     * @return
     */
    public List<AccountSheetCell> findOrderByYmdAsc() {
        // 有効な主キーを持つ全件を降順に
        return new Finder<AccountSheetCell>(helper)
                .where("id > 0")
                .orderBy(AccountSheetCellCol.YMD + " ASC")
                .findAll(AccountSheetCell.class);
    }

    /**
     * 任意のKeyで降順にソートした結果を返す。KeyにはAccountSheetCellColクラスのメンバを指定する。
     * 
     * @param orderKey
     * @return
     */
    public List<AccountSheetCell> findOrderByKeyDesc(String orderKey) {

        return new Finder<AccountSheetCell>(helper)
                .where("id > 0")
                .orderBy(orderKey + " DESC")
                .findAll(AccountSheetCell.class);
    }

    /**
     * 任意のKeyで昇順にソートした結果を返す。KeyにはAccountSheetCellColクラスのメンバを指定する。
     * 
     * @param orderKey
     * @return
     */
    public List<AccountSheetCell> findOrderByKeyAsc(String orderKey) {

        return new Finder<AccountSheetCell>(helper)
                .where("id > 0")
                .orderBy(orderKey + " ASC")
                .findAll(AccountSheetCell.class);
    }

    /**
     * 任意のKeyで、In句にvaluesを設定して検索した結果を返す。 KeyにはAccountSheetCellColクラスのメンバを指定する。
     * 
     * @param key
     * @param values
     * @return
     */
    public List<AccountSheetCell> findWhereInValues(String key, String... values) {
        // where句を設定
        StringBuilder where = new StringBuilder();
        where.append(key + " IN (");

        for (String v : values) {
            where.append("'" + v + "', ");
        }

        where.append("'')");

        Util.d("where句: " + where.toString());

        return new Finder<AccountSheetCell>(helper)
                .where(where.toString())
                .orderBy(AccountSheetCellCol.YMD + " asc")
                .findAll(AccountSheetCell.class);
    }

    /**
     * 任意のKeyで、where条件を設定して検索した結果を返す。 KeyにはAccountSheetCellColクラスのメンバを指定する。
     * 
     * @param key
     * @param values
     * @return
     */
    public List<AccountSheetCell> findWhere(String key, Object value) {

        // where句を設定
        StringBuilder where = new StringBuilder();

        where.append(key + " = '");
        where.append(value.toString());
        where.append("'");

        Util.d("where句: " + where.toString());

        return new Finder<AccountSheetCell>(helper)
                .where(where.toString())
                .findAll(AccountSheetCell.class);
    }

    /**
     * 特定のIDの友達を１人返す。
     */
    public AccountSheetCell findById(Long id)
    {
        return findById(helper, AccountSheetCell.class, id);
    }

    // NOTE: 細かい条件で検索したい場合は，Finderを利用すること。
    // findAllやfindByIdの実装を参照。

    // ------------ U --------------

    public AccountSheetCell update(AccountSheetCell a) {
        if (findById(helper, AccountSheetCell.class, a.getId()) != null) {
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
        AccountSheetCell a = findById(id);

        // DBからの削除を実行
        a.delete(helper);
    }

}

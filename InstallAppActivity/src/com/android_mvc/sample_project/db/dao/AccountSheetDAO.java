package com.android_mvc.sample_project.db.dao;

import java.util.Calendar;
import java.util.List;

import android.content.Context;

import com.android_mvc.framework.db.DBHelper;
import com.android_mvc.framework.db.dao.BaseDAO;
import com.android_mvc.framework.db.dao.Finder;
import com.android_mvc.sample_project.common.Util;
import com.android_mvc.sample_project.db.entity.AccountSheet;
import com.android_mvc.sample_project.db.schema.ColumnDefinition.AccountSheetCol;

/**
 * 変動費明細を読み書きするクラス。
 * 
 * @author id:language_and_engineering
 */
public class AccountSheetDAO extends BaseDAO<AccountSheet>
{

    public AccountSheetDAO(Context context) {
        helper = new DBHelper(context);
    }

    // ------------ C --------------

    /**
     * 1件の変動費明細を保存。
     */
    public AccountSheet create(Calendar ymd)
    {
        // 論理エンティティを構築
        AccountSheet m = new AccountSheet();
        m.setYmd(ymd);

        // DB登録
        m.save(helper);

        return m;
    }

    public AccountSheet create(AccountSheet m) {
        return create(
                m.getYmd()
                );
    }

    // ------------ R --------------

    /**
     * 変動費明細を全て登録順に返す。
     */
    public List<AccountSheet> findAll()
    {
        return findAll(helper, AccountSheet.class);
    }

    /**
     * YMDの降順で全件返す。
     * 
     * @return
     */
    public List<AccountSheet> findOrderByYmdDesc() {
        // 有効な主キーを持つ全件を降順に
        return new Finder<AccountSheet>(helper)
                .where("id > 0")
                .orderBy(AccountSheetCol.YMD + " DESC")
                .findAll(AccountSheet.class);
    }

    /**
     * YMDの昇順で全件返す。
     * 
     * @return
     */
    public List<AccountSheet> findOrderByYmdAsc() {
        // 有効な主キーを持つ全件を降順に
        return new Finder<AccountSheet>(helper)
                .where("id > 0")
                .orderBy(AccountSheetCol.YMD + " ASC")
                .findAll(AccountSheet.class);
    }

    /**
     * 任意のKeyで降順にソートした結果を返す。KeyにはAccountSheetColクラスのメンバを指定する。
     * 
     * @param orderKey
     * @return
     */
    public List<AccountSheet> findOrderByKeyDesc(String orderKey) {

        return new Finder<AccountSheet>(helper)
                .where("id > 0")
                .orderBy(orderKey + " DESC")
                .findAll(AccountSheet.class);
    }

    /**
     * 任意のKeyで昇順にソートした結果を返す。KeyにはAccountSheetColクラスのメンバを指定する。
     * 
     * @param orderKey
     * @return
     */
    public List<AccountSheet> findOrderByKeyAsc(String orderKey) {

        return new Finder<AccountSheet>(helper)
                .where("id > 0")
                .orderBy(orderKey + " ASC")
                .findAll(AccountSheet.class);
    }

    /**
     * 任意のKeyで、In句にvaluesを設定して検索した結果を返す。 KeyにはAccountSheetColクラスのメンバを指定する。
     * 
     * @param key
     * @param values
     * @return
     */
    public List<AccountSheet> findWhereInValues(String key, String... values) {
        // where句を設定
        StringBuilder where = new StringBuilder();
        where.append(key + " IN (");

        for (String v : values) {
            where.append("'" + v + "', ");
        }

        where.append("'')");

        Util.d("where句: " + where.toString());

        return new Finder<AccountSheet>(helper)
                .where(where.toString())
                .orderBy(AccountSheetCol.YMD + " asc")
                .findAll(AccountSheet.class);
    }

    /**
     * 任意のKeyで、where条件を設定して検索した結果を返す。 KeyにはAccountSheetColクラスのメンバを指定する。
     * 
     * @param key
     * @param values
     * @return
     */
    public List<AccountSheet> findWhere(String key, Object value) {

        // where句を設定
        StringBuilder where = new StringBuilder();

        where.append(key + " = '");
        where.append(value.toString());
        where.append("'");

        Util.d("where句: " + where.toString());

        return new Finder<AccountSheet>(helper)
                .where(where.toString())
                .findAll(AccountSheet.class);
    }

    /**
     * 特定のIDの友達を１人返す。
     */
    public AccountSheet findById(Long id)
    {
        return findById(helper, AccountSheet.class, id);
    }

    // NOTE: 細かい条件で検索したい場合は，Finderを利用すること。
    // findAllやfindByIdの実装を参照。

    // ------------ U --------------

    public AccountSheet update(AccountSheet a) {
        if (findById(helper, AccountSheet.class, a.getId()) != null) {
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
        AccountSheet a = findById(id);

        // DBからの削除を実行
        a.delete(helper);
    }

}

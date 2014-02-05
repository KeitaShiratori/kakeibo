package com.android_mvc.sample_project.db.dao;

import java.util.Calendar;
import java.util.List;

import android.content.Context;

import com.android_mvc.framework.db.DBHelper;
import com.android_mvc.framework.db.dao.BaseDAO;
import com.android_mvc.framework.db.dao.Finder;
import com.android_mvc.sample_project.common.Util;
import com.android_mvc.sample_project.db.entity.MyWallet;
import com.android_mvc.sample_project.db.entity.MyWallet;
import com.android_mvc.sample_project.db.schema.ColumnDefinition.MyWalletCol;

/**
 * 変動費明細を読み書きするクラス。
 * 
 * @author id:language_and_engineering
 */
public class MyWalletDAO extends BaseDAO<MyWallet>
{

    public MyWalletDAO(Context context) {
        helper = new DBHelper(context);
    }

    // ------------ C --------------

    /**
     * 1件の変動費明細を保存。
     */
    public MyWallet create(Calendar ymd, Integer kingaku, Integer zandaka, Integer hikidashi)
    {
        // 論理エンティティを構築
        MyWallet m = new MyWallet();
        m.setYmd(ymd);
        m.setKingaku(kingaku);
        m.setZandaka(zandaka);
        m.setHikidashi(hikidashi);

        // DB登録
        m.save(helper);

        return m;
    }

    public MyWallet create(MyWallet m) {
        return create(
                m.getYmd(),
                m.getKingaku(),
                m.getZandaka(),
                m.getHikidashi());
    }

    // ------------ R --------------

    /**
     * 変動費明細を全て登録順に返す。
     */
    public List<MyWallet> findAll()
    {
        return findAll(helper, MyWallet.class);
    }

    /**
     * YMDの降順で全件返す。
     * 
     * @return
     */
    public List<MyWallet> findOrderByYMDDesc() {
        // 有効な主キーを持つ全件を降順に
        return new Finder<MyWallet>(helper)
                .where("id > 0")
                .orderBy(MyWalletCol.YMD + " DESC")
                .findAll(MyWallet.class);
    }

    /**
     * YMDの昇順で全件返す。
     * 
     * @return
     */
    public List<MyWallet> findOrderByYMDAsc() {
        // 有効な主キーを持つ全件を降順に
        return new Finder<MyWallet>(helper)
                .where("id > 0")
                .orderBy(MyWalletCol.YMD + " ASC")
                .findAll(MyWallet.class);
    }

    /**
     * 任意のKeyで降順にソートした結果を返す。KeyにはMyWalletColクラスのメンバを指定する。
     * 
     * @param orderKey
     * @return
     */
    public List<MyWallet> findOrderByKeyDesc(String orderKey) {

        return new Finder<MyWallet>(helper)
                .where("id > 0")
                .orderBy(orderKey + " DESC")
                .findAll(MyWallet.class);
    }

    /**
     * 任意のKeyで昇順にソートした結果を返す。KeyにはMyWalletColクラスのメンバを指定する。
     * 
     * @param orderKey
     * @return
     */
    public List<MyWallet> findOrderByKeyAsc(String orderKey) {

        return new Finder<MyWallet>(helper)
                .where("id > 0")
                .orderBy(orderKey + " ASC")
                .findAll(MyWallet.class);
    }

    /**
     * 任意のKeyで、In句にvaluesを設定して検索した結果を返す。 KeyにはMyWalletColクラスのメンバを指定する。
     * 
     * @param key
     * @param values
     * @return
     */
    public List<MyWallet> findWhereInValues(String key, String... values) {
        // where句を設定
        StringBuilder where = new StringBuilder();
        where.append(key + " IN (");

        for (String v : values) {
            where.append("'" + v + "', ");
        }

        where.append("'')");

        Util.d("where句: " + where.toString());

        return new Finder<MyWallet>(helper)
                .where(where.toString())
                .orderBy(MyWalletCol.YMD + " asc")
                .findAll(MyWallet.class);
    }

    /**
     * 任意のKeyで、where条件を設定して検索した結果を返す。 KeyにはMyWalletColクラスのメンバを指定する。
     * 
     * @param key
     * @param values
     * @return
     */
    public List<MyWallet> findWhere(String key, Object value) {

        // where句を設定
        StringBuilder where = new StringBuilder();

        where.append(key + " = '");
        where.append(value.toString());
        where.append("'");

        Util.d("where句: " + where.toString());

        return new Finder<MyWallet>(helper)
                .where(where.toString())
                .findAll(MyWallet.class);
    }

    /**
     * 特定のIDの友達を１人返す。
     */
    public MyWallet findById(Long my_wallet_id)
    {
        return findById(helper, MyWallet.class, my_wallet_id);
    }

    // NOTE: 細かい条件で検索したい場合は，Finderを利用すること。
    // findAllやfindByIdの実装を参照。

    // ------------ U --------------

    public MyWallet update(MyWallet c) {
        if (findById(helper, MyWallet.class, c.getId()) != null) {
            c.save(helper);
        }
        return c;
    }

    // ------------ D --------------

    /**
     * 特定のIDの変動費明細を削除。
     */
    public void deleteById(Long my_wallet_id)
    {
        MyWallet v = findById(my_wallet_id);

        // DBからの削除を実行
        v.delete(helper);
    }

}

package com.android_mvc.sample_project.db.dao;

import java.util.Calendar;
import java.util.List;

import android.content.Context;

import com.android_mvc.framework.db.DBHelper;
import com.android_mvc.framework.db.dao.BaseDAO;
import com.android_mvc.framework.db.dao.Finder;
import com.android_mvc.sample_project.common.Util;
import com.android_mvc.sample_project.db.entity.CreditCardSetting;
import com.android_mvc.sample_project.db.schema.ColumnDefinition.CreditCardSettingCol;

/**
 * 変動費明細を読み書きするクラス。
 * 
 * @author id:language_and_engineering
 */
public class CreditCardSettingDAO extends BaseDAO<CreditCardSetting>
{

    public CreditCardSettingDAO(Context context) {
        helper = new DBHelper(context);
    }

    // ------------ C --------------

    /**
     * 1件の変動費明細を保存。
     */
    public CreditCardSetting create(Calendar simeYmd, Calendar siharaiYmd)
    {
        // 論理エンティティを構築
        CreditCardSetting m = new CreditCardSetting();
        m.setSimeYmd(simeYmd);
        m.setSiharaiYmd(siharaiYmd);

        // DB登録
        m.save(helper);

        return m;
    }

    public CreditCardSetting create(CreditCardSetting c) {
        return create(
                c.getSimeYmd(),
                c.getSiharaiYmd()
                );
    }

    // ------------ R --------------

    /**
     * 変動費明細を全て登録順に返す。
     */
    public List<CreditCardSetting> findAll()
    {
        return findAll(helper, CreditCardSetting.class);
    }

    /**
     * 任意のKeyで降順にソートした結果を返す。KeyにはCreditCardSettingColクラスのメンバを指定する。
     * 
     * @param orderKey
     * @return
     */
    public List<CreditCardSetting> findOrderByKeyDesc(String orderKey) {

        return new Finder<CreditCardSetting>(helper)
                .where("id > 0")
                .orderBy(orderKey + " DESC")
                .findAll(CreditCardSetting.class);
    }

    /**
     * 任意のKeyで昇順にソートした結果を返す。KeyにはCreditCardSettingColクラスのメンバを指定する。
     * 
     * @param orderKey
     * @return
     */
    public List<CreditCardSetting> findOrderByKeyAsc(String orderKey) {

        return new Finder<CreditCardSetting>(helper)
                .where("id > 0")
                .orderBy(orderKey + " ASC")
                .findAll(CreditCardSetting.class);
    }

    /**
     * 任意のKeyで、In句にvaluesを設定して検索した結果を返す。 KeyにはCreditCardSettingColクラスのメンバを指定する。
     * 
     * @param key
     * @param values
     * @return
     */
    public List<CreditCardSetting> findWhereInValues(String key, String... values) {
        // where句を設定
        StringBuilder where = new StringBuilder();
        where.append(key + " IN (");

        for (String v : values) {
            where.append("'" + v + "', ");
        }

        where.append("'')");

        Util.d("where句: " + where.toString());

        return new Finder<CreditCardSetting>(helper)
                .where(where.toString())
                .orderBy(CreditCardSettingCol.ID + " asc")
                .findAll(CreditCardSetting.class);
    }

    /**
     * 任意のKeyで、where条件を設定して検索した結果を返す。 KeyにはCreditCardSettingColクラスのメンバを指定する。
     * 
     * @param key
     * @param values
     * @return
     */
    public List<CreditCardSetting> findWhere(String key, Object value) {

        // where句を設定
        StringBuilder where = new StringBuilder();

        where.append(key + " = '");
        where.append(value.toString());
        where.append("'");

        Util.d("where句: " + where.toString());

        return new Finder<CreditCardSetting>(helper)
                .where(where.toString())
                .findAll(CreditCardSetting.class);
    }

    /**
     * 特定のIDのレコードを１件返す。
     */
    public CreditCardSetting findById(Long id)
    {
        return findById(helper, CreditCardSetting.class, id);
    }

    /**
     * 最新のレコードを１件返す。
     * @return
     */
    public CreditCardSetting findNewestOne(){
        return findNewestOne(helper, CreditCardSetting.class);
    }
    // NOTE: 細かい条件で検索したい場合は，Finderを利用すること。
    // findAllやfindByIdの実装を参照。

    // ------------ U --------------

    public CreditCardSetting update(CreditCardSetting a) {
        if (findById(helper, CreditCardSetting.class, a.getId()) != null) {
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
        CreditCardSetting a = findById(id);

        // DBからの削除を実行
        a.delete(helper);
    }

}

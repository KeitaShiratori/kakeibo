package com.android_mvc.sample_project.db.dao;

import java.util.List;

import android.content.Context;

import com.android_mvc.framework.db.DBHelper;
import com.android_mvc.framework.db.dao.BaseDAO;
import com.android_mvc.framework.db.dao.Finder;
import com.android_mvc.sample_project.db.entity.PayType;

/**
 * 変動費明細を読み書きするクラス。
 * 
 * @author id:language_and_engineering
 */
public class PayTypeDAO extends BaseDAO<PayType>
{

    public PayTypeDAO(Context context) {
        helper = new DBHelper(context);
    }

    // ------------ C --------------

    /**
     * 1件の変動費明細を保存。
     */
    public PayType create(String PayTypeName)
    {
        // 論理エンティティを構築
        PayType p = new PayType();
        p.setPayTypeName(PayTypeName);

        // DB登録
        p.save(helper);

        return p;
    }

    // ------------ R --------------

    /**
     * 変動費明細を全てIDの昇順に返す。
     */
    public List<PayType> findAll()
    {
        return new Finder<PayType>(helper)
                .where("id > 0")
                .orderBy("id ASC")
                .findAll(PayType.class);
    }

    /**
     * 特定のIDの友達を１人返す。
     */
    public PayType findById(Long pay_type_id)
    {
        return findById(helper, PayType.class, pay_type_id);
    }

    // NOTE: 細かい条件で検索したい場合は，Finderを利用すること。
    // findAllやfindByIdの実装を参照。

    // ------------ U --------------

    // ------------ D --------------

    /**
     * 特定のIDの変動費明細を削除。
     */
    public void deleteById(Long pay_type_id)
    {
        PayType p = findById(pay_type_id);

        // DBからの削除を実行
        p.delete(helper);
    }

}

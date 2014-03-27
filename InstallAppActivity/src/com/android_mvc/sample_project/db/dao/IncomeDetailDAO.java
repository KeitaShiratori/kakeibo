package com.android_mvc.sample_project.db.dao;

import java.util.Calendar;
import java.util.List;

import android.content.Context;

import com.android_mvc.framework.db.DBHelper;
import com.android_mvc.framework.db.dao.BaseDAO;
import com.android_mvc.framework.db.dao.Finder;
import com.android_mvc.sample_project.common.Util;
import com.android_mvc.sample_project.db.entity.IncomeDetail;
import com.android_mvc.sample_project.db.schema.ColumnDefinition.IncomeDetailCol;

/**
 * 変動費明細を読み書きするクラス。
 * 
 * @author id:language_and_engineering
 */
public class IncomeDetailDAO extends BaseDAO<IncomeDetail>
{

    public IncomeDetailDAO(Context context) {
        helper = new DBHelper(context);
    }

    // ------------ C --------------

    /**
     * 1件の変動費明細を保存。
     */
    public IncomeDetail create(Integer categoryType, Integer payType, Calendar budgetYMD, Integer budgetIncome, Integer settleIncome)
    {
        // 論理エンティティを構築
        IncomeDetail v = new IncomeDetail();
        v.setCategoryType(categoryType);
        v.setPayType(payType);
        v.setBudgetYmd(budgetYMD);
        v.setBudgetIncome(budgetIncome);
        v.setSettleIncome(settleIncome);

        // DB登録
        v.save(helper);

        return v;
    }

    public IncomeDetail create(IncomeDetail c) {
        return create(c.getCategoryType(), c.getPayType(), c.getBudgetYmd(), c.getBudgetIncome(), c.getSettleIncome());
    }

    // ------------ R --------------

    /**
     * 変動費明細を全て登録順に返す。
     */
    public List<IncomeDetail> findAll()
    {
        return findAll(helper, IncomeDetail.class);
    }

    public List<IncomeDetail> findOrderByBudgetYmd() {
        return new Finder<IncomeDetail>(helper)
                .where("id > 0")
                .orderBy(IncomeDetailCol.BUDGET_YMD + " DESC")
                .findAll(IncomeDetail.class);
    }

    /**
     * 任意のKeyで、where条件を設定して検索した結果を返す。 KeyにはIncomeDetailColクラスのメンバを指定する。
     * 
     * @param key
     * @param values
     * @return
     */
    public List<IncomeDetail> findWhere(String key, Object value) {

        // where句を設定
        StringBuilder where = new StringBuilder();

        where.append(key + " = '");
        where.append(value.toString());
        where.append("'");

        Util.d("where句: " + where.toString());

        return new Finder<IncomeDetail>(helper)
                .where(where.toString())
                .findAll(IncomeDetail.class);
    }

    /**
     * 特定のIDの友達を１人返す。
     */
    public IncomeDetail findById(Long cost_detail_id)
    {
        return findById(helper, IncomeDetail.class, cost_detail_id);
    }

    // NOTE: 細かい条件で検索したい場合は，Finderを利用すること。
    // findAllやfindByIdの実装を参照。

    // ------------ U --------------

    public IncomeDetail update(IncomeDetail c) {
        if (findById(helper, IncomeDetail.class, c.getId()) != null) {
            c.save(helper);
        }
        return c;
    }

    // ------------ D --------------

    /**
     * 特定のIDの変動費明細を削除。
     */
    public void deleteById(Long cost_detail_id)
    {
        IncomeDetail v = findById(cost_detail_id);

        // DBからの削除を実行
        v.delete(helper);
    }

}

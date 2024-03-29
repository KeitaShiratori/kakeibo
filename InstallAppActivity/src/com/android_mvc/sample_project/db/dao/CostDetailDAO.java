package com.android_mvc.sample_project.db.dao;

import java.util.Calendar;
import java.util.List;

import android.content.Context;

import com.android_mvc.framework.db.DBHelper;
import com.android_mvc.framework.db.dao.BaseDAO;
import com.android_mvc.framework.db.dao.Finder;
import com.android_mvc.sample_project.common.Util;
import com.android_mvc.sample_project.db.entity.CostDetail;
import com.android_mvc.sample_project.db.schema.ColumnDefinition.CostDetailCol;

/**
 * 変動費明細を読み書きするクラス。
 * 
 * @author id:language_and_engineering
 */
public class CostDetailDAO extends BaseDAO<CostDetail>
{

    public CostDetailDAO(Context context) {
        helper = new DBHelper(context);
    }

    // ------------ C --------------

    /**
     * 1件の変動費明細を保存。
     */
    public CostDetail create(Integer categoryType, Integer payType, Calendar budgetYMD, Integer budgetCost, Integer settleCost, Integer divideNum)
    {
        // 論理エンティティを構築
        CostDetail v = new CostDetail();
        v.setCategoryType(categoryType);
        v.setPayType(payType);
        v.setBudgetYmd(budgetYMD);
        v.setBudgetCost(budgetCost);
        v.setSettleCost(settleCost);
        v.setDivideNum(divideNum);

        Util.d(v.getBudgetYmd().toString());

        // DB登録
        v.save(helper);

        return v;
    }

    public CostDetail create(CostDetail c) {
        return create(c.getCategoryType(), c.getPayType(), c.getBudgetYmd(), c.getBudgetCost(), c.getSettleCost(), c.getDivideNum());
    }

    // ------------ R --------------

    /**
     * 変動費明細を全て登録順に返す。
     */
    public List<CostDetail> findAll()
    {
        return findAll(helper, CostDetail.class);
    }

    public List<CostDetail> findByOrder() {
        // 有効な主キーを持つ全件を降順に
        return new Finder<CostDetail>(helper)
                .where("id > 0")
                .orderBy(CostDetailCol.BUDGET_YMD + " DESC,"
                        + CostDetailCol.CATEGORY_TYPE + " DESC,"
                        + CostDetailCol.PAY_TYPE + " DESC")
                .findAll(CostDetail.class);
    }

    public List<CostDetail> findOrderByDesc(String orderKey) {

        return new Finder<CostDetail>(helper)
                .where("id > 0")
                .orderBy(orderKey + " DESC")
                .findAll(CostDetail.class);
    }

    public List<CostDetail> findOrderByDesc(String... orderKey) {

        String orderBy = new String();
        for (String key : orderKey) {
            orderBy += key;
            orderBy += " DESC,";
        }

        // 最後の","を除去
        Util.d("orderBy: " + orderBy.toString());
        Util.d("orderBy.length: " + orderBy.length());
        orderBy.substring(0, orderBy.length() - 1);
        Util.d("substringed orderBy: " + orderBy.toString());
        Util.d("substringed orderBy.length: " + orderBy.length());

        return new Finder<CostDetail>(helper)
                .where("id > 0")
                .orderBy(orderBy)
                .findAll(CostDetail.class);
    }

    public List<CostDetail> findOrderByAsc(String orderKey) {

        return new Finder<CostDetail>(helper)
                .where("id > 0")
                .orderBy(orderKey + " ASC")
                .findAll(CostDetail.class);
    }

    public List<CostDetail> findOrderByAsc(String... orderKey) {

        String orderBy = new String();
        for (String key : orderKey) {
            orderBy += key;
            orderBy += " ASC,";
        }

        // 最後の","を除去
        Util.d("orderBy: " + orderBy.toString());
        Util.d("orderBy.length: " + orderBy.length());
        orderBy = orderBy.substring(0, orderBy.length() - 1);
        Util.d("substringed orderBy: " + orderBy.toString());
        Util.d("substringed orderBy.length: " + orderBy.length());

        return new Finder<CostDetail>(helper)
                .where("id > 0")
                .orderBy(orderBy)
                .findAll(CostDetail.class);
    }

    public List<CostDetail> findWhereIn(String key, String... values) {
        // where句を設定
        StringBuilder where = new StringBuilder();
        where.append(key + " IN (");

        for (String v : values) {
            where.append("'" + v + "', ");
        }

        where.append("'')");

        Util.d("where句: " + where.toString());

        return new Finder<CostDetail>(helper)
                .where(where.toString())
                .orderBy("Budget_YMD asc")
                .findAll(CostDetail.class);
    }

    /**
     * 任意のKeyで、where条件を設定して検索した結果を返す。 KeyにはCostDetailColクラスのメンバを指定する。
     * 
     * @param key
     * @param values
     * @return
     */
    public List<CostDetail> findWhere(String key, Object value) {

        // where句を設定
        StringBuilder where = new StringBuilder();

        where.append(key + " = '");
        where.append(value.toString());
        where.append("'");

        Util.d("where句: " + where.toString());

        return new Finder<CostDetail>(helper)
                .where(where.toString())
                .findAll(CostDetail.class);
    }

    /**
     * 特定のIDの友達を１人返す。
     */
    public CostDetail findById(Long cost_detail_id)
    {
        return findById(helper, CostDetail.class, cost_detail_id);
    }

    // NOTE: 細かい条件で検索したい場合は，Finderを利用すること。
    // findAllやfindByIdの実装を参照。

    // ------------ U --------------

    public CostDetail update(CostDetail c) {
        if (findById(helper, CostDetail.class, c.getId()) != null) {
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
        CostDetail v = findById(cost_detail_id);

        // DBからの削除を実行
        v.delete(helper);
    }

}

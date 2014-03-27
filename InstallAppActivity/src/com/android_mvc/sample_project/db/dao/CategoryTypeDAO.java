package com.android_mvc.sample_project.db.dao;

import java.util.List;

import android.content.Context;

import com.android_mvc.framework.db.DBHelper;
import com.android_mvc.framework.db.dao.BaseDAO;
import com.android_mvc.framework.db.dao.Finder;
import com.android_mvc.sample_project.common.Util;
import com.android_mvc.sample_project.db.entity.CategoryType;

/**
 * 変動費明細を読み書きするクラス。
 * 
 * @author id:language_and_engineering
 */
public class CategoryTypeDAO extends BaseDAO<CategoryType>
{

    public CategoryTypeDAO(Context context) {
        helper = new DBHelper(context);
    }

    // ------------ C --------------

    /**
     * 1件の変動費明細を保存。
     */
    public CategoryType create(String categoryTypeName, Integer parentCategoryType)
    {
        // 論理エンティティを構築
        CategoryType c = new CategoryType();
        c.setCategoryTypeName(categoryTypeName);
        c.setParentCategoryType(parentCategoryType);

        // DB登録
        c.save(helper);

        return c;
    }

    // ------------ R --------------

    /**
     * 変動費明細を全て登録順に返す。
     */
    public List<CategoryType> findAll()
    {
        return new Finder<CategoryType>(helper)
                .where("id > 0")
                .orderBy("id ASC")
                .findAll(CategoryType.class);
    }

    /**
     * 特定のIDのカテゴリを１つ返す。
     */
    public CategoryType findById(Long category_type_id)
    {
        return findById(helper, CategoryType.class, category_type_id);
    }

    /**
     * 任意のKeyで降順にソートした結果を返す。KeyにはCategoryTypeColクラスのメンバを指定する。
     * 
     * @param orderKey
     * @return
     */
    public List<CategoryType> findOrderByKeyDesc(String orderKey) {

        return new Finder<CategoryType>(helper)
                .where("id > 0")
                .orderBy(orderKey + " DESC")
                .findAll(CategoryType.class);
    }

    /**
     * 任意のKeyで昇順にソートした結果を返す。KeyにはCategoryTypeColクラスのメンバを指定する。
     * 
     * @param orderKey
     * @return
     */
    public List<CategoryType> findOrderByKeyAsc(String orderKey) {

        return new Finder<CategoryType>(helper)
                .where("id > 0")
                .orderBy(orderKey + " ASC")
                .findAll(CategoryType.class);
    }

    /**
     * 任意のKeyで、where条件を設定して検索した結果を返す。 KeyにはCategoryTypeColクラスのメンバを指定する。
     * 
     * @param key
     * @param values
     * @return
     */
    public List<CategoryType> findWhere(String key, Object value) {

        // where句を設定
        StringBuilder where = new StringBuilder();

        where.append(key + " = '");
        where.append(value.toString());
        where.append("'");

        Util.d("where句: " + where.toString());

        return new Finder<CategoryType>(helper)
                .where(where.toString())
                .findAll(CategoryType.class);
    }

    // NOTE: 細かい条件で検索したい場合は，Finderを利用すること。
    // findAllやfindByIdの実装を参照。

    // ------------ U --------------

    // ------------ D --------------

    /**
     * 特定のIDの変動費明細を削除。
     */
    public void deleteById(Long category_type_id)
    {
        CategoryType c = findById(category_type_id);

        // DBからの削除を実行
        c.delete(helper);
    }

}

package com.android_mvc.sample_project.db.entity;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.android_mvc.framework.common.FWUtil;
import com.android_mvc.framework.db.dao.Finder;
import com.android_mvc.sample_project.db.dao.CategoryTypeDAO;
import com.android_mvc.sample_project.db.entity.lib.LogicalEntity;
import com.android_mvc.sample_project.db.schema.ColumnDefinition.CategoryTypeCol;

public class CategoryType extends LogicalEntity<CategoryType> {
    // Intent経由でエンティティを運搬可能にするために
    private static final long serialVersionUID = 1L;

    @Override
    public String tableName() {
        return "category_type";
    }

    @Override
    public final String[] columns() {
        return new String[] {
                CategoryTypeCol.ID,
                CategoryTypeCol.CATEGORY_TYPE_NAME,
                CategoryTypeCol.PARENT_CATEGORY_TYPE,
        };
    }

    // メンバ
    private String category_type_name = null;
    private Integer parent_category_type = null;

    // IDEが自動生成したG&S

    public String getCategoryTypeName() {
        return category_type_name;
    }

    public void setCategoryTypeName(String category_type_name) {
        this.category_type_name = category_type_name;
    }

    public Integer getParentCategoryType() {
        return parent_category_type;
    }

    public void setParentCategoryType(Integer parent_category_type) {
        this.parent_category_type = parent_category_type;
    }

    // カスタムG&S

    public Spinner getSpinner(Context context) {
        Spinner ret = new Spinner(context);
        List<CategoryType> categoryList = new CategoryTypeDAO(context).findAll();
        List<CharSequence> list = new ArrayList<CharSequence>();
        for (CategoryType c : categoryList) {
            list.add(c.getCategoryTypeName());
        }
        ArrayAdapter<CharSequence> categoryAdapter = new ArrayAdapter<CharSequence>(context, android.R.layout.simple_spinner_item, list);

        ret.setAdapter(categoryAdapter);
        return ret;
    }

    // ----- LP変換(Logical <-> Physical) -----

    /**
     * DBの格納値から論理エンティティを構成
     */
    @Override
    public CategoryType logicalFromPhysical(Cursor c)
    {
        setId(c.getLong(0));
        setCategoryTypeName(c.getString(1));
        setParentCategoryType(c.getInt(2));

        return this;
    }

    /**
     * 自身をDBに新規登録可能なデータ型に変換して返す
     */
    @Override
    protected ContentValues toPhysicalEntity(ContentValues values)
    {
        // entityをContentValueに変換

        if (getId() != null)
        {
            values.put(CategoryTypeCol.ID, getId());
        }

        if (getCategoryTypeName() != null) {
            values.put(CategoryTypeCol.CATEGORY_TYPE_NAME, getCategoryTypeName());
        }

        values.put(CategoryTypeCol.PARENT_CATEGORY_TYPE, getParentCategoryType());

        return values;
    }

}

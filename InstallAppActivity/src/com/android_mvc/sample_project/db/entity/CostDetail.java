package com.android_mvc.sample_project.db.entity;

import java.util.Calendar;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.view.Gravity;
import android.view.View.OnClickListener;

import com.android_mvc.framework.ui.view.MButton;
import com.android_mvc.framework.ui.view.MLinearLayout;
import com.android_mvc.framework.ui.view.MTextView;
import com.android_mvc.sample_project.R;
import com.android_mvc.sample_project.R.drawable;
import com.android_mvc.sample_project.db.dao.CategoryTypeDAO;
import com.android_mvc.sample_project.db.dao.PayTypeDAO;
import com.android_mvc.sample_project.db.entity.lib.LPUtil;
import com.android_mvc.sample_project.db.entity.lib.LogicalEntity;
import com.android_mvc.sample_project.db.schema.ColumnDefinition.CostDetailCol;

public class CostDetail extends LogicalEntity<CostDetail> {
    // Intent経由でエンティティを運搬可能にするために
    private static final long serialVersionUID = 1L;

    @Override
    public String tableName() {
        return "cost_detail";
    }

    @Override
    public final String[] columns() {
        return new String[] {
                CostDetailCol.ID,
                CostDetailCol.CATEGORY_TYPE,
                CostDetailCol.PAY_TYPE,
                CostDetailCol.BUDGET_YMD,
                CostDetailCol.BUDGET_COST,
                CostDetailCol.SETTLE_YMD,
                CostDetailCol.SETTLE_COST
        };
    }

    // メンバ
    private Integer category_type = null;
    private Integer pay_type = null;
    private Calendar budget_ymd = null;
    private Integer budget_cost = null;
    private Calendar settle_ymd = null;
    private Integer settle_cost = null;

    // IDEが自動生成したG&S

    public Integer getCategoryType() {
        return category_type;
    }

    public void setCategoryType(Integer category_type) {
        this.category_type = category_type;
    }

    public Integer getPayType() {
        return pay_type;
    }

    public void setPayType(Integer pay_type) {
        this.pay_type = pay_type;
    }

    public Calendar getBudgetYmd() {
        return budget_ymd;
    }

    public void setBudgetYmd(Calendar budget_ymd) {
        this.budget_ymd = budget_ymd;
    }

    public Integer getBudgetCost() {
        return budget_cost;
    }

    public void setBudgetCost(Integer budget_cost) {
        this.budget_cost = budget_cost;
    }

    public Calendar getSettleYmd() {
        return settle_ymd;
    }

    public void setSettleYmd(Calendar settle_ymd) {
        this.settle_ymd = settle_ymd;
    }

    public Integer getSettleCost() {
        return settle_cost;
    }

    public void setSettleCost(Integer settle_cost) {
        this.settle_cost = settle_cost;
    }

    // カスタムG&S

    // 有効日付を返す
    public Calendar getEffectiveYMD() {
        Calendar ret = null;

        if (this.settle_ymd != null) {
            ret = this.settle_ymd;
        }
        else if (this.budget_ymd != null) {
            ret = this.budget_ymd;
        }

        return ret;
    }

    public Integer getEffectiveCost() {
        Integer ret = null;

        if (this.settle_cost != null) {
            ret = this.settle_cost;
        }
        else if (this.budget_cost != null) {
            ret = this.budget_cost;
        }

        return ret;
    }

    public Integer getEffectiveSettleCost() {
        Integer ret = 0;

        if (this.settle_cost != null) {
            ret = this.settle_cost;
        }

        return ret;
    }

    /**
     * 変動費明細のレコードを表示する
     */
    public MLinearLayout getDescription(Activity activity, Context context, OnClickListener l1, OnClickListener l2) {
        String budgetCost = (getBudgetCost() == null) ? "未入力" : getBudgetCost() + "円";
        String settleCost = (getSettleCost() == null) ? "未入力" : getSettleCost() + "円";
        String categoryType = (getCategoryType() == null) ? "未入力" : new CategoryTypeDAO(activity).findById(Long.parseLong(getCategoryType().toString())).getCategoryTypeName();
        String payType = (getPayType() == null) ? "未入力" : new PayTypeDAO(activity).findById(Long.parseLong(getPayType().toString())).getPayTypeName();

        return new MLinearLayout(activity)
                .orientationVertical()
                .widthWrapContent()
                .heightWrapContent()
                .paddingLeftPx(10)
                .backgroundDrawable(R.drawable.border)
                .add(
                        new MLinearLayout(activity)
                                .orientationHorizontal()
                                .widthFillParent()
                                .heightWrapContent()
                                .add(
                                        new MTextView(activity)
                                                .gravity(Gravity.CENTER_VERTICAL)
                                                .text(categoryType)
                                                .backgroundDrawable(drawable.record_design)
                                                .widthWrapContent()
                                        ,
                                        new MTextView(activity)
                                                .gravity(Gravity.CENTER_VERTICAL)
                                                .text("予算: " + budgetCost)
                                                .backgroundDrawable(drawable.record_design)
                                                .widthWrapContent()
                                        ,
                                        new MTextView(activity)
                                                .gravity(Gravity.CENTER_VERTICAL)
                                                .text("変更")
                                                .backgroundDrawable(R.drawable.button_design_1)
                                                .click(l1)
                                )
                        ,

                        new MLinearLayout(activity)
                                .orientationHorizontal()
                                .widthFillParent()
                                .heightWrapContent()
                                .add(

                                        new MTextView(activity)
                                                .gravity(Gravity.CENTER_VERTICAL)
                                                .text(payType)
                                                .backgroundDrawable(drawable.record_design)
                                                .widthWrapContent()
                                        ,
                                        new MTextView(activity)
                                                .text("実績: " + settleCost)
                                                .gravity(Gravity.CENTER_VERTICAL)
                                                .backgroundDrawable(drawable.record_design)
                                                .widthWrapContent()
                                        ,
                                        new MTextView(activity)
                                                .text("削除")
                                                .backgroundDrawable(R.drawable.button_design_1)
                                                .gravity(Gravity.CENTER_VERTICAL)
                                                .click(l2)

                                )
                );

    }

    // ----- LP変換(Logical <-> Physical) -----

    /**
     * DBの格納値から論理エンティティを構成
     */
    @Override
    public CostDetail logicalFromPhysical(Cursor c)
    {
        setId(c.getLong(0));
        setCategoryType(c.getInt(1));
        setPayType(c.getInt(2));
        setBudgetYmd(LPUtil.decodeTextToCalendar(c.getString(3)));
        setBudgetCost(Integer.parseInt(c.getString(4)));
        String tmp = c.getString(5);
        if (!tmp.isEmpty()) {
            setSettleYmd(LPUtil.decodeTextToCalendar(c.getString(5)));
        } else {
            setSettleYmd(null);
        }
        tmp = c.getString(6);
        if (!tmp.isEmpty()) {
            setSettleCost(Integer.parseInt(c.getString(6)));
        } else {
            setSettleCost(null);
        }

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
            values.put("id", getId());
        }

        if (getCategoryType() != null) {
            values.put("category_type", getCategoryType());
        }

        if (getPayType() != null) {
            values.put("pay_type", getPayType());
        }

        if (getBudgetYmd() != null) {
            values.put("budget_ymd", LPUtil.encodeCalendarToText(getBudgetYmd()));
        }

        if (getBudgetCost() != null) {
            values.put("budget_cost", getBudgetCost().toString());
        }

        if (getSettleYmd() != null) {
            values.put("settle_ymd", LPUtil.encodeCalendarToText(getSettleYmd()));
        } else {
            values.put("settle_ymd", "");
        }

        if (getSettleCost() != null) {
            values.put("settle_cost", getSettleCost().toString());
        } else {
            values.put("settle_cost", "");
        }
        return values;
    }

}

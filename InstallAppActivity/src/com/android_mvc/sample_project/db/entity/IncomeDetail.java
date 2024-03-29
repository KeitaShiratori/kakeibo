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
import com.android_mvc.sample_project.db.schema.ColumnDefinition.IncomeDetailCol;

public class IncomeDetail extends LogicalEntity<IncomeDetail> {
    // Intent経由でエンティティを運搬可能にするために
    private static final long serialVersionUID = 1L;

    @Override
    public String tableName() {
        return "income_detail";
    }

    @Override
    public final String[] columns() {
        return new String[] {
                IncomeDetailCol.ID,
                IncomeDetailCol.CATEGORY_TYPE,
                IncomeDetailCol.PAY_TYPE,
                IncomeDetailCol.BUDGET_YMD,
                IncomeDetailCol.BUDGET_INCOME,
                IncomeDetailCol.SETTLE_INCOME
        };
    }

    // メンバ
    private Integer category_type = null;
    private Integer pay_type = null;
    private Calendar budget_ymd = null;
    private Integer budget_income = null;
    private Integer settle_income = null;

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

    public Integer getBudgetIncome() {
        return budget_income;
    }

    public void setBudgetIncome(Integer budget_income) {
        this.budget_income = budget_income;
    }

    public Integer getSettleIncome() {
        return settle_income;
    }

    public void setSettleIncome(Integer settle_income) {
        this.settle_income = settle_income;
    }

    // カスタムG&S

    // 有効日付を返す
    public Calendar getEffectiveYMD() {
        Calendar ret = null;

        if (this.budget_ymd != null) {
            ret = this.budget_ymd;
        }

        return ret;
    }

    public Integer getEffectiveIncome() {
        Integer ret = null;

        if (this.settle_income != null) {
            ret = this.settle_income;
        }
        else if (this.budget_income != null) {
            ret = this.budget_income;
        }

        return ret;
    }

    public Integer getEffectiveSettleIncome() {
        Integer ret = 0;

        if (this.settle_income != null) {
            ret = this.settle_income;
        }

        return ret;
    }

    /**
     * 繰り越し判定用
     * 
     * @return
     */
    public boolean isKurikosi() {
        return (getCategoryType() != null && getCategoryType() == 15);
    }

    /**
     * 変動費明細のレコードを表示する
     */
    public MLinearLayout getDescription(Activity activity, Context context, OnClickListener l1, OnClickListener l2) {
        String budgetIncome = (getBudgetIncome() == null) ? "未入力" : getBudgetIncome() + "円";
        String settleIncome = (getSettleIncome() == null) ? "未入力" : getSettleIncome() + "円";

        return new MLinearLayout(activity)
                .orientationVertical()
                .widthWrapContent()
                .heightWrapContent()
                .backgroundDrawable(R.drawable.border)
                .add(
                        new MLinearLayout(activity)
                                .orientationHorizontal()
                                .widthFillParent()
                                .heightWrapContent()
                                .add(
                                        new MTextView(activity)
                                                .gravity(Gravity.CENTER_VERTICAL)
                                                .text(isKurikosi() ? "繰り越し" : "")
                                                .backgroundDrawable(drawable.record_design)
                                                .widthWrapContent()
                                        ,
                                        new MTextView(activity)
                                                .gravity(Gravity.CENTER_VERTICAL)
                                                .text("予定: " + budgetIncome)
                                                .backgroundDrawable(drawable.record_design)
                                                .widthWrapContent()
                                        ,
                                        new MTextView(activity)
                                                .gravity(Gravity.CENTER_VERTICAL)
                                                .text(isKurikosi() ? "変更不可" : "変更")
                                                .backgroundDrawable(isKurikosi() ? R.drawable.record_design : R.drawable.button_design_1)
                                                .click(isKurikosi() ? null : l1)
                                )
                        ,

                        new MLinearLayout(activity)
                                .orientationHorizontal()
                                .widthFillParent()
                                .heightWrapContent()
                                .add(

                                        new MTextView(activity)
                                                .gravity(Gravity.CENTER_VERTICAL)
                                                .backgroundDrawable(drawable.record_design)
                                                .widthWrapContent()
                                        ,
                                        new MTextView(activity)
                                                .text("実績: " + settleIncome)
                                                .gravity(Gravity.CENTER_VERTICAL)
                                                .backgroundDrawable(drawable.record_design)
                                                .widthWrapContent()
                                        ,
                                        new MTextView(activity)
                                                .gravity(Gravity.CENTER_VERTICAL)
                                                .text(isKurikosi() ? "削除不可" : "削除")
                                                .backgroundDrawable(isKurikosi() ? R.drawable.record_design : R.drawable.button_design_1)
                                                .click(isKurikosi() ? null : l2)

                                )
                );

    }

    public MTextView getCategoryTypeView(Activity activity) {
        return new MTextView(activity)
                .gravity(Gravity.CENTER_VERTICAL)
                .text(isKurikosi() ? "繰り越し" : "")
                .backgroundDrawable(drawable.record_design)
                .widthWrapContent();
    }

    public MTextView getPayTypeView(Activity activity) {
        return new MTextView(activity)
                .gravity(Gravity.CENTER_VERTICAL)
                .backgroundDrawable(drawable.record_design)
                .widthWrapContent();
    }

    public MTextView getBudgetIncomeView(Activity activity) {
        String budgetIncome = (getBudgetIncome() == null) ? "未入力" : getBudgetIncome() + "円";
        return new MTextView(activity)
                .gravity(Gravity.CENTER_VERTICAL)
                .text("予定: " + budgetIncome)
                .backgroundDrawable(drawable.record_design)
                .widthWrapContent();
    }

    public MTextView getSettleIncomeView(Activity activity) {
        String settleIncome = (getSettleIncome() == null) ? "未入力" : getSettleIncome() + "円";
        return new MTextView(activity)
                .text("実績: " + settleIncome)
                .gravity(Gravity.CENTER_VERTICAL)
                .backgroundDrawable(drawable.record_design)
                .widthWrapContent();
    }

    // ----- LP変換(Logical <-> Physical) -----

    /**
     * DBの格納値から論理エンティティを構成
     */
    @Override
    public IncomeDetail logicalFromPhysical(Cursor c)
    {
        setId(c.getLong(0));
        setCategoryType(c.getInt(1));
        // setPayType(c.getInt(2));
        setBudgetYmd(LPUtil.decodeTextToCalendar(c.getString(3)));
        setBudgetIncome(c.getInt(4));
        String tmp = c.getString(5);
        tmp = c.getString(5);
        if (!tmp.isEmpty()) {
            setSettleIncome(Integer.parseInt(tmp));
        } else {
            setSettleIncome(null);
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
        else {
            values.put("category_type", 0);
        }

        values.put("pay_type", 0);

        if (getBudgetYmd() != null) {
            values.put(IncomeDetailCol.BUDGET_YMD, LPUtil.encodeCalendarToText(getBudgetYmd()));
        }

        if (getBudgetIncome() != null) {
            values.put(IncomeDetailCol.BUDGET_INCOME, getBudgetIncome());
        }

        if (getSettleIncome() != null) {
            values.put(IncomeDetailCol.SETTLE_INCOME, getSettleIncome().toString());
        } else {
            values.put(IncomeDetailCol.SETTLE_INCOME, "");
        }
        return values;
    }

}

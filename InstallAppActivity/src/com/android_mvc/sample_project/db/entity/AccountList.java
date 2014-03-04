package com.android_mvc.sample_project.db.entity;

import java.util.Calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.view.Gravity;

import com.android_mvc.framework.ui.view.MLinearLayout;
import com.android_mvc.framework.ui.view.MTextView;
import com.android_mvc.sample_project.R;
import com.android_mvc.sample_project.common.Util;
import com.android_mvc.sample_project.db.entity.lib.LPUtil;
import com.android_mvc.sample_project.db.entity.lib.LogicalEntity;
import com.android_mvc.sample_project.db.schema.ColumnDefinition.AccountListCol;

public class AccountList extends LogicalEntity<AccountList> {
    // Intent経由でエンティティを運搬可能にするために
    private static final long serialVersionUID = 1L;

    @Override
    public String tableName() {
        return "account_list";
    }

    @Override
    public final String[] columns() {
        return new String[] {
                AccountListCol.ID,
                AccountListCol.YMD,
                AccountListCol.BUDGET_KINGAKU,
                AccountListCol.SETTLE_KINGAKU
        };
    }

    // メンバ
    private Calendar ymd = null;
    private Integer budgetKingaku = null;
    private Integer settleKingaku = null;

    // IDEが自動生成したG&S

    public Calendar getYmd() {
        return ymd;
    }

    public void setYmd(Calendar ymd) {
        this.ymd = ymd;
    }

    public Integer getBudgetKingaku() {
        return budgetKingaku;
    }

    public void setBudgetKingaku(Integer budgetKingaku) {
        this.budgetKingaku = budgetKingaku;
    }

    public Integer getSettleKingaku() {
        return settleKingaku;
    }

    public void setSettleKingaku(Integer settleKingaku) {
        this.settleKingaku = settleKingaku;
    }

    // カスタムG&S
    /**
     * 貯金シートの１セルを返す
     * 
     * @param context
     * @return
     */
    public MLinearLayout getRecord(Context context) {

        String dayOfWeek = "";
        int w = getYmd().get(Calendar.DAY_OF_WEEK);
        switch (w) {
        case Calendar.SUNDAY:
            dayOfWeek = Util._(context, R.string.NITIKAKKO);
            break;
        case Calendar.MONDAY:
            dayOfWeek = Util._(context, R.string.GETUKAKKO);
            break;
        case Calendar.TUESDAY:
            dayOfWeek = Util._(context, R.string.KAKAKKO);
            break;
        case Calendar.WEDNESDAY:
            dayOfWeek = Util._(context, R.string.SUIKAKKO);
            break;
        case Calendar.THURSDAY:
            dayOfWeek = Util._(context, R.string.MOKUKAKKO);
            break;
        case Calendar.FRIDAY:
            dayOfWeek = Util._(context, R.string.KINKAKKO);
            break;
        case Calendar.SATURDAY:
            dayOfWeek = Util._(context, R.string.DOKAKKO);
            break;
        }

        MLinearLayout ret = new MLinearLayout(context)
                .orientationHorizontal()
                .widthFillParent()
                .heightWrapContent()
                .backgroundDrawable(R.drawable.record_design_list);

        MTextView date = new MTextView(context)
                .gravity(Gravity.CENTER_VERTICAL)
                .text(getYmd().get(Calendar.DAY_OF_MONTH) + dayOfWeek)
                ;

        MTextView bKingaku = new MTextView(context)
                .text(getBudgetKingaku() + Util._(context, R.string.ENN))
                .gravity(Gravity.CENTER)
                .textColor(android.graphics.Color.BLUE);

        MTextView sKingaku = new MTextView(context)
                .text(getSettleKingaku() + Util._(context, R.string.ENN))
                .gravity(Gravity.CENTER_VERTICAL);

        ret.add(date, bKingaku, sKingaku);

        return ret;
    }

    // ----- LP変換(Logical <-> Physical) -----

    /**
     * DBの格納値から論理エンティティを構成
     */
    @Override
    public AccountList logicalFromPhysical(Cursor c) {
        setId(c.getLong(0));
        setYmd(LPUtil.decodeTextToCalendar(c.getString(1)));
        setBudgetKingaku(c.getInt(2));
        setSettleKingaku(c.getInt(3));

        return this;
    }

    /**
     * 自身をDBに新規登録可能なデータ型に変換して返す
     */
    @Override
    protected ContentValues toPhysicalEntity(ContentValues values) {
        // entityをContentValueに変換

        if (getId() != null) {
            values.put(AccountListCol.ID, getId());
        }

        if (getYmd() != null) {
            values.put(AccountListCol.YMD,
                    LPUtil.encodeCalendarToText(getYmd()));
        }

        if (getBudgetKingaku() != null) {
            values.put(AccountListCol.BUDGET_KINGAKU, getBudgetKingaku());
        }

        if (getSettleKingaku() != null) {
            values.put(AccountListCol.SETTLE_KINGAKU, getSettleKingaku());
        }

        return values;
    }

}

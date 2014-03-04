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
import com.android_mvc.sample_project.db.schema.ColumnDefinition.AccountSheetCol;

public class AccountSheet extends LogicalEntity<AccountSheet> {
    // Intent経由でエンティティを運搬可能にするために
    private static final long serialVersionUID = 1L;

    @Override
    public String tableName() {
        return "account_sheet";
    }

    @Override
    public final String[] columns() {
        return new String[] {
                AccountSheetCol.ID,
                AccountSheetCol.YMD
        };
    }

    // メンバ
    private Calendar ymd = null;

    // IDEが自動生成したG&S

    public Calendar getYmd() {
        return ymd;
    }

    public void setYmd(Calendar ymd) {
        this.ymd = ymd;
    }

    // カスタムG&S

    /**
     * 貯金シート画面のヘッダーを返す
     * 
     * @param context
     * @return
     */
    public MLinearLayout getHeader(Context context) {

        MLinearLayout ret = new MLinearLayout(context)
                .orientationVertical()
                .widthFillParent()
                .heightWrapContent()
                .paddingLeftPx(10);

        MLinearLayout child1 = new MLinearLayout(context)
                .orientationVertical()
                .widthFillParent()
                .heightWrapContent();

        MLinearLayout child2 = new MLinearLayout(context)
                .orientationVertical()
                .widthFillParent()
                .heightWrapContent();

        MLinearLayout child3 = new MLinearLayout(context)
                .orientationHorizontal()
                .widthFillParent()
                .heightWrapContent();

        MTextView title = new MTextView(context, null, R.attr.text_view_style_h1)
                .gravity(Gravity.CENTER_VERTICAL)
                .text(Util._(context, R.string.TYOKIN_SHITO));

        MTextView label = new MTextView(context)
                .text(getYmd().get(Calendar.YEAR) + "/" + (getYmd().get(Calendar.MONTH) + 1))
                .gravity(Gravity.CENTER)
                .backgroundDrawable(R.drawable.header_design_title)
                ;

        MTextView monday = new MTextView(context)
                .text(Util._(context, R.string.GETU))
                .gravity(Gravity.CENTER_VERTICAL)
                .backgroundDrawable(R.drawable.header_design_weekday);

        MTextView tuesday = new MTextView(context)
                .text(Util._(context, R.string.KA))
                .gravity(Gravity.CENTER_VERTICAL)
                .backgroundDrawable(R.drawable.header_design_weekday);

        MTextView wednesday = new MTextView(context)
                .text(Util._(context, R.string.SUI))
                .gravity(Gravity.CENTER_VERTICAL)
                .backgroundDrawable(R.drawable.header_design_weekday);

        MTextView thursday = new MTextView(context)
                .text(Util._(context, R.string.MOKU))
                .gravity(Gravity.CENTER_VERTICAL)
                .backgroundDrawable(R.drawable.header_design_weekday);

        MTextView friday = new MTextView(context)
                .text(Util._(context, R.string.KIN))
                .gravity(Gravity.CENTER_VERTICAL)
                .backgroundDrawable(R.drawable.header_design_weekday);

        MTextView saturday = new MTextView(context)
                .text(Util._(context, R.string.DO))
                .gravity(Gravity.CENTER_VERTICAL)
                .backgroundDrawable(R.drawable.header_design_saturday);

        MTextView sunday = new MTextView(context)
                .text(Util._(context, R.string.NITI))
                .gravity(Gravity.CENTER_VERTICAL)
                .backgroundDrawable(R.drawable.header_design_sunday);

        child1.add(title);
        child2.add(label);
        child3.add(sunday,monday,tuesday,wednesday,thursday,friday,saturday);

        ret.add(child1, child2, child3);

        return ret;
    }


    // ----- LP変換(Logical <-> Physical) -----

    /**
     * DBの格納値から論理エンティティを構成
     */
    @Override
    public AccountSheet logicalFromPhysical(Cursor c) {
        setId(c.getLong(0));
        setYmd(LPUtil.decodeTextToCalendar(c.getString(1)));

        return this;
    }

    /**
     * 自身をDBに新規登録可能なデータ型に変換して返す
     */
    @Override
    protected ContentValues toPhysicalEntity(ContentValues values) {
        // entityをContentValueに変換

        if (getId() != null) {
            values.put(AccountSheetCol.ID, getId());
        }

        if (getYmd() != null) {
            values.put(AccountSheetCol.YMD,
                    LPUtil.encodeCalendarToText(getYmd()));
        }

        return values;
    }

}

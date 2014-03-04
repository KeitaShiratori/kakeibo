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
import com.android_mvc.sample_project.db.schema.ColumnDefinition.CreditCardSettingCol;

public class CreditCardSetting extends LogicalEntity<CreditCardSetting> {
    // Intent経由でエンティティを運搬可能にするために
    private static final long serialVersionUID = 1L;

    @Override
    public String tableName() {
        return "credit_card_setting";
    }

    @Override
    public final String[] columns() {
        return new String[] {
                CreditCardSettingCol.ID,
                CreditCardSettingCol.SIME_YMD,
                CreditCardSettingCol.SIHARAI_YMD };
    }

    // メンバ
    private Calendar simeYmd = null;
    private Calendar siharaiYmd = null;

    // IDEが自動生成したG&S

    public Calendar getSimeYmd() {
        return simeYmd;
    }

    public void setSimeYmd(Calendar simeYmd) {
        this.simeYmd = simeYmd;
    }

    public Calendar getSiharaiYmd() {
        return siharaiYmd;
    }

    public void setSiharaiYmd(Calendar siharaiYmd) {
        this.siharaiYmd = siharaiYmd;
    }

    // カスタムG&S


    // ----- LP変換(Logical <-> Physical) -----

    /**
     * DBの格納値から論理エンティティを構成
     */
    @Override
    public CreditCardSetting logicalFromPhysical(Cursor c) {
        setId(c.getLong(0));
        setSimeYmd(LPUtil.decodeTextToCalendarYMD(c.getString(1)));
        setSiharaiYmd(LPUtil.decodeTextToCalendarYMD(c.getString(2)));

        Util.d(getSimeYmd().toString());
        Util.d(getSiharaiYmd().toString());
        return this;
    }

    /**
     * 自身をDBに新規登録可能なデータ型に変換して返す
     */
    @Override
    protected ContentValues toPhysicalEntity(ContentValues values) {
        // entityをContentValueに変換

        if (getId() != null) {
            values.put(CreditCardSettingCol.ID, getId());
        }

        if (getSimeYmd() != null) {
            values.put(CreditCardSettingCol.SIME_YMD, 
                    LPUtil.encodeCalendarToTextYMD(getSimeYmd()));
        }

        if (getSiharaiYmd() != null) {
            values.put(CreditCardSettingCol.SIHARAI_YMD,
                    LPUtil.encodeCalendarToTextYMD(getSiharaiYmd()));
        }

        return values;
    }

}

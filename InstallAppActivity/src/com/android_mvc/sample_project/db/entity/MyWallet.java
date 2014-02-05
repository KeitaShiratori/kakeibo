package com.android_mvc.sample_project.db.entity;

import java.util.Calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.view.Gravity;

import com.android_mvc.framework.ui.view.MLinearLayout;
import com.android_mvc.framework.ui.view.MTextView;
import com.android_mvc.sample_project.R;
import com.android_mvc.sample_project.db.entity.lib.LPUtil;
import com.android_mvc.sample_project.db.entity.lib.LogicalEntity;
import com.android_mvc.sample_project.db.schema.ColumnDefinition.MyWalletCol;

public class MyWallet extends LogicalEntity<MyWallet> {
    // Intent経由でエンティティを運搬可能にするために
    private static final long serialVersionUID = 1L;

    @Override
    public String tableName() {
        return "my_wallet";
    }

    @Override
    public final String[] columns() {
        return new String[] {
                MyWalletCol.ID,
                MyWalletCol.YMD,
                MyWalletCol.KINGAKU,
                MyWalletCol.ZANDAKA,
                MyWalletCol.HIKIDASHI,
        };
    }

    // メンバ
    private Calendar ymd = null;
    private Integer kingaku = null;
    private Integer zandaka = null;
    private Integer hikidashi = null;

    // IDEが自動生成したG&S
    public Calendar getYmd() {
        return ymd;
    }

    public void setYmd(Calendar ymd) {
        this.ymd = ymd;
    }

    public Integer getKingaku() {
        return kingaku;
    }

    public void setKingaku(Integer kingaku) {
        this.kingaku = kingaku;
    }

    public Integer getZandaka() {
        return zandaka;
    }

    public void setZandaka(Integer zandaka) {
        this.zandaka = zandaka;
    }

    public Integer getHikidashi() {
        return hikidashi;
    }

    public void setHikidashi(Integer hikidashi) {
        this.hikidashi = hikidashi;
    }

    // カスタムG&S

    
    
    // ----- LP変換(Logical <-> Physical) -----

    /**
     * DBの格納値から論理エンティティを構成
     */
    @Override
    public MyWallet logicalFromPhysical(Cursor c)
    {
        setId(c.getLong(0));
        String tmp = c.getString(1);
        if (!tmp.isEmpty()) {
            setYmd(LPUtil.decodeTextToCalendar(c.getString(1)));
        } else {
            setYmd(null);
        }
        tmp = c.getString(2);
        if (!tmp.isEmpty()) {
            setKingaku(Integer.parseInt(c.getString(2)));
        } else {
            setKingaku(null);
        }
        tmp = c.getString(3);
        if (!tmp.isEmpty()) {
            setZandaka(Integer.parseInt(c.getString(3)));
        } else {
            setZandaka(null);
        }
        tmp = c.getString(4);
        if (!tmp.isEmpty()) {
            setHikidashi(Integer.parseInt(c.getString(4)));
        } else {
            setHikidashi(null);
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
            values.put(MyWalletCol.ID, getId());
        }

        if (getYmd() != null) {
            values.put(MyWalletCol.YMD, LPUtil.encodeCalendarToText(getYmd()));
        } else {
            values.put(MyWalletCol.YMD, "");
        }

        if (getKingaku() != null) {
            values.put(MyWalletCol.KINGAKU, getKingaku().toString());
        } else {
            values.put(MyWalletCol.KINGAKU, "");
        }

        if (getKingaku() != null) {
            values.put(MyWalletCol.ZANDAKA, getZandaka().toString());
        } else {
            values.put(MyWalletCol.ZANDAKA, "");
        }

        if (getKingaku() != null) {
            values.put(MyWalletCol.HIKIDASHI, getHikidashi().toString());
        } else {
            values.put(MyWalletCol.HIKIDASHI, "");
        }
        return values;
    }

}

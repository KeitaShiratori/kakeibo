package com.android_mvc.sample_project.db.entity;

import java.util.Calendar;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.widget.LinearLayout;

import com.android_mvc.framework.ui.view.MLinearLayout;
import com.android_mvc.framework.ui.view.MTextView;
import com.android_mvc.sample_project.db.entity.lib.LPUtil;
import com.android_mvc.sample_project.db.entity.lib.LogicalEntity;
import com.android_mvc.sample_project.db.schema.ColumnDefinition.AccountBookCol;

public class AccountBook extends LogicalEntity<AccountBook> {
    // Intent経由でエンティティを運搬可能にするために
    private static final long serialVersionUID = 1L;

    @Override
    public String tableName() {
        return "account_book";
    }

    @Override
    public final String[] columns() {
        return new String[] { AccountBookCol.ID,
                AccountBookCol.MOKUHYOU_KINGAKU, AccountBookCol.MOKUHYOU_KIKAN,
                AccountBookCol.START_DATE };
    }

    // メンバ
    private Integer mokuhyou_kingaku = null;
    private Integer mokuhyou_kikan = null;
    private Calendar start_date = null;

    // IDEが自動生成したG&S

    public Integer getMokuhyouKingaku() {
        return mokuhyou_kingaku;
    }

    public void setMokuhyouKingaku(Integer mokuhyou_kingaku) {
        this.mokuhyou_kingaku = mokuhyou_kingaku;
    }

    public Integer getMokuhyouKikan() {
        return mokuhyou_kikan;
    }

    public void setMokuhyouKikan(Integer mokuhyou_kikan) {
        this.mokuhyou_kikan = mokuhyou_kikan;
    }

    public Calendar getStartDate() {
        return start_date;
    }

    public void setStartDate(Calendar start_date) {
        this.start_date = start_date;
    }

    // カスタムG&S

    /**
     * 家計簿のレコードを表示する
     */
    public String getDescription() {
        String account_book_description = // ※forループ中で呼ばれるので，本当はStringBuilderを（略
        "目標金額: " + getMokuhyouKingaku() + "円, " + "目標期間: " + getMokuhyouKikan()
                + "ヶ月, " + "開始日: "
                + LPUtil.encodeCalendarToTextYMD(getStartDate());

        return account_book_description;
    }

    // ----- LP変換(Logical <-> Physical) -----

    /**
     * DBの格納値から論理エンティティを構成
     */
    @Override
    public AccountBook logicalFromPhysical(Cursor c) {
        setId(c.getLong(0));
        setMokuhyouKingaku(c.getInt(1));
        setMokuhyouKikan(c.getInt(2));
        setStartDate(LPUtil.decodeTextToCalendar(c.getString(3)));

        return this;
    }

    /**
     * 自身をDBに新規登録可能なデータ型に変換して返す
     */
    @Override
    protected ContentValues toPhysicalEntity(ContentValues values) {
        // entityをContentValueに変換

        if (getId() != null) {
            values.put(AccountBookCol.ID, getId());
        }

        if (getMokuhyouKingaku() != null) {
            values.put(AccountBookCol.MOKUHYOU_KINGAKU, getMokuhyouKingaku());
        }

        if (getMokuhyouKikan() != null) {
            values.put(AccountBookCol.MOKUHYOU_KIKAN, getMokuhyouKikan());
        }

        if (getStartDate() != null) {
            values.put(AccountBookCol.START_DATE,
                    LPUtil.encodeCalendarToText(getStartDate()));
        }

        return values;
    }

}

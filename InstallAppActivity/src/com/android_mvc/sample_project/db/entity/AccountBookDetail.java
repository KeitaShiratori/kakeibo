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

public class AccountBookDetail extends LogicalEntity<AccountBookDetail> {
    // Intent経由でエンティティを運搬可能にするために
    private static final long serialVersionUID = 1L;

    @Override
    public String tableName() {
        return "account_book_detail";
    }

    @Override
    public final String[] columns() {
        return new String[] {
                "id",
                "mokuhyou_month_kingaku",
                "mokuhyou_month",
                "auto_input_flag" };
    }

    // メンバ
    private Integer mokuhyou_month_kingaku = null;
    private Calendar mokuhyou_month = null;
    private Boolean auto_input_flag = null;

    // IDEが自動生成したG&S

    public Integer getMokuhyouMonthKingaku() {
        return mokuhyou_month_kingaku;
    }

    public void setMokuhyouMonthKingaku(Integer mokuhyou_month_kingaku) {
        this.mokuhyou_month_kingaku = mokuhyou_month_kingaku;
    }

    public Calendar getMokuhyouMonth() {
        return mokuhyou_month;
    }

    public void setMokuhyouMonth(Calendar mokuhyou_month) {
        this.mokuhyou_month = mokuhyou_month;
    }

    public Boolean getAutoInputFlag() {
        return auto_input_flag;
    }

    public void setAutoInputFlag(Boolean auto_input_flag) {
        this.auto_input_flag = auto_input_flag;
    }

    // カスタムG&S

    /**
     * 家計簿のレコードを表示する
     */
    public String getDescription() {
        String account_book_detail_description = // ※forループ中で呼ばれるので，本当はStringBuilderを（略
        getMokuhyouMonth().get(Calendar.YEAR) + "/"
                + (getMokuhyouMonth().get(Calendar.MONTH) + 1) + ": "
                + getMokuhyouMonthKingaku() + "円, " + "自動入力：";

        // 自動入力フラグがtrueならON、falseならOFF
        if (getAutoInputFlag()) {
            account_book_detail_description += "ON";
        } else {
            account_book_detail_description += "OFF";
        }

        return account_book_detail_description;
    }

    // ----- LP変換(Logical <-> Physical) -----

    /**
     * DBの格納値から論理エンティティを構成
     */
    @Override
    public AccountBookDetail logicalFromPhysical(Cursor c) {
        setId(c.getLong(0));
        setMokuhyouMonthKingaku(c.getInt(1));
        setMokuhyouMonth(LPUtil.decodeTextToCalendar(c.getString(2)));
        setAutoInputFlag(LPUtil.decodeIntegerToBoolean(c.getInt(3)));

        return this;
    }

    /**
     * 自身をDBに新規登録可能なデータ型に変換して返す
     */
    @Override
    protected ContentValues toPhysicalEntity(ContentValues values) {
        // entityをContentValueに変換

        if (getId() != null) {
            values.put("id", getId());
        }

        if (getMokuhyouMonthKingaku() != null) {
            values.put("mokuhyou_month_kingaku", getMokuhyouMonthKingaku());
        }

        if (getMokuhyouMonth() != null) {
            values.put("mokuhyou_month",
                    LPUtil.encodeCalendarToText(getMokuhyouMonth()));
        }

        values.put("auto_input_flag",
                LPUtil.encodeBooleanToInteger(getAutoInputFlag()));

        return values;
    }

}

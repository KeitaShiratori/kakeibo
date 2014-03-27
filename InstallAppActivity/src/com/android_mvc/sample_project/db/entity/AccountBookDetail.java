package com.android_mvc.sample_project.db.entity;

import java.util.Calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.view.Gravity;
import android.view.View.OnClickListener;

import com.android_mvc.framework.ui.view.MButton;
import com.android_mvc.framework.ui.view.MLinearLayout;
import com.android_mvc.framework.ui.view.MTextView;
import com.android_mvc.sample_project.R;
import com.android_mvc.sample_project.db.dao.AccountBookDAO;
import com.android_mvc.sample_project.db.entity.lib.LPUtil;
import com.android_mvc.sample_project.db.entity.lib.LogicalEntity;
import com.android_mvc.sample_project.db.schema.ColumnDefinition.AccountBookDetailCol;

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
                AccountBookDetailCol.ID,
                AccountBookDetailCol.MOKUHYOU_MONTH_KINGAKU,
                AccountBookDetailCol.MOKUHYOU_MONTH,
                AccountBookDetailCol.AUTO_INPUT_FLAG,
                AccountBookDetailCol.SIME_FLAG
        };
    }

    // メンバ
    private Integer mokuhyou_month_kingaku = null;
    private Calendar mokuhyou_month = null;
    private Boolean auto_input_flag = null;
    private Boolean sime_flag = null;

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

    public Boolean getSimeFlag() {
        return sime_flag;
    }

    public void setSimeFlag(Boolean sime_flag) {
        this.sime_flag = sime_flag;
    }

    // カスタムG&S

    /**
     * 家計簿レコードのヘッダを返却する
     */
    public MLinearLayout getHeader(Context context) {
        MLinearLayout ret = new MLinearLayout(context)
                .orientationHorizontal()
                .heightWrapContent();

        MTextView tv1 = new MTextView(context)
                .gravity(Gravity.CENTER)
                .text("月別目標")
                .backgroundDrawable(R.drawable.header_design_h40_w115)
                .textsize(18);
        MTextView tv2 = new MTextView(context)
                .gravity(Gravity.CENTER)
                .text("目標金額")
                .backgroundDrawable(R.drawable.header_design_h40_w115)
                .textsize(18);
        MTextView tv3 = new MTextView(context)
                .gravity(Gravity.CENTER)
                .text("自動入力")
                .backgroundDrawable(R.drawable.header_design_h40_w115)
                .textsize(18);

        ret.add(tv1, tv2, tv3);

        return ret;
    }

    /**
     * 家計簿のレコードを表示する
     */
    public MLinearLayout getDescription(Context context, OnClickListener l) {
        int baseDate = new AccountBookDAO(context).findAll().get(0).getStartDate().get(Calendar.DAY_OF_MONTH);

        MLinearLayout ret = new MLinearLayout(context)
                .orientationHorizontal()
                .heightWrapContent();

        getMokuhyouMonth().set(Calendar.DAY_OF_MONTH, baseDate);

        Calendar nextMonth = (Calendar) getMokuhyouMonth().clone();
        nextMonth.add(Calendar.MONTH, 1);
        nextMonth.add(Calendar.DAY_OF_MONTH, -1);

        MTextView tv1 = new MTextView(context)
                .gravity(Gravity.CENTER)
                .text(getMokuhyouMonth().get(Calendar.YEAR) + "/"
                        + (getMokuhyouMonth().get(Calendar.MONTH) + 1) + "/"
                        + getMokuhyouMonth().get(Calendar.DAY_OF_MONTH)
                        + "\n～"
                        + nextMonth.get(Calendar.YEAR) + "/"
                        + (nextMonth.get(Calendar.MONTH) + 1) + "/"
                        + nextMonth.get(Calendar.DAY_OF_MONTH))
                .backgroundDrawable(R.drawable.record_design);

        MTextView tv2 = new MTextView(context)
                .gravity(Gravity.CENTER)
                .text(getMokuhyouMonthKingaku() + "円"
                        + "\n")
                .backgroundDrawable(R.drawable.record_design);

        MTextView bt3 = new MTextView(context)
                .gravity(Gravity.CENTER)
                .click(getSimeFlag() ? null : l);

        // 自動入力フラグがtrueならON、falseならOFF
        if (getAutoInputFlag()) {
            bt3.text("ON"
                    + "\n")
                    .backgroundDrawable(R.drawable.button_design_1);
        } else {
            if (getSimeFlag()) {
                bt3.text("締め"
                        + "\n")
                        .backgroundDrawable(R.drawable.record_design);
            }
            else {
                bt3.text("OFF"
                        + "\n")
                        .backgroundDrawable(R.drawable.button_design_2);
            }
        }

        ret.add(tv1, tv2, bt3);
        return ret;
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
        setSimeFlag(LPUtil.decodeIntegerToBoolean(c.getInt(4)));

        return this;
    }

    /**
     * 自身をDBに新規登録可能なデータ型に変換して返す
     */
    @Override
    protected ContentValues toPhysicalEntity(ContentValues values) {
        // entityをContentValueに変換

        if (getId() != null) {
            values.put(AccountBookDetailCol.ID, getId());
        }

        if (getMokuhyouMonthKingaku() != null) {
            values.put(AccountBookDetailCol.MOKUHYOU_MONTH_KINGAKU, getMokuhyouMonthKingaku());
        }

        if (getMokuhyouMonth() != null) {
            values.put(AccountBookDetailCol.MOKUHYOU_MONTH,
                    LPUtil.encodeCalendarToText(getMokuhyouMonth()));
        }

        values.put(AccountBookDetailCol.AUTO_INPUT_FLAG,
                LPUtil.encodeBooleanToInteger(getAutoInputFlag()));

        values.put(AccountBookDetailCol.SIME_FLAG,
                LPUtil.encodeBooleanToInteger(getSimeFlag()));

        return values;
    }

}

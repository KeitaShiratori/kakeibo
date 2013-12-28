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
     * 家計簿レコードのヘッダを返却する
     */
    public MLinearLayout getHeader(Context context) {
        MLinearLayout ret = new MLinearLayout(context)
                .orientationHorizontal()
                .heightWrapContent()
                .paddingLeftPx(10);

        MTextView tv1 = new MTextView(context)
                .gravity(Gravity.CENTER_VERTICAL)
                .text("月別目標")
                .backgroundDrawable(R.drawable.header_design);
        MTextView tv2 = new MTextView(context)
                .gravity(Gravity.CENTER_VERTICAL)
                .text("目標金額")
                .backgroundDrawable(R.drawable.header_design);
        MTextView tv3 = new MTextView(context)
                .gravity(Gravity.CENTER_VERTICAL)
                .text("自動入力")
                .backgroundDrawable(R.drawable.header_design);

        ret.add(tv1, tv2, tv3);

        return ret;
    }

    /**
     * 家計簿のレコードを表示する
     */
    public MLinearLayout getDescription(Context context, OnClickListener l) {
        MLinearLayout ret = new MLinearLayout(context)
                .orientationHorizontal()
                .heightWrapContent()
                .paddingLeftPx(10);

        MTextView tv1 = new MTextView(context)
                .gravity(Gravity.CENTER_VERTICAL)
                .text(getMokuhyouMonth().get(Calendar.YEAR) + "年"
                        + (getMokuhyouMonth().get(Calendar.MONTH) + 1) + "月")
                .backgroundDrawable(R.drawable.record_design);

        MTextView tv2 = new MTextView(context)
                .gravity(Gravity.CENTER_VERTICAL)
                .text(getMokuhyouMonthKingaku() + "円")
                .backgroundDrawable(R.drawable.record_design);

        MTextView bt3 = new MTextView(context)
                .gravity(Gravity.CENTER_VERTICAL)
                .click(l);

        // 自動入力フラグがtrueならON、falseならOFF
        if (getAutoInputFlag()) {
            bt3.text("ON")
                    .backgroundDrawable(R.drawable.button_design_1);
        } else {
            bt3.text("OFF")
                    .backgroundDrawable(R.drawable.button_design_2);

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
